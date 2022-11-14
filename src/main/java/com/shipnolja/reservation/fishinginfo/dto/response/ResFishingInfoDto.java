package com.shipnolja.reservation.fishinginfo.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ResFishingInfoDto {

    private Long Id;

    private String area;

    private String detailArea;

    private String port;

    private String shipName;

    private String target;

    private LocalDateTime startDate;

    private LocalDateTime startTime;

    private LocalDateTime EndTime;

    private String infoReservationStatus;

    private Integer infoCapacity;

    private String Image;

    private int totalPage;

    private long totalElement;
}
