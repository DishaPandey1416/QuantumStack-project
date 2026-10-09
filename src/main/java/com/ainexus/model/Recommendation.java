package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="recommendations") public class Recommendation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @ManyToOne(optional=false) private Requirement requirement;
 @Column(nullable=false) private String technology; private Double confidence; @Column(length=3000) private String reason; @Column(length=2000) private String alternatives; private LocalDateTime createdAt;
 public Recommendation(){} public Recommendation(Requirement r,String t,double c,String reason,String alt){requirement=r;technology=t;confidence=c;this.reason=reason;alternatives=alt;createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public Requirement getRequirement(){return requirement;} public String getTechnology(){return technology;} public Double getConfidence(){return confidence;} public String getReason(){return reason;} public String getAlternatives(){return alternatives;} public LocalDateTime getCreatedAt(){return createdAt;}
}
