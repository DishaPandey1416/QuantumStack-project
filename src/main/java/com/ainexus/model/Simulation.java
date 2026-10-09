package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="simulations") public class Simulation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @ManyToOne(optional=false) private User user;
 @Column(nullable=false) private String type; @Column(nullable=false,length=12000) private String inputJson; @Column(nullable=false,length=12000) private String resultJson; private LocalDateTime createdAt;
 public Simulation(){} public Simulation(User u,String t,String in,String out){user=u;type=t;inputJson=in;resultJson=out;createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public User getUser(){return user;} public String getType(){return type;} public String getInputJson(){return inputJson;} public String getResultJson(){return resultJson;} public LocalDateTime getCreatedAt(){return createdAt;}
}
