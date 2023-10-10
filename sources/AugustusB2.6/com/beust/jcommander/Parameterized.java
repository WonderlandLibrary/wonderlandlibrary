// 
// Decompiled by Procyon v0.5.36
// 

package com.beust.jcommander;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import com.beust.jcommander.internal.Lists;
import java.util.List;
import com.beust.jcommander.internal.Sets;
import java.util.Collections;
import java.util.Set;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class Parameterized
{
    private Field field;
    private Method method;
    private Method getter;
    private WrappedParameter wrappedParameter;
    private ParametersDelegate parametersDelegate;
    
    public Parameterized(final WrappedParameter wrappedParameter, final ParametersDelegate parametersDelegate, final Field field, final Method method) {
        this.wrappedParameter = wrappedParameter;
        this.method = method;
        this.field = field;
        if (this.field != null) {
            setFieldAccessible(this.field);
        }
        this.parametersDelegate = parametersDelegate;
    }
    
    private static void describeClassTree(final Class<?> obj, final Set<Class<?>> set) {
        if (obj == null) {
            return;
        }
        if (Object.class.equals(obj) || set.contains(obj)) {
            return;
        }
        set.add(obj);
        describeClassTree(obj.getSuperclass(), set);
        Class<?>[] interfaces;
        for (int length = (interfaces = obj.getInterfaces()).length, i = 0; i < length; ++i) {
            describeClassTree(interfaces[i], set);
        }
    }
    
    private static Set<Class<?>> describeClassTree(final Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptySet();
        }
        final Set<Class<?>> linkedHashSet = Sets.newLinkedHashSet();
        describeClassTree(clazz, linkedHashSet);
        return linkedHashSet;
    }
    
    public static List<Parameterized> parseArg(final Object o) {
        final List<Parameterized> arrayList = Lists.newArrayList();
        final Iterator<Class<?>> iterator = describeClassTree(o.getClass()).iterator();
        while (iterator.hasNext()) {
            Class clazz;
            Field[] declaredFields;
            for (int length = (declaredFields = (clazz = iterator.next()).getDeclaredFields()).length, i = 0; i < length; ++i) {
                final Field field;
                final Parameter annotation = (field = declaredFields[i]).getAnnotation(Parameter.class);
                final ParametersDelegate annotation2 = field.getAnnotation(ParametersDelegate.class);
                final DynamicParameter annotation3 = field.getAnnotation(DynamicParameter.class);
                if (annotation != null) {
                    arrayList.add(new Parameterized(new WrappedParameter(annotation), null, field, null));
                }
                else if (annotation3 != null) {
                    arrayList.add(new Parameterized(new WrappedParameter(annotation3), null, field, null));
                }
                else if (annotation2 != null) {
                    arrayList.add(new Parameterized(null, annotation2, field, null));
                }
            }
            Method[] declaredMethods;
            for (int length2 = (declaredMethods = clazz.getDeclaredMethods()).length, j = 0; j < length2; ++j) {
                final Method method;
                (method = declaredMethods[j]).setAccessible(true);
                final Parameter annotation4 = method.getAnnotation(Parameter.class);
                final ParametersDelegate annotation5 = method.getAnnotation(ParametersDelegate.class);
                final DynamicParameter annotation6 = method.getAnnotation(DynamicParameter.class);
                if (annotation4 != null) {
                    arrayList.add(new Parameterized(new WrappedParameter(annotation4), null, null, method));
                }
                else if (annotation6 != null) {
                    arrayList.add(new Parameterized(new WrappedParameter(annotation6), null, null, method));
                }
                else if (annotation5 != null) {
                    arrayList.add(new Parameterized(null, annotation5, null, method));
                }
            }
        }
        return arrayList;
    }
    
    public WrappedParameter getWrappedParameter() {
        return this.wrappedParameter;
    }
    
    public Class<?> getType() {
        if (this.method != null) {
            return this.method.getParameterTypes()[0];
        }
        return this.field.getType();
    }
    
    public String getName() {
        if (this.method != null) {
            return this.method.getName();
        }
        return this.field.getName();
    }
    
    public Object get(final Object obj) {
        try {
            if (this.method != null) {
                if (this.getter == null) {
                    this.getter = this.method.getDeclaringClass().getMethod("g" + this.method.getName().substring(1), (Class<?>[])new Class[0]);
                }
                return this.getter.invoke(obj, new Object[0]);
            }
            try {
                return this.field.get(obj);
            }
            catch (IllegalArgumentException | InvocationTargetException ex) {
                final Object o;
                throw new ParameterException((Throwable)o);
            }
        }
        catch (SecurityException ex2) {}
        catch (IllegalArgumentException ex3) {}
        catch (InvocationTargetException ex4) {}
        catch (IllegalAccessException ex5) {}
        catch (NoSuchMethodException ex6) {
            final String name = this.method.getName();
            final String string = Character.toLowerCase(name.charAt(3)) + name.substring(4);
            Object value = null;
            try {
                final Field declaredField;
                if ((declaredField = this.method.getDeclaringClass().getDeclaredField(string)) != null) {
                    setFieldAccessible(declaredField);
                    value = declaredField.get(obj);
                }
            }
            catch (NoSuchFieldException | IllegalAccessException ex7) {}
            return value;
        }
    }
    
    @Override
    public int hashCode() {
        return (31 + ((this.field == null) ? 0 : this.field.hashCode())) * 31 + ((this.method == null) ? 0 : this.method.hashCode());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final Parameterized parameterized = (Parameterized)o;
        if (this.field == null) {
            if (parameterized.field != null) {
                return false;
            }
        }
        else if (!this.field.equals(parameterized.field)) {
            return false;
        }
        if (this.method == null) {
            if (parameterized.method != null) {
                return false;
            }
        }
        else if (!this.method.equals(parameterized.method)) {
            return false;
        }
        return true;
    }
    
    public boolean isDynamicParameter(final Field field) {
        if (this.method != null) {
            return this.method.getAnnotation(DynamicParameter.class) != null;
        }
        return this.field.getAnnotation(DynamicParameter.class) != null;
    }
    
    private static void setFieldAccessible(final Field field) {
        if (Modifier.isFinal(field.getModifiers())) {
            throw new ParameterException("Cannot use final field " + field.getDeclaringClass().getName() + "#" + field.getName() + " as a parameter; compile-time constant inlining may hide new values written to it.");
        }
        field.setAccessible(true);
    }
    
    private static String errorMessage(final Method obj, final Exception ex) {
        return "Could not invoke " + obj + "\n    Reason: " + ex.getMessage();
    }
    
    public void set(final Object o, final Object value) {
        try {
            if (this.method == null) {
                this.field.set(o, value);
                return;
            }
            this.method.invoke(o, value);
        }
        catch (IllegalAccessException | IllegalArgumentException ex4) {
            final Exception ex;
            throw new ParameterException(errorMessage(this.method, ex));
        }
        catch (InvocationTargetException ex3) {
            final InvocationTargetException ex2 = ex3;
            if (ex3.getTargetException() instanceof ParameterException) {
                throw (ParameterException)ex2.getTargetException();
            }
            throw new ParameterException(errorMessage(this.method, ex2));
        }
    }
    
    public ParametersDelegate getDelegateAnnotation() {
        return this.parametersDelegate;
    }
    
    public Type getGenericType() {
        if (this.method != null) {
            return this.method.getGenericParameterTypes()[0];
        }
        return this.field.getGenericType();
    }
    
    public Parameter getParameter() {
        return this.wrappedParameter.getParameter();
    }
    
    public Type findFieldGenericType() {
        if (this.method != null) {
            return null;
        }
        final Type type;
        if (this.field.getGenericType() instanceof ParameterizedType && (type = ((ParameterizedType)this.field.getGenericType()).getActualTypeArguments()[0]) instanceof Class) {
            return type;
        }
        return null;
    }
    
    public boolean isDynamicParameter() {
        return this.wrappedParameter.getDynamicParameter() != null;
    }
}
