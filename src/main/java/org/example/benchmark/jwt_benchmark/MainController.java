package org.example.benchmark.jwt_benchmark;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/benchmark")
@RequiredArgsConstructor
public class MainController {

    @Qualifier("HS512_Generator")
    private final JwtGenerator hs512Gen;

    @Qualifier("HS512_Verifier")
    private final JwtVerifier hs512Ver;

    @Qualifier("RS256_Generator")
    private final JwtGenerator rs256Gen;

    @Qualifier("RS256_Verifier")
    private final JwtVerifier rs256Ver;

    @GetMapping("/run")
    public Map<String, Object> run(@RequestParam(defaultValue = "1000") int iterations) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "user123");
        claims.put("role", "admin");

        // HS512 Sign
        long start = System.nanoTime();
        String hsToken = null;
        for (int i = 0; i < iterations; i++) {
            hsToken = hs512Gen.generate(claims);
        }
        long hsSignTime = System.nanoTime() - start;

        // HS512 Verify
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            hs512Ver.verify(hsToken);
        }
        long hsVerifyTime = System.nanoTime() - start;

        // RS256 Sign
        start = System.nanoTime();
        String rsToken = null;
        for (int i = 0; i < iterations; i++) {
            rsToken = rs256Gen.generate(claims);
        }
        long rsSignTime = System.nanoTime() - start;

        // RS256 Verify
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            rs256Ver.verify(rsToken);
        }
        long rsVerifyTime = System.nanoTime() - start;

        result.put("iterations", iterations);
        result.put("HS512_sign_ms", hsSignTime / 1_000_000.0);
        result.put("HS512_verify_ms", hsVerifyTime / 1_000_000.0);
        result.put("RS256_sign_ms", rsSignTime / 1_000_000.0);
        result.put("RS256_verify_ms", rsVerifyTime / 1_000_000.0);

        return result;
    }
}
