package com.shipnolja.reservation.fishinginfo.controller;
import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"FishingInfo - api - 매니저만 사용 가능 (등록, 수정, 삭제)"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager/fishingInfo")
public class FishingInfoController {

    private final FishingInfoService fishingInfoService;

    /* 출조 정보 등록 */
    @ApiOperation(value = "출조 정보 등록", notes = "출조 정보를 등록 합니다.")
    @PostMapping("")
    public ResResultDto fishingInfoWrite(@LoginUser UserInfo userInfo,
                                         @ApiParam(value = "출조 정보를 갖는 객체", required = true) @RequestBody ReqFishingInfoDto reqFishingInfoDto) {

        return fishingInfoService.fishingInfoWrite(userInfo, reqFishingInfoDto);
    }

    /* 출조 정보 수정 */
    @PostMapping("/update")
    public ResResultDto fishingInfoUpdate(@LoginUser UserInfo userInfo,
                                          @ApiParam(value = "출조 정보 아이디", required = true) @RequestParam Long fishingInfo_id,
                                          @ApiParam(value = "출조 정보를 갖는 객체", required = true) @RequestBody ReqFishingInfoDto reqFishingInfoDto) {

        return fishingInfoService.fishingInfoUpdate(userInfo, reqFishingInfoDto, fishingInfo_id);
    }

    /* 출조 정보 삭제 */
}
