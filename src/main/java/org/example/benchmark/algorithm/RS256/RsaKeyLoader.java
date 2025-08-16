package org.example.benchmark.algorithm.RS256;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@UtilityClass
public class RsaKeyLoader {

    private InputStream open(String location) throws Exception {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Key location is empty");
        }

        if (location.startsWith("classpath:")) {
            String cp = location.substring("classpath:".length());
            InputStream in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(cp);
            if (in == null) {
                throw new IllegalArgumentException("Key not found on classpath: " + cp);
            }
            return in;
        }

        if (location.startsWith("file:")) {
            return Files.newInputStream(Path.of(URI.create(location)));
        }

        InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(location);
        if (in != null) return in;

        Path p = Path.of(location);
        if (Files.exists(p)) return Files.newInputStream(p);

        throw new IllegalArgumentException("Key not found: " + location);
    }

    private String readPem(String location) {
        try (InputStream is = open(location)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to read key: " + location, e);
        }
    }

    private byte[] decodePem(String pem) {
        String cleaned = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        return Base64.getDecoder().decode(cleaned);
    }

    @SneakyThrows
    public PrivateKey loadPrivate(String location) {
        String pem = readPem(location);
        byte[] der = decodePem(pem);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    @SneakyThrows
    public PublicKey loadPublic(String location) {
        String pem = readPem(location);
        byte[] der = decodePem(pem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
