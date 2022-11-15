package com.shipnolja.reservation.reservation.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class ReqFishingReserveDto {

    @ApiModelProperty(value = "예약자 전화번호", example = "010-xxxx-xxxx", required = true)
    @NotBlank(message = "전화번호는 필수 입력입니다.")
    private String reservationPhone;

    @ApiModelProperty(value = "예약자 이름", example = "xxx", required = true)
    @NotBlank(message = "예약자 이름은 필수 입력입니다.")
    private String reservationName;

    @ApiModelProperty(value = "예약자 전달 사항", example = "배 멀미가 심해요~", required = false)
    private String userMessage;

    @ApiModelProperty(value = "예약 인원 수", example = "x명", required = true)
    @NotBlank(message = "예약 인원수는 필수 입력입니다.")
    private int reservationNum;
}
