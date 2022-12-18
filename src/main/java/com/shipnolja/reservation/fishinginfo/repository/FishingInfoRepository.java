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

    @Query(value = "select fi from FishingInfo fi " +
            "where fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchAll(@Param("startDate") LocalDate startDate, Pageable pageable);

    /* 지역명 */
    @Query(value = "select fi from FishingInfo fi join fi.shipInfo si " +
            "where si.area = :content and fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchArea(@Param("content") String content, @Param("startDate") LocalDate startDate,
                                                    Pageable pageable);

    /* 예약 상태 */
    @Query(value = "select fi from FishingInfo fi " +
            "where fi.infoReservationStatus = :content and fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchReservationStatus(@Param("content") String content, @Param("startDate") LocalDate startDate,
                                              Pageable pageable);

    /* 상세 지역 */
    @Query(value = "select fi from FishingInfo fi join fi.shipInfo si " +
            "where si.detailArea = :content and fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchDetailArea(@Param("content") String content, @Param("startDate") LocalDate startDate,
                                                          Pageable pageable);

    /* 항구명 */
    @Query(value = "select fi from FishingInfo fi join fi.shipInfo si " +
            "where si.port = :content and fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchPort(@Param("content") String content, @Param("startDate") LocalDate startDate,
                                 Pageable pageable);

    /* 선박명 */
    @Query(value = "select fi from FishingInfo fi join fi.shipInfo si " +
            "where si.name = :content and fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchName(@Param("content") String content, @Param("startDate") LocalDate startDate,
                                 Pageable pageable);

    /* 어종 */
    @Query(value = "select fi from FishingInfo fi " +
            "where fi.infoTarget = :target and fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchTarget(@Param("target") String target, @Param("startDate") LocalDate startDate,
                                             Pageable pageable);
    
    /* 출조 날짜 */
    @Query(value = "select fi from FishingInfo fi " +
            "where fi.infoStartDate = :content and fi.infoStartDate >= :startDate ")
    Page<FishingInfo> searchStartDate(@Param("content") LocalDate content, @Param("startDate") LocalDate startDate,
                                          Pageable pageable);



    /* 사업자 마이페이지 */
    List<FishingInfo> findByShipInfo(ShipInfo shipInfo);

    Optional<FishingInfo> findByShipInfoAndInfoId(ShipInfo shipInfo, Long fishingInfo_id);
    Page<FishingInfo> findByShipInfoAndInfoStartDate(ShipInfo shipInfo, LocalDate startDate, Pageable pageable);
    Page<FishingInfo> findByShipInfoAndInfoReservationStatus(ShipInfo shipInfo, String content, Pageable pageable);
}
