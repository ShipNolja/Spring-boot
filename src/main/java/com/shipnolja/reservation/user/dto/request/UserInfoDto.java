package com.shipnolja.reservation.user.dto.request;


import com.shipnolja.reservation.user.model.UserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    //일반사용자 Dto

    @ApiModelProperty(value = "회원 아이디", example = "xxx@xxxx", required = true)
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다")
    private String userid;

    @ApiModelProperty(value = "비밀번호", example = "영문자,숫자,특수기호 포함 6~12자까지", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
            message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
    private String password;

    @ApiModelProperty(value = "회원 이름", example = "20자길이 제한", required = true)
    @NotBlank(message = "사용자명은 필수 입력 값입니다.")
    @Size(max = 20, message = "이름의 최대 길이는 20자 입니다.")
    private String name;

    @ApiModelProperty(value = "회원 핸드폰번호", example = "01012341234", required = true)
    @NotBlank(message = "핸드폰번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^[0-9]+$", message = "핸드폰번호는 숫자만 허용합니다.")
    private String phone;




}
