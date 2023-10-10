// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.util.proxy;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import java.util.Objects;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import java.security.PrivilegedActionException;
import java.lang.invoke.MethodHandles;
import java.security.PrivilegedExceptionAction;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.lang.reflect.Method;

class SecurityActions extends SecurityManager
{
    public static final SecurityActions stack;
    
    public Class<?> getCallerClass() {
        return this.getClassContext()[2];
    }
    
    static Method[] getDeclaredMethods(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredMethods();
        }
        return AccessController.doPrivileged((PrivilegedAction<Method[]>)new PrivilegedAction<Method[]>() {
            @Override
            public Method[] run() {
                return clazz.getDeclaredMethods();
            }
        });
    }
    
    static Constructor<?>[] getDeclaredConstructors(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredConstructors();
        }
        return (Constructor<?>[])AccessController.doPrivileged((PrivilegedAction<Constructor[]>)new PrivilegedAction<Constructor<?>[]>() {
            @Override
            public Constructor<?>[] run() {
                return (Constructor<?>[])clazz.getDeclaredConstructors();
            }
        });
    }
    
    static MethodHandle getMethodHandle(final Class<?> clazz, final String name, final Class<?>[] params) throws NoSuchMethodException {
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<MethodHandle>)new PrivilegedExceptionAction<MethodHandle>() {
                @Override
                public MethodHandle run() throws IllegalAccessException, NoSuchMethodException, SecurityException {
                    final Method rmet = clazz.getDeclaredMethod(name, (Class[])params);
                    rmet.setAccessible(true);
                    final MethodHandle meth = MethodHandles.lookup().unreflect(rmet);
                    rmet.setAccessible(false);
                    return meth;
                }
            });
        }
        catch (PrivilegedActionException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                throw (NoSuchMethodException)e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }
    
    static Method getDeclaredMethod(final Class<?> clazz, final String name, final Class<?>[] types) throws NoSuchMethodException {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredMethod(name, types);
        }
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<Method>)new PrivilegedExceptionAction<Method>() {
                @Override
                public Method run() throws Exception {
                    return clazz.getDeclaredMethod(name, (Class[])types);
                }
            });
        }
        catch (PrivilegedActionException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                throw (NoSuchMethodException)e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }
    
    static Constructor<?> getDeclaredConstructor(final Class<?> clazz, final Class<?>[] types) throws NoSuchMethodException {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredConstructor(types);
        }
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<Constructor<?>>)new PrivilegedExceptionAction<Constructor<?>>() {
                @Override
                public Constructor<?> run() throws Exception {
                    return clazz.getDeclaredConstructor((Class<?>[])types);
                }
            });
        }
        catch (PrivilegedActionException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                throw (NoSuchMethodException)e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }
    
    static void setAccessible(final AccessibleObject ao, final boolean accessible) {
        if (System.getSecurityManager() == null) {
            ao.setAccessible(accessible);
        }
        else {
            AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    ao.setAccessible(accessible);
                    return null;
                }
            });
        }
    }
    
    static void set(final Field fld, final Object target, final Object value) throws IllegalAccessException {
        if (System.getSecurityManager() == null) {
            fld.set(target, value);
        }
        else {
            try {
                AccessController.doPrivileged((PrivilegedExceptionAction<Object>)new PrivilegedExceptionAction<Void>() {
                    @Override
                    public Void run() throws Exception {
                        fld.set(target, value);
                        return null;
                    }
                });
            }
            catch (PrivilegedActionException e) {
                if (e.getCause() instanceof NoSuchMethodException) {
                    throw (IllegalAccessException)e.getCause();
                }
                throw new RuntimeException(e.getCause());
            }
        }
    }
    
    static TheUnsafe getSunMiscUnsafeAnonymously() throws ClassNotFoundException {
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<TheUnsafe>)new PrivilegedExceptionAction<TheUnsafe>() {
                @Override
                public TheUnsafe run() throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
                    final Class<?> unsafe = Class.forName("sun.misc.Unsafe");
                    final Field theUnsafe = unsafe.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    final SecurityActions stack = SecurityActions.stack;
                    Objects.requireNonNull(stack);
                    final TheUnsafe usf = new TheUnsafe(unsafe, theUnsafe.get(null));
                    theUnsafe.setAccessible(false);
                    SecurityActions.disableWarning(usf);
                    return usf;
                }
            });
        }
        catch (PrivilegedActionException e) {
            if (e.getCause() instanceof ClassNotFoundException) {
                throw (ClassNotFoundException)e.getCause();
            }
            if (e.getCause() instanceof NoSuchFieldException) {
                throw new ClassNotFoundException("No such instance.", e.getCause());
            }
            if (e.getCause() instanceof IllegalAccessException || e.getCause() instanceof IllegalAccessException || e.getCause() instanceof SecurityException) {
                throw new ClassNotFoundException("Security denied access.", e.getCause());
            }
            throw new RuntimeException(e.getCause());
        }
    }
    
    static void disableWarning(final TheUnsafe tu) {
        try {
            if (ClassFile.MAJOR_VERSION < 53) {
                return;
            }
            final Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            final Field logger = cls.getDeclaredField("logger");
            tu.call("putObjectVolatile", cls, tu.call("staticFieldOffset", logger), null);
        }
        catch (Exception ex) {}
    }
    
    static {
        stack = new SecurityActions();
    }
    
    class TheUnsafe
    {
        final Class<?> unsafe;
        final Object theUnsafe;
        final Map<String, List<Method>> methods;
        
        TheUnsafe(final Class<?> c, final Object o) {
            this.methods = new HashMap<String, List<Method>>();
            this.unsafe = c;
            this.theUnsafe = o;
            for (final Method m : this.unsafe.getDeclaredMethods()) {
                if (!this.methods.containsKey(m.getName())) {
                    this.methods.put(m.getName(), Collections.singletonList(m));
                }
                else {
                    if (this.methods.get(m.getName()).size() == 1) {
                        this.methods.put(m.getName(), new ArrayList<Method>(this.methods.get(m.getName())));
                    }
                    this.methods.get(m.getName()).add(m);
                }
            }
        }
        
        private Method getM(final String name, final Object[] o) {
            return this.methods.get(name).get(0);
        }
        
        public Object call(final String name, final Object... args) {
            try {
                return this.getM(name, args).invoke(this.theUnsafe, args);
            }
            catch (Throwable t) {
                t.printStackTrace();
                return null;
            }
        }
    }
}
