package org.example.benchmark.jwt_benchmark;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.UtilityClass;
import org.example.benchmark.model.User;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@UtilityClass
public class JwtClaimsFactory {

    public static JWTClaimsSet fromMap(String sub, long validSeconds, Map<String, Object> extra) {
        Instant now = Instant.now();
        JWTClaimsSet.Builder b = new JWTClaimsSet.Builder()
                .subject(sub)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(validSeconds)))
                .jwtID(UUID.randomUUID().toString());
        if (extra != null) extra.forEach(b::claim);
        return b.build();
    }
    public static JWTClaimsSet fromUser(User user, long validSeconds) {
        return new JWTClaimsSet.Builder()
                .subject(user.getUserId())
                .claim("username", user.getUsername())
                .expirationTime(new Date(System.currentTimeMillis() + validSeconds * 1000))
                .issueTime(new Date())
                .build();
    }

}
