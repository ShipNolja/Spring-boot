package com.shipnolja.reservation.wish.repository;

import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.wish.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Optional<Wish> findByUserInfoAndShipInfo(UserInfo userInfo, ShipInfo shipInfo);

    Page<Wish> findByUserInfo(UserInfo userInfo, Pageable pageable);

    Optional<Wish> findByIdAndUserInfo(Long wishId,UserInfo userInfo);

    Long countByShipInfo(ShipInfo shipInfo);
}
