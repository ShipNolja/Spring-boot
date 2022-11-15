package com.shipnolja.reservation.fishingCondition.service;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionListDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FishingConditionService {
    ResResultDto upload(UserInfo userInfo, FishingConditionDto fishingConditionDto, List<MultipartFile> files);

    ResFishingConditionListDto list(String fish,String date,
                                    String searchWord,int page);
}
