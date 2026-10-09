package com.ainexus.repository;
import com.ainexus.model.User;
import com.ainexus.model.Team;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.TeamMember;

public interface TeamMemberRepository extends JpaRepository<TeamMember,Long> { List<TeamMember> findByTeam(Team team); Optional<TeamMember> findByTeamAndUser(Team team,User user); Optional<TeamMember> findByUser(User user); long countByTeam(Team team); }
