package com.ainexus.repository;
import com.ainexus.model.User;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation,Long> { List<Recommendation> findByRequirementUserOrderByCreatedAtDesc(User user); }
