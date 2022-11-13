package com.shipnolja.reservation.fishinginfo.service.Impl;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
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
import org.springframework.stereotype.Service;

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
                        .infoTarget(reqFishingInfoDto.getInfoTarget())
                        .infoAssemblePoint(reqFishingInfoDto.getInfoAssemblePoint())
                        .infoStartPoint(reqFishingInfoDto.getInfoStartPoint())
                        .infoCapacity(reqFishingInfoDto.getInfoCapacity())
                        .infoStartTime(reqFishingInfoDto.getInfoStartTime())
                        .infoStartDate(reqFishingInfoDto.getInfoStartDate())
                        .infoReservationStatus(reqFishingInfoDto.getInfoReservationStatus())
                        .shipInfo(checkShipInfo)
                        .build()
        );

        return new ResResultDto(fishingInfo.getInfoId(), "출조 정보를 등록 했습니다.");
    }
}
