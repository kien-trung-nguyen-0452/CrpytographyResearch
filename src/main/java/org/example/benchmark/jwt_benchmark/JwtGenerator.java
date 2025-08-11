package org.example.benchmark.jwt_benchmark;

import java.util.Map;

public interface JwtGenerator {
    String generate(Map<String, Object> claims);
}