package com.shipnolja.reservation.fishinginfo.service;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface FishingInfoService {

    /* 출조 정보 등록 */
    ResResultDto fishingInfoWrite(UserInfo userInfo, ReqFishingInfoDto reqFishingInfo);

    /* 출조 정보 검색 */
    List<ResFishingInfoDto> simpleInfoList(int page, String sortMethod, String sortBy, String area, String detailArea, String port,
                                           String shipName, String target, LocalDateTime startDate, LocalDateTime endDate);

    ResFishingInfoDto detailsInfoList(Pageable pageable, int page);
}
