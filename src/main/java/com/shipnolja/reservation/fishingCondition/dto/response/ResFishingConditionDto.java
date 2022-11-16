package com.shipnolja.reservation.fishingCondition.dto.response;

import com.shipnolja.reservation.fishingCondition.model.FishingConditionFiles;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
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
    private LocalDate date;

    //어종
    private String fish;

    //파일
    private List<Map<String, Object>>  files;

    @Builder

    public ResFishingConditionDto(Long id, String title, String content, LocalDate date, String fish, List<Map<String, Object>>  files) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.fish = fish;
        this.files = files;
    }
}
