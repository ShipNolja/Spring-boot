package com.shipnolja.reservation.ship.repository;

import com.shipnolja.reservation.ship.model.ShipInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipRepository extends JpaRepository<ShipInfo,Long> {
    @Query(value = "select s from ShipInfo s " +
            "where s.name like %:shipName% " +
            "and s.port like %:port% " +
            "and s.area like %:area% " +
            "and s.detailArea like %:detailArea%")
    Page<ShipInfo> findShipInfoList(@Param("shipName") String shipName,
                                    @Param("port") String port,
                                    @Param("area")String area,
                                    @Param("detailArea") String detailArea,
                                    Pageable pageable);
}
