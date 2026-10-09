package com.ainexus.repository;
import com.ainexus.model.Team;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.Project;

public interface ProjectRepository extends JpaRepository<Project,Long> { Optional<Project> findByTeam(Team team); }
