package com.shipnolja.reservation.review.service.Impl;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.fishinginfo.repository.FishingInfoRepository;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.reservation.repository.ReservationRepository;
import com.shipnolja.reservation.review.dto.request.ReqReviewDto;
import com.shipnolja.reservation.review.dto.response.ResReviewListDto;
import com.shipnolja.reservation.review.model.Review;
import com.shipnolja.reservation.review.repository.ReviewRepository;
import com.shipnolja.reservation.review.service.ReviewService;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final FishingInfoRepository fishingInfoRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    /* 후기 목록 조회 */
    @Override
    public List<ResReviewListDto> reviewList(Long ship_id, int page) {

        ShipInfo checkShipInfo = shipRepository.findById(ship_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("reviewCreate"));
        Page<Review> reviewPage = reviewRepository.findByShipInfo(checkShipInfo, pageable);

        List<ResReviewListDto> reviewList = new ArrayList<>();

        for(Review review : reviewPage) {

            ResReviewListDto resReviewListDto = new ResReviewListDto();

            resReviewListDto.setReviewId(review.getReviewId());
            resReviewListDto.setReservationId(review.getReservation().getReservationId());
            resReviewListDto.setShipId(review.getShipInfo().getId());
            resReviewListDto.setFishingInfoId(review.getReservation().getFishingInfo().getInfoId());
            resReviewListDto.setReviewTitle(review.getReviewTitle());
            resReviewListDto.setReviewContent(review.getReviewContent());
            resReviewListDto.setReviewCreate(review.getReviewCreate());
            resReviewListDto.setReviewRating(review.getReviewRating());
            resReviewListDto.setReviewUpdate(review.getReviewUpdate());
            resReviewListDto.setTotalPage(reviewPage.getTotalPages());
            resReviewListDto.setTotalElement(reviewPage.getTotalElements());

            reviewList.add(resReviewListDto);
        }
        return reviewList;
    }

    /* 후기 작성 */
    @Override
    public ResResultDto reviewWrite(UserInfo userInfo, Long reservation_id, ReqReviewDto reqReviewDto) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Reservation checkReservation = reservationRepository.findById(reservation_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findById(checkReservation.getFishingInfo().getInfoId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        ShipInfo checkShipInfo = shipRepository.findById(checkFishingInfo.getShipInfo().getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        /* 방문완료 상태에서만, 하나의 예약 번호로 하나의 후기만 작성 */
        if(!checkReservation.getReservationStatus().equals("방문완료")) {

            return new ResResultDto(-1L,"방문 완료 시에만 후기를 작성할 수 있습니다.");
        } else if(reviewRepository.findByReservation(checkReservation).isPresent()) {

            return new ResResultDto(-2L,"예약 번호로 작성된 후기가 있습니다.");
        }


        Review review = reviewRepository.save(
                Review.builder()
                        .reviewTitle(reqReviewDto.getReviewTitle())
                        .reviewContent(reqReviewDto.getReviewContent())
                        .reviewRating(reqReviewDto.getReviewRating())
                        .reservation(checkReservation)
                        .userInfo(checkUserInfo)
                        .shipInfo(checkShipInfo)
                        .build()
        );

        return new ResResultDto(review.getReviewId(),"후기를 작성 했습니다.");
    }

    /* 후기 수정 */
    @Override
    public ResResultDto reviewUpdate(UserInfo userInfo, ReqReviewDto reqReviewDto, Long reservation_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Reservation checkReservation = reservationRepository.findById(reservation_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findById(checkReservation.getFishingInfo().getInfoId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        ShipInfo checkShipInfo = shipRepository.findById(checkFishingInfo.getShipInfo().getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        Review checkReview = reviewRepository.findByReservation(checkReservation)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("작성한 후기를 찾을 수 없습니다."));


        checkReview = reviewRepository.save(
                Review.builder()
                        .reviewId(checkReview.getReviewId())
                        .reviewTitle(reqReviewDto.getReviewTitle())
                        .reviewContent(reqReviewDto.getReviewContent())
                        .reviewRating(reqReviewDto.getReviewRating())
                        .reviewCreate(checkReview.getReviewCreate())
                        .reservation(checkReservation)
                        .userInfo(checkUserInfo)
                        .shipInfo(checkShipInfo)
                        .build()
        );


        return new ResResultDto(checkReview.getReviewId(),"후기를 수정 했습니다.");
    }

    /* 후기 삭제 */
    @Override
    public ResResultDto reviewDelete(UserInfo userInfo, Long reservation_id) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Reservation checkReservation = reservationRepository.findById(reservation_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findById(checkReservation.getFishingInfo().getInfoId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        ShipInfo checkShipInfo = shipRepository.findById(checkFishingInfo.getShipInfo().getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        Review checkReview = reviewRepository.findByReservation(checkReservation)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("작성한 후기를 찾을 수 없습니다."));

        reviewRepository.deleteById(checkReview.getReviewId());

        return new ResResultDto(checkReview.getReviewId(),"후기를 삭제 했습니다.");
    }
}
