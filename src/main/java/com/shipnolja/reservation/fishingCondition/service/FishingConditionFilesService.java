package com.shipnolja.reservation.fishingCondition.service;

import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FishingConditionFilesService {
    Long uploadFile(List<MultipartFile> files , FishingCondition fishingCondition);
}
