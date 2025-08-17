package org.example.benchmark.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BenchmarkResult {
    private String algorithm;
    private String UserNumber;
    private boolean verified;
    private long totalTimeNano;
    private double avgTimeNano;
    private int iterations;
    private double cpuUsage;     // % CPU sử dụng
    private long ramUsage;       // RAM sử dụng (bytes)

    public String toCsv() {
        return String.format("%s,%s,%s,%d,%.2f,%d,%.2f,%d",
                algorithm,
                UserNumber,
                verified,
                totalTimeNano,
                avgTimeNano,
                iterations,
                cpuUsage,
                ramUsage
        );
    }

    public static String csvHeader() {
        return "Algorithm,UserNumber,Verified,TotalTime(ns),AvgTime(ns),Iterations,CPU(%),RAM(bytes)";
    }
}
