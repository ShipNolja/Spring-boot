package com.shipnolja.reservation.ship.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShipInfoDto {

    //선박 등록 번호
    private String registerNumber;

    //선박이름
    private String name;

    //은행명
    private String bankName;

    //계좌번호
    private String bankNum;

    //지역
    private String area;

    //세부지역
    private String detailArea;

    //항구
    private String port;
}
