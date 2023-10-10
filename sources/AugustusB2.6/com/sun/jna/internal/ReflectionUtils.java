// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.logging.Level;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ReflectionUtils
{
    private static final Logger LOG;
    private static final Method METHOD_IS_DEFAULT;
    private static final Method METHOD_HANDLES_LOOKUP;
    private static final Method METHOD_HANDLES_LOOKUP_IN;
    private static final Method METHOD_HANDLES_PRIVATE_LOOKUP_IN;
    private static final Method METHOD_HANDLES_LOOKUP_UNREFLECT_SPECIAL;
    private static final Method METHOD_HANDLES_LOOKUP_FIND_SPECIAL;
    private static final Method METHOD_HANDLES_BIND_TO;
    private static final Method METHOD_HANDLES_INVOKE_WITH_ARGUMENTS;
    private static final Method METHOD_TYPE;
    private static Constructor CONSTRUCTOR_LOOKUP_CLASS;
    
    private static Constructor getConstructorLookupClass() {
        if (ReflectionUtils.CONSTRUCTOR_LOOKUP_CLASS == null) {
            final Class lookup = lookupClass("java.lang.invoke.MethodHandles$Lookup");
            ReflectionUtils.CONSTRUCTOR_LOOKUP_CLASS = lookupDeclaredConstructor(lookup, Class.class);
        }
        return ReflectionUtils.CONSTRUCTOR_LOOKUP_CLASS;
    }
    
    private static Constructor lookupDeclaredConstructor(final Class clazz, final Class... arguments) {
        if (clazz == null) {
            ReflectionUtils.LOG.log(Level.FINE, "Failed to lookup method: <init>#{1}({2})", new Object[] { clazz, Arrays.toString(arguments) });
            return null;
        }
        try {
            final Constructor init = clazz.getDeclaredConstructor((Class[])arguments);
            init.setAccessible(true);
            return init;
        }
        catch (Exception ex) {
            ReflectionUtils.LOG.log(Level.FINE, "Failed to lookup method: <init>#{1}({2})", new Object[] { clazz, Arrays.toString(arguments) });
            return null;
        }
    }
    
    private static Method lookupMethod(final Class clazz, final String methodName, final Class... arguments) {
        if (clazz == null) {
            ReflectionUtils.LOG.log(Level.FINE, "Failed to lookup method: {0}#{1}({2})", new Object[] { clazz, methodName, Arrays.toString(arguments) });
            return null;
        }
        try {
            return clazz.getMethod(methodName, (Class[])arguments);
        }
        catch (Exception ex) {
            ReflectionUtils.LOG.log(Level.FINE, "Failed to lookup method: {0}#{1}({2})", new Object[] { clazz, methodName, Arrays.toString(arguments) });
            return null;
        }
    }
    
    private static Class lookupClass(final String name) {
        try {
            return Class.forName(name);
        }
        catch (ClassNotFoundException ex) {
            ReflectionUtils.LOG.log(Level.FINE, "Failed to lookup class: " + name, ex);
            return null;
        }
    }
    
    public static boolean isDefault(final Method method) {
        if (ReflectionUtils.METHOD_IS_DEFAULT == null) {
            return false;
        }
        try {
            return (boolean)ReflectionUtils.METHOD_IS_DEFAULT.invoke(method, new Object[0]);
        }
        catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
        catch (IllegalArgumentException ex2) {
            throw new RuntimeException(ex2);
        }
        catch (InvocationTargetException ex3) {
            final Throwable cause = ex3.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            }
            if (cause instanceof Error) {
                throw (Error)cause;
            }
            throw new RuntimeException(cause);
        }
    }
    
    public static Object getMethodHandle(final Method method) throws Exception {
        assert isDefault(method);
        final Object baseLookup = createLookup();
        try {
            final Object lookup = createPrivateLookupIn(method.getDeclaringClass(), baseLookup);
            final Object mh = mhViaFindSpecial(lookup, method);
            return mh;
        }
        catch (Exception ex) {
            final Object lookup2 = getConstructorLookupClass().newInstance(method.getDeclaringClass());
            final Object mh2 = mhViaUnreflectSpecial(lookup2, method);
            return mh2;
        }
    }
    
    private static Object mhViaFindSpecial(final Object lookup, final Method method) throws Exception {
        return ReflectionUtils.METHOD_HANDLES_LOOKUP_FIND_SPECIAL.invoke(lookup, method.getDeclaringClass(), method.getName(), ReflectionUtils.METHOD_TYPE.invoke(null, method.getReturnType(), method.getParameterTypes()), method.getDeclaringClass());
    }
    
    private static Object mhViaUnreflectSpecial(final Object lookup, final Method method) throws Exception {
        final Object l2 = ReflectionUtils.METHOD_HANDLES_LOOKUP_IN.invoke(lookup, method.getDeclaringClass());
        return ReflectionUtils.METHOD_HANDLES_LOOKUP_UNREFLECT_SPECIAL.invoke(l2, method, method.getDeclaringClass());
    }
    
    private static Object createPrivateLookupIn(final Class type, final Object lookup) throws Exception {
        return ReflectionUtils.METHOD_HANDLES_PRIVATE_LOOKUP_IN.invoke(null, type, lookup);
    }
    
    private static Object createLookup() throws Exception {
        return ReflectionUtils.METHOD_HANDLES_LOOKUP.invoke(null, new Object[0]);
    }
    
    public static Object invokeDefaultMethod(final Object target, final Object methodHandle, final Object... args) throws Throwable {
        final Object boundMethodHandle = ReflectionUtils.METHOD_HANDLES_BIND_TO.invoke(methodHandle, target);
        return ReflectionUtils.METHOD_HANDLES_INVOKE_WITH_ARGUMENTS.invoke(boundMethodHandle, args);
    }
    
    static {
        LOG = Logger.getLogger(ReflectionUtils.class.getName());
        final Class methodHandles = lookupClass("java.lang.invoke.MethodHandles");
        final Class methodHandle = lookupClass("java.lang.invoke.MethodHandle");
        final Class lookup = lookupClass("java.lang.invoke.MethodHandles$Lookup");
        final Class methodType = lookupClass("java.lang.invoke.MethodType");
        METHOD_IS_DEFAULT = lookupMethod(Method.class, "isDefault", new Class[0]);
        METHOD_HANDLES_LOOKUP = lookupMethod(methodHandles, "lookup", new Class[0]);
        METHOD_HANDLES_LOOKUP_IN = lookupMethod(lookup, "in", Class.class);
        METHOD_HANDLES_LOOKUP_UNREFLECT_SPECIAL = lookupMethod(lookup, "unreflectSpecial", Method.class, Class.class);
        METHOD_HANDLES_LOOKUP_FIND_SPECIAL = lookupMethod(lookup, "findSpecial", Class.class, String.class, methodType, Class.class);
        METHOD_HANDLES_BIND_TO = lookupMethod(methodHandle, "bindTo", Object.class);
        METHOD_HANDLES_INVOKE_WITH_ARGUMENTS = lookupMethod(methodHandle, "invokeWithArguments", Object[].class);
        METHOD_HANDLES_PRIVATE_LOOKUP_IN = lookupMethod(methodHandles, "privateLookupIn", Class.class, lookup);
        METHOD_TYPE = lookupMethod(methodType, "methodType", Class.class, Class[].class);
    }
}
