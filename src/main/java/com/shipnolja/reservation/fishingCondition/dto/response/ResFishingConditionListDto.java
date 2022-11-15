package com.shipnolja.reservation.fishingCondition.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ResFishingConditionListDto {

    private Long id;

    //제목
    private String title;

    //낚시한 날짜(xx일 조황 정보입니다)
    private String date;

    //어종
    private String fish;

    private String file;
}
