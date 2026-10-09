package com.ainexus.service;

import com.ainexus.dto.InviteRequest;
import com.ainexus.dto.TeamRequest;
import com.ainexus.model.Team;
import com.ainexus.model.TeamInvitation;
import com.ainexus.model.TeamMember;
import com.ainexus.model.User;
import com.ainexus.repository.TeamInvitationRepository;
import com.ainexus.repository.TeamMemberRepository;
import com.ainexus.repository.TeamRepository;
import com.ainexus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TeamService {
    private final TeamRepository teams;
    private final TeamMemberRepository members;
    private final TeamInvitationRepository invitations;
    private final UserRepository users;

    public TeamService(TeamRepository teams, TeamMemberRepository members,
                       TeamInvitationRepository invitations, UserRepository users) {
        this.teams = teams;
        this.members = members;
        this.invitations = invitations;
        this.users = users;
    }

    public Map<String, Object> create(User leader, TeamRequest request) {
        if (members.findByUser(leader).isPresent()) {
            throw new IllegalArgumentException("You are already a member of a team.");
        }
        if (teams.findByLeader(leader).isPresent()) {
            throw new IllegalArgumentException("You already lead a team.");
        }

        String code = "QN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Team team = teams.save(new Team(code, request.getName().trim(), leader));
        members.save(new TeamMember(team, leader, "LEADER"));
        return detail(team);
    }

    public Map<String, Object> myTeam(User user) {
        Team team = members.findByUser(user).map(TeamMember::getTeam).orElse(null);
        if (team == null) {
            return Map.of("hasTeam", false);
        }
        return detail(team);
    }

    public void invite(User leader, InviteRequest request) {
        Team team = teams.findByLeader(leader)
                .orElseThrow(() -> new IllegalArgumentException("Only a team leader can invite members."));
        if (members.countByTeam(team) >= 4) {
            throw new IllegalArgumentException("A team can have maximum 4 members.");
        }

        String email = request.getEmail().trim().toLowerCase();
        User invitee = users.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Student account not found. Ask them to register first."));
        if (invitee.getId().equals(leader.getId())) {
            throw new IllegalArgumentException("You cannot invite yourself.");
        }
        if (members.findByUser(invitee).isPresent()) {
            throw new IllegalArgumentException("This student is already a member of a team.");
        }
        if (invitations.existsByTeamAndInviteeAndStatus(team, invitee, "PENDING")) {
            throw new IllegalArgumentException("A pending invitation already exists for this student.");
        }

        invitations.save(new TeamInvitation(team, invitee));
    }

    public List<Map<String, Object>> invitations(User user) {
        return invitations.findByInviteeAndStatus(user, "PENDING").stream()
                .map(i -> {
                    Map<String, Object> detail = new LinkedHashMap<>();
                    detail.put("id", i.getId());
                    detail.put("team", i.getTeam().getName());
                    detail.put("code", i.getTeam().getTeamCode());
                    detail.put("leader", i.getTeam().getLeader().getName());
                    return detail;
                })
                .toList();
    }

    public void respond(User user, Long id, boolean accept) {
        TeamInvitation invitation = invitations.findByIdAndInvitee(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Invitation not found."));
        if (!"PENDING".equals(invitation.getStatus())) {
            throw new IllegalArgumentException("Invitation already handled.");
        }

        if (accept) {
            if (members.findByUser(user).isPresent()) {
                throw new IllegalArgumentException("You are already in a team.");
            }
            if (members.countByTeam(invitation.getTeam()) >= 4) {
                throw new IllegalArgumentException("Team is already full.");
            }
            members.save(new TeamMember(invitation.getTeam(), user, "MEMBER"));
            invitation.setStatus("ACCEPTED");
        } else {
            invitation.setStatus("DECLINED");
        }
        invitations.save(invitation);
    }

    private Map<String, Object> detail(Team team) {
        List<Map<String, Object>> memberDetails = members.findByTeam(team).stream()
                .map(m -> {
                    Map<String, Object> detail = new LinkedHashMap<>();
                    detail.put("id", m.getUser().getId());
                    detail.put("name", m.getUser().getName());
                    detail.put("email", m.getUser().getEmail());
                    detail.put("role", m.getRole());
                    return detail;
                })
                .toList();

        return Map.of(
                "hasTeam", true,
                "id", team.getId(),
                "name", team.getName(),
                "code", team.getTeamCode(),
                "leaderId", team.getLeader().getId(),
                "leaderName", team.getLeader().getName(),
                "members", memberDetails
        );
    }
}
