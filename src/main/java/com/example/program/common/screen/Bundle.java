package com.example.program.common.screen;

import java.util.HashMap;
import java.util.Map;

public class Bundle {
    private Map<String, Object> cache = new HashMap<>();

    public Bundle put(String key, Object obj) {
        cache.put(key, obj);
        return this;
    }

    public Object get(String key) {
        if (cache.containsKey(key))
            return cache.get(key);
        return null;
    }

    public Integer getInt(String key, Integer defaultValue) {
        if (cache.containsKey(key) && cache.get(key) instanceof Integer) {
            return (Integer) cache.get(key);
        } else
            return defaultValue;
    }

    public Integer getInt(String key) {
        return getInt(key, 0);
    }

    public Boolean getBoolean(String key) {

        return getBoolean(key, false);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        if (cache.containsKey(key) && cache.get(key) instanceof Boolean) {
            return (Boolean) cache.get(key);
        } else
            return defaultValue;
    }

    public boolean has(String key) {
        return cache.containsKey(key);
    }

    public void copy(Bundle bundle) {
        cache.putAll(bundle.cache);
    }

    public static Bundle create() {
        return new Bundle();
    }
}
