package com.shipnolja.reservation.fishingCondition.service.Impl;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import com.shipnolja.reservation.fishingCondition.repository.FishingConditionRepository;
import com.shipnolja.reservation.fishingCondition.service.FishingConditionFilesService;
import com.shipnolja.reservation.fishingCondition.service.FishingConditionService;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.exception.CustomException;
import com.shipnolja.reservation.util.responseDto.ResResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FishingConditionImpl implements FishingConditionService {
    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final FishingConditionRepository fishingConditionRepository;
    private final FishingConditionFilesService fishingConditionFilesService;

    @Override
    public ResResultDto upload(UserInfo userInfo, FishingConditionDto fishingConditionDto, List<MultipartFile> files) {

        UserInfo uploadUser = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        ShipInfo uploadShip = shipRepository.findByUserInfo(uploadUser).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("회사 정보를 찾을 수 없습니다.")
        );

        if(!CollectionUtils.isEmpty(files)){
            for(MultipartFile multipartFile : files) {

                String originFileName = multipartFile.getOriginalFilename().toLowerCase();

                //List에 값이 있으면 saveFileUpload 실행
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {

                    FishingCondition fishingCondition = fishingConditionRepository.save(
                            FishingCondition.builder()
                                    .shipInfo(uploadShip)
                                    .title(fishingConditionDto.getTitle())
                                    .content(fishingConditionDto.getContent())
                                    .date(fishingConditionDto.getDate())
                                    .fish(fishingConditionDto.getFish())
                                    .uploadDate(LocalDateTime.now())
                                    .build()
                    );

                    if (fishingConditionFilesService.uploadFile(files, fishingCondition).equals(-1L)) {
                        return new ResResultDto(-2L,"파일 업로드 실패."); //파일업로드 실패하면 -2L반환
                    }
                    return new ResResultDto(fishingCondition.getId(),"업로드 성공.");
                }
            }
        }
        else if(CollectionUtils.isEmpty(files)){
            FishingCondition fishingCondition = fishingConditionRepository.save(
                    FishingCondition.builder()
                            .shipInfo(uploadShip)
                            .title(fishingConditionDto.getTitle())
                            .content(fishingConditionDto.getContent())
                            .date(fishingConditionDto.getDate())
                            .fish(fishingConditionDto.getFish())
                            .uploadDate(LocalDateTime.now())
                            .build()
            );
            return new ResResultDto(fishingCondition.getId(),"업로드 성공.");
        }
        return new ResResultDto(-1L,"게시글 업로드 실패."); //상품업로드 실패시 -1L반환
    }
}
