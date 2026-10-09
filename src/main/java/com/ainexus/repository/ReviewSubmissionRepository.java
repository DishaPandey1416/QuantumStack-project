package com.ainexus.repository;
import com.ainexus.model.Project;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.ReviewSubmission;

public interface ReviewSubmissionRepository extends JpaRepository<ReviewSubmission,Long> { List<ReviewSubmission> findByProjectOrderByReviewNoAsc(Project project); Optional<ReviewSubmission> findByProjectAndReviewNo(Project project,Integer reviewNo); }
