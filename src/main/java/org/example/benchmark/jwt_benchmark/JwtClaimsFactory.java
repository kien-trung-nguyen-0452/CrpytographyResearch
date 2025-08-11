package org.example.benchmark.jwt_benchmark;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class JwtClaimsFactory {

    public JWTClaimsSet fromMap(String sub, long validSeconds, Map<String, Object> extra) {
        Instant now = Instant.now();
        JWTClaimsSet.Builder b = new JWTClaimsSet.Builder()
                .subject(sub)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(validSeconds)))
                .jwtID(UUID.randomUUID().toString());
        if (extra != null) extra.forEach(b::claim);
        return b.build();
    }
}
