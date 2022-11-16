package com.shipnolja.reservation.review.repository;

import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /* 예약에 대한 후기 조회 */
    Optional<Review> findByReservation(Reservation reservation);
}
