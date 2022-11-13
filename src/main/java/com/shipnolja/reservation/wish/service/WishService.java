package com.shipnolja.reservation.wish.service;

import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.wish.dto.request.WishIdDto;
import com.shipnolja.reservation.wish.dto.response.ResWishListDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface WishService {
    // 즐겨찾기 등록
    Long post( UserInfo userInfo, Long shipId);
    
    //즐겨찾기 리스트
    List<ResWishListDto> wishList(UserInfo userInfo, int page);

    //즐겨찾기 삭제
    Long wishDelete(UserInfo userInfo , List<WishIdDto> wishIdDto);

    //즐겨찾기 체크
    Long wishCheck(UserInfo userInfo,Long shipId);
}
