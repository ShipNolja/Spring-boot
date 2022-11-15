package com.shipnolja.reservation.reservation.service;

import com.shipnolja.reservation.reservation.dto.request.ReqFishingReserveDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

public interface ReservationService {

    /* 예약 등록 */
    ResResultDto fishingReserve(UserInfo userInfo, ReqFishingReserveDto reqFishingReserveDto, Long ship_id, Long info_id);
}
