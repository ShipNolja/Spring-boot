package com.shipnolja.reservation.config.token.service;

import com.shipnolja.reservation.config.JwtTokenProvider;
import com.shipnolja.reservation.config.token.dto.TokenDto;
import com.shipnolja.reservation.config.token.dto.TokenRequestDto;
import com.shipnolja.reservation.config.token.model.RefreshToken;
import com.shipnolja.reservation.config.token.repository.RefreshTokenJpaRepo;
import com.shipnolja.reservation.user.dto.request.LoginDto;
import com.shipnolja.reservation.user.dto.response.ResLoginDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CRefreshTokenException;
import com.shipnolja.reservation.util.exception.CUserNotFoundException;
import com.shipnolja.reservation.util.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LonginTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenJpaRepo tokenJpaRepo;
    private final UserRepository userRepository;

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        UserInfo loginUserInfo = userRepository.findByUserid(loginDto.getUserId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("회원가입 되지 않은 정보입니다.")
        );
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(loginDto.getPassword(),loginUserInfo.getPassword()))
            throw new CustomException.ResourceNotFoundException("비밀번호 오류입니다.");

        //Access,Refresh 토큰 발급급
        TokenDto tokenDto = jwtTokenProvider.createToken(loginUserInfo.getId(), loginUserInfo.getRole());

        //RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .userPk(loginUserInfo.getId())
                .token(tokenDto.getRefreshToken())
                .build();
        tokenJpaRepo.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 만료된 refresh token 에러
        if (!jwtTokenProvider.validationToken(tokenRequestDto.getRefreshToken())) {
            throw new CRefreshTokenException();
        }

        // AccessToken 에서 Username (pk) 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // user pk로 유저 검색 / repo 에 저장된 Refresh Token 이 없음
        UserInfo userInfo = userRepository.findById(Long.parseLong(authentication.getName()))
                .orElseThrow(CUserNotFoundException::new);
        RefreshToken refreshToken = tokenJpaRepo.findByUserPk(userInfo.getId())
                .orElseThrow(CRefreshTokenException::new);

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new CRefreshTokenException();

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto newCreatedToken = jwtTokenProvider.createToken(userInfo.getId(), userInfo.getRole());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        tokenJpaRepo.save(updateRefreshToken);

        return newCreatedToken;
    }

}
