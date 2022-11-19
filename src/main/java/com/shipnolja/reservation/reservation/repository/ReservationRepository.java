package com.shipnolja.reservation.reservation.repository;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByReservationDate(LocalDate startDate);

    List<Reservation> findByFishingInfo(FishingInfo fishingInfo);

    /* 회원 마이페이지 */

    Optional<Reservation> findByUserInfoAndReservationId(UserInfo userInfo, Long reservation_id);
    
    /* 회원 예약상태에 따른 목록 */
    Page<Reservation> findByUserInfoAndReservationStatusContaining(UserInfo userInfo, String content, Pageable pageable);
    
    /* 회원 예약날짜에 따른 목록 */
    Page<Reservation> findByUserInfoAndReservationDate(UserInfo userInfo, LocalDate reservationDate, Pageable pageable);

    
    /* 사업자 마이페이지 */

    /* 예약자명 */
    Page<Reservation> findByFishingInfoAndReservationNameContaining(FishingInfo fishingInfo, String reservationName, Pageable pageable);
    /* 예약날짜 */
    Page<Reservation> findByFishingInfoAndReservationDate(FishingInfo fishingInfo, LocalDate reservationDate, Pageable pageable);
    /* 예약전체 */
    Page<Reservation> findByFishingInfo(FishingInfo fishingInfo, Pageable pageable);
    /* 예약상태 */
    Page<Reservation> findByFishingInfoAndReservationStatus(FishingInfo fishingInfo, String content, Pageable pageable);

    Optional<Reservation> findByFishingInfoAndReservationId(FishingInfo fishingInfo, Long reservation_id);

    @Transactional
    @Modifying
    @Query(value = "update Reservation r set r.reservationStatus = :reservationStatus where r.reservationId = :reservationId")
    void statusUpdate(@Param("reservationStatus") String reservationStatus,
                      @Param("reservationId") Long reservation_id);

}

