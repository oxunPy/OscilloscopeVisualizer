package com.example.program.util.widget.combo;

import com.example.program.app.property.OsciLanguageProperty;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

public class LanguageComboboxConverter extends StringConverter<OsciLanguageProperty> {
    private Map<String, OsciLanguageProperty> map = new HashMap<>();
    @Override
    public String toString(OsciLanguageProperty object) {
        if(object != null){
            String str = object.getName();
            map.put(str, object);
            return str;
        }
        return null;
    }

    @Override
    public OsciLanguageProperty fromString(String string) {
        if(string != null && map.containsKey(string)){
            return map.get(string);
        }
        return null;
    }
}
