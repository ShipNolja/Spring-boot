package com.shipnolja.reservation.user.controller;


import com.shipnolja.reservation.config.JwtTokenProvider;
import com.shipnolja.reservation.config.token.dto.TokenDto;
import com.shipnolja.reservation.config.token.dto.TokenRequestDto;
import com.shipnolja.reservation.config.token.service.LonginTokenService;
import com.shipnolja.reservation.user.dto.request.LoginDto;
import com.shipnolja.reservation.user.dto.request.UserInfoDto;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = {"User - 모든 사용자 - SignUp,login"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PermitAllController {

    private final UserService userService;
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

    @ApiOperation(value = "아이디 중복 체크",notes = "이메일 형식의 아이디를 중복 체크 해줍니다.")
    @GetMapping("/{userId}")
    public ResResultDto userIdCheck(
            @ApiParam(value = "회원 가입 할 아이디", required = true) @PathVariable String userId
    ){
        Long result =  userService.userIdCheck(userId);

        if(result == -1L){
            return new ResResultDto(result,"이미 사용중인 아이디 입니다.");
        }else{
            return new ResResultDto(result,"사용 가능한 아이디 입니다.");
        }

    }

    @ApiOperation(value = "핸드폰 번호 중복 체크",notes = "핸드폰 번호를 중복 체크 해줍니다.")
    @GetMapping("/{userPhone}")
    public ResResultDto userPhoneCheck(
            @ApiParam(value = "회원 가입 할 핸드폰 번호", required = true) @RequestParam String userPhone
    ){
        Long result =  userService.userPhoneCheck(userPhone);

        if(result == -1L){
            return new ResResultDto(result,"이미 사용중인 핸드폰 번호 입니다.");
        }else{
            return new ResResultDto(result,"사용 가능한 핸드폰 번호 입니다.");
        }

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
