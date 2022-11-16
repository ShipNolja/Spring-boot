package com.shipnolja.reservation.reservation.service.Impl;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.fishinginfo.repository.FishingInfoRepository;
import com.shipnolja.reservation.reservation.dto.request.ReqFishingReserveDto;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.reservation.repository.ReservationRepository;
import com.shipnolja.reservation.reservation.service.ReservationService;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
@RequiredArgsConstructor
public class ReservationImpl implements ReservationService {

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final FishingInfoRepository fishingInfoRepository;
    private final ReservationRepository reservationRepository;


    /* 예약 등록 */
    @Override
    public ResResultDto fishingReserve(UserInfo userInfo, ReqFishingReserveDto reqFishingReserveDto, Long ship_id, Long info_id) {
        
        UserInfo checkUserInfo = userRepository.findById(userInfo.getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다."));

        ShipInfo checkShipInfo = shipRepository.findById(ship_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findById(info_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        /* 같은 날짜에 중복 예약 못하고, 예약마감 상태인 경우 예약 불가 */
        if(reservationRepository.findByReservationDate(checkFishingInfo.getInfoStartDate()).isPresent()) {

            return new ResResultDto(-1L,"해당 날짜에 이미 예약이 있습니다 취소 후 예약 해주세요"); //완
        } else if(checkFishingInfo.getInfoReservationStatus().equals("예약마감")) {

            return new ResResultDto(-2L,"예약이 마감 되었습니다 다른 날짜에 이용해주세요"); //완
        } else if(checkFishingInfo.getInfoCapacity() < reqFishingReserveDto.getReservationNum()) {

            return new ResResultDto(-3L,"남은 예약 인원보다 많은 인원은 예약이 불가능 합니다 인원 수를 확인 해주세요"); //완
        }

        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .reservationPhone(reqFishingReserveDto.getReservationPhone())
                        .reservationName(reqFishingReserveDto.getReservationName())
                        .reservationNum(reqFishingReserveDto.getReservationNum())
                        .reservationDate(checkFishingInfo.getInfoStartDate())
                        .fishingInfo(checkFishingInfo)
                        .userInfo(checkUserInfo)
                        .userMessage(reqFishingReserveDto.getUserMessage())
                        .build()
        );

        /* 출조 정보 수용인원을 예약 인원 만큼 - 
        *  1차 캐시 최신화 */
        fishingInfoRepository.updateReserveRegistration(reqFishingReserveDto.getReservationNum(),checkFishingInfo.getInfoId());

        /* 다시 재 조회 해서 수용인원 0명이면 예약 마감으로 상태 변경 */
        FishingInfo newFishingInfo = fishingInfoRepository.findById(info_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));


        if(newFishingInfo.getInfoCapacity() == 0) {

            fishingInfoRepository.updateStatusOff("예약마감", newFishingInfo.getInfoId());
        }

        return new ResResultDto(reservation.getReservationId(), "예약을 완료 했습니다.");
    }
}
