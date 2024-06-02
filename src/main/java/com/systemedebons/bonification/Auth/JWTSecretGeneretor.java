package com.systemedebons.bonification.Auth;

import java.security.SecureRandom;
import java.util.Base64;

public class JWTSecretGeneretor {

    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secretKey = new byte[32];
        secureRandom.nextBytes(secretKey);
        String base64Secret = Base64.getEncoder().encodeToString(secretKey);
        System.out.println("Your Jwt Secret:" + base64Secret);
    }
}
