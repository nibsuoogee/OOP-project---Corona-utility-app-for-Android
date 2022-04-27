package com.example.olio_ht;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashFunction {

    // Takes password and salt strings, generates SHA-512 hash form, returns hash string
    public static String getHash(String inputStringBytes, String salt) {
        String hashValue = inputStringBytes;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(inputStringBytes.getBytes());
            md.update(salt.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte bt: digest) {
                sb.append(String.format("%02x",bt));
            }
            hashValue = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashValue;
    }

    // Generates and returns random salt string
    public static String createSalt() {
        byte[] bytes = new byte[20];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(bytes);
        String salt = bytes.toString();
        return salt;
    }
}
