package com.shipnolja.reservation.ship.dto.response;

import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.user.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResShipInfo {

    private Long id;

    //선장님 이름
    private String userName;
    
    //선장님 핸드폰 번호
    private String userPhone;

    private String registerNumber;

    private String image;

    private String name;

    private String bankName;

    private String bankNum;

    private String area;

    private String detailArea;

    private String port;

    private String streetAddress;

    private Double shipRatingAvg;

    //찜 카운트
    private long wishCount;

    public ResShipInfo(ShipInfo shipInfo,Long wishCount,Double shipRatingAvg) {
        this.id = shipInfo.getId();
        this.userName = shipInfo.getUserInfo().getName();
        this.userPhone = shipInfo.getUserInfo().getPhone();
        this.registerNumber = shipInfo.getRegisterNumber();
        this.image = shipInfo.getImage();
        this.name = shipInfo.getName();
        this.bankName = shipInfo.getBankName();
        this.bankNum = shipInfo.getBankNum();
        this.area = shipInfo.getArea();
        this.detailArea = shipInfo.getDetailArea();
        this.port = shipInfo.getPort();
        this.streetAddress = shipInfo.getStreetAddress();
        this.wishCount = wishCount;
        this.shipRatingAvg = shipRatingAvg;
    }
}
