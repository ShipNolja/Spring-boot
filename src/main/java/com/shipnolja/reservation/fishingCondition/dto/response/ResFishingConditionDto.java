package com.shipnolja.reservation.fishingCondition.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResFishingConditionDto {

    private Long id;

    //제목
    private String title;

    //내용
    private String content;

    //낚시한 날짜(xx일 조황 정보입니다)
    private String date;

    //어종
    private String fish;

    //파일
    private List<Map<String,Object>> files;
}
