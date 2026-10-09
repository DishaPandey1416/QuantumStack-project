package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="team_members",uniqueConstraints=@UniqueConstraint(columnNames={"team_id","user_id"})) public class TeamMember {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Team team; @ManyToOne(optional=false) private User user;
 @Column(nullable=false) private String role="MEMBER"; @Column(nullable=false) private LocalDateTime joinedAt;
 public TeamMember(){} public TeamMember(Team t,User u,String r){team=t;user=u;role=r;joinedAt=LocalDateTime.now();}
 public Long getId(){return id;} public Team getTeam(){return team;} public User getUser(){return user;} public String getRole(){return role;} public LocalDateTime getJoinedAt(){return joinedAt;}
}
