package com.capstone.favicon.dataset.controller;

import com.capstone.favicon.dataset.domain.Dataset;
import com.capstone.favicon.dataset.domain.DatasetTheme;
import com.capstone.favicon.dataset.application.DatasetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/data-set")
public class DatasetController {

    private final DatasetService datasetService;

    public DatasetController(DatasetService datasetService) {
        this.datasetService = datasetService;
    }

    @GetMapping
    public List<Dataset> getDatasets() {
        return datasetService.findAllDatasets();
    }

    @GetMapping("/top10")
    public List<Dataset> getTop10Datasets() {
        return datasetService.getTop10ByDownload();
    }

    @PostMapping("/incrementDownload/{datasetId}")
    public void incrementDownload(@PathVariable Long datasetId) {
        datasetService.incrementDownloadCount(datasetId);
    }

    @GetMapping("/theme")
    public List<DatasetTheme> filterByCategory(@RequestParam String theme) {
        return datasetService.filterByCategory(theme);
    }

    @GetMapping("/count")
    public long getTotalDatasetCount() {
        return datasetService.getTotalDatasetCount();
    }

    @GetMapping("/{datasetId}")
    public Optional<Dataset> getDatasetDetails(@PathVariable Long datasetId) {
        return datasetService.getDatasetDetails(datasetId);
    }

    @GetMapping("/ratio")
    public ResponseEntity<Map<String, Double>> getThemeRatio() {
        return ResponseEntity.ok(datasetService.getThemeRatio());
    }

    @GetMapping("/category/{themeId}")
    public ResponseEntity<List<Dataset>> getDatasetsByCategory(@PathVariable Long themeId) {
        List<Dataset> datasets = datasetService.getDatasetsByCategory(themeId);
        return ResponseEntity.ok(datasets);
    }

}
