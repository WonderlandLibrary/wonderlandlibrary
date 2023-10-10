package rosegoldclient.utils;

import rosegoldclient.Main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static boolean invoke(Class<?> _class, String methodName) {
        try {
            final Method method = _class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(Main.mc);
            return true;
        } catch(Exception ignored) {}
        return false;
    }

    public static Object getFieldByName(final Class clazz, final String name, final Object object) {
        try {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static Object getFieldByName(final Object obj, final String name) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
