package org.example.benchmark.jwt_benchmark;

import org.example.benchmark.model.User;

import java.util.Map;

public interface JwtGenerator {
    String generate(User user);
}