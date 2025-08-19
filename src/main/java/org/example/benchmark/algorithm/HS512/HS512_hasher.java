package org.example.benchmark.algorithm.HS512;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.benchmark.jwt_benchmark.JwtGenerator;
import org.example.benchmark.jwt_benchmark.JwtClaimsFactory;
import org.example.benchmark.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HS512_hasher implements JwtGenerator {

    @Value("${jwt.hs512.signer-key}")
    private String signerKeyB64;

    @Value("${jwt.valid-seconds:3600}")
    long validSeconds;

    @Override
    public String generate(User user) {
        try {
            byte[] secret = Base64.getDecoder().decode(signerKeyB64);

            JWTClaimsSet c = JwtClaimsFactory.fromUser(user, validSeconds);

            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512)
                    .type(JOSEObjectType.JWT).build();

            SignedJWT jwt = new SignedJWT(header, c);
            jwt.sign(new MACSigner(secret));

            return jwt.serialize();
        } catch (Exception e) {
            throw new IllegalStateException("HS512 sign error", e);
        }
    }
}
