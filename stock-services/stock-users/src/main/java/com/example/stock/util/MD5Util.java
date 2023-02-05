package com.example.stock.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

public class MD5Util {

    public static final Integer SALT_LENGTH = 5;
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String lowSalt = salt.substring(0, salt.length()/2);
        String highSalt = salt.substring(salt.length()/2, salt.length());
        String src = lowSalt + inputPass + highSalt;
        return md5(src);
    }
}
