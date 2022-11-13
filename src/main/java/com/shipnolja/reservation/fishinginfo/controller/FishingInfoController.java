package com.shipnolja.reservation.fishinginfo.controller;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class FishingInfoController {

    private final FishingInfoService fishingInfoService;

    @ApiOperation(value = "출조 정보 등록", notes = "출조 정보를 등록 합니다.")
    @PostMapping("/fishingInfo/write")
    public ResResultDto fishingInfoWrite(@LoginUser UserInfo userInfo,
                                         @RequestBody ReqFishingInfoDto reqFishingInfoDto) {

        return fishingInfoService.fishingInfoWrite(userInfo, reqFishingInfoDto);
    }
}
