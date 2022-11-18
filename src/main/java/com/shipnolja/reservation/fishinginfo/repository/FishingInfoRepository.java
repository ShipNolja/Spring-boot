package com.shipnolja.reservation.fishinginfo.repository;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.ship.model.ShipInfo;
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

public interface FishingInfoRepository extends JpaRepository<FishingInfo, Long> {

    /* 예약 등록 시 수용 인원 감소 */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update FishingInfo fi set fi.infoCapacity = fi.infoCapacity - :reservationNum where fi.infoId = :infoId")
    void updateReserveRegistration(@Param("reservationNum") int reservationNum,
                                   @Param("infoId") Long info_id);

    /* 예약 취소 시 수용 인원 증가 */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update FishingInfo fi set fi.infoCapacity = fi.infoCapacity + :reservationNum where fi.infoId = :infoId")
    void updateReserveCancel(@Param("reservationNum") int reservationNum,
                             @Param("infoId") Long info_id);



    /* 예약 마감 상태 변경 */
    @Transactional
    @Modifying
    @Query("update FishingInfo fi set fi.infoReservationStatus = :reservationStatus where fi.infoId = :infoId")
    void updateInfoStatus(@Param("reservationStatus") String reservationStatus,
                         @Param("infoId") Long info_id);



    /* 해당 선박의 출조 정보 목록 */
    Page<FishingInfo> findByShipInfo(ShipInfo shipInfo, Pageable pageable);

    /* 지역명 */
    Page<FishingInfo> findByShipInfo_AreaContaining(String content, Pageable pageable);

    /* 예약 상태 */
    Page<FishingInfo> findByInfoReservationStatusContaining(String content, Pageable pageable);

    /* 상세 지역 */
    Page<FishingInfo> findByShipInfo_DetailAreaContaining(String content, Pageable pageable);

    /* 항구명 */
    Page<FishingInfo> findByShipInfo_PortContaining(String content, Pageable pageable);

    /* 선박명 */
    Page<FishingInfo> findByShipInfo_NameContaining(String content, Pageable pageable);

    /* 어종 */
    Page<FishingInfo> findByInfoTargetContaining(String target, Pageable pageable);
    
    /* 출조 날짜 */
    Page<FishingInfo> findByInfoStartDate(LocalDate startDate, Pageable pageable);



    /* 사업자 마이페이지 */
    List<FishingInfo> findByShipInfo(ShipInfo shipInfo);

    Optional<FishingInfo> findByShipInfoAndInfoId(ShipInfo shipInfo, Long fishingInfo_id);
    Page<FishingInfo> findByShipInfoAndInfoStartDate(ShipInfo shipInfo, LocalDate startDate, Pageable pageable);
    Page<FishingInfo> findByShipInfoAndInfoReservationStatusContaining(ShipInfo shipInfo, String content, Pageable pageable);
}
