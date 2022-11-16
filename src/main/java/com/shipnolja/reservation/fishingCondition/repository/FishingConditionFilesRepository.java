package com.shipnolja.reservation.fishingCondition.repository;

import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import com.shipnolja.reservation.fishingCondition.model.FishingConditionFiles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FishingConditionFilesRepository extends JpaRepository<FishingConditionFiles,Long> {

    Optional<FishingConditionFiles> findFirstByFishingCondition(FishingCondition fishingCondition);

    List<FishingConditionFiles> findPathByFishingCondition(FishingCondition fishingCondition);


}
