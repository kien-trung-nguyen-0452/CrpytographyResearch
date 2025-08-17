package org.example.benchmark.jwt_benchmark;

import lombok.RequiredArgsConstructor;
import org.example.benchmark.model.BenchmarkResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/benchmark")
@RequiredArgsConstructor
public class MainController {

    private final Benchmark benchmark;
    @GetMapping("/run/{userNum}")
    public List<BenchmarkResult> runBenchmark(@PathVariable int userNum) {
        return benchmark.runBenchmark(userNum);
    }
}
