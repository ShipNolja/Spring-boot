package com.shipnolja.reservation.fishingCondition.model;


import com.shipnolja.reservation.ship.model.ShipInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class FishingConditionFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "fishing_condition")
    private FishingCondition fishingCondition;

    //저장되는 파일명
    @Column(name = "save_name")
    private String saveName;

    //저장되는 파일 경로
    @Column(name = "path")
    private String path;

    //원본 파일명
    @Column(name = "origin")
    private String origin;

    @Builder
    public FishingConditionFiles(Long id, FishingCondition fishingCondition, String saveName, String path, String origin) {
        this.id = id;
        this.fishingCondition = fishingCondition;
        this.saveName = saveName;
        this.path = path;
        this.origin = origin;
    }
}
