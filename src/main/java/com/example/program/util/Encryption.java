package com.example.program.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Encrypt text through MD5
 */
public class Encryption {

    /**
     * Convert string to MD5 hash for encryption of user password in database
     */
    public static String convert(String original) {
        try {
            StringBuilder hexString = new StringBuilder();
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            byte messageDigest[] = algorithm.digest(original.getBytes("UTF-8"));

            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Message.error("Error converting user password \n" + ex);
        }

        return "";
    }

    public static void main(String[] args) {
        Pattern PIN4_NUMBER_PATTERN = Pattern.compile("^\\d{1,5}$");
        System.out.println(PIN4_NUMBER_PATTERN.matcher("2323").matches());
        System.out.println(PIN4_NUMBER_PATTERN.matcher("20323").matches());
        System.out.println(PIN4_NUMBER_PATTERN.matcher("2f23").matches());
        System.out.println(PIN4_NUMBER_PATTERN.matcher("032").matches());
        System.out.println(PIN4_NUMBER_PATTERN.matcher("23d9").matches());
        System.out.println(PIN4_NUMBER_PATTERN.matcher("2009").matches());
    }
}
