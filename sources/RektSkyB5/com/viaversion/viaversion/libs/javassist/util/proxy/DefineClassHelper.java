/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.util.proxy;

import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import com.viaversion.viaversion.libs.javassist.util.proxy.SecurityActions;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;

public class DefineClassHelper {
    private static final Helper privileged = ClassFile.MAJOR_VERSION > 54 ? new Java11() : (ClassFile.MAJOR_VERSION >= 53 ? new Java9() : (ClassFile.MAJOR_VERSION >= 51 ? new Java7() : new JavaOther()));

    public static Class<?> toClass(String className, Class<?> neighbor, ClassLoader loader, ProtectionDomain domain, byte[] bcode) throws CannotCompileException {
        try {
            return privileged.defineClass(className, bcode, 0, bcode.length, neighbor, loader, domain);
        }
        catch (RuntimeException e2) {
            throw e2;
        }
        catch (CannotCompileException e3) {
            throw e3;
        }
        catch (ClassFormatError e4) {
            Throwable t2 = e4.getCause();
            throw new CannotCompileException(t2 == null ? e4 : t2);
        }
        catch (Exception e5) {
            throw new CannotCompileException(e5);
        }
    }

    public static Class<?> toClass(Class<?> neighbor, byte[] bcode) throws CannotCompileException {
        try {
            DefineClassHelper.class.getModule().addReads(neighbor.getModule());
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandles.Lookup prvlookup = MethodHandles.privateLookupIn(neighbor, (MethodHandles.Lookup)lookup);
            return prvlookup.defineClass(bcode);
        }
        catch (IllegalAccessException | IllegalArgumentException e2) {
            throw new CannotCompileException(e2.getMessage() + ": " + neighbor.getName() + " has no permission to define the class");
        }
    }

    public static Class<?> toClass(MethodHandles.Lookup lookup, byte[] bcode) throws CannotCompileException {
        try {
            return lookup.defineClass(bcode);
        }
        catch (IllegalAccessException | IllegalArgumentException e2) {
            throw new CannotCompileException(e2.getMessage());
        }
    }

