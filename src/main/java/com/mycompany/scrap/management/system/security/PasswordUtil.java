package com.mycompany.scrap.management.system.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    private static final String ALGORITHM =
            "PBKDF2WithHmacSHA256";

    private PasswordUtil() {
        // Prevent creating objects from this utility class
    }

    /**
     * Creates a secure password hash.
     *
     * Format:
     * iterations:salt:hash
     */
    public static String hashPassword(String password) {

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Password cannot be empty."
            );
        }

        try {

            SecureRandom random = new SecureRandom();

            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            KeySpec specification = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHM);

            byte[] hash = factory
                    .generateSecret(specification)
                    .getEncoded();

            return ITERATIONS
                    + ":"
                    + Base64.getEncoder().encodeToString(salt)
                    + ":"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException
                | InvalidKeySpecException e) {

            throw new IllegalStateException(
                    "Password hashing failed.",
                    e
            );
        }
    }

    /**
     * Verifies a password against its stored hash.
     */
    public static boolean verifyPassword(
            String password,
            String storedPassword
    ) {

        if (password == null
                || storedPassword == null
                || storedPassword.isBlank()) {

            return false;
        }

        try {

            String[] parts = storedPassword.split(":");

            if (parts.length != 3) {
                return false;
            }

            int iterations = Integer.parseInt(parts[0]);

            byte[] salt = Base64.getDecoder()
                    .decode(parts[1]);

            byte[] expectedHash = Base64.getDecoder()
                    .decode(parts[2]);

            KeySpec specification = new PBEKeySpec(
                    password.toCharArray(),
                    salt,
                    iterations,
                    expectedHash.length * 8
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(ALGORITHM);

            byte[] actualHash = factory
                    .generateSecret(specification)
                    .getEncoded();

            return constantTimeEquals(
                    expectedHash,
                    actualHash
            );

        } catch (IllegalArgumentException
                | NoSuchAlgorithmException
                | InvalidKeySpecException e) {

            return false;
        }
    }

    /**
     * Compares two byte arrays safely.
     */
    private static boolean constantTimeEquals(
            byte[] first,
            byte[] second
    ) {

        if (first.length != second.length) {
            return false;
        }

        int result = 0;

        for (int i = 0; i < first.length; i++) {
            result |= first[i] ^ second[i];
        }

        return result == 0;
    }
}