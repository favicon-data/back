package com.capstone.favicon.user.controller;

import com.capstone.favicon.user.domain.DataRequest;
import com.capstone.favicon.user.domain.Question;
import com.capstone.favicon.user.domain.Answer;
import com.capstone.favicon.user.application.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/list")
    public ResponseEntity<List<DataRequest>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @PostMapping("/list")
    public ResponseEntity<DataRequest> createRequest(@RequestBody DataRequest dataRequest) {
        return ResponseEntity.ok(requestService.createRequest(dataRequest));
    }

    @PutMapping("/list/{requestId}/review")
    public ResponseEntity<DataRequest> updateReviewStatus(@PathVariable Long requestId, @RequestParam DataRequest.ReviewStatus status) {
        return ResponseEntity.ok(requestService.updateReviewStatus(requestId, status));
    }

    @GetMapping("/question")
    public ResponseEntity<List<Question>> getQuestions(@RequestParam Long userId) {
        return ResponseEntity.ok(requestService.getQuestionsByUser(userId));
    }

    @GetMapping("/answer")
    public ResponseEntity<List<Answer>> getAnswers(@RequestParam Long questionId) {
        return ResponseEntity.ok(requestService.getAnswersByQuestion(questionId));
    }
}