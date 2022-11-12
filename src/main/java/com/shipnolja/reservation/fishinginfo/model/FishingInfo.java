package com.shipnolja.reservation.fishinginfo.model;

import com.shipnolja.reservation.ship.model.ShipInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class FishingInfo {

    /* 아이디 */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long infoId;

    /* 공지사항 */
    @Column(name = "info_notice")
    private String infoNotice;

    /* 대상어종 */
    @Column(name = "info_target")
    private String infoTarget;

    /* 집결지 */
    @Column(name = "info_assemble_point")
    private String infoAssemblePoint;

    /* 출항지 */
    @Column(name = "info_start_point")
    private String infoStartPoint;

    /* 출항 시간 */
    @Column(name = "info_start_time")
    private LocalDateTime infoStartTime;
    
    /* 예약 상태 */
    @Enumerated(value = EnumType.STRING)
    @Column(name = "into_reservation_status")
    private reservationStatus infoReservationStatus;

    /* 선박 정보 */
    @ManyToOne
    @JoinColumn(name = "ship_Info")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShipInfo shipInfo;


    @Builder
    public FishingInfo(Long infoId, String infoNotice, String infoTarget, String infoAssemblePoint, String infoStartPoint,
                       LocalDateTime infoStartTime, reservationStatus infoReservationStatus, ShipInfo shipInfo) {

        this.infoId = infoId;
        this.infoNotice = infoNotice;
        this.infoTarget = infoTarget;
        this.infoAssemblePoint = infoAssemblePoint;
        this.infoStartPoint = infoStartPoint;
        this.infoStartTime = infoStartTime;
        this.infoReservationStatus = infoReservationStatus;
        this.shipInfo = shipInfo;

    }
}
