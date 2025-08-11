package org.example.benchmark.algorithm.RS256;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.benchmark.jwt_benchmark.JwtVerifier;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;

@Slf4j
@Component("RS256_Verifier")
@RequiredArgsConstructor
public class RS256_verifier implements JwtVerifier {

    private final PublicKey publicKey;

    @Override
    public boolean verify(String jwt) {
        try {
            SignedJWT s = SignedJWT.parse(jwt);
            boolean sig = s.verify(new RSASSAVerifier((RSAPublicKey) publicKey));
            boolean notExpired = s.getJWTClaimsSet().getExpirationTime().after(new Date());
            return sig && notExpired;
        } catch (Exception e) {
            if (e instanceof ParseException) log.debug("Parse JWT failed: {}", e.getMessage());
            return false;
        }
    }
}
