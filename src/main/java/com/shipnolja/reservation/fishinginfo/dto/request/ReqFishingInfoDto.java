package com.shipnolja.reservation.fishinginfo.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class ReqFishingInfoDto {


    /* 공지사항 */
    @ApiModelProperty(value = "공지사항", example = "xx어종 출조 모집, 낚시대 대여 가능 등")
    private String infoNotice;
    
    /* 대상어종 */
    @ApiModelProperty(value = "대상어종", example = "광어, 우럭, 갑오징어 등", required = true)
    @NotBlank(message = "대상어종은 필수 입력입니다.")
    private String infoTarget;

    /* 출항시간 */
    @ApiModelProperty(value = "출항시간", example = "06:00 ~ 14:30", required = true)
    private LocalDateTime infoStartTime;

    /* 출항지 */
    @ApiModelProperty(value = "출항지", example = "인천 중구 항동7가", required = true)
    private String infoStartPoint;

    /* 예약상태 */
    @ApiModelProperty(value = "예약 상태", example = "예약 가능, 예약 마감", required = true)
    @NotBlank(message = "예약 상태는 필수 입력입니다.")
    private String infoReservationStatus;

    /* 집결지 */
    @ApiModelProperty(value = "집결지", example = "인천 중구 항동7가", required = true)
    private String infoAssemblePoint;

    /* 수용인원 */
    @ApiModelProperty(value = "수용 인원", example = "xx명", required = true)
    private Integer infoCapacity;

    /* 출항일시 */
    @ApiModelProperty(value = "출항 일시", example = "xx월 xx일(x요일) or (일)", required = true)
    private LocalDateTime infoStartDate;
}
