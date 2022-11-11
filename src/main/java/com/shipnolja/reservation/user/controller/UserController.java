package com.shipnolja.reservation.user.controller;

import com.shipnolja.reservation.ship.dto.request.ShipInfoDto;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.dto.response.ResUserInfoDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@Api(tags = {"2. User - api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "내 정보 조회",notes = "내 정보를 조회 합니다.")
    @GetMapping("")
    public ResUserInfoDto check(@LoginUser UserInfo userInfo){
        return new ResUserInfoDto(userService.userInfoCheck(userInfo));
    }

    @PostMapping("/test")
    public ResResultDto userResponseTest() {
        return new ResResultDto(1L , "성공!");
    }

    @ApiOperation(value = "내 정보 조회",notes = "내 정보를 조회 합니다.")
    @PostMapping("")
    public ResResultDto shipRegistration(@LoginUser UserInfo userInfo,
                                         @ApiParam(value = "선박 가입 정보 DTO", required = true) @RequestBody ShipInfoDto shipInfoDto){
        Long result = userService.shipRegistration(userInfo,shipInfoDto);
        return new ResResultDto(result,"선박 등록을 성공했습니다");
    }

}
