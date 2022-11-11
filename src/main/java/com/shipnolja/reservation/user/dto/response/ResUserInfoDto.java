package com.shipnolja.reservation.user.dto.response;

import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserInfoDto {
    private Long id;
    private String userid;
    private String password;
    private String name;
    private String phone;
    private UserRole role;



    public ResUserInfoDto(UserInfo userInfo) {
        this.id = userInfo.getId();
        this.userid = userInfo.getUserid();
        this.password = userInfo.getPassword();
        this.name = userInfo.getName();
        this.phone = userInfo.getPhone();
        this.role = userInfo.getRole();

    }
}
