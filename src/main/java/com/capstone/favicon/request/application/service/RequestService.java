package com.capstone.favicon.request.application.service;

import com.capstone.favicon.request.domain.DataRequest;
import com.capstone.favicon.request.domain.Question;
import com.capstone.favicon.request.domain.Answer;
import java.util.List;

public interface RequestService {
    List<DataRequest> getAllRequests();
    DataRequest createRequest(DataRequest dataRequest);
    DataRequest updateReviewStatus(Long requestId, DataRequest.ReviewStatus status);
    List<Question> getQuestionsByUser(Long userId);
    List<Answer> getAnswersByQuestion(Long questionId);
}