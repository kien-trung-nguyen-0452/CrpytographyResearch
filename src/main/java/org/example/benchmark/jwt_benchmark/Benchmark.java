package org.example.benchmark.jwt_benchmark;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.benchmark.algorithm.HS512.HS512_hasher;
import org.example.benchmark.algorithm.HS512.HS512_verifier;
import org.example.benchmark.algorithm.RS256.RS256_hasher;
import org.example.benchmark.algorithm.RS256.RS256_verifier;
import org.example.benchmark.model.BenchmarkResult;
import org.example.benchmark.model.User;
import org.example.benchmark.ulti.UserSimulator;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class Benchmark {

    private final HS512_hasher hs512Hasher;
    private final HS512_verifier hs512Verifier;
    private final RS256_hasher rs256Hasher;
    private final RS256_verifier rs256Verifier;

    public List<BenchmarkResult> runBenchmark(int userNum) {
        Queue<User> users = UserSimulator.getSimulationModels(userNum);
        List<BenchmarkResult> results = new ArrayList<>();

        // Chạy HS512
        results.addAll(runForAlgorithm("HS512", users));

        // Chạy RS256
        results.addAll(runForAlgorithm("RS256", users));

        // Xuất CSV
        exportCsv(results, "benchmark_results.csv");

        return results;
    }

    private List<BenchmarkResult> runForAlgorithm(String algorithm, Queue<User> users) {
        List<BenchmarkResult> results = new ArrayList<>();

        for (User user : users) {
            boolean verified = true;
            long start = System.nanoTime();
            try {
                String token;
                if ("HS512".equals(algorithm)) {
                    token = hs512Hasher.generate(userToClaims(user));
                    verified = hs512Verifier.verify(token);
                } else {
                    token = rs256Hasher.generate(userToClaims(user));
                    verified = rs256Verifier.verify(token);
                }
            } catch (Exception e) {
                verified = false;
                log.error("Error hashing/verifying {} for user {}: {}", algorithm, user.getUsername(), e.getMessage());
            }
            long end = System.nanoTime();
            long totalTime = end - start;

            results.add(new BenchmarkResult(
                    algorithm,
                    user.getUsername(),
                    verified,
                    totalTime,
                    (double) totalTime,
                    1,
                    user.getCpuUsage(),
                    user.getRamUsage()
            ));
        }

        return results;
    }

    private void exportCsv(List<BenchmarkResult> results, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(BenchmarkResult.csvHeader() + "\n");
            for (BenchmarkResult r : results) {
                writer.write(r.toCsv() + "\n");
            }
            log.info("Benchmark results exported to {}", fileName);
        } catch (IOException e) {
            log.error("Error writing CSV: {}", e.getMessage());
        }
    }

    private java.util.Map<String, Object> userToClaims(User user) {
        java.util.Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("sub", user.getUsername());
        claims.put("cpu", user.getCpuUsage());
        claims.put("ram", user.getRamUsage());
        return claims;
    }
}
