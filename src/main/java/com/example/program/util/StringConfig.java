package com.example.program.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class StringConfig {

    public static ResourceBundle bundle;
    public static Locale appLocale;
    public static final String SPACE = " ";

    public static ResourceBundle getPropertiesFromResource(Locale locale) {
        return ResourceBundle.getBundle("i18.messages", locale);
    }

    public static ResourceBundle getPropertiesFromResource() {
        return getPropertiesFromResource(getAppLocale());
    }

    public StringConfig(Locale locale) {
        bundle = getPropertiesFromResource(locale);
    }

    public StringConfig() {
        bundle = getPropertiesFromResource();
    }

    public static String getLocaleValue(String key, Locale locale) {
        if (bundle == null) bundle = getPropertiesFromResource(locale);

        return bundle.getString(key);
    }

    public static String getValue(String key) {
        return getLocaleValue(key, getAppLocale());
    }

    public static String formatValue(String key, Object... inners) {
        String value = getValue(key);
        return String.format(value,inners);
    }

    public static Locale getAppLocale() {
        appLocale = getLocale("en");
        return appLocale;
    }

    public static Locale getLocale(String localeStr) {
        return new Locale(localeStr);
    }

    public static String getValueEndSpace(String key) {
        return getValue(key) + SPACE;
    }
}
