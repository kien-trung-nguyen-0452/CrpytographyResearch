package org.example.benchmark.algorithm.HS512;


import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HS512_hasher {
    @NonFinal
    @Value("${jwt.hs512.signer-key}")
    protected String signerKey;


}
