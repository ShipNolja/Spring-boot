package com.shipnolja.reservation.fishingCondition.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class FishingConditionDto {
    //제목
    @ApiModelProperty(value = "제목", example = "요즘 조황이 좋습니다!", required = true)
    private String title;

    //내용
    @ApiModelProperty(value = "게시글 내용", example = "이마이 커요 요즘 파도도 잔잔하고 아주 좋네요", required = true)
    private String content;

    //낚시한 날짜(xx일 조황 정보입니다)
    @ApiModelProperty(value = "사진속 출조 날짜", example = "20220411", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    //어종
    @ApiModelProperty(value = "어종", example = "광어", required = true)
    private String fish;
}
