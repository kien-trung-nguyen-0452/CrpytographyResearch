package org.example.benchmark.algorithm.HS512;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.security.Key;


@Service
@RequiredArgsConstructor
public class HS512_verifier {
    @NonFinal
    @Value("${jwt.hs512.signer-key}")
    public String signerKey;



}
