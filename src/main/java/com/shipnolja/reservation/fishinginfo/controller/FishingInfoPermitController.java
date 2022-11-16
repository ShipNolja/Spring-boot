package com.shipnolja.reservation.fishinginfo.controller;

import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoListDto;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Api(tags = {"FishingInfo - api - 모든 이용자가 이용 가능 (목록 조회)"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fishingInfo")
public class FishingInfoPermitController {

    private final FishingInfoService fishingInfoService;

    /*
        -검색-
        sortMethod => 오름차순 내림차순
        + 정렬기준은 내 테이블에 있는거를 기준으로 잡아야함
        sortBy 정렬기준 => target(대상어종), startPoint(출항지), startDate(출항일자), reservationStatus(예약상태)
    */

    /* 출조 정보 검색 */
    @ApiOperation(value = "출조 정보 검색", notes = "출조 정보 목록을 출력합니다.")
    @GetMapping("/simpleList")
    public List<ResFishingInfoListDto> simpleInfoList(@ApiParam(value = "페이지 번호", required = true) @RequestParam int page,
                                                      @ApiParam(value = "정렬 방법 : asc , desc", required = true) @RequestParam String sortMethod,
                                                      @ApiParam(value = "정렬 기준 : id(선박 아이디), shipName(선박명)", required = true) @RequestParam String sortBy,
                                                      @ApiParam(value = "검색 기준 : 지역, 상세 지역, 항구 ,선박명, 예약 상태, 출항 날짜, 전체", required = true) @RequestParam String searchBy,
                                                      @ApiParam(value = "검색어 : 검색 기준에 맞는 검색어 입력", required = true) @RequestParam(required = false) String content,
                                                      @ApiParam(value = "대상 어종 : 광어", required = true) @RequestParam String target,
                                                      @ApiParam(value = "출항 시간 : xxxx-xx-xxTxx:xx",required = true)
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate infoStartDate) {

        return fishingInfoService.simpleInfoList(page, sortMethod, sortBy, searchBy, content, target, infoStartDate);
    }

    /* 출조 정보 상세 목록 */
    @ApiOperation(value = "출조 정보 상세 목록", notes = "출조 정보 상세 목록을 출력합니다.")
    @GetMapping("/detailList")
    public List<ResFishingInfoListDto> detailsInfoList(@ApiParam(value = "페이지 번호",required = true) @RequestParam int page,
                                                       @ApiParam(value = "선상 아이디",required = true) @RequestParam Long ship_id) {

        return fishingInfoService.detailsInfoList(page, ship_id);
    }

}
