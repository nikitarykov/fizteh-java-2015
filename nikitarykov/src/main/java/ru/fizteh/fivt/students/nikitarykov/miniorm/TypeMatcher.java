package ru.fizteh.fivt.students.nikitarykov.miniorm;


import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by Nikita Rykov on 18.12.2015.
 */
public class TypeMatcher {
    public static String match(Class clazz) {
        if (clazz.isArray()) {
            return "ARRAY";
        } else if (clazz.equals(Integer.class)) {
            return "INT";
        } else if (clazz.equals(Boolean.class)) {
            return "BOOL";
        } else if (clazz.equals(Byte.class)) {
            return "TINYINT";
        } else if (clazz.equals(Short.class)) {
            return "INT2";
        } else if (clazz.equals(Double.class)) {
            return "DOUBLE";
        } else if (clazz.equals(Float.class)) {
            return "FLOAT4";
        } else if (clazz.equals(Float.class)) {
            return "FLOAT4";
        } else if (clazz.equals(Date.class)) {
            return "DATE";
        } else if (clazz.equals(Date.class)) {
            return "DATE";
        } else if (clazz.equals(Time.class)) {
            return "TIME";
        } else if (clazz.equals(Timestamp.class)) {
            return "DATETIME";
        }  else if (clazz.equals(Character.class)) {
            return "CHAR";
        } else if (clazz.equals(String.class)) {
            return "VARCHAR(256)";
        } else if (clazz.equals(UUID.class)) {
            return "UUID";
        } else {
            return "OTHER";
        }
    }
}
