package com.shipnolja.reservation.review.controller;

import com.shipnolja.reservation.review.dto.request.ReqReviewDto;
import com.shipnolja.reservation.review.service.ReviewService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Review - api - 모든 사용자가 이용 가능 (작성, 수정, 삭제)"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/review")
public class ReviewController {

    private final ReviewService reviewService;

    @ApiOperation(value = "후기 작성",notes = "예약에 대한 후기를 작성합니다.")
    @PostMapping("")
    public ResResultDto reviewWrite(@LoginUser UserInfo userInfo,
                                    @ApiParam(value = "예약 아이디", required = true) @RequestParam Long reservationId,
                                    @ApiParam(value = "출조 정보 아이디", required = true) @RequestParam Long fishingInfoId,
                                    @ApiParam(value = "후기 정보를 갖는 객체", required = true) @RequestBody ReqReviewDto reqReviewDto) {

        return reviewService.reviewWrite(userInfo, reservationId, fishingInfoId, reqReviewDto);
    }
}
