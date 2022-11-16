package com.shipnolja.reservation.review.service;

import com.shipnolja.reservation.review.dto.request.ReqReviewDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

public interface ReviewService {
    ResResultDto reviewWrite(UserInfo userInfo, Long reservationId, Long fishingInfoId, ReqReviewDto reqReviewDto);
}
