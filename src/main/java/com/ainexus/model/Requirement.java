package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="requirements") public class Requirement {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @ManyToOne(optional=false) private User user;
 @Column(nullable=false) private String title; @Column(nullable=false,length=5000) private String description;
 private String problemType; private String datasetSize; private String priority; private String budget; private LocalDateTime createdAt;
 public Requirement(){} public Requirement(User u,String t,String d,String pt,String ds,String pr,String b){user=u;title=t;description=d;problemType=pt;datasetSize=ds;priority=pr;budget=b;createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public User getUser(){return user;} public String getTitle(){return title;} public String getDescription(){return description;} public String getProblemType(){return problemType;} public String getDatasetSize(){return datasetSize;} public String getPriority(){return priority;} public String getBudget(){return budget;} public LocalDateTime getCreatedAt(){return createdAt;}
}
