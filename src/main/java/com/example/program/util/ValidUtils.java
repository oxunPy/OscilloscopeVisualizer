package com.example.program.util;

import org.apache.commons.lang.math.NumberUtils;

public class ValidUtils {
    public static boolean isValidLine(String line) {
        return line != null && !line.trim().isEmpty();
    }

    public static boolean isValidNumber(String line) {
        return isValidLine(line) && NumberUtils.isNumber(line);
    }
}
