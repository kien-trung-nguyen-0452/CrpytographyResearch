package org.example.benchmark.jwt_benchmark;

import lombok.RequiredArgsConstructor;
import org.example.benchmark.model.BenchmarkResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/benchmark")
@RequiredArgsConstructor
public class MainController {


    private final Benchmark benchmark;
    @GetMapping("/run/{algorithm}/{userNum}")
    public ResponseEntity<BenchmarkResult> runBenchmark(@PathVariable int userNum, @PathVariable String algorithm) {
        var result = benchmark.runBenchmark(userNum, algorithm);
        return ResponseEntity.ok(result);
    }
}
