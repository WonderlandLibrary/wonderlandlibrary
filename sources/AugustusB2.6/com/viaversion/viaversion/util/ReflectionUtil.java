// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.util;

import java.util.Collections;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtil
{
    public static Object invokeStatic(final Class<?> clazz, final String method) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method m = clazz.getDeclaredMethod(method, (Class<?>[])new Class[0]);
        return m.invoke(null, new Object[0]);
    }
    
    public static Object invoke(final Object o, final String method) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method m = o.getClass().getDeclaredMethod(method, (Class<?>[])new Class[0]);
        return m.invoke(o, new Object[0]);
    }
    
    public static <T> T getStatic(final Class<?> clazz, final String f, final Class<T> type) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return type.cast(field.get(null));
    }
    
    public static void setStatic(final Class<?> clazz, final String f, final Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        field.set(null, value);
    }
    
    public static <T> T getSuper(final Object o, final String f, final Class<T> type) throws NoSuchFieldException, IllegalAccessException {
        final Field field = o.getClass().getSuperclass().getDeclaredField(f);
        field.setAccessible(true);
        return type.cast(field.get(o));
    }
    
    public static <T> T get(final Object instance, final Class<?> clazz, final String f, final Class<T> type) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return type.cast(field.get(instance));
    }
    
    public static <T> T get(final Object o, final String f, final Class<T> type) throws NoSuchFieldException, IllegalAccessException {
        final Field field = o.getClass().getDeclaredField(f);
        field.setAccessible(true);
        return type.cast(field.get(o));
    }
    
    public static <T> T getPublic(final Object o, final String f, final Class<T> type) throws NoSuchFieldException, IllegalAccessException {
        final Field field = o.getClass().getField(f);
        field.setAccessible(true);
        return type.cast(field.get(o));
    }
    
    public static void set(final Object o, final String f, final Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = o.getClass().getDeclaredField(f);
        field.setAccessible(true);
        field.set(o, value);
    }
    
    public static final class ClassReflection
    {
        private final Class<?> handle;
        private final Map<String, Field> fields;
        private final Map<String, Method> methods;
        
        public ClassReflection(final Class<?> handle) {
            this(handle, true);
        }
        
        public ClassReflection(final Class<?> handle, final boolean recursive) {
            this.fields = new ConcurrentHashMap<String, Field>();
            this.methods = new ConcurrentHashMap<String, Method>();
            this.scanFields(this.handle = handle, recursive);
            this.scanMethods(handle, recursive);
        }
        
        private void scanFields(final Class<?> host, final boolean recursive) {
            if (recursive && host.getSuperclass() != null && host.getSuperclass() != Object.class) {
                this.scanFields(host.getSuperclass(), true);
            }
            for (final Field field : host.getDeclaredFields()) {
                field.setAccessible(true);
                this.fields.put(field.getName(), field);
            }
        }
        
        private void scanMethods(final Class<?> host, final boolean recursive) {
            if (recursive && host.getSuperclass() != null && host.getSuperclass() != Object.class) {
                this.scanMethods(host.getSuperclass(), true);
            }
            for (final Method method : host.getDeclaredMethods()) {
                method.setAccessible(true);
                this.methods.put(method.getName(), method);
            }
        }
        
        public Object newInstance() throws ReflectiveOperationException {
            return this.handle.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        
        public Field getField(final String name) {
            return this.fields.get(name);
        }
        
        public void setFieldValue(final String fieldName, final Object instance, final Object value) throws IllegalAccessException {
            this.getField(fieldName).set(instance, value);
        }
        
        public <T> T getFieldValue(final String fieldName, final Object instance, final Class<T> type) throws IllegalAccessException {
            return type.cast(this.getField(fieldName).get(instance));
        }
        
        public <T> T invokeMethod(final Class<T> type, final String methodName, final Object instance, final Object... args) throws InvocationTargetException, IllegalAccessException {
            return type.cast(this.getMethod(methodName).invoke(instance, args));
        }
        
        public Method getMethod(final String name) {
            return this.methods.get(name);
        }
        
        public Collection<Field> getFields() {
            return Collections.unmodifiableCollection((Collection<? extends Field>)this.fields.values());
        }
        
        public Collection<Method> getMethods() {
            return Collections.unmodifiableCollection((Collection<? extends Method>)this.methods.values());
        }
    }
}
