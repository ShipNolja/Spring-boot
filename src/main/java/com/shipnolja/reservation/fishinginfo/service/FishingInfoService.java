package com.shipnolja.reservation.fishinginfo.service;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

public interface FishingInfoService {

    /* 출조 정보 등록 */
    ResResultDto fishingInfoWrite(UserInfo userInfo, ReqFishingInfoDto reqFishingInfo);
}
