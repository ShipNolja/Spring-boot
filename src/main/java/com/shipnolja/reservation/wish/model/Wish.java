package com.shipnolja.reservation.wish.model;

import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.user.model.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JoinColumn(name = "user_info")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserInfo userInfo;

    @JoinColumn(name = "ship_info")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShipInfo shipInfo;

    @Builder
    public Wish(Long id, UserInfo userInfo, ShipInfo shipInfo) {
        this.id = id;
        this.userInfo = userInfo;
        this.shipInfo = shipInfo;
    }
}
