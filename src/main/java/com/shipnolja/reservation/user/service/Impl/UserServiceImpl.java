package com.shipnolja.reservation.user.service.Impl;

import com.shipnolja.reservation.config.JwtTokenProvider;
import com.shipnolja.reservation.config.token.dto.TokenDto;
import com.shipnolja.reservation.config.token.dto.TokenRequestDto;
import com.shipnolja.reservation.config.token.model.RefreshToken;
import com.shipnolja.reservation.config.token.repository.RefreshTokenJpaRepo;
import com.shipnolja.reservation.user.dto.request.LoginDto;
import com.shipnolja.reservation.user.dto.request.UserInfoDto;
import com.shipnolja.reservation.user.dto.response.ResLoginDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(userPk)).orElseThrow(
                ()->new UsernameNotFoundException(userPk)
        );
    }

    //회원가입
    @Override
    public Long join(UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = userInfoDto.getPassword();
        userInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        //중복 id,email 검증
        Integer idCheckResult = userIdCheck(userInfoDto.getUserid());


        if(idCheckResult.equals(-1)) {
            return -1L;
        }
        return userRepository.save(
                UserInfo.builder()
                        .userid(userInfoDto.getUserid())
                        .password(userInfoDto.getPassword())
                        .name(userInfoDto.getName())
                        .phone(userInfoDto.getPhone())
                        .role(UserRole.ROLE_USER)//임시로 권한 USER로 지정
                        .userEnabled(true)
                        .build()
        ).getId();
    }


    //유저 아이디 중복 체크
    @Override
    public Integer userIdCheck(String userid) {
        //.isPresent , Optional객체가 있으면 true null이면 false 반환
        if (userRepository.findByUserid(userid).isPresent()) {
            return -1; //같은 userid있으면 -1반환
        }
        return 1;
    }

    //회원 정보 조회
    @Override
    public UserInfo userInfoCheck(UserInfo userInfo) {
        return userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다.")
        );
    }
}
