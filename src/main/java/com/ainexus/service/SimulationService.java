package com.ainexus.service;

import com.ainexus.model.Simulation;
import com.ainexus.model.User;
import com.ainexus.repository.SimulationRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class SimulationService {
    private final SimulationRepository simulations;

    public SimulationService(SimulationRepository simulations) {
        this.simulations = simulations;
    }

    /**
     * Educational simulator. A gate in the UI is applied to every qubit.
     * H and X are implemented using state amplitudes so repeated operations
     * do not produce mathematically misleading probability-only results.
     */
    public Map<String, Object> quantum(User user, int qubits, List<String> gates) {
        if (qubits < 1 || qubits > 8) {
            throw new IllegalArgumentException("Quantum simulator supports 1-8 qubits.");
        }
        if (gates == null || gates.isEmpty() || gates.size() > 20) {
            throw new IllegalArgumentException("Choose between 1 and 20 gates.");
        }

        int states = 1 << qubits;
        double[] real = new double[states];
        double[] imag = new double[states];
        real[0] = 1.0;

        List<String> normalizedGates = new ArrayList<>();
        for (String rawGate : gates) {
            String gate = rawGate == null ? "" : rawGate.trim().toUpperCase(Locale.ROOT);
            if (!gate.equals("H") && !gate.equals("X")) {
                throw new IllegalArgumentException("Unsupported quantum gate: " + rawGate + ". Supported gates: H, X.");
            }
            normalizedGates.add(gate);
            for (int qubit = 0; qubit < qubits; qubit++) {
                if (gate.equals("X")) {
                    applyX(real, imag, qubit);
                } else {
                    applyH(real, imag, qubit);
                }
            }
        }

        List<Double> probabilities = new ArrayList<>(states);
        double total = 0.0;
        for (int i = 0; i < states; i++) {
            double p = real[i] * real[i] + imag[i] * imag[i];
            probabilities.add(p);
            total += p;
        }
        if (total > 0) {
            for (int i = 0; i < probabilities.size(); i++) {
                probabilities.set(i, round(probabilities.get(i) / total));
            }
        }

        Map<String, Object> result = Map.of(
                "qubits", qubits,
                "gates", normalizedGates,
                "probabilities", probabilities,
                "note", "Educational state-vector simulator. Each selected H or X gate is applied to every qubit."
        );
        simulations.save(new Simulation(user, "QUANTUM", normalizedGates.toString(), result.toString()));
        return result;
    }

    private void applyX(double[] real, double[] imag, int qubit) {
        int bit = 1 << qubit;
        for (int i = 0; i < real.length; i++) {
            if ((i & bit) == 0) {
                int j = i | bit;
                double tr = real[i];
                double ti = imag[i];
                real[i] = real[j];
                imag[i] = imag[j];
                real[j] = tr;
                imag[j] = ti;
            }
        }
    }

    private void applyH(double[] real, double[] imag, int qubit) {
        int bit = 1 << qubit;
        double invSqrt2 = 1.0 / Math.sqrt(2.0);
        for (int i = 0; i < real.length; i++) {
            if ((i & bit) == 0) {
                int j = i | bit;
                double ar = real[i];
                double ai = imag[i];
                double br = real[j];
                double bi = imag[j];
                real[i] = (ar + br) * invSqrt2;
                imag[i] = (ai + bi) * invSqrt2;
                real[j] = (ar - br) * invSqrt2;
                imag[j] = (ai - bi) * invSqrt2;
            }
        }
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

    public Map<String, Object> network(User user, String topology, int nodes, double bandwidth, double latency) {
        String normalizedTopology = topology == null ? "STAR" : topology.trim().toUpperCase(Locale.ROOT);
        if (!List.of("STAR", "RING", "MESH", "TREE", "BUS").contains(normalizedTopology)) {
            throw new IllegalArgumentException("Unsupported topology. Choose STAR, RING, MESH, TREE or BUS.");
        }
        if (nodes < 2 || nodes > 100) {
            throw new IllegalArgumentException("Nodes must be between 2 and 100.");
        }
        if (!Double.isFinite(bandwidth) || bandwidth <= 0 || bandwidth > 100000) {
            throw new IllegalArgumentException("Bandwidth must be greater than 0 and at most 100000 Mbps.");
        }
        if (!Double.isFinite(latency) || latency < 0 || latency > 10000) {
            throw new IllegalArgumentException("Latency must be between 0 and 10000 ms.");
        }

        double topologyFactor = switch (normalizedTopology) {
            case "MESH" -> 1.00;
            case "STAR" -> 0.96;
            case "RING" -> 0.91;
            case "TREE" -> 0.88;
            case "BUS" -> 0.75;
            default -> 1.0;
        };
        double congestion = Math.min(0.35, Math.max(0, nodes - 10) * 0.008);
        double latencyPenalty = Math.min(0.50, latency / 2000.0);
        double throughput = bandwidth * topologyFactor * (1.0 - congestion) * (1.0 - latencyPenalty);
        double packetLoss = Math.min(15.0, Math.max(0.0, congestion * 100.0 + latency / 400.0));
        double jitter = latency * 0.12 + nodes * 0.03;

        Map<String, Object> result = Map.of(
                "topology", normalizedTopology,
                "nodes", nodes,
                "bandwidthMbps", round(bandwidth),
                "latencyMs", round(latency),
                "estimatedThroughputMbps", round(throughput),
                "estimatedPacketLossPercent", round(packetLoss),
                "jitterMs", round(jitter)
        );
        simulations.save(new Simulation(user, "NETWORK", normalizedTopology + "/" + nodes, result.toString()));
        return result;
    }

    public List<Map<String, Object>> history(User user) {
        return simulations.findByUserOrderByCreatedAtDesc(user).stream()
                .<Map<String, Object>>map(s -> {
                    Map<String, Object> detail = new LinkedHashMap<>();
                    detail.put("id", s.getId());
                    detail.put("type", s.getType());
                    detail.put("input", s.getInputJson());
                    detail.put("result", s.getResultJson());
                    detail.put("createdAt", s.getCreatedAt().toString());
                    return detail;
                })
                .toList();
    }
}
