package com.shipnolja.reservation.fishingCondition.service.Impl;

import com.shipnolja.reservation.fishingCondition.dto.request.FishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFileDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionDto;
import com.shipnolja.reservation.fishingCondition.dto.response.ResFishingConditionListDto;
import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import com.shipnolja.reservation.fishingCondition.model.FishingConditionFiles;
import com.shipnolja.reservation.fishingCondition.repository.FishingConditionFilesRepository;
import com.shipnolja.reservation.fishingCondition.repository.FishingConditionRepository;
import com.shipnolja.reservation.fishingCondition.service.FishingConditionService;
import com.shipnolja.reservation.ship.model.ShipInfo;
import com.shipnolja.reservation.ship.repository.ShipRepository;
import com.shipnolja.reservation.user.model.UserInfo;
import com.shipnolja.reservation.user.repository.UserRepository;
import com.shipnolja.reservation.util.S3FileUploadService;
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
    private final S3FileUploadService s3FileUploadService;

    //조황정보 등록
    @Override
    public ResResultDto upload(UserInfo userInfo, FishingConditionDto fishingConditionDto,  List<MultipartFile> files) {

        UserInfo uploadUser = userRepository.findById(userInfo.getId()).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("사용자 정보를 찾을 수 없습니다.")
        );

        ShipInfo uploadShip = shipRepository.findByUserInfo(uploadUser).orElseThrow(
                () -> new CustomException.ResourceNotFoundException("회사 정보를 찾을 수 없습니다.")
        );

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

        if(!CollectionUtils.isEmpty(files)){
            for(MultipartFile multipartFile : files){
                String originFileName = multipartFile.getOriginalFilename();
                if (originFileName.endsWith(".jpg") || originFileName.endsWith(".png") || originFileName.endsWith(".jpeg")) {
                }else{
                    return new ResResultDto(-1L,"jpg,png,jpeg 확장자만 사용 가능합니다.");
                }
            }

            List<ResFileDto> resFileDtoList = s3FileUploadService.uploadFishingConditionFile(files);

            for(ResFileDto resFileDto : resFileDtoList){
                fishingConditionFilesRepository.save(
                        FishingConditionFiles.builder()
                                .saveName(resFileDto.getSaveName())
                                .path(resFileDto.getFilePath())
                                .origin(resFileDto.getOrigin())
                                .fishingCondition(fishingCondition)
                                .build()
                );
            }
            return new ResResultDto(fishingCondition.getId(),"사진 포함 조황 정보를 등록하였습니다.");
        }
        else return new ResResultDto(fishingCondition.getId(),"조황 정보를 등록하였습니다.");

    }

    //조황 정보 목록
    @Override
    public List<ResFishingConditionListDto> list(String fish, LocalDate startDate,LocalDate endDate ,String title, String sortBy, String sortMethod, int page) {
        Pageable pageable = null;

        if(sortMethod.equals("asc")) {
            pageable = PageRequest.of(page,10, Sort.by(sortBy).ascending());
        } else if(sortMethod.equals("desc")) {
            pageable = PageRequest.of(page, 10, Sort.by(sortBy).descending());
        }

        Page<FishingCondition> fishingConditionPage = fishingConditionRepository.findByFishContainingAndTitleContainingAndDateBetween(fish,title,startDate,endDate,pageable);

        List<ResFishingConditionListDto> resFishingConditionList = new ArrayList<>();

        fishingConditionPage.forEach(entity->{
            ResFishingConditionListDto listDto = new ResFishingConditionListDto();


                    if(fishingConditionFilesRepository.findFirstByFishingCondition(entity).isEmpty()){
                        listDto.setId(entity.getId());
                        listDto.setDate(entity.getDate());
                        listDto.setTitle(entity.getTitle());
                        listDto.setFish(entity.getFish());
                        listDto.setFile(null);
                    }else if(fishingConditionFilesRepository.findFirstByFishingCondition(entity).isPresent()){
                        FishingConditionFiles fishingConditionFiles = fishingConditionFilesRepository.findFirstByFishingCondition(entity).get();
                        listDto.setId(entity.getId());
                        listDto.setDate(entity.getDate());
                        listDto.setTitle(entity.getTitle());
                        listDto.setFish(entity.getFish());
                        listDto.setFile(fishingConditionFiles.getPath());
                    }


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
