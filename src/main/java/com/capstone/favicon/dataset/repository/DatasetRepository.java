package com.capstone.favicon.dataset.repository;

import com.capstone.favicon.dataset.domain.Dataset;
import com.capstone.favicon.dataset.domain.DatasetTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {
    Optional<Dataset> findByS3Key(String s3Key);
    List<Dataset> findAllByOrderByDownloadDesc();
    List<Dataset> findTop9ByOrderByDownloadDesc();
    long countByDatasetTheme_DatasetThemeId(Long datasetThemeId);
    List<Dataset> findByDatasetTheme_DatasetThemeId(Long datasetThemeId);
    Optional<Dataset> findByDatasetThemeAndNameAndOrganization(DatasetTheme datasetTheme, String name, String organization);

    @Query("SELECT d FROM Dataset d JOIN FETCH d.datasetTheme")
    List<Dataset> findAllWithTheme();

    @Query(value = """
    SELECT * FROM dataset
    WHERE title ILIKE CONCAT('%', :keyword, '%') OR description ILIKE CONCAT('%', :keyword, '%')
    ORDER BY similarity(title, :keyword) DESC, created_at DESC
    """, nativeQuery = true)
    List<Dataset> searchByText(@Param("keyword") String keyword);

    @Query(value = """
    SELECT * FROM dataset
    WHERE title ILIKE CONCAT('%', :keyword, '%') OR description ILIKE CONCAT('%', :keyword, '%')
    AND category = :category
    ORDER BY similarity(title, :keyword) DESC, created_at DESC
    """, nativeQuery = true)
    List<Dataset> searchWithCategory(@Param("keyword") String keyword, @Param("category") String category);

}
