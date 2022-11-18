package com.shipnolja.reservation.ship.service;

import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoListDto;
import com.shipnolja.reservation.reservation.dto.response.ResReservationListDto;
import com.shipnolja.reservation.ship.dto.response.ResManagerShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfoList;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.util.responseDto.ResResultDto;

import java.util.List;

public interface ShipService {
    //선박 리스트 검색
    List<ResShipInfoList> shipList(String searchRequirements,String searchWord,String sortBy,String sortMethod,int page);

    ResShipInfo shipInfo(Long id);

    ResManagerShipInfo shipMangerInfo(Long id);

    /* 매니저 출조 예약자 목록 (진모) */
    List<ResReservationListDto> managerReservationList(UserInfo userInfo, String sortMethod, String searchBy, String content, int page);

    /* 등록한 출조 정보 목록 */
    List<ResFishingInfoListDto> managerFishingInfoList(UserInfo userInfo, String sortMethod, String searchBy, String content, int page);

    /* 예약자 상태 변경 */
    ResResultDto managerStatusUpdate(UserInfo userInfo, Long fishingInfo_id, Long reservation_id, String status);
}

