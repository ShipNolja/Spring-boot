package com.shipnolja.reservation.fishingCondition.service.Impl;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionListDto;
import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import com.shipnolja.reservation.fishingCondition.model.FishingConditionFiles;
import com.shipnolja.reservation.fishingCondition.repository.FishingConditionFilesRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class FishingConditionImpl implements FishingConditionService {
    private final UserRepository userRepository;
    private final ShipRepository shipRepository;
    private final FishingConditionRepository fishingConditionRepository;
    private final FishingConditionFilesRepository fishingConditionFilesRepository;
    private final FishingConditionFilesService fishingConditionFilesService;

    //조황정보 등록
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

    //조황 정보 목록
    @Override
    public List<ResFishingConditionListDto> list(String fish, LocalDate date, String title, String sortBy, String sortMethod, int page) {
        Pageable pageable = null;

        if(sortMethod.equals("asc")) {
            pageable = PageRequest.of(page,10, Sort.by(sortBy).ascending());
        } else if(sortMethod.equals("desc")) {
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());
        }

        Page<FishingCondition> fishingConditionPage = fishingConditionRepository.findByFishContainingAndDateAndTitleContaining(fish,date,title,pageable);

        List<ResFishingConditionListDto> resFishingConditionList = new ArrayList<>();

        fishingConditionPage.forEach(entity->{
            ResFishingConditionListDto listDto = new ResFishingConditionListDto();

            FishingConditionFiles fishingConditionFiles = fishingConditionFilesRepository.findFirstByFishingCondition(entity).orElseThrow(
                    ()->new CustomException.ResourceNotFoundException("이미지가 없습니다.")
            );
            listDto.setId(entity.getId());
            listDto.setDate(entity.getDate());
            listDto.setTitle(entity.getTitle());
            listDto.setFish(entity.getFish());
            listDto.setFile(fishingConditionFiles.getPath());

            resFishingConditionList.add(listDto);
            }
        );

        return resFishingConditionList;
    }

    //조황 정보 상세 조회
    @Override
    public ResFishingConditionDto fishingCondition(Long id) {
        FishingCondition fishingCondition = fishingConditionRepository.findById(id).orElseThrow(
                ()->new CustomException.ResourceNotFoundException("조황 정보를 찾을 수 없습니다")
        );

        List<FishingConditionFiles> fishingConditionFiles = fishingConditionFilesRepository.findPathByFishingCondition(fishingCondition);

        List<Map<String, Object>> mapFiles = new ArrayList<>();


        fishingConditionFiles.forEach(entity->{
                    Map<String, Object> filesMap = new HashMap<>();
                    filesMap.put("file_id",entity.getId());
                    filesMap.put("file_name",entity.getSaveName());
                    filesMap.put("file_path",entity.getPath());
                    mapFiles.add(filesMap);
                }
        );

        return ResFishingConditionDto.builder()
                .id(fishingCondition.getId())
                .fish(fishingCondition.getFish())
                .date(fishingCondition.getDate())
                .title(fishingCondition.getTitle())
                .content(fishingCondition.getContent())
                .files(mapFiles)
                .build();
    }

}
