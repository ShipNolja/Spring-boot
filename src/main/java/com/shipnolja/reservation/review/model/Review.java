package com.shipnolja.reservation.review.model;

import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.user.model.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "review_title")
    private String reviewTitle;

    @Column(name = "review_content")
    private String reviewContent;

    @CreationTimestamp
    private LocalDateTime reviewCreate;

    @UpdateTimestamp
    private LocalDateTime reviewUpdate;

    @Column(name = "review_rating")
    private Double reviewRating;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "shipinfo_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShipInfo shipInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserInfo userInfo;

    @Builder
    public Review(Long reviewId, String reviewTitle, String reviewContent, LocalDateTime reviewCreate, LocalDateTime reviewUpdate,
                  Double reviewRating, Reservation reservation, ShipInfo shipInfo, UserInfo userInfo) {

        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.reviewCreate = reviewCreate;
        this.reviewUpdate = reviewUpdate;
        this.reviewRating = reviewRating;
        this.reservation = reservation;
        this.shipInfo = shipInfo;
        this.userInfo = userInfo;
    }
}
