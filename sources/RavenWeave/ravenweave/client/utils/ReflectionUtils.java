package ravenweave.client.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static void setPrivateValue(Class clazz, Object obj, Object value, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getPrivateValue(Class clazz, Object obj, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Field findField(Class clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (Exception e) {
            return null;
        }
    }

    public static Method findMethod(Class clazz, Object unknown, String name, Class... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
