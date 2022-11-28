package com.shipnolja.reservation.ship.service.Impl;

import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoListDto;
import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.fishinginfo.repository.FishingInfoRepository;
import com.shipnolja.reservation.reservation.dto.response.ResReservationListDto;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.reservation.repository.ReservationRepository;
import com.shipnolja.reservation.ship.dto.response.ResManagerShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfoList;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.ship.service.ShipService;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import com.shipnolja.reservation.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipServiceImpl implements ShipService {

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final WishRepository wishRepository;
    private final FishingInfoRepository fishingInfoRepository;
    private final ReservationRepository reservationRepository;

    //선박 리스트 조회
    @Override
    public List<ResShipInfoList> shipList(String searchRequirements,String searchWord, String sortBy, String sortMethod, int page) {
        
        Pageable pageable = null;

        if(sortMethod.equals("asc"))
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).ascending());
        else if(sortMethod.equals("desc"))
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());

        String shipName = "",port = "",area = "",detailArea = "";

        switch (searchRequirements) {
            case "shipName":
                shipName = searchWord;
                break;
            case "port":
                port = searchWord;
                break;
            case "area":
                area = searchWord;
                break;
            case "detailArea":
                detailArea = searchWord;
                break;
        }

        Page<ShipInfo> shipInfoPage = shipRepository.findShipInfoList(
                shipName,port,area,detailArea,pageable
        );

        List<ShipInfo> shipInfoList = shipInfoPage.getContent();

        List<ResShipInfoList> shipInfoListDto = new ArrayList<>();



        shipInfoList.forEach(entity->{
            ResShipInfoList listDto = new ResShipInfoList();
            double shipRatingAvg; // 상품 평점
            if(shipRepository.findShipRating(entity)==null){
                shipRatingAvg = 0;
            }else {
                shipRatingAvg = Math.round(shipRepository.findShipRating(entity) * 10) / 10.0;
            }

            listDto.setId(entity.getId());
            listDto.setImage(entity.getImage());
            listDto.setName(entity.getName());
            listDto.setArea(entity.getArea());
            listDto.setDetailArea(entity.getDetailArea());
            listDto.setPort(entity.getPort());
            listDto.setStreetAddress(entity.getStreetAddress());
            listDto.setWishCount(wishRepository.countByShipInfo(entity));
            listDto.setShipRatingAvg(shipRatingAvg);
            listDto.setTotalPage(shipInfoPage.getTotalPages());
            listDto.setTotalElement(shipInfoPage.getTotalElements());
            shipInfoListDto.add(listDto);
            }
        );

        return shipInfoListDto;
    }

    @Override
    public ResShipInfo shipInfo(Long id) {
        ShipInfo shipInfo = shipRepository.findById(id).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("선상 정보를 찾을 수 없습니다")
        );

        double shipRatingAvg; // 상품 평점

        if(shipRepository.findShipRating(shipInfo)==null){
            shipRatingAvg = 0;
        }else {
            shipRatingAvg = Math.round(shipRepository.findShipRating(shipInfo) * 10) / 10.0;
        }
        return new ResShipInfo(shipInfo,wishRepository.countByShipInfo(shipInfo),shipRatingAvg);
    }

    @Override
    public ResManagerShipInfo shipMangerInfo(Long id) {
        ShipInfo shipManagerInfo = shipRepository.findById(id).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("선상 정보를 찾을 수 없습니다")
        );

        return new ResManagerShipInfo(shipManagerInfo);
    }


    /* 등록한 출조 목록 */
    @Override
    public List<ResFishingInfoListDto> managerFishingInfoList(UserInfo userInfo, String sortMethod, String searchBy, String content, int page) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 이용자만 사용할 수 있습니다."));

        ShipInfo checkShipInfo = shipRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        Pageable pageable = null;
        Page<FishingInfo> fishingInfoPage = null;

        List<ResFishingInfoListDto> fishingInfoList = new ArrayList<>();

        if(sortMethod.equals("asc")) {
            pageable = PageRequest.of(page,10, Sort.by("infoStartDate").ascending());
        } else if(sortMethod.equals("desc")) {
            pageable = PageRequest.of(page, 10, Sort.by("infoStartDate").descending());
        }
        
        switch (searchBy) {
            case "출조날짜" :
                LocalDate fishingInfoDate = LocalDate.parse(content, DateTimeFormatter.ISO_DATE);
                fishingInfoPage = fishingInfoRepository.findByShipInfoAndInfoStartDate(checkShipInfo, fishingInfoDate, pageable);
                break;
            case "예약상태" :
                fishingInfoPage = fishingInfoRepository.findByShipInfoAndInfoReservationStatus(checkShipInfo, content, pageable);
                break;
        }

        if(fishingInfoPage != null) {

            for(FishingInfo fishingInfo : fishingInfoPage) {

                ResFishingInfoListDto resFishingInfoListDto = new ResFishingInfoListDto();

                resFishingInfoListDto.setFishingInfoId(fishingInfo.getInfoId());
                resFishingInfoListDto.setShipInfoId(checkShipInfo.getId());
                resFishingInfoListDto.setArea(fishingInfo.getShipInfo().getArea());
                resFishingInfoListDto.setDetailArea(fishingInfo.getShipInfo().getDetailArea());
                resFishingInfoListDto.setPort(fishingInfo.getShipInfo().getPort());
                resFishingInfoListDto.setShipName(fishingInfo.getShipInfo().getName());
                resFishingInfoListDto.setTarget(fishingInfo.getInfoTarget());
                resFishingInfoListDto.setInfoStartDate(fishingInfo.getInfoStartDate());
                resFishingInfoListDto.setInfoStartTime(fishingInfo.getInfoStartTime());
                resFishingInfoListDto.setInfoEndTime(fishingInfo.getInfoEndTime());
                resFishingInfoListDto.setInfoReservationStatus(fishingInfo.getInfoReservationStatus());
                resFishingInfoListDto.setInfoCapacity(fishingInfo.getInfoCapacity());
                resFishingInfoListDto.setImage(fishingInfo.getShipInfo().getImage());
                resFishingInfoListDto.setInfoMessage(fishingInfo.getInfoMessage());
                resFishingInfoListDto.setInfoNotice(fishingInfo.getInfoNotice());
                resFishingInfoListDto.setInfoAssemblePoint(fishingInfo.getInfoAssemblePoint());
                resFishingInfoListDto.setInfoStartPoint(fishingInfo.getInfoStartPoint());
                resFishingInfoListDto.setTotalPage(fishingInfoPage.getTotalPages());
                resFishingInfoListDto.setTotalElement(fishingInfoPage.getTotalElements());

                fishingInfoList.add(resFishingInfoListDto);
            }
        }
        return fishingInfoList;
    }

    /* 출조 예약자 목록 */
    @Override
    public List<ResReservationListDto> managerReservationList(UserInfo userInfo, String sortMethod, String searchBy, String content, int page) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 이용자만 사용할 수 있습니다."));

        ShipInfo checkShipInfo = shipRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        List<FishingInfo> checkFishingInfo = fishingInfoRepository.findByShipInfo(checkShipInfo);

        List<ResReservationListDto> reservationList = new ArrayList<>();

        for(FishingInfo fishingInfo : checkFishingInfo) {

            Pageable pageable = null;
            Page<Reservation> reservationPage = null;

            if(sortMethod.equals("asc")) {
                pageable = PageRequest.of(page,10, Sort.by("reservationDate").ascending());
            } else if(sortMethod.equals("desc")) {
                pageable = PageRequest.of(page, 10, Sort.by("reservationDate").descending());
            }

            switch (searchBy) {
                case "출조날짜" :
                    LocalDate reservationDate = LocalDate.parse(content, DateTimeFormatter.ISO_DATE);
                    reservationPage = reservationRepository.findByFishingInfoAndReservationDate(fishingInfo, reservationDate, pageable);
                    break;
                case "예약자명" :
                    reservationPage = reservationRepository.findByFishingInfoAndReservationNameContaining(fishingInfo, content, pageable);
                    break;
                case "예약상태" :
                    reservationPage = reservationRepository.findByFishingInfoAndReservationStatus(fishingInfo, content, pageable);
                    break;
                case "전체" :
                    /* 선박이 올린 출조 정보 순회하면서 등록한 모든 출조 정보의 예약자 출력 */
                    reservationPage = reservationRepository.findByFishingInfo(fishingInfo, pageable);
                    break;
            }

            if(reservationPage != null) {

                for(Reservation reservation : reservationPage) {

                    ResReservationListDto resReservationListDto = new ResReservationListDto();

                    resReservationListDto.setReservationId(reservation.getReservationId());
                    resReservationListDto.setFishingInfoId(fishingInfo.getInfoId());
                    resReservationListDto.setReservationName(reservation.getReservationName());
                    resReservationListDto.setReservationNum(reservation.getReservationNum());
                    resReservationListDto.setReservationDate(reservation.getReservationDate());
                    resReservationListDto.setReservationPhone(reservation.getReservationPhone());
                    resReservationListDto.setUserMessage(reservation.getUserMessage());
                    resReservationListDto.setReservationStatus(reservation.getReservationStatus());
                    resReservationListDto.setTotalPage(reservationPage.getTotalPages());
                    resReservationListDto.setTotalElement(reservationPage.getTotalElements());

                    reservationList.add(resReservationListDto);
                }
            }
        }
        return reservationList;
    }

    /* 예약자 상태 변경 */
    @Override
    public ResResultDto managerStatusUpdate(UserInfo userInfo, Long fishingInfo_id, Long reservation_id, String status) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 이용자만 사용할 수 있습니다."));

        ShipInfo checkShipInfo = shipRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findByShipInfoAndInfoId(checkShipInfo, fishingInfo_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        Reservation checkReservation = reservationRepository.findByFishingInfoAndReservationId(checkFishingInfo, reservation_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));

        if(checkReservation.getReservationStatus().equals("예약완료") && status.equals("방문완료")) {
            /* 방문완료로 상태 변경 */
            reservationRepository.statusUpdate(status, checkReservation.getReservationId());
        } else {
            return new ResResultDto(-1L,"변경할 수 없는 상태입니다.");
        }

        return new ResResultDto(checkReservation.getReservationId(),"예약 상태를 변경 했습니다.");
    }
}
