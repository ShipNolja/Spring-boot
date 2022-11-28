package com.shipnolja.reservation.user.service.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.fishinginfo.repository.FishingInfoRepository;
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
import com.shipnolja.reservation.util.S3FileUploadService;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final S3FileUploadService s3FileUploadService;
    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final FishingInfoRepository fishingInfoRepository;
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
    public Long shipRegistration(UserInfo userInfo, ShipInfoDto shipInfoDto, List<MultipartFile> files) {
        UserInfo registerUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다.")
        );
        
        //일반 사용자인지 검증
        userRepository.findByIdAndRole(registerUserInfo.getId(),UserRole.ROLE_USER).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("이미 사업자로 등록된 회원입니다.")
        );

        String originFileName = files.get(0).getOriginalFilename();

        if(files.isEmpty()){
            return -1L; //파일 없음
        }
        else if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")){
            String filePath = s3FileUploadService.uploadFile(files).get(0);

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
        else return -2L; // 확장자 오류
    }

    /* 회원 예약 목록 조회 */
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
                reservationPage = reservationRepository.findByUserInfoAndReservationDate(userInfo, reservationDate, pageable);
                break;
        }

        if (reservationPage != null) {

            for(Reservation reservation : reservationPage) {

                ResReservationListDto resReservationListDto = new ResReservationListDto();

                resReservationListDto.setReservationId(reservation.getReservationId());
                resReservationListDto.setFishingInfoId(reservation.getFishingInfo().getInfoId());
                resReservationListDto.setReservationDate(reservation.getReservationDate());
                resReservationListDto.setReservationName(reservation.getReservationName());
                resReservationListDto.setReservationNum(reservation.getReservationNum());
                resReservationListDto.setUserMessage(reservation.getUserMessage());
                resReservationListDto.setReservationStatus(reservation.getReservationStatus());
                resReservationListDto.setReservationPhone(reservation.getReservationPhone());
                resReservationListDto.setTotalPage(reservationPage.getTotalPages());
                resReservationListDto.setTotalElement(reservationPage.getTotalElements());

                reservationList.add(resReservationListDto);
            }
        }
        return reservationList;
    }

    /* 회원 예약 상태 변경 */
    @Override
    public ResResultDto userStatusUpdate(UserInfo userInfo, Long reservation_id, String status) {

        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        Reservation checkReservation = reservationRepository.findByUserInfoAndReservationId(checkUserInfo, reservation_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("예약 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findById(checkReservation.getFishingInfo().getInfoId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        /* 예약 취소로 상태 변경 후 출조 정보 예약인원 변경 */
        if(checkReservation.getReservationStatus().equals("예약완료") && status.equals("예약취소")) {

            fishingInfoRepository.updateReserveCancel(checkReservation.getReservationNum(), checkFishingInfo.getInfoId());

            FishingInfo newFishingInfo = fishingInfoRepository.findById(checkReservation.getFishingInfo().getInfoId())
                    .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

            if(newFishingInfo.getInfoCapacity() != 0) {

                fishingInfoRepository.updateInfoStatus("예약가능", newFishingInfo.getInfoId());
            }

            reservationRepository.deleteById(checkReservation.getReservationId());

        } else {
            return new ResResultDto(-1L,"변경할 수 없는 상태입니다.");
        }

        return new ResResultDto(checkReservation.getReservationId(), "예약을 취소 했습니다.");
    }
}
