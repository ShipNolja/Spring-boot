package com.shipnolja.reservation.util.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResResultDto {
    private Long id;
    private String message;

    public ResResultDto(Long id , String message) {
        this.id = id;
        this.message = message;
    }

}
