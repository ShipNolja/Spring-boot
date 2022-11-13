package com.shipnolja.reservation.wish.service.Impl;

import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.wish.dto.request.WishIdDto;
import com.shipnolja.reservation.wish.dto.response.ResWishListDto;
import com.shipnolja.reservation.wish.model.Wish;
import com.shipnolja.reservation.wish.repository.WishRepository;
import com.shipnolja.reservation.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {
    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final WishRepository wishRepository;

    //즐겨찾기 등록
    @Override
    public Long post(UserInfo userInfo, Long shipId) {
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("회원 정보를 찾을 수 없습니다.")
        );
        ShipInfo wishShipInfo = shipRepository.findById(shipId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다.")
        );
        if(wishRepository.findByUserInfoAndShipInfo(wishUserInfo,wishShipInfo).isEmpty()){
            return wishRepository.save(
                    Wish.builder()
                            .userInfo(wishUserInfo)
                            .shipInfo(wishShipInfo)
                            .build()
            ).getId();
        }else{
            wishRepository.delete(wishRepository.findByUserInfoAndShipInfo(wishUserInfo,wishShipInfo).get());
            return -1L;
        }
    }

    @Override
    public List<ResWishListDto> wishList(UserInfo userInfo, int page) {
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Wish> wishPage = wishRepository.findByUserInfo(wishUserInfo,pageable);

        List<Wish> wishList = wishPage.getContent();

        List<ResWishListDto> wishListDto = new ArrayList<>();
        wishList.forEach(entity->{
            ResWishListDto listDto = new ResWishListDto();
            listDto.setWishId(entity.getId());
            listDto.setName(entity.getShipInfo().getName());
            listDto.setImage(entity.getShipInfo().getImage());
            listDto.setArea(entity.getShipInfo().getArea());
            listDto.setDetailArea(entity.getShipInfo().getDetailArea());
            listDto.setPort(entity.getShipInfo().getPort());
            listDto.setTotalElement(wishPage.getTotalElements());
            listDto.setTotalPage(wishPage.getTotalPages());
            wishListDto.add(listDto);
        });

        return wishListDto;
    }

    @Override
    public Long wishDelete(UserInfo userInfo, List<WishIdDto> wishIdDto) {
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );

        for(WishIdDto idDto : wishIdDto){
            Wish wish = wishRepository.findByIdAndUserInfo(idDto.getWishId(),wishUserInfo).orElseThrow(
                    () -> new CustomException.ResourceNotFoundException("선택한 선상 정보가 즐겨찾기 목록에 존재 하지 않습니다.")
            );
            wishRepository.delete(wish);
        }


        return 1L;
    }

    @Override
    public Long wishCheck(UserInfo userInfo, Long shipId) {
        UserInfo wishUserInfo = userRepository.findById(userInfo.getId()).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("로그인 사용자를 찾을 수 없습니다")
        );
        ShipInfo wishShipInfo = shipRepository.findById(shipId).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("선상 정보를 찾을 수 없습니다")
        );

        long result = 1L;

        if(wishRepository.findByUserInfoAndShipInfo(wishUserInfo,wishShipInfo).isEmpty()){
            result = -1L;
        }

        return result;
    }


}
