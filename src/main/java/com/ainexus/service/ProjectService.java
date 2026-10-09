package com.ainexus.service;

import com.ainexus.dto.ProjectRequest;
import com.ainexus.dto.ReviewRequest;
import com.ainexus.model.Project;
import com.ainexus.model.ReviewSubmission;
import com.ainexus.model.Team;
import com.ainexus.model.TeamMember;
import com.ainexus.model.User;
import com.ainexus.repository.ProjectRepository;
import com.ainexus.repository.ReviewSubmissionRepository;
import com.ainexus.repository.TeamMemberRepository;
import com.ainexus.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {
    private static final LocalDate REVIEW_1_DEADLINE = LocalDate.of(2026, 10, 10);
    private static final LocalDate REVIEW_2_DEADLINE = LocalDate.of(2026, 11, 15);

    private final TeamRepository teams;
    private final TeamMemberRepository members;
    private final ProjectRepository projects;
    private final ReviewSubmissionRepository reviews;

    public ProjectService(TeamRepository teams, TeamMemberRepository members,
                          ProjectRepository projects, ReviewSubmissionRepository reviews) {
        this.teams = teams;
        this.members = members;
        this.projects = projects;
        this.reviews = reviews;
    }

    public Map<String, Object> get(User user) {
        Team team = members.findByUser(user).map(TeamMember::getTeam).orElse(null);
        if (team == null) {
            Map<String, Object> empty = new LinkedHashMap<>();
            empty.put("exists", false);
            empty.put("team", "");
            empty.put("teamSize", 0);
            empty.put("title", "");
            empty.put("description", "");
            empty.put("status", "NOT_STARTED");
            empty.put("reviews", List.of());
            return empty;
        }
        Project project = projects.findByTeam(team).orElse(null);
        if (project == null) {
            Map<String, Object> empty = new LinkedHashMap<>();
            empty.put("exists", false);
            empty.put("team", team.getName());
            empty.put("teamSize", members.countByTeam(team));
            empty.put("title", "");
            empty.put("description", "");
            empty.put("status", "NOT_STARTED");
            empty.put("reviews", List.of());
            return empty;
        }
        return detail(project);
    }

    public Map<String, Object> save(User user, ProjectRequest request) {
        Team team = findTeam(user);
        if (!team.getLeader().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Only the team leader can submit the project.");
        }
        long size = members.countByTeam(team);
        if (size < 3 || size > 4) {
            throw new IllegalArgumentException("A project team must contain 3 to 4 members before project submission.");
        }

        Project project = projects.findByTeam(team).orElseGet(() -> new Project(team, request.getTitle().trim(), request.getDescription().trim()));
        if (!"REVIEW_0_PENDING".equals(project.getStatus())) {
            throw new IllegalArgumentException("Project details are locked after Review 0 submission.");
        }
        project.setTitle(request.getTitle().trim());
        project.setDescription(request.getDescription().trim());
        projects.save(project);
        return detail(project);
    }

    public Map<String, Object> submitReview(User user, ReviewRequest request) {
        Project project = getProject(user);
        int reviewNo = request.getReviewNo();

        switch (reviewNo) {
            case 0 -> {
                if (members.countByTeam(project.getTeam()) < 3) {
                    throw new IllegalArgumentException("A project team must have at least 3 members before Review 0 submission.");
                }
                if (!"REVIEW_0_PENDING".equals(project.getStatus())) {
                    throw new IllegalArgumentException("Review 0 is already submitted or approved.");
                }
            }
            case 1 -> {
                if (!"REVIEW_1_OPEN".equals(project.getStatus())) {
                    throw new IllegalArgumentException("Review 1 is not open yet. Review 0 must be approved.");
                }
                if (LocalDate.now().isAfter(REVIEW_1_DEADLINE)) {
                    throw new IllegalArgumentException("Review 1 deadline has passed: 10 October 2026.");
                }
            }
            case 2 -> {
                if (!"REVIEW_2_OPEN".equals(project.getStatus())) {
                    throw new IllegalArgumentException("Review 2 is not open yet. Review 1 must be approved.");
                }
                if (LocalDate.now().isAfter(REVIEW_2_DEADLINE)) {
                    throw new IllegalArgumentException("Review 2 deadline has passed: 15 November 2026.");
                }
            }
            default -> throw new IllegalArgumentException("Review number must be 0, 1 or 2.");
        }

        if (reviews.findByProjectAndReviewNo(project, reviewNo).isPresent()) {
            throw new IllegalArgumentException("This review has already been submitted.");
        }

        ReviewSubmission submission = reviews.save(new ReviewSubmission(project, reviewNo, request.getContent().trim()));
        submission.setStatus("SUBMITTED");
        reviews.save(submission);

        project.setStatus("REVIEW_" + reviewNo + "_SUBMITTED");
        projects.save(project);
        return detail(project);
    }

    public List<Map<String, Object>> allProjects() {
        return projects.findAll().stream().map(this::detail).toList();
    }

    public Map<String, Object> approve(Long id, int reviewNo, Integer marks, String feedback) {
        if (reviewNo < 0 || reviewNo > 2) {
            throw new IllegalArgumentException("Review number must be 0, 1 or 2.");
        }
        Project project = projects.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found."));
        ReviewSubmission submission = reviews.findByProjectAndReviewNo(project, reviewNo)
                .orElseThrow(() -> new IllegalArgumentException("Review submission not found."));

        if (!"SUBMITTED".equals(submission.getStatus())) {
            throw new IllegalArgumentException("This review is already processed.");
        }

        int maxMarks = reviewNo == 1 ? 33 : reviewNo == 2 ? 17 : 0;
        if (marks == null || marks < 0 || marks > maxMarks) {
            throw new IllegalArgumentException("Marks must be between 0 and " + maxMarks + " for Review " + reviewNo + ".");
        }
        String cleanFeedback = feedback == null ? "" : feedback.trim();
        if (cleanFeedback.length() > 2000) {
            throw new IllegalArgumentException("Faculty feedback cannot exceed 2000 characters.");
        }

        submission.setStatus("APPROVED");
        submission.setMarks(marks);
        submission.setFeedback(cleanFeedback);
        reviews.save(submission);

        project.setStatus(reviewNo == 0 ? "REVIEW_1_OPEN" : reviewNo == 1 ? "REVIEW_2_OPEN" : "COMPLETED");
        projects.save(project);
        return detail(project);
    }

    private Team findTeam(User user) {
        return members.findByUser(user)
                .map(TeamMember::getTeam)
                .orElseThrow(() -> new IllegalArgumentException("Join a team first."));
    }

    private Project getProject(User user) {
        Team team = findTeam(user);
        return projects.findByTeam(team)
                .orElseThrow(() -> new IllegalArgumentException("Create your project first."));
    }

    private Map<String, Object> detail(Project project) {
        List<Map<String, Object>> reviewDetails = reviews.findByProjectOrderByReviewNoAsc(project).stream()
                .<Map<String, Object>>map(s -> {
                    Map<String, Object> detail = new LinkedHashMap<>();
                    detail.put("reviewNo", s.getReviewNo());
                    detail.put("content", s.getContent());
                    detail.put("status", s.getStatus());
                    detail.put("marks", s.getMarks() == null ? Integer.valueOf(0) : s.getMarks());
                    detail.put("feedback", s.getFeedback() == null ? "" : s.getFeedback());
                    detail.put("submittedAt", s.getSubmittedAt().toString());
                    return detail;
                })
                .toList();

        return Map.of(
                "exists", true,
                "id", project.getId(),
                "title", project.getTitle(),
                "description", project.getDescription(),
                "status", project.getStatus(),
                "team", project.getTeam().getName(),
                "reviews", reviewDetails,
                "dates", Map.of("review1Deadline", REVIEW_1_DEADLINE.toString(), "review2Deadline", REVIEW_2_DEADLINE.toString())
        );
    }
}
