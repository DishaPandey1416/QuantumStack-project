package com.ainexus.config;

import com.ainexus.model.User;
import com.ainexus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(UserRepository users,
                           PasswordEncoder encoder,
                           @Value("${app.demo-data.enabled:true}") boolean enabled) {
        return args -> {
            if (!enabled) return;

            if (!users.existsByEmail("admin@ainexus.local")) {
                users.save(new User("AI Nexus Admin", "admin@ainexus.local", encoder.encode("Admin@123"), "ADMIN"));
            }
            if (!users.existsByEmail("faculty@ainexus.local")) {
                users.save(new User("AI Nexus Faculty", "faculty@ainexus.local", encoder.encode("Faculty@123"), "FACULTY"));
            }
        };
    }
}
