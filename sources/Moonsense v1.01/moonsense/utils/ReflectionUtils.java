// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class ReflectionUtils
{
    public static Object getPrivateFieldValueByType(final Object o, final Class objectClasstype, final Class fieldClasstype) {
        return getPrivateFieldValueByType(o, objectClasstype, fieldClasstype, 0);
    }
    
    public static Object getPrivateFieldValueByType(final Object o, final Class objectClasstype, final Class fieldClasstype, final int index) {
        Class objectClass;
        for (objectClass = o.getClass(); !objectClass.equals(objectClasstype) && objectClass.getSuperclass() != null; objectClass = objectClass.getSuperclass()) {}
        int counter = 0;
        final Field[] fields = objectClass.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fieldClasstype.equals(fields[i].getType())) {
                if (counter == index) {
                    try {
                        fields[i].setAccessible(true);
                        return fields[i].get(o);
                    }
                    catch (IllegalAccessException ex) {}
                }
                ++counter;
            }
        }
        return null;
    }
    
    public static Object getFieldValueByName(final Object o, final String fieldName) {
        final Field[] fields = o.getClass().getFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fieldName.equals(fields[i].getName())) {
                try {
                    fields[i].setAccessible(true);
                    return fields[i].get(o);
                }
                catch (IllegalAccessException ex) {}
            }
        }
        return null;
    }
    
    public static ArrayList getFieldsByType(final Object o, final Class objectClassBaseType, final Class fieldClasstype) {
        final ArrayList matches = new ArrayList();
        for (Class objectClass = o.getClass(); !objectClass.equals(objectClassBaseType) && objectClass.getSuperclass() != null; objectClass = objectClass.getSuperclass()) {
            final Field[] fields = objectClass.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                if (fieldClasstype.equals(fields[i].getType())) {
                    fields[i].setAccessible(true);
                    matches.add(fields[i]);
                }
            }
        }
        return matches;
    }
    
    public static Field getFieldByType(final Object o, final Class objectClasstype, final Class fieldClasstype) {
        return getFieldByType(o, objectClasstype, fieldClasstype, 0);
    }
    
    public static Field getFieldByType(final Object o, final Class objectClasstype, final Class fieldClasstype, final int index) {
        Class objectClass;
        for (objectClass = o.getClass(); !objectClass.equals(objectClasstype) && objectClass.getSuperclass() != null; objectClass = objectClass.getSuperclass()) {}
        int counter = 0;
        final Field[] fields = objectClass.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            if (fieldClasstype.equals(fields[i].getType())) {
                if (counter == index) {
                    fields[i].setAccessible(true);
                    return fields[i];
                }
                ++counter;
            }
        }
        return null;
    }
    
    public static Method getMethodByType(final Class objectType, final Class returnType, final Class... parameterTypes) {
        return getMethodByType(0, objectType, returnType, parameterTypes);
    }
    
    public static Method getMethodByType(final int index, final Class objectType, final Class returnType, final Class... parameterTypes) {
        final Method[] methods = objectType.getDeclaredMethods();
        int counter = 0;
        for (int i = 0; i < methods.length; ++i) {
            if (returnType.equals(methods[i].getReturnType())) {
                final Class[] methodParameterTypes = methods[i].getParameterTypes();
                if (parameterTypes.length == methodParameterTypes.length) {
                    boolean match = true;
                    for (int t = 0; t < parameterTypes.length; ++t) {
                        if (parameterTypes[t] != methodParameterTypes[t]) {
                            match = false;
                        }
                    }
                    if (counter == index) {
                        methods[i].setAccessible(true);
                        return methods[i];
                    }
                }
                ++counter;
            }
        }
        return null;
    }
    
    public static boolean classExists(final String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (ClassNotFoundException var2) {
            return false;
        }
    }
}
