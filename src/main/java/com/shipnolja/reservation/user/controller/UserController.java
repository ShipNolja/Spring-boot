package com.shipnolja.reservation.user.controller;

import com.shipnolja.reservation.reservation.dto.response.ResReservationListDto;
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

    /* 회원 예약 목록 조회 */
    @ApiOperation(value = "회원 예약 목록 조회",notes = "회원이 예약한 목록을 조회합니다.")
    @GetMapping("/reservationList")
    public List<ResReservationListDto> userReservationList(@LoginUser UserInfo userInfo,
                                                           @ApiParam(value = "정렬 방법 : asc , desc", required = true) @RequestParam String sortMethod,
                                                           @ApiParam(value = "검색 기준 : 예약상태, 예약날짜", required = true) @RequestParam String searchBy,
                                                           @ApiParam(value = "검색값 : 예약완료, 예약취소, 방문완료, xxxx(년)-xx(월)-xx(일)", required = true) @RequestParam String content,
                                                           int page) {

        return userService.userReservationList(userInfo, page, sortMethod, searchBy, content);
    }

    /* 회원 예약 상태 변경 */
    @ApiOperation(value = "회원 예약 취소",notes = "회원이 예약한 출조 정보를 취소합니다.")
    @DeleteMapping("/statusUpdate")
    public ResResultDto userStatusUpdate(@LoginUser UserInfo userInfo,
                                         @ApiParam(value = "예약 아이디", required = true) @RequestParam Long reservation_id,
                                         @ApiParam(value = "상태 변경 값", required = true) @RequestParam String status) {

        return userService.userStatusUpdate(userInfo, reservation_id, status);
    }

}
