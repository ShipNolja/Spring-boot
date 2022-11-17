package com.shipnolja.reservation.fishingCondition.service.Impl;

import com.shipnolja.reservation.fishingCondition.model.FishingCondition;
import com.shipnolja.reservation.fishingCondition.model.FishingConditionFiles;
import com.shipnolja.reservation.fishingCondition.repository.FishingConditionFilesRepository;
import com.shipnolja.reservation.fishingCondition.service.FishingConditionFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FishingConditionFilesImpl implements FishingConditionFilesService {

    private final FishingConditionFilesRepository fishingConditionFilesRepository;
    @Override
    public Long uploadFile(List<MultipartFile> files, FishingCondition fishingCondition) {

        Long id = null;

        //파일 저장 경로
        String savePath = System.getProperty("user.dir") + "//src//main//resources//static//product_files";

        //파일 저장되는 폳더 없으면 생성
        if(!new File(savePath).exists()) {
            try{
                new File(savePath).mkdir();
            } catch (Exception e2) {
                e2.getStackTrace();
            }
        }

        // 다중 파일 처리
        // multipartfile : files  files에서 더 꺼낼게 없을 때 까지 multipartfile에 담아줌
        for(MultipartFile multipartFile : files) {

            //파일명 소문자로 추출
            String originFileName = multipartFile.getOriginalFilename().toLowerCase().replace(" ", "");

            //UUID로 파일명 중복되지 않게 유일한 식별자 + 확장자로 저장
            UUID uuid = UUID.randomUUID();
            String saveFileName = uuid + "__" + originFileName;

            //File로 저장 경로와 저장 할 파일명 합쳐서 transferTo() 사용하여 업로드하려는 파일을 해당 경로로 저장
            String filepath = savePath + "//" + saveFileName;
            String dbFilePath = "../fishingCondition_files/" + saveFileName;

            try {
                multipartFile.transferTo(new File(filepath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            id = fishingConditionFilesRepository.save(
                    FishingConditionFiles.builder()
                            .fishingCondition(fishingCondition)
                            .origin(originFileName)
                            .path(dbFilePath)
                            .saveName(saveFileName)
                            .build()
            ).getId();
        }
        return id;
    }
}
