package com.shipnolja.reservation.wish.controller;

import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import com.shipnolja.reservation.wish.dto.request.WishIdDto;
import com.shipnolja.reservation.wish.dto.response.ResWishListDto;
import com.shipnolja.reservation.wish.service.WishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Wish - ROLE_USER"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/wish")
public class WishController {

    private final WishService wishService;

    @ApiOperation(value = "즐겨찾기 등록",notes = "해당 선상을 즐겨찾기 목록에 등록합니다.")
    @PostMapping("/{shipId}")
    public ResResultDto post(@LoginUser UserInfo userInfo,@PathVariable Long shipId){
        Long result = wishService.post(userInfo,shipId);
        return result == -1L?
                new ResResultDto(result,"즐겨찾기에서 제거했습니다.") : new ResResultDto(result,"즐겨찾기에 등록했습니다.");
    }

    //로그인 사용자의 위시 리스트
    @GetMapping(value = "/list/{page}")
    public List<ResWishListDto> wishList(@LoginUser UserInfo userInfo, @PathVariable int page){
        return wishService.wishList(userInfo,page);
    }

    //즐겨찾기 등록 유무 검증
    @GetMapping(value = "/{shipId}")
    public ResResultDto wishCheck(@LoginUser UserInfo userInfo,@PathVariable("shipId") Long shipId){
        Long result = wishService.wishCheck(userInfo,shipId);
        return result == -1L ?
                new ResResultDto(result,"즐겨찾기에 등록되지 않은 선상 정보입니다.") : new ResResultDto(result,"즐겨찾기에 등록된 선상 정보입니다.");
    }

    //즐겨찾기 삭제
    @DeleteMapping(value = "")
    public ResResultDto wishDelete(@LoginUser UserInfo userInfo, @RequestBody List<WishIdDto> wishIdDto){
        Long result = wishService.wishDelete(userInfo,wishIdDto);

        return new ResResultDto(result,"선택한 선상정보를 삭제하였습니다.");
    }
}
