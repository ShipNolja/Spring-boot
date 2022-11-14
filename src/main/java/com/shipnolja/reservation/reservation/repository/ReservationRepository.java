package com.shipnolja.reservation.reservation.repository;

import com.shipnolja.reservation.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
