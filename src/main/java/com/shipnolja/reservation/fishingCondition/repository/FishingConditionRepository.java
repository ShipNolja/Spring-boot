package com.shipnolja.reservation.fishingCondition.repository;

import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishingConditionRepository extends JpaRepository<FishingCondition,Long> {
}
