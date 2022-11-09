package com.shipnolja.reservation.user.dto.response;

import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import lombok.Getter;

@Getter
public class ResLoginDto {
    private final Long userId;
    private final UserRole roles;


    public ResLoginDto(UserInfo userInfo){
        this.userId = userInfo.getId();
        this.roles = userInfo.getRole();
    }

}
