package com.shipnolja.reservation.fishingCondition.model;


import com.shipnolja.reservation.ship.model.ShipInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class FishingCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "shipInfo")
    private ShipInfo shipInfo;

    //제목
    @Column(name = "title")
    private String title;

    //내용
    @Column(name = "content")
    private String content;

    //낚시한 날짜(xx일 조황 정보입니다)
    @Column(name = "date")
    private LocalDate date;

    //어종
    @Column(name = "fish")
    private String fish;
    
    //게시글 업로드 날짜
    @Column(name = "upload_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime uploadDate;

    @Builder
    public FishingCondition( ShipInfo shipInfo, String title, String content, LocalDate date, String fish, LocalDateTime uploadDate) {
        this.shipInfo = shipInfo;
        this.title = title;
        this.content = content;
        this.date = date;
        this.fish = fish;
        this.uploadDate = uploadDate;
    }
}
