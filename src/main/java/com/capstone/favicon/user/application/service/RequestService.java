package com.capstone.favicon.user.application.service;

import com.capstone.favicon.user.domain.DataRequest;
import com.capstone.favicon.user.domain.Question;
import com.capstone.favicon.user.domain.Answer;
import java.util.List;

public interface RequestService {
    List<DataRequest> getAllRequests();
    DataRequest createRequest(DataRequest dataRequest);
    DataRequest updateReviewStatus(Long requestId, DataRequest.ReviewStatus status);
    List<Question> getQuestionsByUser(Long userId);
    List<Answer> getAnswersByQuestion(Long questionId);
}