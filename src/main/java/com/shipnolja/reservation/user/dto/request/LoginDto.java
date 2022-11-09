package com.shipnolja.reservation.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @ApiModelProperty(value = "회원 아이디", example = "xxx@xxxx", required = true)
    private String userId;

    @ApiModelProperty(value = "비밀번호", example = "영문자,숫자,특수기호 포함 6~12자까지", required = true)
    private String password;
}
