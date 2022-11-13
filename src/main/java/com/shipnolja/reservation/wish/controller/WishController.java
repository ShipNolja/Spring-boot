package com.shipnolja.reservation.wish.controller;

import com.shipnolja.reservation.user.annotation.LoginUser;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import com.shipnolja.reservation.wish.dto.request.WishIdDto;
import com.shipnolja.reservation.wish.dto.response.ResWishListDto;
import com.shipnolja.reservation.wish.service.WishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    public ResResultDto post(@LoginUser UserInfo userInfo,@ApiParam(value = "선상 pk", required = true) @PathVariable Long shipId){
        Long result = wishService.post(userInfo,shipId);
        return result == -1L?
                new ResResultDto(result,"즐겨찾기에서 제거했습니다.") : new ResResultDto(result,"즐겨찾기에 등록했습니다.");
    }

    //로그인 사용자의 위시 리스트
    @ApiOperation(value = "즐겨찾기 목록.",notes = "로그인 사용자의 즐겨찾기 목록을 확인합니다.")
    @GetMapping(value = "/list/{page}")
    public List<ResWishListDto> wishList(@LoginUser UserInfo userInfo,@ApiParam(value = "페이지 정보", required = true) @PathVariable int page){
        return wishService.wishList(userInfo,page);
    }

    //즐겨찾기 등록 유무 검증
    @ApiOperation(value = "즐겨찾기 유무(상태) 확인.",notes = "해당 선박을 즐겨찾기 했는지 유무를 검증합니다.")
    @GetMapping(value = "/{shipId}")
    public ResResultDto wishCheck(@LoginUser UserInfo userInfo,@ApiParam(value = "선상 pk", required = true) @PathVariable("shipId") Long shipId){
        Long result = wishService.wishCheck(userInfo,shipId);
        return result == -1L ?
                new ResResultDto(result,"즐겨찾기에 등록되지 않은 선상 정보입니다.") : new ResResultDto(result,"즐겨찾기에 등록된 선상 정보입니다.");
    }

    //즐겨찾기 삭제
    @ApiOperation(value = "즐겨찾기 목록에서 삭제합니다",notes = "목록중 여러개를 골라서 한번에 삭제합니다.")
    @DeleteMapping(value = "")
    public ResResultDto wishDelete(@LoginUser UserInfo userInfo,@ApiParam(value = "즐겨찾기 pk 값", required = true) @RequestBody List<WishIdDto> wishIdDto){
        Long result = wishService.wishDelete(userInfo,wishIdDto);

        return new ResResultDto(result,"선택한 선상정보를 삭제하였습니다.");
    }
}
