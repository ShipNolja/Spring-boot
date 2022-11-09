package com.shipnolja.reservation.user.service;

import com.shipnolja.reservation.user.dto.request.LoginDto;
import com.shipnolja.reservation.user.dto.request.UserInfoDto;
import com.shipnolja.reservation.user.dto.response.ResLoginDto;
import com.shipnolja.reservation.user.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    //회원가입
    Long join(UserInfoDto userInfoDto);

    //로그인
    ResLoginDto login(LoginDto loginDto);

    //아이디 중복 체크
    Integer userIdCheck(String userid);

    //회원 정보 조회
    UserInfo userInfoCheck(UserInfo userInfo);
}
