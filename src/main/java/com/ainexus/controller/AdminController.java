package com.ainexus.controller;

import com.ainexus.config.RoleGuard;
import com.ainexus.model.User;
import com.ainexus.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final ProjectService service;

    public AdminController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("/projects")
    public List<Map<String, Object>> projects(HttpServletRequest request) {
        User user = (User) request.getAttribute("AUTH_USER");
        RoleGuard.requireFacultyOrAdmin(user);
        return service.allProjects();
    }

    @PostMapping("/projects/{id}/reviews/{reviewNo}/approve")
    public Map<String, Object> approve(
            HttpServletRequest request,
            @PathVariable Long id,
            @PathVariable int reviewNo,
            @RequestParam(defaultValue = "0") Integer marks,
            @RequestParam(defaultValue = "") String feedback) {
        User user = (User) request.getAttribute("AUTH_USER");
        RoleGuard.requireFacultyOrAdmin(user);
        return service.approve(id, reviewNo, marks, feedback);
    }
}
