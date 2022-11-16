package com.shipnolja.reservation.ship.service;

import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoListDto;
import com.shipnolja.reservation.reservation.dto.response.ResReservationListDto;
import com.shipnolja.reservation.ship.dto.response.ResManagerShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfoList;
import com.shipnolja.reservation.user.model.UserInfo;

import java.util.List;

public interface ShipService {
    //선박 리스트 검색
    List<ResShipInfoList> shipList(String area, String detailArea,String port, String shipName,String sortBy,String sortMethod,int page);

    ResShipInfo shipInfo(Long id);

    ResManagerShipInfo shipMangerInfo(Long id);

    /* 매니저 출조 예약자 목록 (진모) */
    ResReservationListDto managerReservationList(UserInfo userInfo, Long ship_id, Long info_id, String sortMethod, String searchBy, String content, int page);

    List<ResFishingInfoListDto> managerFishingInfoList(UserInfo userInfo, Long ship_id, String sortMethod, String searchBy, String content, int page);
}

