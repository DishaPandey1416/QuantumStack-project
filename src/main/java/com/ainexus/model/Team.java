package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="teams") public class Team {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String teamCode;
 @Column(nullable=false) private String name;
 @ManyToOne(optional=false) private User leader;
 @Column(nullable=false) private LocalDateTime createdAt;
 public Team(){} public Team(String code,String n,User l){teamCode=code;name=n;leader=l;createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getTeamCode(){return teamCode;} public String getName(){return name;} public User getLeader(){return leader;} public LocalDateTime getCreatedAt(){return createdAt;}
}
