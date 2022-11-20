package com.shipnolja.reservation.review.repository;

import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.review.model.Review;
import com.shipnolja.reservation.ship.model.ShipInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /* 예약 후 후기 작성 했는지 조회 */
    Optional<Review> findByReservation(Reservation reservation);

    /* 후기 목록 조회 */
    Page<Review> findByShipInfo(ShipInfo shipInfo, Pageable pageable);
}
