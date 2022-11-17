package com.shipnolja.reservation.user.service;


import com.shipnolja.reservation.reservation.dto.response.ResReservationListDto;
import com.shipnolja.reservation.ship.dto.request.ShipInfoDto;
import com.shipnolja.reservation.user.dto.request.UserInfoDto;
import com.shipnolja.reservation.user.model.UserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends UserDetailsService {
    //회원가입
    Long join(UserInfoDto userInfoDto);

    //아이디 중복 체크
    Long userIdCheck(String userid);

    //핸드폰 번호 중복 체크
    Long userPhoneCheck(String userPhone);

    //회원 정보 조회
    UserInfo userInfoCheck(UserInfo userInfo);

    //사업자 등록
    Long shipRegistration(UserInfo userInfo, ShipInfoDto shipInfoDto, MultipartFile file);

    /* 회원 예약 목록 조회 (진모) */
    List<ResReservationListDto> userReservationList(UserInfo userInfo, int page, String sortMethod, String searchBy, String content);


}
