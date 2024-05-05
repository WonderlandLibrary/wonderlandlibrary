package intent.AquaDev.aqua.altloader;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static Object getFieldByClass(Object o, Class<?> searchingClazz) {
        return ReflectionUtil.getFieldByClass(o.getClass(), o, searchingClazz);
    }

    public static Object getFieldByClass(Class<?> parentClass, Object o, Class<?> searchingClazz) {
        Field field = null;
        for (Field f : parentClass.getDeclaredFields()) {
            if (!f.getType().equals(searchingClazz)) continue;
            field = f;
            break;
        }
        if (field == null) {
            return null;
        }
        try {
            boolean isAccessible = field.isAccessible();
            ((Field)field).setAccessible(true);
            Object toReturn = field.get(o);
            field.setAccessible(isAccessible);
            return toReturn;
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setFieldByClass(Object parentObject, Object newObject) {
        Field field = null;
        for (Field f : parentObject.getClass().getDeclaredFields()) {
            if (!f.getType().isInstance(newObject)) continue;
            field = f;
            break;
        }
        if (field == null) {
            return;
        }
        try {
            boolean accessible = field.isAccessible();
            ((Field)field).setAccessible(true);
            field.set(parentObject, newObject);
            field.setAccessible(accessible);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setStaticField(Class clazz, String fieldName, Object value) {
        try {
            Field staticField = clazz.getDeclaredField(fieldName);
            staticField.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt((Object)staticField, staticField.getModifiers() & 0xFFFFFFEF);
            staticField.set(null, value);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
