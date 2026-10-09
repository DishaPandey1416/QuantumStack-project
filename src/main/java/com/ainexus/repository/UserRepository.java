package com.ainexus.repository;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
import com.ainexus.model.User;

public interface UserRepository extends JpaRepository<User,Long> { Optional<User> findByEmail(String email); boolean existsByEmail(String email); }
