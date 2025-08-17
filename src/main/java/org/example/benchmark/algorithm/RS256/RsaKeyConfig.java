package org.example.benchmark.algorithm.RS256;


import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration


public class RsaKeyConfig {
    @NonFinal
    @Value("${jwt.rs256.public-key-path}")
    String publicKeyLocation;
    @NonFinal
    @Value("${jwt.rs256.private-key-path}")
    String privateKeyLocation;

    @Bean
    PublicKey jwtPublicKey() {
        return RsaKeyLoader.loadPublic(publicKeyLocation);
    }

    @Bean
    PrivateKey jwtPrivateKey() {
        return RsaKeyLoader.loadPrivate(privateKeyLocation);
    }
}