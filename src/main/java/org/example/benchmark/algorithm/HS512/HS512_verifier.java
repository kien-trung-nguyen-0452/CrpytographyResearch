package org.example.benchmark.algorithm.HS512;

import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.benchmark.jwt_benchmark.JwtVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component("HS512_Verifier")
@RequiredArgsConstructor
public class HS512_verifier implements JwtVerifier {

    @Value("${jwt.hs512.signer-key}")
    private String signerKeyB64;

    @Override
    public boolean verify(String jwt) {
        try {
            byte[] secret = Base64.getDecoder().decode(signerKeyB64);

            SignedJWT s = SignedJWT.parse(jwt);
            boolean sig = s.verify(new MACVerifier(secret));
            boolean notExpired = s.getJWTClaimsSet().getExpirationTime().after(new Date());
            return sig && notExpired;
        } catch (Exception e) {
            log.debug("Verify HS512 failed: {}", e.getMessage());
            return false;
        }
    }
}
