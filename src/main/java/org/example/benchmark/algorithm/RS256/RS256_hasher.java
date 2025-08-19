package org.example.benchmark.algorithm.RS256;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.benchmark.jwt_benchmark.JwtGenerator;
import org.example.benchmark.jwt_benchmark.JwtClaimsFactory;
import org.example.benchmark.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RS256_hasher implements JwtGenerator {

    private final PrivateKey privateKey;

    @Value("${jwt.valid-seconds:3600}")
    long validSeconds;

    @Override
    public String generate(User user) {
        try {
            JWTClaimsSet c = JwtClaimsFactory.fromUser(user, validSeconds);

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    .type(JOSEObjectType.JWT).build();

            SignedJWT jwt = new SignedJWT(header, c);
            jwt.sign(new RSASSASigner(privateKey));

            return jwt.serialize();
        } catch (Exception e) {
            throw new IllegalStateException("RS256 sign error", e);
        }
    }
}