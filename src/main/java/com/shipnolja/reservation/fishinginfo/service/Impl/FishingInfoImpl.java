package com.shipnolja.reservation.fishinginfo.service.Impl;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.fishinginfo.repository.FishingInfoRepository;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FishingInfoImpl implements FishingInfoService {

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final FishingInfoRepository fishingInfoRepository;

    /* 출조 정보 등록 */
    @Override
    public ResResultDto fishingInfoWrite(UserInfo userInfo, ReqFishingInfoDto reqFishingInfoDto) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 사용자가 아닙니다."));

        ShipInfo checkShipInfo = shipRepository.findById(reqFishingInfoDto.getShipId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        FishingInfo fishingInfo = fishingInfoRepository.save(
                FishingInfo.builder()
                        .infoNotice(reqFishingInfoDto.getInfoNotice())
                        .infoMessage(reqFishingInfoDto.getInfoMessage())
                        .infoTarget(reqFishingInfoDto.getInfoTarget())
                        .infoAssemblePoint(reqFishingInfoDto.getInfoAssemblePoint())
                        .infoStartPoint(reqFishingInfoDto.getInfoStartPoint())
                        .infoCapacity(reqFishingInfoDto.getInfoCapacity())
                        .infoStartTime(reqFishingInfoDto.getInfoStartTime())
                        .infoEndTime(reqFishingInfoDto.getInfoEndTime())
                        .infoStartDate(reqFishingInfoDto.getInfoStartDate())
                        .infoReservationStatus(reqFishingInfoDto.getInfoReservationStatus())
                        .shipInfo(checkShipInfo)
                        .build()
        );

        return new ResResultDto(fishingInfo.getInfoId(), "출조 정보를 등록 했습니다.");
    }
    
    /* 출조 정보 목록 */
    @Override
    public List<ResFishingInfoDto> simpleInfoList(int page, String sortMethod, String sortBy, String area, String detailArea,
                                                  String port, String shipName, String target, String status,LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = null;

        if(sortMethod.equals("asc")) {
            pageable = PageRequest.of(page,10, Sort.by(sortBy).ascending());
        } else if(sortMethod.equals("desc")) {
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());
        }

        Page<FishingInfo> fishingInfoPage = fishingInfoRepository.searchFishingInfoList(target, startDate, endDate, area, detailArea, port, shipName, status, pageable);

        List<ResFishingInfoDto> fishingInfoListDto = new ArrayList<>();

        fishingInfoPage.forEach(fishingInfo -> {

            ResFishingInfoDto infoListDto = new ResFishingInfoDto();

            /* 이미지 추가 */
            infoListDto.setId(fishingInfo.getInfoId());
            infoListDto.setArea(fishingInfo.getShipInfo().getArea());
            infoListDto.setDetailArea(fishingInfo.getShipInfo().getDetailArea());
            infoListDto.setPort(fishingInfo.getShipInfo().getPort());
            infoListDto.setShipName(fishingInfo.getShipInfo().getName());
            infoListDto.setTarget(fishingInfo.getInfoTarget());
            infoListDto.setStartDate(fishingInfo.getInfoStartDate());
            infoListDto.setStartTime(fishingInfo.getInfoStartTime());
            infoListDto.setEndTime(fishingInfo.getInfoEndTime());
            infoListDto.setInfoReservationStatus(fishingInfo.getInfoReservationStatus());
            infoListDto.setInfoCapacity(fishingInfo.getInfoCapacity());
            infoListDto.setImage(fishingInfo.getShipInfo().getImage());
            infoListDto.setTotalPage(fishingInfoPage.getTotalPages());
            infoListDto.setTotalElement(fishingInfoPage.getTotalElements());

            fishingInfoListDto.add(infoListDto);
        });

        return fishingInfoListDto;
    }
    
    /* 출조 정보 상세 목록 */
    @Override
    public List<ResFishingInfoDto> detailsInfoList(int page, Long ship_id) {

        ShipInfo checkShipInfo = shipRepository.findById(ship_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("infoStartDate").ascending());
        Page<FishingInfo> fishingInfoPage = fishingInfoRepository.findByShipInfo(checkShipInfo, pageable);

        List<ResFishingInfoDto> fishingInfoListDto = new ArrayList<>();

        fishingInfoPage.forEach(fishingInfo -> {

            ResFishingInfoDto infoListDto = new ResFishingInfoDto();

            infoListDto.setId(fishingInfo.getInfoId());
            infoListDto.setArea(fishingInfo.getShipInfo().getArea());
            infoListDto.setDetailArea(fishingInfo.getShipInfo().getDetailArea());
            infoListDto.setPort(fishingInfo.getShipInfo().getPort());
            infoListDto.setShipName(fishingInfo.getShipInfo().getName());
            infoListDto.setTarget(fishingInfo.getInfoTarget());
            infoListDto.setStartDate(fishingInfo.getInfoStartDate());
            infoListDto.setStartTime(fishingInfo.getInfoStartTime());
            infoListDto.setEndTime(fishingInfo.getInfoEndTime());
            infoListDto.setInfoReservationStatus(fishingInfo.getInfoReservationStatus());
            infoListDto.setInfoCapacity(fishingInfo.getInfoCapacity());
            infoListDto.setTotalPage(fishingInfoPage.getTotalPages());
            infoListDto.setTotalElement(fishingInfoPage.getTotalElements());

            fishingInfoListDto.add(infoListDto);
        });

        return fishingInfoListDto;
    }
}
