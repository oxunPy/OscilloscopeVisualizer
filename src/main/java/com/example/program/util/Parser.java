package com.example.program.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SQLQuery;
import java.util.Date;
import java.util.Map;

/**
 * Created by admin on 17.05.2016.
 */
public class Parser {

    public static final int _NOT_VARIABLE = -1;
    public static final int _FLOAT = 1;
    public static final int _INT = 0;
    public static final int _DOUBLE = 2;
    public static final int _STRING = 3;
    public static final int _CHAR = 4;
    public static final int _DATE = 5;
    public static final int _BOOLEAN = 6;
    public static final int _SHORT = 7;
    public static final int _LONG = 8;
    public static final int _BYTE = 9;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static int getFieldType(Class clazz) {

        if (long.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz))
            return _LONG;
        if (int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz))
            return _INT;
        if (short.class.isAssignableFrom(clazz))
            return _SHORT;
        if (byte.class.isAssignableFrom(clazz))
            return _BYTE;
        if (boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz))
            return _BOOLEAN;

        if (float.class.isAssignableFrom(clazz) || Float.class.isAssignableFrom(clazz))
            return _FLOAT;
        if (double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz))
            return _DOUBLE;
        if (String.class.isAssignableFrom(clazz))
            return _STRING;
        if (char.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz))
            return _CHAR;

        if (Date.class.isAssignableFrom(clazz))
            return _DATE;

        return _NOT_VARIABLE;

    }

    public static void setParams(Map<String, Object> parametrs, SQLQuery query) {
        for (String key : parametrs.keySet()) {
            Object o = parametrs.get(key);
            switch (Parser.getFieldType(o.getClass())) {
                case Parser._BOOLEAN:
                    query.setBoolean(key, (Boolean) o);
                    break;
                case Parser._FLOAT:
                    query.setFloat(key, (Float) o);
                    break;
                case Parser._INT:
                    query.setInteger(key, (Integer) o);
                    break;
                case Parser._DOUBLE:
                    query.setDouble(key, (Double) o);
                    break;
                case Parser._STRING:
                    query.setString(key, (String) o);
                    break;
                case Parser._CHAR:
                    query.setCharacter(key, (Character) o);
                    break;
                case Parser._DATE:
                    query.setDate(key, (Date) o);
                    break;
                case Parser._SHORT:
                    query.setShort(key, (Short) o);
                    break;
                case Parser._LONG:
                    query.setLong(key, (Long) o);
                    break;
                case Parser._BYTE:
                    query.setByte(key, (Byte) o);
                    break;
                case Parser._NOT_VARIABLE:

                    break;
            }
        }
    }
}
