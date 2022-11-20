package com.shipnolja.reservation.fishinginfo.service.Impl;

import com.shipnolja.reservation.fishinginfo.dto.request.ReqFishingInfoDto;
import com.shipnolja.reservation.fishinginfo.dto.response.ResFishingInfoListDto;
import com.shipnolja.reservation.fishinginfo.model.FishingInfo;
import com.shipnolja.reservation.fishinginfo.repository.FishingInfoRepository;
import com.shipnolja.reservation.fishinginfo.service.FishingInfoService;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.model.UserRole;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FishingInfoImpl implements FishingInfoService {

    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final FishingInfoRepository fishingInfoRepository;

    /* 출조 정보 등록 */
    @Override
    public ResResultDto fishingInfoWrite(UserInfo userInfo, ReqFishingInfoDto reqFishingInfoDto) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 사용자가 아닙니다."));

        ShipInfo checkShipInfo = shipRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        List<FishingInfo> fishingInfoList = fishingInfoRepository.findByShipInfo(checkShipInfo);

        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = null;

        for (FishingInfo fishingInfo : fishingInfoList) {

            startDate = fishingInfo.getInfoStartDate();
        }

        /* 등록 날짜에 이미 등록된 출조 정보가 있거나 금일 이전의 날짜인 경우 등록 불가 */
        if (fishingInfoList.isEmpty() && reqFishingInfoDto.getInfoStartDate().isBefore(currentDate)) {

            return new ResResultDto(-1L, "금일을 포함한 이후 날짜로만 등록 가능 합니다.");

        } else if (startDate != null && startDate.compareTo(reqFishingInfoDto.getInfoStartDate()) == 0) {

            return new ResResultDto(-2L, "해당 날짜에 이미 등록된 출조 정보가 있습니다.");
        }


        FishingInfo fishingInfo = fishingInfoRepository.save(
                FishingInfo.builder()
                        .infoNotice(reqFishingInfoDto.getInfoNotice())
                        .infoMessage(reqFishingInfoDto.getInfoMessage())
                        .infoTarget(reqFishingInfoDto.getInfoTarget())
                        .infoAssemblePoint(checkShipInfo.getStreetAddress())
                        .infoStartPoint(checkShipInfo.getStreetAddress())
                        .infoCapacity(reqFishingInfoDto.getInfoCapacity())
                        .infoStartTime(reqFishingInfoDto.getInfoStartTime())
                        .infoEndTime(reqFishingInfoDto.getInfoEndTime())
                        .infoStartDate(reqFishingInfoDto.getInfoStartDate())
                        .infoReservationStatus(reqFishingInfoDto.getInfoReservationStatus())
                        .shipInfo(checkShipInfo)
                        .build()
        );

        return new ResResultDto(fishingInfo.getInfoId(), "출조 정보를 등록 했습니다.");
    }

    /* 출조 정보 목록 */
    @Override
    public List<ResFishingInfoListDto> simpleInfoList(int page, String sortMethod, String sortBy, String searchBy,
                                                      String content, String target, LocalDate infoStartDate) {
        Pageable pageable = null;
        Page<FishingInfo> fishingInfoPage;

        if (sortMethod.equals("asc")) {
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).ascending());
        } else if (sortMethod.equals("desc")) {
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());
        }

        if (target.equals("전체") && pageable != null) {
            fishingInfoPage = fishingInfoRepository.findAll(pageable);
        } else {
            fishingInfoPage = fishingInfoRepository.findByInfoTargetContaining(target, pageable);
        }

        switch (searchBy) {
            case "지역":
                fishingInfoPage = fishingInfoRepository.findByShipInfo_AreaContaining(content, pageable);
                break;
            case "상세지역":
                fishingInfoPage = fishingInfoRepository.findByShipInfo_DetailAreaContaining(content, pageable);
                break;
            case "항구":
                fishingInfoPage = fishingInfoRepository.findByShipInfo_PortContaining(content, pageable);
                break;
            case "선박명":
                fishingInfoPage = fishingInfoRepository.findByShipInfo_NameContaining(content, pageable);
                break;
            case "예약상태":
                fishingInfoPage = fishingInfoRepository.findByInfoReservationStatusContaining(content, pageable);
                break;
            case "출항날짜":
                fishingInfoPage = fishingInfoRepository.findByInfoStartDate(infoStartDate, pageable);
                break;
            case "전체":
                if (pageable != null) {
                    fishingInfoPage = fishingInfoRepository.findAll(pageable);
                }
                break;
        }


        List<ResFishingInfoListDto> fishingInfoListDto = new ArrayList<>();

        if (fishingInfoPage != null) {

            int totalPages = fishingInfoPage.getTotalPages();
            long totalElements = fishingInfoPage.getTotalElements();

            LocalDate currentDate = LocalDate.now();

            fishingInfoPage.forEach(fishingInfo -> {

                if (fishingInfo.getInfoStartDate().compareTo(currentDate) >= 0) {

                    ResFishingInfoListDto infoListDto = new ResFishingInfoListDto();

                    infoListDto.setFishingInfoId(fishingInfo.getInfoId());
                    infoListDto.setShipInfoId(fishingInfo.getShipInfo().getId());
                    infoListDto.setArea(fishingInfo.getShipInfo().getArea());
                    infoListDto.setDetailArea(fishingInfo.getShipInfo().getDetailArea());
                    infoListDto.setPort(fishingInfo.getShipInfo().getPort());
                    infoListDto.setShipName(fishingInfo.getShipInfo().getName());
                    infoListDto.setTarget(fishingInfo.getInfoTarget());
                    infoListDto.setInfoStartDate(fishingInfo.getInfoStartDate());
                    infoListDto.setInfoStartTime(fishingInfo.getInfoStartTime());
                    infoListDto.setInfoEndTime(fishingInfo.getInfoEndTime());
                    infoListDto.setInfoReservationStatus(fishingInfo.getInfoReservationStatus());
                    infoListDto.setInfoCapacity(fishingInfo.getInfoCapacity());
                    infoListDto.setImage(fishingInfo.getShipInfo().getImage());
                    infoListDto.setInfoMessage(fishingInfo.getInfoMessage());
                    infoListDto.setInfoNotice(fishingInfo.getInfoNotice());
                    infoListDto.setInfoAssemblePoint(fishingInfo.getInfoAssemblePoint());
                    infoListDto.setInfoStartPoint(fishingInfo.getInfoStartPoint());
                    infoListDto.setTotalPage(totalPages);
                    infoListDto.setTotalElement(totalElements);

                    fishingInfoListDto.add(infoListDto);
                }
            });
        }


        return fishingInfoListDto;
    }

    /* 출조 정보 상세 목록 */
    @Override
    public List<ResFishingInfoListDto> detailsInfoList(int page, Long ship_id) {

        ShipInfo checkShipInfo = shipRepository.findById(ship_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("infoStartDate").ascending());
        Page<FishingInfo> fishingInfoPage = fishingInfoRepository.findByShipInfo(checkShipInfo, pageable);

        List<ResFishingInfoListDto> fishingInfoListDto = new ArrayList<>();

        fishingInfoPage.forEach(fishingInfo -> {

            LocalDate currentDate = LocalDate.now();

            /* 현재 날짜 이후 출조 정보만 출력 */
            if (fishingInfo.getInfoStartDate().compareTo(currentDate) >= 0) {

                ResFishingInfoListDto infoListDto = new ResFishingInfoListDto();

                infoListDto.setFishingInfoId(fishingInfo.getInfoId());
                infoListDto.setShipInfoId(checkShipInfo.getId());
                infoListDto.setArea(fishingInfo.getShipInfo().getArea());
                infoListDto.setDetailArea(fishingInfo.getShipInfo().getDetailArea());
                infoListDto.setPort(fishingInfo.getShipInfo().getPort());
                infoListDto.setShipName(fishingInfo.getShipInfo().getName());
                infoListDto.setTarget(fishingInfo.getInfoTarget());
                infoListDto.setInfoStartDate(fishingInfo.getInfoStartDate());
                infoListDto.setInfoStartTime(fishingInfo.getInfoStartTime());
                infoListDto.setInfoEndTime(fishingInfo.getInfoEndTime());
                infoListDto.setInfoReservationStatus(fishingInfo.getInfoReservationStatus());
                infoListDto.setInfoCapacity(fishingInfo.getInfoCapacity());
                infoListDto.setInfoMessage(fishingInfo.getInfoMessage());
                infoListDto.setInfoNotice(fishingInfo.getInfoNotice());
                infoListDto.setInfoAssemblePoint(fishingInfo.getInfoAssemblePoint());
                infoListDto.setInfoStartPoint(fishingInfo.getInfoStartPoint());
                infoListDto.setImage(fishingInfo.getShipInfo().getImage());
                infoListDto.setTotalPage(fishingInfoPage.getTotalPages());
                infoListDto.setTotalElement(fishingInfoPage.getTotalElements());

                fishingInfoListDto.add(infoListDto);
            }

        });

        return fishingInfoListDto;
    }

    /* 출조 정보 수정 */
    @Override
    public ResResultDto fishingInfoUpdate(UserInfo userInfo, ReqFishingInfoDto reqFishingInfoDto, Long fishingInfo_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 사용자가 아닙니다."));

        ShipInfo checkShipInfo = shipRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findByShipInfoAndInfoId(checkShipInfo, fishingInfo_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        List<FishingInfo> fishingInfoList = fishingInfoRepository.findByShipInfo(checkShipInfo);

        /* 수정 시 수용 인원 0명인데 예약 가능으로 값이 들어온 경우 -> 반대의 경우 처리 */
        if (reqFishingInfoDto.getInfoCapacity() == 0 && reqFishingInfoDto.getInfoReservationStatus().equals("예약가능")) {

            return new ResResultDto(-1L, "인원이 0명인 경우 예약 마감으로 변경해야 합니다. 다시 시도해 주세요");

        } else if (reqFishingInfoDto.getInfoCapacity() > 0 && reqFishingInfoDto.getInfoReservationStatus().equals("예약마감")) {

            return new ResResultDto(-2L, "인원이 1명 이상인 경우 예약 가능으로 변경해야 합니다. 다시 시도해 주세요");
        }

        /* 리스트에서 수정하려는 데이터 제거 => List에 값이 다 담겨 오기 때문에 밑에 -3L에서 걸림 */
        fishingInfoList.remove(checkFishingInfo);

        /* 수정 하려는 날짜에 이미 등록된 출조 정보가 있거나 금일 이전의 날짜인 경우 */
        for (FishingInfo fishingInfo : fishingInfoList) {

            LocalDate currentDate = LocalDate.now();

            /* 수정하려는 날짜를 포함한 db에 없는 현재 날짜 이후라면 수정 가능 */
            if (fishingInfo.getInfoStartDate().isEqual(reqFishingInfoDto.getInfoStartDate())) {

                return new ResResultDto(-3L, "해당 날짜에 이미 등록된 출조 정보가 있습니다.");

            } else if (reqFishingInfoDto.getInfoStartDate().compareTo(currentDate) < 0) {

                return new ResResultDto(-4L, "금일 이전 날짜로는 수정할 수 없습니다.");
            }
        }

        /* 집결지 출항지는 프론트에서 값 전달해준다 하면 dto 수정 */
        checkFishingInfo = fishingInfoRepository.save(
                FishingInfo.builder()
                        .infoId(checkFishingInfo.getInfoId())
                        .infoNotice(reqFishingInfoDto.getInfoNotice())
                        .infoMessage(reqFishingInfoDto.getInfoMessage())
                        .infoTarget(reqFishingInfoDto.getInfoTarget())
                        .infoAssemblePoint(checkShipInfo.getStreetAddress())
                        .infoStartPoint(checkShipInfo.getStreetAddress())
                        .infoCapacity(reqFishingInfoDto.getInfoCapacity())
                        .infoStartTime(reqFishingInfoDto.getInfoStartTime())
                        .infoEndTime(reqFishingInfoDto.getInfoEndTime())
                        .infoStartDate(reqFishingInfoDto.getInfoStartDate())
                        .infoReservationStatus(reqFishingInfoDto.getInfoReservationStatus())
                        .shipInfo(checkShipInfo)
                        .build()
        );

        return new ResResultDto(checkFishingInfo.getInfoId(), "출조 정보를 수정 했습니다.");
    }
    
    /* 출조 정보 삭제 */
    @Override
    public ResResultDto fishingInfoDelete(UserInfo userInfo, Long fishingInfo_id) {

        UserInfo checkUserInfo = userRepository.findByIdAndRole(userInfo.getId(), UserRole.ROLE_MANAGER)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("매니저 사용자가 아닙니다."));

        ShipInfo checkShipInfo = shipRepository.findByUserInfo(checkUserInfo)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        FishingInfo checkFishingInfo = fishingInfoRepository.findByShipInfoAndInfoId(checkShipInfo, fishingInfo_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        fishingInfoRepository.deleteById(checkFishingInfo.getInfoId());

        return new ResResultDto(checkFishingInfo.getInfoId(),"출조 정보를 삭제 했습니다.");
    }

    /* 출조 정보 예약 페이지 조회 */
    @Override
    public ResFishingInfoListDto reservationPage(Long fishingInfo_id) {

        FishingInfo checkFishingInfo = fishingInfoRepository.findById(fishingInfo_id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("출조 정보를 찾을 수 없습니다."));

        ShipInfo checkShipInfo = shipRepository.findById(checkFishingInfo.getShipInfo().getId())
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("선박 정보를 찾을 수 없습니다."));

        ResFishingInfoListDto infoListDto = new ResFishingInfoListDto();

        infoListDto.setFishingInfoId(checkFishingInfo.getInfoId());
        infoListDto.setShipInfoId(checkShipInfo.getId());
        infoListDto.setArea(checkShipInfo.getArea());
        infoListDto.setDetailArea(checkShipInfo.getDetailArea());
        infoListDto.setPort(checkShipInfo.getPort());
        infoListDto.setShipName(checkShipInfo.getName());
        infoListDto.setTarget(checkFishingInfo.getInfoTarget());
        infoListDto.setInfoStartDate(checkFishingInfo.getInfoStartDate());
        infoListDto.setInfoStartTime(checkFishingInfo.getInfoStartTime());
        infoListDto.setInfoEndTime(checkFishingInfo.getInfoEndTime());
        infoListDto.setInfoReservationStatus(checkFishingInfo.getInfoReservationStatus());
        infoListDto.setInfoCapacity(checkFishingInfo.getInfoCapacity());
        infoListDto.setInfoMessage(checkFishingInfo.getInfoMessage());
        infoListDto.setInfoNotice(checkFishingInfo.getInfoNotice());
        infoListDto.setInfoAssemblePoint(checkFishingInfo.getInfoAssemblePoint());
        infoListDto.setInfoStartPoint(checkFishingInfo.getInfoStartPoint());
        infoListDto.setImage(checkFishingInfo.getShipInfo().getImage());


        return infoListDto;
    }
}
