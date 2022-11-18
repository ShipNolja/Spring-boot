package com.shipnolja.reservation.user.controller;

import com.shipnolja.reservation.ship.dto.request.ShipInfoDto;
import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.dto.response.ResUserInfoDto;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.service.UserService;
import com.shipnolja.reservation.util.S3FileUploadService;

import com.shipnolja.reservation.util.responseDto.ResResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Api(tags = {"User - ROLE_USER"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "내 정보 조회",notes = "내 정보를 조회 합니다.")
    @GetMapping("")
    public ResUserInfoDto check(@LoginUser UserInfo userInfo){
        return new ResUserInfoDto(userService.userInfoCheck(userInfo));
    }

    @PostMapping("/test")
    public ResResultDto userResponseTest() {
        return new ResResultDto(1L , "성공!");
    }

    @ApiOperation(value = "선박 등록",notes = "선박을 등록합니다.")
    @PostMapping(value = "",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResResultDto shipRegistration(@LoginUser UserInfo userInfo,
                                         @ApiParam(value = "선박 가입 정보 DTO", required = true) @RequestPart(value = "dto") ShipInfoDto shipInfoDto,
                                         @ApiParam(value = "선박 이미지", required = false) @RequestPart(value = "image") List<MultipartFile> files){

        Long result = userService.shipRegistration(userInfo,shipInfoDto,files);
        if (result==-1L){
            return new ResResultDto(result,"파일이 없습니다.");
        }
        else if(result==-2L){
            return new ResResultDto(result,"png,jpeg,jpg 확장자만 사용 가능합니다.");
        }

        return new ResResultDto(result,"선박 등록을 성공했습니다.");

    }
}
