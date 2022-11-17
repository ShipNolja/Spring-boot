package com.shipnolja.reservation.user.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.shipnolja.reservation.ship.dto.request.ShipInfoDto;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.dto.request.UserInfoDto;

import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Value("{cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final AmazonS3Client amazonS3Client;

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(userPk)).orElseThrow(
                ()->new UsernameNotFoundException(userPk)
        );
    }

    //회원가입
    @Transactional
    @Override
    public Long join(UserInfoDto userInfoDto) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String rawPassword = userInfoDto.getPassword();
        userInfoDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        //중복 id,email 검증
        Long idCheckResult = userIdCheck(userInfoDto.getUserid());


        if(idCheckResult.equals(-1L)) {
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
    public Long userIdCheck(String userid) {
        //.isPresent , Optional객체가 있으면 true null이면 false 반환
        if (userRepository.findByUserid(userid).isPresent()) {
            return -1L; //같은 userid있으면 -1반환
        }
        return 1L;
    }

    @Override
    public Long userPhoneCheck(String userPhone) {
        if(userRepository.findByPhone(userPhone).isPresent()){
            return -1L;
        }
        return 1L;
    }

    //회원 정보 조회
    @Override
    public UserInfo userInfoCheck(UserInfo userInfo) {
        return userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다.")
        );
    }

    //선박 등록
    @Transactional
    @Override
    public Long shipRegistration(UserInfo userInfo, ShipInfoDto shipInfoDto, String filePath) {
        UserInfo registerUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다.")
        );
        
        //일반 사용자인지 검증
        userRepository.findByIdAndRole(registerUserInfo.getId(),UserRole.ROLE_USER).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("이미 사업자로 등록된 회원입니다.")
        );



        //사업자로 권한 변경
        userRepository.updateUserRole(UserRole.ROLE_MANAGER,registerUserInfo.getId());

        ShipInfo registerShipInfo =
                shipRepository.save(
                        ShipInfo.builder()
                                .userInfo(registerUserInfo)
                                .registerNumber(shipInfoDto.getRegisterNumber())
                                .image(filePath)
                                .name(shipInfoDto.getName())
                                .bankName(shipInfoDto.getBankName())
                                .bankNum(shipInfoDto.getBankNum())
                                .area(shipInfoDto.getArea())
                                .detailArea(shipInfoDto.getDetailArea())
                                .port(shipInfoDto.getPort())
                                .streetAddress(shipInfoDto.getStreetAddress())
                                .build()
                );

        return registerShipInfo.getId();
    }

    private String createFileName(String fileName) { // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) { // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단하였습니다.
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
}