    static Class<?> toPublicClass(String className, byte[] bcode) throws CannotCompileException {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            lookup = lookup.dropLookupMode(2);
            return lookup.defineClass(bcode);
        }
        catch (Throwable t2) {
            throw new CannotCompileException(t2);
        }
    }

    private DefineClassHelper() {
    }

    private static class JavaOther
    extends Helper {
        private final Method defineClass = this.getDefineClassMethod();
        private final SecurityActions stack = SecurityActions.stack;

        private JavaOther() {
        }

        private final Method getDefineClassMethod() {
            if (privileged != null && this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
            }
            catch (NoSuchMethodException e2) {
                throw new RuntimeException("cannot initialize", e2);
            }
        }

        @Override
        Class<?> defineClass(String name, byte[] b2, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
            Class<?> klass = this.stack.getCallerClass();
            if (klass != DefineClassHelper.class && klass != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                SecurityActions.setAccessible(this.defineClass, true);
                return (Class)this.defineClass.invoke(loader, name, b2, off, len, protectionDomain);
            }
            catch (Throwable e2) {
                if (e2 instanceof ClassFormatError) {
                    throw (ClassFormatError)e2;
                }
                if (e2 instanceof RuntimeException) {
                    throw (RuntimeException)e2;
                }
                throw new CannotCompileException(e2);
            }
        }
    }

    private static class Java7
    extends Helper {
        private final SecurityActions stack = SecurityActions.stack;
        private final MethodHandle defineClass = this.getDefineClassMethodHandle();

        private Java7() {
        }

        private final MethodHandle getDefineClassMethodHandle() {
            if (privileged != null && this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getMethodHandle(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
            }
            catch (NoSuchMethodException e2) {
                throw new RuntimeException("cannot initialize", e2);
            }
        }

        @Override
        Class<?> defineClass(String name, byte[] b2, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            if (this.stack.getCallerClass() != DefineClassHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return (Class)this.defineClass.invokeWithArguments(loader, name, b2, off, len, protectionDomain);
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

    private static class Java9
    extends Helper {
        private final Object stack;
        private final Method getCallerClass;
        private final ReferencedUnsafe sunMiscUnsafe = this.getReferencedUnsafe();

        Java9() {
            Class<?> stackWalkerClass = null;
            try {
                stackWalkerClass = Class.forName("java.lang.StackWalker");
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            if (stackWalkerClass != null) {
                try {
                    Class<?> optionClass = Class.forName("java.lang.StackWalker$Option");
                    this.stack = stackWalkerClass.getMethod("getInstance", optionClass).invoke(null, optionClass.getEnumConstants()[0]);
                    this.getCallerClass = stackWalkerClass.getMethod("getCallerClass", new Class[0]);
                }
                catch (Throwable e2) {
                    throw new RuntimeException("cannot initialize", e2);
                }
            } else {
                this.stack = null;
                this.getCallerClass = null;
            }
        }

        private final ReferencedUnsafe getReferencedUnsafe() {
            try {
                if (privileged != null && this.getCallerClass.invoke(this.stack, new Object[0]) != this.getClass()) {
                    throw new IllegalAccessError("Access denied for caller.");
                }
            }
            catch (Exception e2) {
                throw new RuntimeException("cannot initialize", e2);
            }
            try {
                SecurityActions.TheUnsafe usf = SecurityActions.getSunMiscUnsafeAnonymously();
                List<Method> defineClassMethod = usf.methods.get("defineClass");
                if (null == defineClassMethod) {
                    return null;
                }
                MethodHandle meth = MethodHandles.lookup().unreflect(defineClassMethod.get(0));
                return new ReferencedUnsafe(usf, meth);
            }
            catch (Throwable e3) {
                throw new RuntimeException("cannot initialize", e3);
            }
        }

        @Override
        Class<?> defineClass(String name, byte[] b2, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            try {
                if (this.getCallerClass.invoke(this.stack, new Object[0]) != DefineClassHelper.class) {
                    throw new IllegalAccessError("Access denied for caller.");
                }
            }
            catch (Exception e2) {
                throw new RuntimeException("cannot initialize", e2);
            }
            return this.sunMiscUnsafe.defineClass(name, b2, off, len, loader, protectionDomain);
        }

        final class ReferencedUnsafe {
            private final SecurityActions.TheUnsafe sunMiscUnsafeTheUnsafe;
            private final MethodHandle defineClass;

            ReferencedUnsafe(SecurityActions.TheUnsafe usf, MethodHandle meth) {
                this.sunMiscUnsafeTheUnsafe = usf;
                this.defineClass = meth;
            }

            Class<?> defineClass(String name, byte[] b2, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
                try {
                    if (Java9.this.getCallerClass.invoke(Java9.this.stack, new Object[0]) != Java9.class) {
                        throw new IllegalAccessError("Access denied for caller.");
                    }
                }
                catch (Exception e2) {
                    throw new RuntimeException("cannot initialize", e2);
                }
                try {
                    return (Class)this.defineClass.invokeWithArguments(this.sunMiscUnsafeTheUnsafe.theUnsafe, name, b2, off, len, loader, protectionDomain);
                }
                catch (Throwable e3) {
                    if (e3 instanceof RuntimeException) {
                        throw (RuntimeException)e3;
                    }
                    if (e3 instanceof ClassFormatError) {
                        throw (ClassFormatError)e3;
                    }
                    throw new ClassFormatError(e3.getMessage());
                }
            }
        }
    }

    private static class Java11
    extends JavaOther {
        private Java11() {
        }

        @Override
        Class<?> defineClass(String name, byte[] bcode, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
            if (neighbor != null) {
                return DefineClassHelper.toClass(neighbor, bcode);
            }
            return super.defineClass(name, bcode, off, len, neighbor, loader, protectionDomain);
        }
    }

    private static abstract class Helper {
        private Helper() {
        }

        abstract Class<?> defineClass(String var1, byte[] var2, int var3, int var4, Class<?> var5, ClassLoader var6, ProtectionDomain var7) throws ClassFormatError, CannotCompileException;
    }
}

