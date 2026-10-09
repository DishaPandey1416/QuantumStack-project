package com.ainexus.controller;

import com.ainexus.dto.ProjectRequest;
import com.ainexus.dto.ReviewRequest;
import com.ainexus.model.User;
import com.ainexus.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

    private User user(HttpServletRequest request) {
        return (User) request.getAttribute("AUTH_USER");
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request) {
        return service.get(user(request));
    }

    @PostMapping("/me")
    public Map<String, Object> save(HttpServletRequest request, @Valid @RequestBody ProjectRequest body) {
        return service.save(user(request), body);
    }

    @PostMapping("/reviews")
    public Map<String, Object> review(HttpServletRequest request, @Valid @RequestBody ReviewRequest body) {
        return service.submitReview(user(request), body);
    }
}
