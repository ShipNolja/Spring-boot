package com.shipnolja.reservation.category.repository;

import com.shipnolja.reservation.category.model.FishCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishCategoryRepository extends JpaRepository<FishCategory,Long> {
}
