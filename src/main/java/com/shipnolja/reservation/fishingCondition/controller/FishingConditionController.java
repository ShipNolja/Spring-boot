package com.shipnolja.reservation.fishingCondition.controller;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.service.FishingConditionService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager/fishing-condition")
public class FishingConditionController {
    private final FishingConditionService fishingConditionService;
    //게시글 작성
    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto postWrite(@LoginUser UserInfo userInfo,
                                  @Valid @RequestPart(value="keys") FishingConditionDto fishingConditionDto,
                                  @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        return fishingConditionService.upload(userInfo,fishingConditionDto, files);
    }
}
