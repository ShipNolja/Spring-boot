package com.shipnolja.reservation.fishingCondition.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionListDto;
import com.shipnolja.reservation.fishingCondition.service.FishingConditionService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.S3FileUploadService;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Api(tags = {"FishingCondition - 조황 정보 관련 기능 - 조회는 모든사용자 , 등록은 Manager"})
public class FishingConditionController {
    private final FishingConditionService fishingConditionService;
    private final S3FileUploadService s3FileUploadService;
    //조황 정보 작성
    @PostMapping(value = "/manager/fishing-condition", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto postWrite(@LoginUser UserInfo userInfo,
                                  @ApiParam(value = "조황 정보 등록 dto", required = true) @RequestPart(value="keys") FishingConditionDto fishingConditionDto,
                                  @ApiParam(value = "조황 정보에 등록 할 사진 목록", required = false)  @RequestPart(value = "images", required = false) List<MultipartFile> files) {

        return fishingConditionService.upload(userInfo,fishingConditionDto, files);
    }
    
    //조황 정보 목록 조회
    @GetMapping("/fishing-condition/{sortBy}/{sortMethod}/{page}")
    public List<ResFishingConditionListDto> list(@ApiParam(value = "어종 이름", required = true)  @RequestParam String fish,
                                                 @ApiParam(value = "yyyy-mm-dd 형식 날짜", required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate startDate,
                                                 @ApiParam(value = "yyyy-mm-dd 형식 날짜", required = true)  @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate endDate,
                                                 @ApiParam(value = "id,title,fish", required = true) @PathVariable String sortBy,
                                                 @ApiParam(value = "asc / desc", required = true) @PathVariable String sortMethod ,
                                                 @ApiParam(value = "page 정보", required = true) @PathVariable int page,
                                                 @ApiParam(value = "조황 정보 제목", required = true) @RequestParam  String title){


        return fishingConditionService.list(fish,startDate, endDate, title,sortBy,sortMethod,page);
    }

    @GetMapping("fishing-condition/{id}")
    public ResFishingConditionDto fishingCondition(@ApiParam(value = "조황 정보 id 값", required = true)@PathVariable Long id){
        return fishingConditionService.fishingCondition(id);
    }
}
