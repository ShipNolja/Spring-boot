package com.shipnolja.reservation.fishinginfo.repository;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface FishingInfoRepository extends JpaRepository<FishingInfo, Long> {
    
    
    /* 출조 목록 검색 */
    @Query(value =
            "select fi from FishingInfo fi join fi.shipInfo si "
            + "where fi.infoTarget like %:target% "
            + "and si.area like %:area% "
            + "and si.detailArea like %:detailArea% "
            + "and si.port like %:port% "
            + "and si.name like %:shipName% "
            + "and fi.infoStartDate between :startDate and :endDate"
    )
    Page<FishingInfo> searchFishingInfoList(@Param("target") String target,
                                            @Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate,
                                            @Param("area") String area,
                                            @Param("detailArea") String detailArea,
                                            @Param("port") String port,
                                            @Param("shipName") String shipName,
                                            Pageable pageable);
}
