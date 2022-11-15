package com.shipnolja.reservation.fishingCondition.controller;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.service.FishingConditionService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FishingConditionController {
    private final FishingConditionService fishingConditionService;
    //조황 정보 작성
    @PostMapping(value = "/manager/fishing-condition", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto postWrite(@LoginUser UserInfo userInfo,
                                  @Valid @RequestPart(value="keys") FishingConditionDto fishingConditionDto,
                                  @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        return fishingConditionService.upload(userInfo,fishingConditionDto, files);
    }
    
    //조황 정보 조회
    @GetMapping("/fishing-condition/{fish}/{date}/{searchWord}/{page}")
    public String list(@PathVariable String fish,@PathVariable  @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
                       @PathVariable String searchWord,@PathVariable int page){
        return null;
    }
}
