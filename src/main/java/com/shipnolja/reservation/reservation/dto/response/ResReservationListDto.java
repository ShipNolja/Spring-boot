package com.shipnolja.reservation.reservation.dto.response;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ResReservationListDto {

    private Long fishingInfoId;

    private Long reservationId;

    private LocalDate reservationDate;

    private String reservationStatus;

    private String reservationName;

    private int reservationNum;

    private String reservationPhone;

    private String userMessage;

    private int totalPage;

    private long totalElement;

}
