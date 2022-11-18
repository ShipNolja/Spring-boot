package com.shipnolja.reservation.fishingCondition.service;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFileDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionListDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface FishingConditionService {
    ResResultDto upload(UserInfo userInfo, FishingConditionDto fishingConditionDto, List<MultipartFile> files);

    //목록 조회
    List<ResFishingConditionListDto> list(String fish, LocalDate startDate,LocalDate endDate, String title, String sortBy, String sortMethod , int page);

    //상세 조회
    ResFishingConditionDto fishingCondition(Long id);
}
