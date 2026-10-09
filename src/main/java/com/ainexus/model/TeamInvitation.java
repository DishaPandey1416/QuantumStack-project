package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="team_invitations") public class TeamInvitation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Team team; @ManyToOne(optional=false) private User invitee;
 @Column(nullable=false) private String status="PENDING"; @Column(nullable=false) private LocalDateTime createdAt;
 public TeamInvitation(){} public TeamInvitation(Team t,User u){team=t;invitee=u;createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public Team getTeam(){return team;} public User getInvitee(){return invitee;} public String getStatus(){return status;} public void setStatus(String s){status=s;} public LocalDateTime getCreatedAt(){return createdAt;}
}
