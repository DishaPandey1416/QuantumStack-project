package com.ainexus.model;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="auth_tokens", indexes=@Index(name="idx_token_value",columnList="tokenValue",unique=true))
public class AuthToken {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=100) private String tokenValue;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User user;
 @Column(nullable=false) private LocalDateTime expiresAt;
 public AuthToken(){} public AuthToken(String t,User u,LocalDateTime e){tokenValue=t;user=u;expiresAt=e;}
 public String getTokenValue(){return tokenValue;} public User getUser(){return user;} public LocalDateTime getExpiresAt(){return expiresAt;}
}
