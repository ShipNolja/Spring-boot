package com.shipnolja.reservation.fishinginfo.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ResFishingInfoListDto {

    private Long Id;

    private String area;

    private String detailArea;

    private String port;

    private String shipName;

    private String target;

    private LocalDate infoStartDate;

    private LocalTime startTime;

    private LocalTime EndTime;

    private String infoReservationStatus;

    private int infoCapacity;

    private String Image;

    private int totalPage;

    private long totalElement;
}
