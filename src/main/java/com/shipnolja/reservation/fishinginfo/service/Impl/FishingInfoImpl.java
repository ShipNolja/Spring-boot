package com.shipnolja.reservation.fishinginfo.service.Impl;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
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

    @Override
    public ResResultDto fishingInfoWrite(UserInfo userInfo, ReqFishingInfoDto reqFishingInfoDto) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 사용자가 아닙니다."));



        return new ResResultDto(0L, "출조 정보를 등록 했습니다.");
    }
}
