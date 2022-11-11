package com.shipnolja.reservation.user.controller;


import com.shipnolja.reservation.config.JwtTokenProvider;
import com.shipnolja.reservation.config.token.dto.TokenDto;
import com.shipnolja.reservation.config.token.dto.TokenRequestDto;
import com.shipnolja.reservation.config.token.service.LonginTokenService;
import com.shipnolja.reservation.user.dto.request.LoginDto;
import com.shipnolja.reservation.user.dto.request.UserInfoDto;
import com.shipnolja.reservation.user.dto.response.ResLoginDto;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"1. User - SignUp,login"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PermitAllController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final LonginTokenService tokenService;

    @ApiOperation(value = "회원 가입",notes = "회원을 등록합니다.")
    @PostMapping("/sign-up")
    public ResResultDto join(@Valid @RequestBody @ApiParam(value = "회원 가입 정보를 갖는 객체", required = true) UserInfoDto userInfoDto){
        Long result = userService.join(userInfoDto);
        return result == -1L ?
                new ResResultDto(result,"회원가입 실패.") : new ResResultDto(result,"회원가입 성공.");
    }

    @ApiOperation(value = "로그인",notes = "이메일 형식으로 로그인")
    @PostMapping("/login")
    public TokenDto login(
            @RequestBody @ApiParam(value = "회원 가입 정보를 갖는 객체", required = true)LoginDto loginDto
            ){
        return tokenService.login(loginDto);
    }

    @ApiOperation(
            value = "액세스, 리프레시 토큰 재발급",
            notes = "엑세스 토큰 만료시 회원 검증 후 리프레쉬 토큰을 검증해서 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public TokenDto refresh(
            @ApiParam(value = "토큰 재발급 요청 DTO", required = true)
            @RequestBody TokenRequestDto tokenRequestDto) {
        return tokenService.reissue(tokenRequestDto);
    }

}
