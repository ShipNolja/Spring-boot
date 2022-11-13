package com.shipnolja.reservation.wish.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResWishListDto {
    //즐겨찾기 아이디
    private Long wishId;

    //-----------------선박정보------------------
    private String image;

    private String name;

    private String area;

    private String detailArea;

    private String port;

    private int totalPage;

    private long totalElement;

}
