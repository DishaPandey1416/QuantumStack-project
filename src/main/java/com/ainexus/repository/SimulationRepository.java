package com.ainexus.repository;
import com.ainexus.model.User;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.Simulation;

public interface SimulationRepository extends JpaRepository<Simulation,Long> { List<Simulation> findByUserOrderByCreatedAtDesc(User user); }
