package com.example.stock.util;

public class RandomStringGenerator {
    public static String get(int length) {
        String all = "qwertyuiopasdfghjklzxcvbnm1234567890";
        char[] chars = new char[length];
        for(int i = 0; i < length; i++) {
            chars[i] = all.charAt((int) (Math.random() * all.length()));
        }
        return new String(chars);
    }
}
