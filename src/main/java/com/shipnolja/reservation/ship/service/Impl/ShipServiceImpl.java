package com.shipnolja.reservation.ship.service.Impl;

import com.shipnolja.reservation.ship.dto.response.ResManagerShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfoList;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.ship.service.ShipService;
import com.shipnolja.reservation.util.exception.CustomException;
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
public class ShipServiceImpl implements ShipService {

    private final ShipRepository shipRepository;

    @Override
    public List<ResShipInfoList> shipList(String area, String detailArea, String port, String shipName, String sortBy, String sortMethod, int page) {
        Pageable pageable = null;

        if(sortMethod.equals("asc"))
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).ascending());
        else if(sortMethod.equals("desc"))
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());

        Page<ShipInfo> shipInfoPage = shipRepository.findShipInfoList(
                shipName,port,area,detailArea,pageable
        );

        List<ShipInfo> shipInfoList = shipInfoPage.getContent();

        List<ResShipInfoList> shipInfoListDto = new ArrayList<>();

        shipInfoList.forEach(entity->{
            ResShipInfoList listDto = new ResShipInfoList();
            listDto.setId(entity.getId());
            listDto.setImage(entity.getImage());
            listDto.setName(entity.getName());
            listDto.setArea(entity.getArea());
            listDto.setDetailArea(entity.getDetailArea());
            listDto.setPort(entity.getPort());
            listDto.setStreetAddress(entity.getStreetAddress());
            listDto.setTotalPage(shipInfoPage.getTotalPages());
            listDto.setTotalElement(shipInfoPage.getTotalElements());
            shipInfoListDto.add(listDto);
            }
        );

        return shipInfoListDto;
    }

    @Override
    public ResShipInfo shipInfo(Long id) {
        ShipInfo shipInfo = shipRepository.findById(id).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("선상 정보를 찾을 수 없습니다")
        );

        return new ResShipInfo(shipInfo);
    }

    @Override
    public ResManagerShipInfo shipMangerInfo(Long id) {
        ShipInfo shipManagerInfo = shipRepository.findById(id).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("선상 정보를 찾을 수 없습니다")
        );

        return new ResManagerShipInfo(shipManagerInfo);
    }
}
