package com.ainexus.repository;
import com.ainexus.model.User;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.AuthToken;

public interface AuthTokenRepository extends JpaRepository<AuthToken,Long> { Optional<AuthToken> findByTokenValue(String tokenValue); void deleteByUser(User user); }
