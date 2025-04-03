package com.capstone.favicon.request.repository;

import com.capstone.favicon.request.domain.DataRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DataRequestRepository extends JpaRepository<DataRequest, Long> {
    List<DataRequest> findByReviewStatus(DataRequest.ReviewStatus reviewStatus);
}