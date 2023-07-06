package com.arturola.graphql.util;

import org.bouncycastle.crypto.generators.OpenBSDBCrypt;

import java.nio.charset.StandardCharsets;

public class HashUtil {
    private static final String BCRYPT_SALT = "$2a$10$2yjZ5QJ8Q";
    public static boolean isBycriptMatch(String original, String hashValue) {
        System.out.println(original.getBytes(StandardCharsets.UTF_8));

        return OpenBSDBCrypt.checkPassword(hashValue, original.getBytes(StandardCharsets.UTF_8));
    }

    public static String getBcryptHash(String original) {
        return OpenBSDBCrypt.generate(
                original.getBytes(StandardCharsets.UTF_8), BCRYPT_SALT.getBytes(StandardCharsets.UTF_8), 5
        );
    }
}
