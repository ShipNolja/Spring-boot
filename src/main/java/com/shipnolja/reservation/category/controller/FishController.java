package com.shipnolja.reservation.category.controller;

import com.shipnolja.reservation.category.model.FishCategory;
import com.shipnolja.reservation.category.repository.FishCategoryRepository;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"fishCategory - 단순 출력"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fish-category")
public class FishController {

    private final FishCategoryRepository fishCategoryRepository;

    @GetMapping("")
    public List<FishCategory> select(){
        return fishCategoryRepository.findAll();
    }
}
