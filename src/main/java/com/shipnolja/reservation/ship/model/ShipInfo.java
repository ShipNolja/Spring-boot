package com.shipnolja.reservation.ship.model;

import com.shipnolja.reservation.user.model.UserInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor
public class ShipInfo {

    //선박아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    //유저 정보 외래키
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn
    private UserInfo userInfo;

    //선박 등록 번호
    @Column(name = "register_number")
    private String registerNumber;

    //선박 대표 이미지
    @Column(name = "image")
    private String image;

    //선박이름
    @Column(name = "name")
    private String name;

    //은행명
    @Column(name = "bank_name")
    private String bankName;

    //계좌번호
    @Column(name = "bank_num")
    private String bankNum;

    //지역
    @Column(name = "area")
    private String area;
    
    //세부지역
    @Column(name = "detail_area")
    private String detailArea;
    
    //항구
    @Column(name = "port")
    private String port;

    //도로명 주소
    @Column(name = "street_address")
    private String streetAddress;

    @Builder
    public ShipInfo( UserInfo userInfo, String registerNumber,String image ,String name, String bankName,
                     String bankNum, String area, String detailArea, String port, String streetAddress) {
        this.userInfo = userInfo;
        this.registerNumber = registerNumber;
        this.image = image;
        this.name = name;
        this.bankName = bankName;
        this.bankNum = bankNum;
        this.area = area;
        this.detailArea = detailArea;
        this.port = port;
        this.streetAddress = streetAddress;
    }
}
