package org.example.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

public class KeyGenerator {
   public static KeyPair keyPair;

    public KeyGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    public static void generateKey() throws IOException {
        String base64PrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String base64PublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        writeToFile("private_key.pem", "PRIVATE KEY", base64PrivateKey);
        writeToFile("public_key.pem", "PUBLIC KEY", base64PublicKey);
    }
    private static void writeToFile(String filename, String keyType, String base64Key) throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("-----BEGIN " + keyType + "-----\n");
            writer.write(base64Key);
            writer.write("\n-----END " + keyType + "-----\n");
        }
    }

}
