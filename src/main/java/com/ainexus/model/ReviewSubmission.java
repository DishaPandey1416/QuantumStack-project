package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="review_submissions",uniqueConstraints=@UniqueConstraint(columnNames={"project_id","reviewNo"})) public class ReviewSubmission {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) private Project project; @Column(nullable=false) private Integer reviewNo;
 @Column(nullable=false,length=5000) private String content; @Column(nullable=false) private String status="SUBMITTED";
 private Integer marks; @Column(length=2000) private String feedback; @Column(nullable=false) private LocalDateTime submittedAt;
 public ReviewSubmission(){} public ReviewSubmission(Project p,Integer n,String c){project=p;reviewNo=n;content=c;submittedAt=LocalDateTime.now();}
 public Long getId(){return id;} public Project getProject(){return project;} public Integer getReviewNo(){return reviewNo;} public String getContent(){return content;} public String getStatus(){return status;} public Integer getMarks(){return marks;} public String getFeedback(){return feedback;} public LocalDateTime getSubmittedAt(){return submittedAt;}
 public void setStatus(String v){status=v;} public void setMarks(Integer v){marks=v;} public void setFeedback(String v){feedback=v;}
}
