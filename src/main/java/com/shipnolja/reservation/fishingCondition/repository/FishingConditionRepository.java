package com.shipnolja.reservation.fishingCondition.repository;

import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FishingConditionRepository extends JpaRepository<FishingCondition,Long> {
    Page<FishingCondition> findByFishContainingAndDateAndTitleContaining(String fish, LocalDate date, String title, Pageable pageable);
}
