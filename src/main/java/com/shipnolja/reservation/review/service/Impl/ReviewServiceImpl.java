package com.shipnolja.reservation.review.service.Impl;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.fishinginfo.repository.FishingInfoRepository;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.reservation.repository.ReservationRepository;
import com.shipnolja.reservation.review.dto.request.ReqReviewDto;
import com.shipnolja.reservation.review.model.Review;
import com.shipnolja.reservation.review.repository.ReviewRepository;
import com.shipnolja.reservation.review.service.ReviewService;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final FishingInfoRepository fishingInfoRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ResResultDto reviewWrite(UserInfo userInfo, Long reservationId, Long fishingInfoId, ReqReviewDto reqReviewDto) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findById(fishingInfoId)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        Reservation checkReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));

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
                        .build()
        );

        return new ResResultDto(review.getReviewId(),"후기를 작성 했습니다.");
    }
}
