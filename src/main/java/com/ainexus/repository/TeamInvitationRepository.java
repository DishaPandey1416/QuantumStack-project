package com.ainexus.repository;
import com.ainexus.model.User;
import com.ainexus.model.Team;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.TeamInvitation;

public interface TeamInvitationRepository extends JpaRepository<TeamInvitation,Long> { List<TeamInvitation> findByInviteeAndStatus(User user,String status); Optional<TeamInvitation> findByIdAndInvitee(Long id,User user); boolean existsByTeamAndInviteeAndStatus(Team team, User user, String status); }
