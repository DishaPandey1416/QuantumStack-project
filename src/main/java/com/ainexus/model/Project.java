package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="projects") public class Project {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Team team; @Column(nullable=false) private String title;
 @Column(nullable=false,length=3000) private String description; @Column(nullable=false) private String status="REVIEW_0_PENDING";
 @Column(nullable=false) private LocalDateTime createdAt; @Column(nullable=false) private LocalDateTime updatedAt;
 public Project(){} public Project(Team t,String title,String desc){team=t;this.title=title;description=desc;createdAt=LocalDateTime.now();updatedAt=createdAt;}
 @PreUpdate void update(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public Team getTeam(){return team;} public String getTitle(){return title;} public String getDescription(){return description;} public String getStatus(){return status;} public void setTitle(String v){title=v;} public void setDescription(String v){description=v;} public void setStatus(String v){status=v;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
