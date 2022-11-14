package com.shipnolja.reservation.fishinginfo.controller;

import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        sortBy 정렬기준 => 지역(area),세부지역(detailArea),항구(port),선상이름(name),출항일자(startDate, endDate),어종(target)
    */

    /* 출조 정보 검색 */
    @ApiOperation(value = "출조 정보 검색", notes = "출조 정보 목록을 출력합니다.")
    @GetMapping("/simpleList")
    public List<ResFishingInfoDto> simpleInfoList(@ApiParam(value = "페이지 번호",required = true) @RequestParam int page,
                                                  @ApiParam(value = "정렬 방법 : asc , desc",required = true) @RequestParam String sortMethod,
                                                  @ApiParam(value = "정렬 기준 : target(대상어종), startPoint(출항지), startDate(출항일자), reservationStatus(예약상태)",required = true) @RequestParam String sortBy,
                                                  @ApiParam(value = "지역 정보 : 강원도",required = true) @RequestParam String area,
                                                  @ApiParam(value = "상세 지역 정보 : 고성",required = true) @RequestParam String detailArea,
                                                  @ApiParam(value = "항구 이름 : 거진항",required = true) @RequestParam String port,
                                                  @ApiParam(value = "배 이름 : 써니호",required = true) @RequestParam String shipName,
                                                  @ApiParam(value = "대상 어종 : 광어",required = true) @RequestParam String target,
                                                  @ApiParam(value = "예약 상태 : 예약가능, 예약마감",required = true) @RequestParam String status,
                                                  @ApiParam(value = "출항 시간 : xxxx-xx-xxTxx:xx",required = true)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)@RequestParam(required = false) LocalDateTime startDate,
                                                  @ApiParam(value = "입항 시간 : xxxx-xx-xxTxx:xx",required = true)
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)@RequestParam(required = false) LocalDateTime endDate) {

        return fishingInfoService.simpleInfoList(page, sortMethod, sortBy, area, detailArea, port, shipName, target, status, startDate, endDate);
    }

    /* 출조 정보 상세 목록 */
    @ApiOperation(value = "출조 정보 상세 목록", notes = "출조 정보 상세 목록을 출력합니다.")
    @GetMapping("/detailList")
    public List<ResFishingInfoDto> detailsInfoList(@ApiParam(value = "페이지 번호",required = true) @RequestParam int page,
                                                   @ApiParam(value = "선상 아이디",required = true) @RequestParam Long ship_id) {

        return fishingInfoService.detailsInfoList(page, ship_id);
    }

}
