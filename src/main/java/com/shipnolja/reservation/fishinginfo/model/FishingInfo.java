package com.shipnolja.reservation.fishinginfo.model;

import com.shipnolja.reservation.ship.model.ShipInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
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
    
    /* 수용 인원 */
    @Column(name = "info_capacity")
    private Integer infoCapacity;

    /* 출항 시간 */
    @Column(name = "info_start_time")
    private LocalDateTime infoStartTime;
    
    /* 출항 일시 */
    @Column(name = "info_start_date")
    private LocalDateTime infoStartDate;
    
    /* 예약 상태 */
    /* 에약 가능, 예약 마감 */
    @Column(name = "into_reservation_status")
    private String infoReservationStatus;

    /* 선박 정보 */
    @ManyToOne
    @JoinColumn(name = "ship_Info")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShipInfo shipInfo;


    @Builder
    public FishingInfo(Long infoId, String infoNotice, String infoTarget, String infoAssemblePoint, String infoStartPoint,
                       Integer infoCapacity, LocalDateTime infoStartTime, LocalDateTime infoStartDate, String infoReservationStatus, ShipInfo shipInfo) {

        this.infoId = infoId;
        this.infoNotice = infoNotice;
        this.infoTarget = infoTarget;
        this.infoAssemblePoint = infoAssemblePoint;
        this.infoStartPoint = infoStartPoint;
        this.infoCapacity = infoCapacity;
        this.infoStartTime = infoStartTime;
        this.infoStartDate = infoStartDate;
        this.infoReservationStatus = infoReservationStatus;
        this.shipInfo = shipInfo;

    }
}
