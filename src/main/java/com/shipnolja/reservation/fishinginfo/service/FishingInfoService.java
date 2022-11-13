package com.shipnolja.reservation.fishinginfo.service;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

public interface FishingInfoService {

    ResResultDto fishingInfoWrite(UserInfo userInfo, ReqFishingInfoDto reqFishingInfo);
}
