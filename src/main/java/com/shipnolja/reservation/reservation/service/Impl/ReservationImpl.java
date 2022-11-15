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
import org.springframework.stereotype.Service;

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

        /* 같은 유저가 같은 출조 정보에 2번 예약 못하게 */
        /* 출조 정보 수용인원 -1 */
        /* 수용인원 0명이면 출조 정보 예약 상태를 마감으로 */

        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .reservationPhone(reqFishingReserveDto.getReservationPhone())
                        .reservationName(reqFishingReserveDto.getReservationName())
                        .reservationNum(reqFishingReserveDto.getReservationNum())
                        .fishingInfo(checkFishingInfo)
                        .userInfo(checkUserInfo)
                        .userMessage(reqFishingReserveDto.getUserMessage())
                        .build()
        );

        return new ResResultDto(reservation.getReservationId(),
                checkFishingInfo.getInfoStartDate() + "예약을 완료 했습니다.");
    }
}
