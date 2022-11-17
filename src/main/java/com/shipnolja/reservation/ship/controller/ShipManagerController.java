package com.shipnolja.reservation.ship.controller;

import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoListDto;
import com.shipnolja.reservation.reservation.dto.response.ResReservationListDto;
import com.shipnolja.reservation.ship.dto.response.ResManagerShipInfo;
import com.shipnolja.reservation.ship.service.ShipService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"ShipManger - api - ROLE_MANAGER 만 이용가능"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/manager")
public class ShipManagerController {
    
    private final ShipService shipService;

    @ApiOperation(value = "내 선박 정보",notes = "내 선박의 상세 정보를 확인합니다.")
    @GetMapping("")
    public ResManagerShipInfo shipInfo(@LoginUser UserInfo userInfo){
        return shipService.shipMangerInfo(userInfo.getId());
    }
    
    /* 등록한 출조 정보 목록 */
    @ApiOperation(value = "내 출조 정보 목록", notes = "내가 등록한 출조 정보 목록을 확인합니다.")
    @GetMapping("fishingInfoList")
    public List<ResFishingInfoListDto> managerFishingInfoList(@LoginUser UserInfo userInfo,
                                                              @ApiParam(value = "선박 아이디", required = true) @RequestParam Long ship_id,
                                                              @ApiParam(value = "정렬 방법 : asc , desc", required = true) @RequestParam String sortMethod,
                                                              @ApiParam(value = "검색 기준 : 출조날짜, 예약상태", required = true) @RequestParam String searchBy,
                                                              @ApiParam(value = "검색값 : xxxx(년)-xx(월)-xx(일), 예약가능, 예약마감", required = true) @RequestParam String content,
                                                              int page) {

       return shipService.managerFishingInfoList(userInfo, ship_id, sortMethod, searchBy, content, page);
    }

    /* 출조 예약자 목록 */
    @ApiOperation(value = "출조 예약자 목록",notes = "해당 출조 목록 예약자를 확인합니다.")
    @GetMapping("reservationList")
    public List<ResReservationListDto> managerReservationList(@LoginUser UserInfo userInfo,
                                                        @ApiParam(value = "선박 아이디", required = true) @RequestParam Long ship_id,
                                                        @ApiParam(value = "정렬 방법 : asc , desc", required = true) @RequestParam String sortMethod,
                                                        @ApiParam(value = "검색 기준 : 예약자명, 출조날짜", required = true) @RequestParam String searchBy,
                                                        @ApiParam(value = "검색값 : xxx(예약자명), xxxx(년)-xx(월)-xx(일)", required = true) @RequestParam(required = false) String content,
                                                        int page) {

        return shipService.managerReservationList(userInfo, ship_id, sortMethod, searchBy, content, page);
    }

}
