package com.shipnolja.reservation.reservation.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Reservation - api - 모든 사용자가 이용 가능 (예약 조회, 목록)"})

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationPermitController {
}
