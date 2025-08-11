package org.example.benchmark.algorithm.RS256;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@UtilityClass
public class RsaKeyLoader {

    private String read(String cp) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(cp)) {
            if (is == null) throw new IllegalArgumentException("Key not found: " + cp);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) { throw new IllegalStateException(e); }
    }

    private byte[] der(String pem, String begin, String end) {
        return Base64.getDecoder().decode(pem.replace(begin,"").replace(end,"").replaceAll("\\s",""));
    }

    @SneakyThrows
    public PrivateKey loadPrivate(String cp) {
        String pem = read(cp);
        byte[] data = der(pem, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(data));
    }

    @SneakyThrows
    public PublicKey loadPublic(String cp) {
        String pem = read(cp);
        byte[] data = der(pem, "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----");
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(data));
    }
}