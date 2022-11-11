package com.shipnolja.reservation.config.token.repository;


import com.shipnolja.reservation.config.token.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepo extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByUserPk(Long UserPk);
}
