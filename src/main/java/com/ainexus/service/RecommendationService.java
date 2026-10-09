package com.ainexus.service;

import com.ainexus.dto.RequirementRequest;
import com.ainexus.model.Recommendation;
import com.ainexus.model.Requirement;
import com.ainexus.model.User;
import com.ainexus.repository.RecommendationRepository;
import com.ainexus.repository.RequirementRepository;
import com.ainexus.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RecommendationService {
    private final UserRepository users;
    private final RequirementRepository reqs;
    private final RecommendationRepository recs;

    public RecommendationService(UserRepository users, RequirementRepository reqs, RecommendationRepository recs) {
        this.users = users;
        this.reqs = reqs;
        this.recs = recs;
    }

    public Map<String, Object> analyze(User user, RequirementRequest q) {
        Requirement requirement = reqs.save(new Requirement(
                user, q.getTitle(), q.getDescription(), q.getProblemType(),
                q.getDatasetSize(), q.getPriority(), q.getBudget()));

        String text = (q.getTitle() + " " + q.getDescription() + " "
                + Objects.toString(q.getProblemType(), "")).toLowerCase();

        double ai = 20;
        double quantum = 10;
        double network = 10;

        if (text.matches(".*(machine learning|ml|classification|prediction|image|nlp|anomaly|data|dataset|ai).*")) {
            ai += 45;
        }
        if (text.matches(".*(optimization|combinatorial|quantum|qubit|grover|routing problem).*")) {
            quantum += 40;
        }
        if (text.matches(".*(network|latency|packet|bandwidth|routing|topology|throughput).*")) {
            network += 45;
        }
        if ("VERY_LARGE".equalsIgnoreCase(q.getDatasetSize())) {
            ai += 15;
        }
        if ("HIGH".equalsIgnoreCase(q.getPriority())) {
            ai += 5;
            network += 5;
        }

        double max = Math.max(ai, Math.max(quantum, network));
        String technology = ai == max ? "AI COMPUTING"
                : quantum == max ? "QUANTUM COMPUTING" : "NETWORK LAB";
        double confidence = Math.min(0.99, max / 100.0);

        String reason = technology.startsWith("AI")
                ? "Your requirement is dominated by data-driven computation, pattern detection or intelligent analysis."
                : technology.startsWith("QUANTUM")
                ? "Your requirement contains optimization or quantum-oriented characteristics that suit quantum simulation experiments."
                : "Your requirement is dominated by network behavior, routing, latency or throughput analysis.";

        String alternativeScores = "AI Computing: " + Math.round(ai)
                + " | Quantum: " + Math.round(quantum)
                + " | Network: " + Math.round(network);

        Recommendation recommendation = recs.save(new Recommendation(
                requirement, technology, confidence, reason, alternativeScores));

        Map<String, Object> scores = new LinkedHashMap<>();
        scores.put("AI COMPUTING", ai);
        scores.put("QUANTUM COMPUTING", quantum);
        scores.put("NETWORK LAB", network);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", recommendation.getId());
        result.put("requirementId", requirement.getId());
        result.put("technology", technology);
        result.put("confidence", confidence);
        result.put("reason", reason);
        result.put("scores", scores);
        return result;
    }

    public List<Map<String, Object>> history(User user) {
        return recs.findByRequirementUserOrderByCreatedAtDesc(user).stream()
                .<Map<String, Object>>map(x -> {
                    Map<String, Object> detail = new LinkedHashMap<>();
                    detail.put("id", x.getId());
                    detail.put("technology", x.getTechnology());
                    detail.put("confidence", x.getConfidence());
                    detail.put("reason", x.getReason());
                    detail.put("createdAt", x.getCreatedAt().toString());
                    return detail;
                })
                .toList();
    }
}
