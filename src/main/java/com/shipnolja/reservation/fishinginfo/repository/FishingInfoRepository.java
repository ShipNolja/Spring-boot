package com.shipnolja.reservation.fishinginfo.repository;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.ship.model.ShipInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FishingInfoRepository extends JpaRepository<FishingInfo, Long> {

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
    
    /* 출조 날짜 */
    Page<FishingInfo> findByInfoStartDate(LocalDate startDate, Pageable pageable);

}
