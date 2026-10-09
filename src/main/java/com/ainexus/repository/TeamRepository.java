package com.ainexus.repository;
import com.ainexus.model.User;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.Team;

public interface TeamRepository extends JpaRepository<Team,Long> { Optional<Team> findByTeamCode(String code); Optional<Team> findByLeader(User leader); }
