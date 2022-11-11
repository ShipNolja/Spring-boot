package com.shipnolja.reservation.ship.repository;

import com.shipnolja.reservation.ship.model.ShipInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<ShipInfo,Long> {
}
