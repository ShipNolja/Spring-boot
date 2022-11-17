package com.shipnolja.reservation.ship.repository;

import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.user.model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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

    Optional<ShipInfo> findByUserInfo(UserInfo userInfo);

    //선박 평점
    @Query("select Avg(r.reviewRating) from Review r where r.reservation.fishingInfo.shipInfo = :shipInfo")
    Double findShipRating(@Param("shipInfo") ShipInfo shipInfo);
}
