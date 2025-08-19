package org.example.benchmark.jwt_benchmark;

import com.sun.management.OperatingSystemMXBean;
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

import java.io.File;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class Benchmark {

    private final HS512_hasher hs512Hasher;
    private final HS512_verifier hs512Verifier;
    private final RS256_hasher rs256Hasher;
    private final RS256_verifier rs256Verifier;

    /**
     * Chạy benchmark cho toàn bộ user với 1 thuật toán, trả về kết quả tổng hợp
     */
    public BenchmarkResult runBenchmark(int userNum, String algorithm) {
        // 1. Giả lập user
        List<User> users = UserSimulator.getSimulationModels(userNum);

        // 2. Thread pool = số core CPU
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        // 3. Sampling CPU/RAM song song
        CpuRamSampler sampler = new CpuRamSampler();
        Thread samplerThread = new Thread(sampler);
        samplerThread.start();

        // 4. Chạy benchmark song song
        List<Future<Long>> futures = new ArrayList<>();
        for (User user : users) {
            futures.add(executor.submit(() -> processUser(user, algorithm)));
        }

        long min = Long.MAX_VALUE, max = Long.MIN_VALUE, total = 0;
        boolean allVerified = true;

        for (Future<Long> f : futures) {
            try {
                long time = f.get();
                total += time;
                min = Math.min(min, time);
                max = Math.max(max, time);
            } catch (Exception e) {
                allVerified = false;
                log.error("Error executing task", e);
            }
        }

        executor.shutdown();

        // dừng sampler
        sampler.stop();
        try {
            samplerThread.join();
        } catch (InterruptedException ignored) {}

        double avgTime = (double) total / users.size();

        // 5. Lấy CPU/RAM trung bình
        double avgCpu = sampler.getAvgCpu();
        double avgRam = sampler.getAvgRam();

        // 6. Tạo BenchmarkResult tổng hợp
        BenchmarkResult result = BenchmarkResult.builder()
                .algorithm(algorithm)
                .userNumber(userNum)
                .verified(allVerified)
                .totalTimeNano(total)
                .avgTimeNano(avgTime)
                .minTimeNano(min)
                .maxTimeNano(max)
                .cpuUsage(avgCpu)
                .ramUsage(avgRam)
                .timestamp(System.currentTimeMillis())
                .build();

        // 7. Xuất CSV
        exportCsv(Collections.singletonList(result), "benchmark_results_" + algorithm + ".csv");

        log.info("Benchmark finished for {} users with {}. TotalTime={} ns, AvgTime={} ns, Min={} ns, Max={} ns, CPU={}%, RAM={} MB",
                userNum, algorithm, total, avgTime, min, max,
                String.format("%.2f", avgCpu), String.format("%.2f", avgRam));

        return result;
    }

    /**
     * Xử lý 1 user: tạo token + verify, trả về thời gian nano giây
     */
    private long processUser(User user, String algorithm) {
        long start = System.nanoTime();
        boolean verified;
        switch (algorithm.toUpperCase()) {
            case "HS512" -> {
                String tokenHS = hs512Hasher.generate(user);
                verified = hs512Verifier.verify(tokenHS);
            }
            case "RS256" -> {
                String tokenRS = rs256Hasher.generate(user);
                verified = rs256Verifier.verify(tokenRS);
            }
            default -> throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
        long end = System.nanoTime();
        return end - start;
    }

    /**
     * Lấy RAM đang sử dụng (bytes)
     */
    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Xuất kết quả ra file CSV
     */
    private void exportCsv(List<BenchmarkResult> results, String fileName) {
        try (PrintWriter pw = new PrintWriter(new File(fileName))) {
            pw.println(BenchmarkResult.csvHeader());
            for (BenchmarkResult r : results) {
                pw.println(r.toCsv());
            }
        } catch (Exception e) {
            log.error("Error exporting CSV", e);
        }
    }

    /**
     * Thread sampler đo CPU và RAM định kỳ
     */
    private class CpuRamSampler implements Runnable {
        private final OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        private final List<Double> cpuSamples = new ArrayList<>();
        private final List<Double> ramSamples = new ArrayList<>();
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                cpuSamples.add(osBean.getProcessCpuLoad() * 100);
                ramSamples.add(getUsedMemory() / (1024.0 * 1024.0)); // MB
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
            }
        }

        public void stop() {
            running = false;
        }

        public double getAvgCpu() {
            return cpuSamples.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }

        public double getAvgRam() {
            return ramSamples.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
    }
}
