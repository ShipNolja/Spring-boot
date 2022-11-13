package com.shipnolja.reservation.ship.dto.response;

import com.shipnolja.reservation.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResShipInfoList {

    private Long Id;

    private String image;

    private String name;

    private String area;

    private String detailArea;

    private String port;

    private String streetAddress;

    private int totalPage;

    private long totalElement;
}
