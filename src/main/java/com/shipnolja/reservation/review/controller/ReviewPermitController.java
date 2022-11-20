package com.shipnolja.reservation.review.controller;

import com.shipnolja.reservation.review.dto.response.ResReviewListDto;
import com.shipnolja.reservation.review.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"Review - api - 모든 이용자가 이용 가능 (목록 조회)"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewPermitController {

    private final ReviewService reviewService;

    /* 후기 목록 */
    @GetMapping("/reviewList")
    public List<ResReviewListDto> reviewList(@ApiParam(value = "선박 아이디", required = true) @RequestParam Long ship_id,
                                             @ApiParam(value = "페이지 번호", required = true) int page) {

        return reviewService.reviewList(ship_id, page);
    }
}
