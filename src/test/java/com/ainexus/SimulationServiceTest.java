package com.ainexus;

import com.ainexus.model.User;
import com.ainexus.repository.SimulationRepository;
import com.ainexus.service.SimulationService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SimulationServiceTest {
    private final SimulationRepository repository = mock(SimulationRepository.class);
    private final SimulationService service = new SimulationService(repository);
    private final User user = new User("Test User", "test@example.com", "hashed", "STUDENT");

    @Test
    void hadamardCreatesEqualProbabilities() {
        Map<String, Object> result = service.quantum(user, 1, List.of("H"));
        List<Double> probabilities = (List<Double>) result.get("probabilities");
        assertEquals(List.of(0.5, 0.5), probabilities);
    }

    @Test
    void doubleHadamardReturnsToZeroState() {
        Map<String, Object> result = service.quantum(user, 1, List.of("H", "H"));
        List<Double> probabilities = (List<Double>) result.get("probabilities");
        assertEquals(List.of(1.0, 0.0), probabilities);
    }

    @Test
    void xGateFlipsZeroToOne() {
        Map<String, Object> result = service.quantum(user, 1, List.of("X"));
        List<Double> probabilities = (List<Double>) result.get("probabilities");
        assertEquals(List.of(0.0, 1.0), probabilities);
    }

    @Test
    void networkRejectsInvalidNodeCount() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.network(user, "STAR", 1, 100, 20));
        assertTrue(ex.getMessage().contains("between 2 and 100"));
    }
}
