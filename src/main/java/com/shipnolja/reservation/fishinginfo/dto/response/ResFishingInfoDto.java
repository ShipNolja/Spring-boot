package com.shipnolja.reservation.fishinginfo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter

public class ResFishingInfoDto {

    List<Map<String, Object>> fishingInfoList;
}
