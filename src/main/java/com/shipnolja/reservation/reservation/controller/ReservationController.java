package com.shipnolja.reservation.reservation.controller;

import com.shipnolja.reservation.reservation.dto.request.ReqFishingReserveDto;
import com.shipnolja.reservation.reservation.service.ReservationService;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"Reservation - api - 모든 사용자가 이용 가능 (예약, 수정, 취소)"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    /* 예약 등록 */
    @ApiOperation(value = "예약 등록",notes = "출조 예약을 등록합니다.")
    @PostMapping("")
    public ResResultDto fishingReserve(@LoginUser UserInfo userInfo,
                                       @ApiParam(value = "예약 정보를 갖는 객체", required = true) @RequestBody ReqFishingReserveDto reqFishingReserveDto,
                                       @ApiParam(value = "선상 아이디", required = true) @RequestParam Long ship_id,
                                       @ApiParam(value = "출조 정보 아이디", required = true) @RequestParam Long info_id) {

        return reservationService.fishingReserve(userInfo, reqFishingReserveDto, ship_id, info_id);
    }

}
