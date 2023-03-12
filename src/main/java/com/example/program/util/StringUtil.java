package com.example.program.util;
import org.apache.commons.lang.StringEscapeUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {

    public static final String COMMA = ",";

    /**
     * berilgan tekst ni NULL ga tekshirish
     *
     * @param text
     * @return
     */
    public static boolean isNull(String text) {
        return text == null;
    }

    /**
     * berilgan tekst ni NULL yoki BO`SH ga tekshirish
     *
     * @param text
     * @return
     */
    public static boolean isNullEmpty(String text) {
        return text == null || text.isEmpty();
    }

    /**
     * berilgan tekst ni NULL yoki BO`SH yoki BO`SH PROBELLER ga tekshirish
     *
     * @param text
     * @return
     */
    public static boolean isNullEmptySpace(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * berilgan tekst ni NULL ga tekshirish
     *
     * @param text
     * @return
     */
    public static boolean hasNull(String... text) {
        for (String s : text)
            if (isNull(s))
                return true;

        return false;
    }

    /**
     * berilgan tekst ni NULL yoki BO`SH ga tekshirish
     *
     * @param text
     * @return
     */
    public static boolean hasNullEmpty(String... text) {
        for (String s : text)
            if (isNullEmpty(s))
                return true;

        return false;
    }

    /**
     * berilgan tekst ni NULL yoki BO`SH yoki BO`SH PROBELLER ga tekshirish
     *
     * @param text
     * @return
     */
    public static boolean hasNullEmptySpace(String... text) {
        for (String s : text)
            if (isNullEmptySpace(s))
                return true;

        return false;
    }

    /**
     * Enum obyektning ordinal larini vergul bilan birlashtirish
     *
     * @param enums
     * @return
     */
    public static String joinEnum(List<? extends Enum> enums) {
        if (enums == null)
            return null;
        return enums
                .stream()
                .map(e -> String.valueOf(e.ordinal()))
                .collect(Collectors.joining(COMMA, "", ""));
    }

    /**
     * Enum obyektning ordinal larini vergul bilan birlashtirish
     *
     * @param enums
     * @return
     */
    public static <T extends Enum> String joinEnum(T... enums) {
        if (enums == null)
            return null;
        List<T> list = Arrays.asList(enums);
        return joinEnum(list);
    }

    /**
     * Integer larni vergul bilan birlashtirish
     *
     * @param list
     * @return
     */
    public static String joinInt(List<Integer> list) {
        if (list == null)
            return null;
        return list
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(COMMA, "", ""));
    }

    public static String escapeHtml(String str) {
        return StringEscapeUtils.escapeHtml(str);
    }

    public static String unescapeHtml(String str) {
        return StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeHtml(str));
    }

    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBigDecimal(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal parseBigDecimal(String str) {
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String toString(Object o) {
        return o != null ? o.toString() : null;
    }

    public static String toStringTrim(Object o) {
        return o != null ? o.toString().trim() : null;
    }

    public static String joinListItems(List<Integer> list) {
        StringBuilder result = new StringBuilder();
        for (Integer item : list) {
            result.append((result.length() == 0) ? item : "," + item);
        }
        return result.toString();
    }

    public static String clearAnother(String text) {
        if (text == null) {
            return null;
        }
        return text.replaceAll("[^0-9,^a-z,^A-Z]", "");
    }

    public static boolean notEmpty(String... values) {
        boolean notEmpty = true;
        for (String value : values) {
            if (value == null || value.trim().isEmpty()) {
                notEmpty = false;
                break;
            }
        }
        return notEmpty;
    }

    public static boolean hasKeys(String[] keys, String key) {
        return Arrays.stream(keys).filter(s -> s.equals(key)).findAny().orElse(null) != null;
    }
}
