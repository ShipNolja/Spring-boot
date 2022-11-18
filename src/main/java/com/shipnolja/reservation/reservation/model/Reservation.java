package com.shipnolja.reservation.reservation.model;


import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.user.model.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

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
    private String reservationPhone;

    /* 예약자 전달사항 */
    @Column(name = "user_message")
    private String userMessage;

    /* 예약 인원 */
    @Column(name = "reservation_num")
    private int reservationNum;
    
    /* 예약 날짜 */
    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    /* 예약 상태 
    * 예약 취소(db 삭제), 예약 완료, 방문 완료
    */
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

    /* insert시 예약완료 자동 입력 */
    @PrePersist
    public void status() {
        this.reservationStatus = "예약완료";
    }

    @Builder
    public Reservation(Long reservationId, String reservationName, String reservationPhone, String userMessage,
                       LocalDate reservationDate, int reservationNum, String reservationStatus, UserInfo userInfo, FishingInfo fishingInfo) {

        this.reservationId = reservationId;
        this.reservationName = reservationName;
        this.reservationPhone = reservationPhone;
        this.userMessage = userMessage;
        this.reservationNum = reservationNum;
        this.reservationDate = reservationDate;
        this.reservationStatus = reservationStatus;
        this.userInfo = userInfo;
        this.fishingInfo = fishingInfo;

    }
}
