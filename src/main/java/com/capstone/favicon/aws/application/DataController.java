package com.capstone.favicon.aws.application;

import com.capstone.favicon.dataset.domain.Dataset;
import com.capstone.favicon.dataset.domain.FileExtension;
import com.capstone.favicon.dataset.repository.DatasetRepository;
import com.capstone.favicon.dataset.domain.DatasetTheme;
import com.capstone.favicon.dataset.domain.Resource;
import com.capstone.favicon.dataset.repository.DatasetThemeRepository;
import com.capstone.favicon.dataset.repository.ResourceRepository;
import com.capstone.favicon.aws.application.MetadataParser.DatasetMetadata;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dataset")
@RequiredArgsConstructor
public class DataController {
    private final S3Service s3Service;
    private final DatasetRepository datasetRepository;
    private final DatasetThemeRepository datasetThemeRepository;
    private final ResourceRepository resourceRepository;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 올바르지 않습니다.");
        }

        String fileName = file.getOriginalFilename().trim();
        String fileUrl = s3Service.uploadFile(file);

        // DB에서 datasetTheme 목록 가져오기
        List<DatasetTheme> datasetThemes = datasetThemeRepository.findAll();

        // 파일명에서 메타데이터 추출
        DatasetMetadata metadata = MetadataParser.extractMetadata(fileName, datasetThemes);

        // datasetTheme 찾기
        DatasetTheme datasetTheme = datasetThemes.stream()
                .filter(theme -> theme.getDatasetThemeId().equals(metadata.getDatasetThemeId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 datasetThemeId가 존재하지 않습니다: " + metadata.getDatasetThemeId()));

        // 중복 데이터셋 검사
        Dataset dataset = datasetRepository
                .findByDatasetThemeAndNameAndOrganization(datasetTheme, metadata.getName(), metadata.getOrganization())
                .orElseGet(() -> datasetRepository.save(
                        new Dataset(datasetTheme, metadata.getName(), metadata.getTitle(), metadata.getOrganization())
                ));

        // 파일 확장자 처리 (MetadataParser에서 추출한 type 사용)
        FileExtension type;
        try {
            type = FileExtension.valueOf(metadata.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원되지 않는 파일 확장자입니다: " + metadata.getType());
        }

        // 리소스 저장
        Resource resource = new Resource(dataset, fileName, type, fileUrl);
        resourceRepository.save(resource);

        return "파일이 업로드 되었습니다: " + fileUrl;
    }


    @Transactional
    @DeleteMapping("/delete/{resourceId}")
    public String deleteFile(@PathVariable Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("리소스를 찾을 수 없습니다: " + resourceId));

        Dataset dataset = resource.getDataset();

        // S3에서 파일 삭제
        s3Service.deleteFile(resource.getResourceUrl());

        // ✅ dataset에서 resource 제거
        dataset.setResource(null);

        // ✅ Resource 삭제 (orphanRemoval 활성화)
        resourceRepository.delete(resource);
        resourceRepository.flush();
        System.out.println("Resource 테이블 삭제 완료");

        // ✅ Dataset이 다른 리소스를 가지고 있는지 확인 후 삭제
        if (datasetRepository.existsById(dataset.getDatasetId()) && dataset.getResource() == null) {
            datasetRepository.delete(dataset);
            datasetRepository.flush();
            System.out.println("Dataset 테이블 삭제 완료");
        }

        return "파일이 삭제되었습니다";
    }


    /*@Transactional
    @DeleteMapping("/delete/{resourceId}")
    public String deleteFile(@PathVariable Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new IllegalArgumentException("리소스를 찾을 수 없습니다: " + resourceId));

        Dataset dataset = resource.getDataset();

        // S3에서 파일 삭제
        s3Service.deleteFile(resource.getResourceUrl());

        // Resource 삭제
        resourceRepository.delete(resource);
        resourceRepository.flush();
        System.out.println("Resource 테이블 삭제 완료");

        boolean isDatasetEmpty = resourceRepository.findByDataset(dataset).isEmpty();
        System.out.println("해당 dataset의 리소스 존재 여부: " + isDatasetEmpty);

        // 해당 dataset이 다른 리소스를 가지;고 있는지 확인 후 삭제
        if (dataset != null && resourceRepository.findByDataset(dataset).isEmpty()) {
            datasetRepository.delete(dataset);
            datasetRepository.flush();
            System.out.println("Dataset 테이블 삭제 완료");
        }

        return "파일이 삭제되었습니다";
    }*/
}