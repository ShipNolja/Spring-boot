package com.shipnolja.reservation.reservation.repository;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationDate(LocalDate startDate);


    /* 회원 마이페이지 */
    
    /* 회원 예약상태에 따른 목록 */
    Page<Reservation> findByUserInfoAndReservationStatusContaining(UserInfo userInfo, String content, Pageable pageable);
    
    /* 회원 예약날짜에 따른 목록 */
    Page<Reservation> findByUserInfoAndReservationDate(UserInfo userInfo, LocalDate reservationDate, Pageable pageable);

    
    /* 사업자 마이페이지 */
    Page<Reservation> findByFishingInfoAndReservationName(FishingInfo fishingInfo, String reservationName);
}
