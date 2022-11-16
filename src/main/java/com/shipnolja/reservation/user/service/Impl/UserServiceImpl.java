package com.shipnolja.reservation.user.service.Impl;

import com.shipnolja.reservation.reservation.dto.response.ResReservationListDto;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.reservation.repository.ReservationRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final ReservationRepository reservationRepository;

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
    public Long shipRegistration(UserInfo userInfo, ShipInfoDto shipInfoDto, MultipartFile file) {
        UserInfo registerUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다.")
        );
        
        //일반 사용자인지 검증
        userRepository.findByIdAndRole(registerUserInfo.getId(),UserRole.ROLE_USER).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("이미 사업자로 등록된 회원입니다.")
        );
        //파일 저장 경로
        String savePath = System.getProperty("user.dir") + "//src//main//resources//static//shipImage";

        //파일 저장되는 폳더 없으면 생성
        if(!new File(savePath).exists()) {
            try{
                new File(savePath).mkdir();
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }

        String originFileName = file.getOriginalFilename().toLowerCase();

        //UUID로 파일명 중복되지 않게 유일한 식별자 + 확장자로 저장
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid + "__" + originFileName;

        //File로 저장 경로와 저장 할 파일명 합쳐서 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
        String filepath = savePath + "//" + saveFileName;
        String dbFilePath = "../shipImage/" + saveFileName;

        try {
            file.transferTo(new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //사업자로 권한 변경
        userRepository.updateUserRole(UserRole.ROLE_MANAGER,registerUserInfo.getId());

        ShipInfo registerShipInfo =
                shipRepository.save(
                        ShipInfo.builder()
                                .userInfo(registerUserInfo)
                                .registerNumber(shipInfoDto.getRegisterNumber())
                                .image(dbFilePath)
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


    /* 회원 예약 목록 조회 (진모)*/
    @Override
    public List<ResReservationListDto> userReservationList(UserInfo userInfo, int page, String sortMethod, String searchBy, String content) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Pageable pageable = null;
        Page<Reservation> reservationPage = null;

        List<ResReservationListDto> reservationList = new ArrayList<>();

        if(sortMethod.equals("asc")) {
            pageable = PageRequest.of(page,10, Sort.by("reservationDate").ascending());
        } else if(sortMethod.equals("desc")) {
            pageable = PageRequest.of(page, 10, Sort.by("reservationDate").descending());
        }

        switch(searchBy) {
            case "예약상태" :
                reservationPage = reservationRepository.findByUserInfoAndReservationStatusContaining(userInfo, content, pageable);
                break;
            case "예약날짜" :
                LocalDate reservationDate = LocalDate.parse(content, DateTimeFormatter.ISO_DATE);
                System.out.println(reservationDate);
                reservationPage = reservationRepository.findByUserInfoAndReservationDate(userInfo, reservationDate, pageable);
                break;
        }

        if (reservationPage != null) {

            int totalPages = reservationPage.getTotalPages();
            long totalElements = reservationPage.getTotalElements();

            reservationPage.forEach(reservation -> {

                ResReservationListDto resReservationListDto = new ResReservationListDto();

                resReservationListDto.setReservationId(reservation.getReservationId());
                resReservationListDto.setFishingInfoId(reservation.getFishingInfo().getInfoId());
                resReservationListDto.setReservationDate(reservation.getReservationDate());
                resReservationListDto.setReservationName(reservation.getReservationName());
                resReservationListDto.setReservationNum(reservation.getReservationNum());
                resReservationListDto.setUserMessage(reservation.getUserMessage());
                resReservationListDto.setReservationStatus(reservation.getReservationStatus());
                resReservationListDto.setReservationPhone(reservation.getReservationPhone());
                resReservationListDto.setTotalPage(totalPages);
                resReservationListDto.setTotalElement(totalElements);

                reservationList.add(resReservationListDto);

            });
        }

        return reservationList;
    }
}
