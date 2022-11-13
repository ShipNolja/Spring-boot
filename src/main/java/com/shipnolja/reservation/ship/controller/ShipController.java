package com.shipnolja.reservation.ship.controller;

import com.shipnolja.reservation.ship.dto.response.ResShipInfo;
import com.shipnolja.reservation.ship.dto.response.ResShipInfoList;
import com.shipnolja.reservation.ship.service.ShipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Ship - api - 모든 사용자가 이용 가능"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/ship")
public class ShipController {
    
    private final ShipService shipService;
    
    @ApiOperation(value = "선박 리스트 검색",notes = "선박의 리스트를 검색합니다.")
    @GetMapping("/list")
    public List<ResShipInfoList> shipList(@ApiParam(value = "지역 정보 : 강원도",required = true) @RequestParam String area,
                                          @ApiParam(value = "상세 지역 정보 : 고성",required = true) @RequestParam String detailArea,
                                          @ApiParam(value = "항구 이름 : 거진항 ",required = true) @RequestParam String port,
                                          @ApiParam(value = "배 이름 : 써니호",required = true) @RequestParam String shipName,
                                          @ApiParam(value = "정렬 기준 : name ,id ,detailArea,port ",required = true) @RequestParam String sortBy,
                                          @ApiParam(value = "정렬 방법 : asc , desc",required = true) @RequestParam String sortMethod,
                                          @ApiParam(value = "페이지 번호",required = true) @RequestParam int page){
        return shipService.shipList(area,detailArea,port,shipName,sortBy,sortMethod,page);
    }
    
    @ApiOperation(value = "선박 상세 정보",notes = "선박의 상세 정보를 확인합니다.")
    @GetMapping("/{id}")
    public ResShipInfo shipInfo(@ApiParam(value = "선박 pk 값",required = true) @PathVariable Long id){
        return shipService.shipInfo(id);
    }
}
