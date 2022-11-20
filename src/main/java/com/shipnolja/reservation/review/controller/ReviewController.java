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


    /* 후기 작성 */
    @ApiOperation(value = "후기 작성",notes = "예약에 대한 후기를 작성합니다.")
    @PostMapping("")
    public ResResultDto reviewWrite(@LoginUser UserInfo userInfo,
                                    @ApiParam(value = "예약 아이디", required = true) @RequestParam Long reservation_id,
                                    @ApiParam(value = "후기 정보를 갖는 객체", required = true) @RequestBody ReqReviewDto reqReviewDto) {

        return reviewService.reviewWrite(userInfo, reservation_id, reqReviewDto);
    }

    /* 후기 수정 */
    @ApiOperation(value = "후기 수정",notes = "예약에 대한 후기를 수정합니다.")
    @PutMapping("update")
    public ResResultDto reviewUpdate(@LoginUser UserInfo userInfo,
                                     @RequestBody ReqReviewDto reqReviewDto,
                                     @RequestParam Long reservation_id) {

        return reviewService.reviewUpdate(userInfo, reqReviewDto, reservation_id);
    }

    @ApiOperation(value = "후기 삭제",notes = "예약에 대한 후기를 삭제합니다.")
    @DeleteMapping("delete")
    public ResResultDto reviewDelete(@LoginUser UserInfo userInfo,
                                     @RequestParam Long reservation_id) {

        return reviewService.reviewDelete(userInfo, reservation_id);
    }
}
