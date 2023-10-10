// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.util.proxy;

import java.lang.reflect.AccessibleObject;
import java.lang.invoke.MethodHandle;
import java.util.List;
import java.lang.reflect.Method;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import java.lang.invoke.MethodHandles;
import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import java.security.ProtectionDomain;

public class DefineClassHelper
{
    private static final Helper privileged;
    
    public static Class<?> toClass(final String className, final Class<?> neighbor, final ClassLoader loader, final ProtectionDomain domain, final byte[] bcode) throws CannotCompileException {
        try {
            return DefineClassHelper.privileged.defineClass(className, bcode, 0, bcode.length, neighbor, loader, domain);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (CannotCompileException e2) {
            throw e2;
        }
        catch (ClassFormatError e3) {
            final Throwable t = e3.getCause();
            throw new CannotCompileException((t == null) ? e3 : t);
        }
        catch (Exception e4) {
            throw new CannotCompileException(e4);
        }
    }
    
    public static Class<?> toClass(final Class<?> neighbor, final byte[] bcode) throws CannotCompileException {
        try {
            DefineClassHelper.class.getModule().addReads(neighbor.getModule());
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final MethodHandles.Lookup prvlookup = MethodHandles.privateLookupIn(neighbor, lookup);
            return prvlookup.defineClass(bcode);
        }
        catch (IllegalAccessException | IllegalArgumentException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new CannotCompileException(e.getMessage() + ": " + neighbor.getName() + " has no permission to define the class");
        }
    }
    
    public static Class<?> toClass(final MethodHandles.Lookup lookup, final byte[] bcode) throws CannotCompileException {
        try {
            return lookup.defineClass(bcode);
        }
        catch (IllegalAccessException | IllegalArgumentException ex2) {
            final Exception ex;
            final Exception e = ex;
            throw new CannotCompileException(e.getMessage());
        }
    }
    
    static Class<?> toPublicClass(final String className, final byte[] bcode) throws CannotCompileException {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            lookup = lookup.dropLookupMode(2);
            return lookup.defineClass(bcode);
        }
        catch (Throwable t) {
            throw new CannotCompileException(t);
        }
    }
    
    private DefineClassHelper() {
    }
    
    static {
        privileged = ((ClassFile.MAJOR_VERSION > 54) ? new Java11() : ((ClassFile.MAJOR_VERSION >= 53) ? new Java9() : ((ClassFile.MAJOR_VERSION >= 51) ? new Java7() : new JavaOther())));
    }
    
    private abstract static class Helper
    {
        abstract Class<?> defineClass(final String p0, final byte[] p1, final int p2, final int p3, final Class<?> p4, final ClassLoader p5, final ProtectionDomain p6) throws ClassFormatError, CannotCompileException;
    }
    
    private static class Java11 extends JavaOther
    {
        @Override
        Class<?> defineClass(final String name, final byte[] bcode, final int off, final int len, final Class<?> neighbor, final ClassLoader loader, final ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
            if (neighbor != null) {
                return DefineClassHelper.toClass(neighbor, bcode);
            }
            return super.defineClass(name, bcode, off, len, neighbor, loader, protectionDomain);
        }
    }
    
    private static class Java9 extends Helper
    {
        private final Object stack;
        private final Method getCallerClass;
        private final ReferencedUnsafe sunMiscUnsafe;
        
        Java9() {
            this.sunMiscUnsafe = this.getReferencedUnsafe();
            Class<?> stackWalkerClass = null;
            try {
                stackWalkerClass = Class.forName("java.lang.StackWalker");
            }
            catch (ClassNotFoundException ex) {}
            if (stackWalkerClass != null) {
                try {
                    final Class<?> optionClass = Class.forName("java.lang.StackWalker$Option");
                    this.stack = stackWalkerClass.getMethod("getInstance", optionClass).invoke(null, optionClass.getEnumConstants()[0]);
                    this.getCallerClass = stackWalkerClass.getMethod("getCallerClass", (Class<?>[])new Class[0]);
                    return;
                }
                catch (Throwable e) {
                    throw new RuntimeException("cannot initialize", e);
                }
            }
            this.stack = null;
            this.getCallerClass = null;
        }
        
