package com.example.stock.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String SALT = "1iz9j2a8n";

    public static String inputPassToDBPass(String inputPass) {
        String src = SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(3) +SALT.charAt(6);
        return md5(src);
    }
}
