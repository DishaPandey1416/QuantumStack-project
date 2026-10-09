package com.ainexus.repository;
import com.ainexus.model.User;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.Requirement;

public interface RequirementRepository extends JpaRepository<Requirement,Long> { List<Requirement> findByUserOrderByCreatedAtDesc(User user); }
