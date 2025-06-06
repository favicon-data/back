package com.capstone.favicon.dataset.controller;

import com.capstone.favicon.config.APIResponse;
import com.capstone.favicon.dataset.application.service.S3FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/data-set")
public class s3FileDownloadController {

    @Autowired
    private S3FileDownloadService s3FileDownloadService;

    @GetMapping("/download/{datasetId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long datasetId) throws IOException {
        File downloadedFile = s3FileDownloadService.downloadFile(datasetId);
        Resource fileResource = new FileSystemResource(downloadedFile);
        String fileName = downloadedFile.getName();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }

}
