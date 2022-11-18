package com.shipnolja.reservation.fishingCondition.service;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFileDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionListDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

import java.time.LocalDate;
import java.util.List;

public interface FishingConditionService {
    ResResultDto upload(UserInfo userInfo, FishingConditionDto fishingConditionDto, List<ResFileDto> files);

    //목록 조회
    List<ResFishingConditionListDto> list(String fish, LocalDate date, String title, String sortBy, String sortMethod , int page);

    //상세 조회
    ResFishingConditionDto fishingCondition(Long id);
}
