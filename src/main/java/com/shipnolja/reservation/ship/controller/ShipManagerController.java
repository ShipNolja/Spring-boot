package com.shipnolja.reservation.ship.controller;

import com.shipnolja.reservation.ship.dto.response.ResManagerShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfoList;
import com.shipnolja.reservation.ship.service.ShipService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"4. ShipManger - api - ROLE_MANAGER 만 이용가능"})
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

}
