// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class ReflectorRaw
{
    public static Field getField(final Class cls, final Class fieldType) {
        try {
            final Field[] afield = cls.getDeclaredFields();
            for (int i = 0; i < afield.length; ++i) {
                final Field field = afield[i];
                if (field.getType() == fieldType) {
                    field.setAccessible(true);
                    return field;
                }
            }
            return null;
        }
        catch (Exception var5) {
            return null;
        }
    }
    
    public static Field[] getFields(final Class cls, final Class fieldType) {
        try {
            final Field[] afield = cls.getDeclaredFields();
            return getFields(afield, fieldType);
        }
        catch (Exception var3) {
            return null;
        }
    }
    
    public static Field[] getFields(final Field[] fields, final Class fieldType) {
        try {
            final List list = new ArrayList();
            for (int i = 0; i < fields.length; ++i) {
                final Field field = fields[i];
                if (field.getType() == fieldType) {
                    field.setAccessible(true);
                    list.add(field);
                }
            }
            final Field[] afield = list.toArray(new Field[list.size()]);
            return afield;
        }
        catch (Exception var5) {
            return null;
        }
    }
    
    public static Field[] getFieldsAfter(final Class cls, final Field field, final Class fieldType) {
        try {
            final Field[] afield = cls.getDeclaredFields();
            final List<Field> list = Arrays.asList(afield);
            final int i = list.indexOf(field);
            if (i < 0) {
                return new Field[0];
            }
            final List<Field> list2 = list.subList(i + 1, list.size());
            final Field[] afield2 = list2.toArray(new Field[list2.size()]);
            return getFields(afield2, fieldType);
        }
        catch (Exception var8) {
            return null;
        }
    }
    
    public static Field[] getFields(final Object obj, final Field[] fields, final Class fieldType, final Object value) {
        try {
            final List<Field> list = new ArrayList<Field>();
            for (int i = 0; i < fields.length; ++i) {
                final Field field = fields[i];
                if (field.getType() == fieldType) {
                    final boolean flag = Modifier.isStatic(field.getModifiers());
                    if ((obj != null || flag) && (obj == null || !flag)) {
                        field.setAccessible(true);
                        final Object object = field.get(obj);
                        if (object == value) {
                            list.add(field);
                        }
                        else if (object != null && value != null && object.equals(value)) {
                            list.add(field);
                        }
                    }
                }
            }
            final Field[] afield = list.toArray(new Field[list.size()]);
            return afield;
        }
        catch (Exception var9) {
            return null;
        }
    }
    
    public static Field getField(final Class cls, final Class fieldType, final int index) {
        final Field[] afield = getFields(cls, fieldType);
        return (index >= 0 && index < afield.length) ? afield[index] : null;
    }
    
    public static Field getFieldAfter(final Class cls, final Field field, final Class fieldType, final int index) {
        final Field[] afield = getFieldsAfter(cls, field, fieldType);
        return (index >= 0 && index < afield.length) ? afield[index] : null;
    }
    
    public static Object getFieldValue(final Object obj, final Class cls, final Class fieldType) {
        final ReflectorField reflectorfield = getReflectorField(cls, fieldType);
        return (reflectorfield == null) ? null : (reflectorfield.exists() ? Reflector.getFieldValue(obj, reflectorfield) : null);
    }
    
    public static Object getFieldValue(final Object obj, final Class cls, final Class fieldType, final int index) {
        final ReflectorField reflectorfield = getReflectorField(cls, fieldType, index);
        return (reflectorfield == null) ? null : (reflectorfield.exists() ? Reflector.getFieldValue(obj, reflectorfield) : null);
    }
    
    public static boolean setFieldValue(final Object obj, final Class cls, final Class fieldType, final Object value) {
        final ReflectorField reflectorfield = getReflectorField(cls, fieldType);
        return reflectorfield != null && reflectorfield.exists() && Reflector.setFieldValue(obj, reflectorfield, value);
    }
    
    public static boolean setFieldValue(final Object obj, final Class cls, final Class fieldType, final int index, final Object value) {
        final ReflectorField reflectorfield = getReflectorField(cls, fieldType, index);
        return reflectorfield != null && reflectorfield.exists() && Reflector.setFieldValue(obj, reflectorfield, value);
    }
    
    public static ReflectorField getReflectorField(final Class cls, final Class fieldType) {
        final Field field = getField(cls, fieldType);
        if (field == null) {
            return null;
        }
        final ReflectorClass reflectorclass = new ReflectorClass(cls);
        return new ReflectorField(reflectorclass, field.getName());
    }
    
    public static ReflectorField getReflectorField(final Class cls, final Class fieldType, final int index) {
        final Field field = getField(cls, fieldType, index);
        if (field == null) {
            return null;
        }
        final ReflectorClass reflectorclass = new ReflectorClass(cls);
        return new ReflectorField(reflectorclass, field.getName());
    }
}
