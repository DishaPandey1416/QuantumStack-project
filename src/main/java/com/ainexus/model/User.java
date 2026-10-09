package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String name;
 @Column(nullable=false,unique=true) private String email;
 @Column(nullable=false) private String password;
 @Column(nullable=false) private String role="STUDENT";
 @Column(nullable=false) private LocalDateTime createdAt;
 public User(){} public User(String n,String e,String p,String r){name=n;email=e;password=p;role=r;createdAt=LocalDateTime.now();}
 @PrePersist void pre(){if(createdAt==null)createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;} public String getPassword(){return password;} public String getRole(){return role;} public LocalDateTime getCreatedAt(){return createdAt;}
 public void setName(String v){name=v;} public void setEmail(String v){email=v;} public void setPassword(String v){password=v;} public void setRole(String v){role=v;}
}
