package com.shipnolja.reservation.ship.dto.response;

import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResManagerShipInfo {

    // ------ 사용자 정보 ------
    private Long id;

    private String userId;

    //선장님 이름
    private String userName;

    //선장님 핸드폰 번호
    private String userPhone;

    //권한
    private UserRole role;



    // ------ 선박 정보 ------
    //선박 정보
    private Long shipId;

    private String registerNumber;

    private String image;

    private String name;

    private String bankName;

    private String bankNum;

    private String area;

    private String detailArea;

    private String port;

    private String streetAddress;

    public ResManagerShipInfo(ShipInfo shipInfo) {
        this.id = shipInfo.getUserInfo().getId();
        this.userId = shipInfo.getUserInfo().getUserid();
        this.userName = shipInfo.getUserInfo().getName();
        this.userPhone = shipInfo.getUserInfo().getPhone();
        this.role = shipInfo.getUserInfo().getRole();

        this.shipId = shipInfo.getId();
        this.registerNumber = shipInfo.getRegisterNumber();
        this.image = shipInfo.getImage();
        this.name = shipInfo.getName();
        this.bankName = shipInfo.getBankName();
        this.bankNum = shipInfo.getBankNum();
        this.area = shipInfo.getArea();
        this.detailArea = shipInfo.getDetailArea();
        this.port = shipInfo.getPort();
        this.streetAddress = shipInfo.getStreetAddress();
    }
}
