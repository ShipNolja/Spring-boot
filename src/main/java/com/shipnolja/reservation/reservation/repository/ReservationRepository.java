package com.shipnolja.reservation.reservation.repository;

import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationDate(LocalDate startDate);
}
