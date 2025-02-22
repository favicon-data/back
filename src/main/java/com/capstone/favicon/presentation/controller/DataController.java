package com.capstone.favicon.presentation.controller;


import com.capstone.favicon.application.repository.DataService;
import com.capstone.favicon.config.APIResponse;
import com.capstone.favicon.domain.domain.Data;
import com.capstone.favicon.domain.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/data-table/search-sorted")
    public ResponseEntity<APIResponse<?>> search(@RequestBody SearchDto searchDto) {
        try {
            List<Data> dataList = dataService.search(searchDto.getText());
            return ResponseEntity.ok().body(APIResponse.successAPI("검색결과", dataList));
        } catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(APIResponse.errorAPI(message));
        }
    }

    @GetMapping("/data-table/search-sorted/{category}")
    public ResponseEntity<APIResponse<?>> searchWithCategory(@PathVariable("category") String category, @RequestBody SearchDto searchDto) {
        try {
            List<Data> dataList = dataService.searchWithCategory(searchDto.getText(), category);
            return ResponseEntity.ok().body(APIResponse.successAPI("검색결과", dataList));
        } catch (Exception e) {
            String message = e.getMessage();
            return ResponseEntity.badRequest().body(APIResponse.errorAPI(message));
        }
    }

}
