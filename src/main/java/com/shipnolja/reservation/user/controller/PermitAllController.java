package com.shipnolja.reservation.user.controller;


import com.shipnolja.reservation.config.JwtTokenProvider;
import com.shipnolja.reservation.user.dto.request.LoginDto;
import com.shipnolja.reservation.user.dto.request.UserInfoDto;
import com.shipnolja.reservation.user.dto.response.ResLoginDto;
import com.shipnolja.reservation.user.dto.response.ResToken;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"1. User - SignUp,login"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PermitAllController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //회원가입
    @ApiOperation(value = "회원 가입",notes = "회원을 등록합니다.")
    @PostMapping("/sign-up")
    public ResResultDto join(@Valid @RequestBody @ApiParam(value = "회원 가입 정보를 갖는 객체", required = true) UserInfoDto userInfoDto){
        Long result = userService.join(userInfoDto);
        return result == -1L ?
                new ResResultDto(result,"회원가입 실패.") : new ResResultDto(result,"회원가입 성공.");
    }

    //로그인
    @ApiOperation(value = "로그인",notes = "이메일 형식으로 로그인")
    @PostMapping("/login")
    public ResToken login(
            @RequestBody @ApiParam(value = "회원 가입 정보를 갖는 객체", required = true)LoginDto loginDto
            ){
        ResLoginDto resLoginDto = userService.login(loginDto);
        String token = jwtTokenProvider.createToken(String.valueOf(resLoginDto.getUserId()),resLoginDto.getRoles());
        return new ResToken(token);
    }

}
