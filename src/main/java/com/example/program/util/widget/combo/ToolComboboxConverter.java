package com.example.program.util.widget.combo;

import com.example.program.app.property.OsciLanguageProperty;
import com.example.program.app.property.OsciToolProperty;
import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

public class ToolComboboxConverter extends StringConverter<OsciToolProperty> {
    private Map<String, OsciToolProperty> map = new HashMap<>();
    @Override
    public String toString(OsciToolProperty object) {
        if(object != null){
            String str = object.getName();
            map.put(str, object);
            return str;
        }
        return null;
    }

    @Override
    public OsciToolProperty fromString(String string) {
        if(string != null && map.containsKey(string)){
            return map.get(string);
        }
        return null;
    }
}
