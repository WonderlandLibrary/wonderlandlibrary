package com.thealtening.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author trol
 * @since 10/31/18
 */
public class ReflectionUtility {

    private String className;

    public ReflectionUtility(String className) {
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Class<?> clazz;
    public void setStaticField(String fieldName, Object newValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);

        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

}