        private final ReferencedUnsafe getReferencedUnsafe() {
            try {
                if (DefineClassHelper.privileged != null && this.getCallerClass.invoke(this.stack, new Object[0]) != this.getClass()) {
                    throw new IllegalAccessError("Access denied for caller.");
                }
            }
            catch (Exception e) {
                throw new RuntimeException("cannot initialize", e);
            }
            try {
                final SecurityActions.TheUnsafe usf = SecurityActions.getSunMiscUnsafeAnonymously();
                final List<Method> defineClassMethod = usf.methods.get("defineClass");
                if (null == defineClassMethod) {
                    return null;
                }
                final MethodHandle meth = MethodHandles.lookup().unreflect(defineClassMethod.get(0));
                return new ReferencedUnsafe(usf, meth);
            }
            catch (Throwable e2) {
                throw new RuntimeException("cannot initialize", e2);
            }
        }
        
        @Override
        Class<?> defineClass(final String name, final byte[] b, final int off, final int len, final Class<?> neighbor, final ClassLoader loader, final ProtectionDomain protectionDomain) throws ClassFormatError {
            try {
                if (this.getCallerClass.invoke(this.stack, new Object[0]) != DefineClassHelper.class) {
                    throw new IllegalAccessError("Access denied for caller.");
                }
            }
            catch (Exception e) {
                throw new RuntimeException("cannot initialize", e);
            }
            return this.sunMiscUnsafe.defineClass(name, b, off, len, loader, protectionDomain);
        }
        
        final class ReferencedUnsafe
        {
            private final SecurityActions.TheUnsafe sunMiscUnsafeTheUnsafe;
            private final MethodHandle defineClass;
            
            ReferencedUnsafe(final SecurityActions.TheUnsafe usf, final MethodHandle meth) {
                this.sunMiscUnsafeTheUnsafe = usf;
                this.defineClass = meth;
            }
            
            Class<?> defineClass(final String name, final byte[] b, final int off, final int len, final ClassLoader loader, final ProtectionDomain protectionDomain) throws ClassFormatError {
                try {
                    if (Java9.this.getCallerClass.invoke(Java9.this.stack, new Object[0]) != Java9.class) {
                        throw new IllegalAccessError("Access denied for caller.");
                    }
                }
                catch (Exception e) {
                    throw new RuntimeException("cannot initialize", e);
                }
                try {
                    return (Class<?>)this.defineClass.invokeWithArguments(this.sunMiscUnsafeTheUnsafe.theUnsafe, name, b, off, len, loader, protectionDomain);
                }
                catch (Throwable e2) {
                    if (e2 instanceof RuntimeException) {
                        throw (RuntimeException)e2;
                    }
                    if (e2 instanceof ClassFormatError) {
                        throw (ClassFormatError)e2;
                    }
                    throw new ClassFormatError(e2.getMessage());
                }
            }
        }
    }
    
    private static class Java7 extends Helper
    {
        private final SecurityActions stack;
        private final MethodHandle defineClass;
        
        private Java7() {
            this.stack = SecurityActions.stack;
            this.defineClass = this.getDefineClassMethodHandle();
        }
        
        private final MethodHandle getDefineClassMethodHandle() {
            if (DefineClassHelper.privileged != null && this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getMethodHandle(ClassLoader.class, "defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class });
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException("cannot initialize", e);
            }
        }
        
        @Override
        Class<?> defineClass(final String name, final byte[] b, final int off, final int len, final Class<?> neighbor, final ClassLoader loader, final ProtectionDomain protectionDomain) throws ClassFormatError {
            if (this.stack.getCallerClass() != DefineClassHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return (Class<?>)this.defineClass.invokeWithArguments(loader, name, b, off, len, protectionDomain);
            }
            catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                }
                if (e instanceof ClassFormatError) {
                    throw (ClassFormatError)e;
                }
                throw new ClassFormatError(e.getMessage());
            }
        }
    }
    
    private static class JavaOther extends Helper
    {
        private final Method defineClass;
        private final SecurityActions stack;
        
        private JavaOther() {
            this.defineClass = this.getDefineClassMethod();
            this.stack = SecurityActions.stack;
        }
        
        private final Method getDefineClassMethod() {
            if (DefineClassHelper.privileged != null && this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class });
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException("cannot initialize", e);
            }
        }
        
        @Override
        Class<?> defineClass(final String name, final byte[] b, final int off, final int len, final Class<?> neighbor, final ClassLoader loader, final ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
            final Class<?> klass = this.stack.getCallerClass();
            if (klass != DefineClassHelper.class && klass != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                SecurityActions.setAccessible(this.defineClass, true);
                return (Class<?>)this.defineClass.invoke(loader, name, b, off, len, protectionDomain);
            }
            catch (Throwable e) {
                if (e instanceof ClassFormatError) {
                    throw (ClassFormatError)e;
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                }
                throw new CannotCompileException(e);
            }
        }
    }
}
