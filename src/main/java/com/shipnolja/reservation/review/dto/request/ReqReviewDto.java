package com.shipnolja.reservation.review.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ReqReviewDto {

    @ApiModelProperty(value = "후기 제목", example = "후기 제목", required = true)
    @NotBlank(message = "후기 제목은 필수 입력입니다.")
    private String reviewTitle;

    @ApiModelProperty(value = "후기 내용", example = "너무 재밌었어요~", required = true)
    @NotBlank(message = "후기 내용은 필수 입력입니다.")
    private String reviewContent;

    @ApiModelProperty(value = "후기 별점", example = "0.5부터 5.0까지 0.5점씩 +", required = true)
    @NotBlank(message = "후기 별점은 필수 입력입니다.")
    private Double reviewRating;
}
