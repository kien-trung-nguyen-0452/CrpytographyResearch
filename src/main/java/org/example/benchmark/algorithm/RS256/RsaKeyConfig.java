package org.example.benchmark.algorithm.RS256;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
@RequiredArgsConstructor
public class RsaKeyConfig {

    @Value("${jwt.rs256.private-key-path}")
    String privatePath;

    @Value("${jwt.rs256.public-key-path}")
    String publicPath;

    @Bean
    PublicKey jwtPublicKey() {
        return RsaKeyLoader.loadPublic(publicPath);
    }

    @Bean
    PrivateKey jwtPrivateKey() {
        return RsaKeyLoader.loadPrivate(privatePath);
    }
}
