package com.shipnolja.reservation.review.service;

import com.shipnolja.reservation.review.dto.request.ReqReviewDto;
import com.shipnolja.reservation.review.dto.response.ResReviewListDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

import java.util.List;

public interface ReviewService {
    
    /* 후기 작성 */
    ResResultDto reviewWrite(UserInfo userInfo, Long reservation_id, ReqReviewDto reqReviewDto);

    /* 출조 목록 후기 조회 */
    List<ResReviewListDto> reviewList(Long ship_id, int page);

    /* 후기 수정 */
    ResResultDto reviewUpdate(UserInfo userInfo, ReqReviewDto reqReviewDto, Long reservation_id);

    /* 후기 삭제 */
    ResResultDto reviewDelete(UserInfo userInfo, Long reservation_id);
}
