package com.shipnolja.reservation.fishinginfo.controller;

import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"FishingInfo - api - 모든 이용자가 이용 가능 (조회)"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fishingInfo")
public class FishingInfoPermitController {

    private final FishingInfoService fishingInfoService;

    @ApiOperation(value = "출조 정보 목록", notes = "출조 정보 목록을 출력합니다.")
    @PostMapping("/simple/infoList")
    public ResFishingInfoDto simpleInfoList() {

        return null;
    }

    @ApiOperation(value = "출조 정보 상세 목록", notes = "출조 정보 상세 목록을 출력합니다.")
    @GetMapping("/details/infoList")
    public ResFishingInfoDto detailsInfoList() {

        return null;
    }

}
