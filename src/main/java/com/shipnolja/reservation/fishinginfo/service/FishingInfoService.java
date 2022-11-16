package com.shipnolja.reservation.fishinginfo.service;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoListDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

import java.time.LocalDate;
import java.util.List;

public interface FishingInfoService {

    /* 출조 정보 등록 */
    ResResultDto fishingInfoWrite(UserInfo userInfo, ReqFishingInfoDto reqFishingInfo);

    /* 출조 정보 검색 */
    List<ResFishingInfoListDto> simpleInfoList(int page, String sortMethod, String sortBy, String searchBy, String content,
                                               String target, LocalDate infoStartDate);

    /* 출조 정보 상세 목록 */
    List<ResFishingInfoListDto> detailsInfoList(int page, Long ship_id);
}
