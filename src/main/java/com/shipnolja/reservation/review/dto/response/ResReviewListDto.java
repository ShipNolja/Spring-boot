package com.shipnolja.reservation.review.dto.response;

import com.shipnolja.reservation.reservation.model.Reservation;
import com.shipnolja.reservation.user.model.UserInfo;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ResReviewListDto {

    private Long reviewId;

    private Long reservationId;

    private Long shipId;

    private Long fishingInfoId;

    private String reviewTitle;

    private String reviewContent;

    private LocalDateTime reviewCreate;

    private Double reviewRating;

    private LocalDateTime reviewUpdate;

    private int totalPage;

    private long totalElement;

}
