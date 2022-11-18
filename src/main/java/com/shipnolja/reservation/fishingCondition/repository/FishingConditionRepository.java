package com.shipnolja.reservation.fishingCondition.repository;

import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface FishingConditionRepository extends JpaRepository<FishingCondition,Long> {

    @Query(value = "select f from FishingCondition f " +
            "where f.fish like %:fish% " +
            "and f.title like %:title% " +
            "and f.date between " +
            ":startDate And :endDate"
    )
    Page<FishingCondition> findByFishContainingAndTitleContainingAndDateBetween(String fish, String title, LocalDate startDate,LocalDate endDate, Pageable pageable);
}
