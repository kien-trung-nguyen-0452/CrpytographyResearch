package org.example.benchmark.model;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String userId;
    private String username;
    private double cpuUsage; // % CPU
    private long ramUsage;   // RAM (bytes)
}
