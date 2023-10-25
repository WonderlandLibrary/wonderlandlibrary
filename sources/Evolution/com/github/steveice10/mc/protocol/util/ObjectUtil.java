/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectUtil {
    private ObjectUtil() {
    }

    public static int hashCode(Object ... objects) {
        return Arrays.deepHashCode(objects);
    }

    public static String toString(Object o) {
        if (o == null) {
            return "null";
        }
        try {
            StringBuilder builder = new StringBuilder(o.getClass().getSimpleName()).append('(');
            List<Field> allDeclaredFields = ObjectUtil.getAllDeclaredFields(o.getClass());
            int i = 0;
            while (i < allDeclaredFields.size()) {
                if (i > 0) {
                    builder.append(", ");
                }
                Field field = allDeclaredFields.get(i);
                field.setAccessible(true);
                builder.append(field.getName()).append('=').append(ObjectUtil.memberToString(field.get(o)));
                ++i;
            }
            return builder.append(')').toString();
        }
        catch (Throwable e) {
            return String.valueOf(o.getClass().getSimpleName()) + '@' + Integer.toHexString(o.hashCode()) + '(' + e.toString() + ')';
        }
    }

    private static String memberToString(Object o) {
        if (o == null) {
            return "null";
        }
        if (o.getClass().isArray()) {
            int length = Array.getLength(o);
            if (length > 20) {
                return String.valueOf(o.getClass().getSimpleName()) + "(length=" + length + ')';
            }
            StringBuilder builder = new StringBuilder("[");
            int i = 0;
            while (i < length) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(ObjectUtil.memberToString(Array.get(o, i)));
                ++i;
            }
            return builder.append(']').toString();
        }
        return o.toString();
    }

    private static List<Field> getAllDeclaredFields(Class<?> clazz) {
        ArrayList<Field> fields = new ArrayList<Field>();
        while (clazz != null) {
            Field[] fieldArray = clazz.getDeclaredFields();
            int n = fieldArray.length;
            int n2 = 0;
            while (n2 < n) {
                Field field = fieldArray[n2];
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
                ++n2;
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}

