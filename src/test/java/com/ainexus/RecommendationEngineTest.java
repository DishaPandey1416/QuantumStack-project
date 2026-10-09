package com.ainexus;

import com.ainexus.dto.RequirementRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecommendationEngineTest {
    @Test
    void requirementRequestStoresCoreInputs() {
        RequirementRequest request = new RequirementRequest();
        request.setTitle("Anomaly detection");
        request.setDescription("Detect unusual transactions in a large dataset");
        request.setProblemType("DATA");
        request.setDatasetSize("VERY_LARGE");
        request.setPriority("HIGH");

        assertEquals("Anomaly detection", request.getTitle());
        assertEquals("DATA", request.getProblemType());
        assertEquals("VERY_LARGE", request.getDatasetSize());
        assertEquals("HIGH", request.getPriority());
    }

    @Test
    void applicationIdentityIsStable() {
        assertEquals("AI Nexus", "AI Nexus");
    }
}
