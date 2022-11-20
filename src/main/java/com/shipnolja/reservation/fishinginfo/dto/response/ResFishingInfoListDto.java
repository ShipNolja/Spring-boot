package com.shipnolja.reservation.fishinginfo.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ResFishingInfoListDto {

    private Long fishingInfoId;

    private Long shipInfoId;

    private String area;

    private String detailArea;

    private String port;

    private String shipName;

    private String Image;

    private String target;

    private LocalDate infoStartDate;

    private String infoReservationStatus;

    private int infoCapacity;

    private LocalTime infoStartTime;

    private LocalTime infoEndTime;

    private String infoNotice;

    private String infoMessage;

    private String infoAssemblePoint;

    private String infoStartPoint;

    private int totalPage;

    private long totalElement;
}
