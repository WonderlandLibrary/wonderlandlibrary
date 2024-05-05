/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Lookup {
    private final MethodHandles.Lookup lookup;
    public static final Lookup PUBLIC = new Lookup(MethodHandles.publicLookup());

    public Lookup(MethodHandles.Lookup lookup) {
        this.lookup = lookup;
    }

    public MethodHandle unreflect(Method m) {
        return Lookup.unreflect(this.lookup, m);
    }

    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m) {
        try {
            return lookup.unreflect(m);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to unreflect method " + m);
            ee.initCause(e);
            throw ee;
        }
    }

    public MethodHandle unreflectGetter(Field f) {
        try {
            return this.lookup.unreflectGetter(f);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to unreflect getter for field " + f);
            ee.initCause(e);
            throw ee;
        }
    }

    public MethodHandle findGetter(Class<?> refc, String name, Class<?> type) {
        try {
            return this.lookup.findGetter(refc, name, type);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to access getter for field " + refc.getName() + "." + name + " of type " + type.getName());
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchFieldException e) {
            NoSuchFieldError ee = new NoSuchFieldError("Failed to find getter for field " + refc.getName() + "." + name + " of type " + type.getName());
            ee.initCause(e);
            throw ee;
        }
    }

    public MethodHandle unreflectSetter(Field f) {
        try {
            return this.lookup.unreflectSetter(f);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to unreflect setter for field " + f);
            ee.initCause(e);
            throw ee;
        }
    }

    public MethodHandle unreflectConstructor(Constructor<?> c) {
        return Lookup.unreflectConstructor(this.lookup, c);
    }

    public static MethodHandle unreflectConstructor(MethodHandles.Lookup lookup, Constructor<?> c) {
        try {
            return lookup.unreflectConstructor(c);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to unreflect constructor " + c);
            ee.initCause(e);
            throw ee;
        }
    }

    public MethodHandle findSpecial(Class<?> declaringClass, String name, MethodType type) {
        try {
            return this.lookup.findSpecial(declaringClass, name, type, declaringClass);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to access special method " + Lookup.methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchMethodException e) {
            NoSuchMethodError ee = new NoSuchMethodError("Failed to find special method " + Lookup.methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
    }

    private static String methodDescription(Class<?> declaringClass, String name, MethodType type) {
        return declaringClass.getName() + "#" + name + type;
    }

    public MethodHandle findStatic(Class<?> declaringClass, String name, MethodType type) {
        try {
            return this.lookup.findStatic(declaringClass, name, type);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to access static method " + Lookup.methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchMethodException e) {
            NoSuchMethodError ee = new NoSuchMethodError("Failed to find static method " + Lookup.methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
    }

    public MethodHandle findVirtual(Class<?> declaringClass, String name, MethodType type) {
        try {
            return this.lookup.findVirtual(declaringClass, name, type);
        }
        catch (IllegalAccessException e) {
            IllegalAccessError ee = new IllegalAccessError("Failed to access virtual method " + Lookup.methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
        catch (NoSuchMethodException e) {
            NoSuchMethodError ee = new NoSuchMethodError("Failed to find virtual method " + Lookup.methodDescription(declaringClass, name, type));
            ee.initCause(e);
            throw ee;
        }
    }

    public static MethodHandle findOwnSpecial(MethodHandles.Lookup lookup, String name, Class<?> rtype, Class<?> ... ptypes) {
        return new Lookup(lookup).findOwnSpecial(name, rtype, ptypes);
    }

    public MethodHandle findOwnSpecial(String name, Class<?> rtype, Class<?> ... ptypes) {
        return this.findSpecial(this.lookup.lookupClass(), name, MethodType.methodType(rtype, ptypes));
    }

    public static MethodHandle findOwnStatic(MethodHandles.Lookup lookup, String name, Class<?> rtype, Class<?> ... ptypes) {
        return new Lookup(lookup).findOwnStatic(name, rtype, ptypes);
    }

    public MethodHandle findOwnStatic(String name, Class<?> rtype, Class<?> ... ptypes) {
        return this.findStatic(this.lookup.lookupClass(), name, MethodType.methodType(rtype, ptypes));
    }
}

