package com.shipnolja.reservation.user.controller;

import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.dto.response.ResUserInfoDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"2. User - api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    //회원정보 조회
    @GetMapping("")
    public ResUserInfoDto check(@LoginUser UserInfo userInfo){
        return new ResUserInfoDto(userService.userInfoCheck(userInfo));
    }

    @PostMapping("/test")
    public ResResultDto userResponseTest() {

        return new ResResultDto(1L , "성공!");
    }

}
