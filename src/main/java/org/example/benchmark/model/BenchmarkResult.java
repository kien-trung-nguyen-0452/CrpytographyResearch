package org.example.benchmark.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenchmarkResult implements Serializable {
    private String algorithm;
    private int userNumber;        // số lượng user giả lập
    private boolean verified;
    private long totalTimeNano;
    private double avgTimeNano;
    private long minTimeNano;
    private long maxTimeNano;
    private double cpuUsage;       // % CPU sử dụng trung bình
    private double ramUsage;       // MB RAM sử dụng trung bình
    private long timestamp;        // thời điểm chạy benchmark (epoch millis)

    // Xuất ra 1 dòng CSV
    public String toCsv() {
        return String.format("%s,%d,%b,%d,%.2f,%d,%d,%.2f,%.2f,%d",
                algorithm,
                userNumber,
                verified,
                totalTimeNano,
                avgTimeNano,
                minTimeNano,
                maxTimeNano,
                cpuUsage,
                ramUsage,
                timestamp
        );
    }

    // Header CSV
    public static String csvHeader() {
        return "Algorithm,UserNumber,Verified,TotalTime(ns),AvgTime(ns),MinTime(ns),MaxTime(ns),CPU(%),RAM(MB),Timestamp";
    }
}
