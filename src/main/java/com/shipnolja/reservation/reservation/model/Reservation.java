package com.shipnolja.reservation.reservation.model;


import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.user.model.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Reservation {

    /* 예약 아이디 */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    /* 예약자 명 */
    @Column(name = "reservation_name")
    private String reservationName;

    /* 예약자 전화번호 */
    @Column(name = "reservation_phone")
    private String phone;

    /* 선박 전달사항 */
    @Column(name = "ship_message")
    private String shipMessage;

    /* 예약자 전달사항 */
    @Column(name = "user_message")
    private String userMessage;

    /* 예약 인원 */
    @Column(name = "reservation_num")
    private int reservationNum;

    /* 예약 상태 */
    @Column(name = "reservation_status")
    private String reservationStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "fishingInfo_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private FishingInfo fishingInfo;

    @Builder
    public Reservation(Long reservationId, String reservationName, String phone, String shipMessage, String userMessage,
                       int reservationNum, String reservationStatus, UserInfo userInfo, FishingInfo fishingInfo) {

        this.reservationId = reservationId;
        this.reservationName = reservationName;
        this.phone = phone;
        this.shipMessage = shipMessage;
        this.userMessage = userMessage;
        this.reservationNum = reservationNum;
        this.reservationStatus = reservationStatus;
        this.userInfo = userInfo;
        this.fishingInfo = fishingInfo;

    }
}
