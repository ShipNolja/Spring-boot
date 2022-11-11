package com.shipnolja.reservation.ship.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ShipInfoDto {

    @ApiModelProperty(value = "선박 등록 번호", example = "20203030", required = true)
    private String registerNumber;

    @ApiModelProperty(value = "배 이름", example = "써니호", required = true)
    private String name;

    @ApiModelProperty(value = "은행명", example = "국민", required = true)
    private String bankName;

    @ApiModelProperty(value = "계좌번호", example = "940020020404", required = true)
    private String bankNum;

    @ApiModelProperty(value = "지역", example = "강원도", required = true)
    private String area;

    @ApiModelProperty(value = "세부지역", example = "속초", required = true)
    private String detailArea;

    @ApiModelProperty(value = "항구", example = "대포항", required = true)
    private String port;
}
