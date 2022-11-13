package com.shipnolja.reservation.ship.service;

import com.shipnolja.reservation.ship.dto.response.ResManagerShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfoList;

import java.util.List;

public interface ShipService {
    //선박 리스트 검색
    List<ResShipInfoList> shipList(String area, String detailArea,String port, String shipName,String sortBy,String sortMethod,int page);

    ResShipInfo shipInfo(Long id);

    ResManagerShipInfo shipMangerInfo(Long id);
}

