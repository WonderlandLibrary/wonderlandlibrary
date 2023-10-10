// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.util.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandle;
import com.viaversion.viaversion.libs.javassist.bytecode.ClassFile;
import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import java.net.URL;

public class DefinePackageHelper
{
    private static final Helper privileged;
    
    public static void definePackage(final String className, final ClassLoader loader) throws CannotCompileException {
        try {
            DefinePackageHelper.privileged.definePackage(loader, className, null, null, null, null, null, null, null);
        }
        catch (IllegalArgumentException e2) {}
        catch (Exception e) {
            throw new CannotCompileException(e);
        }
    }
    
    private DefinePackageHelper() {
    }
    
    static {
        privileged = ((ClassFile.MAJOR_VERSION >= 53) ? new Java9() : ((ClassFile.MAJOR_VERSION >= 51) ? new Java7() : new JavaOther()));
    }
    
    private abstract static class Helper
    {
        abstract Package definePackage(final ClassLoader p0, final String p1, final String p2, final String p3, final String p4, final String p5, final String p6, final String p7, final URL p8) throws IllegalArgumentException;
    }
    
    private static class Java9 extends Helper
    {
        @Override
        Package definePackage(final ClassLoader loader, final String name, final String specTitle, final String specVersion, final String specVendor, final String implTitle, final String implVersion, final String implVendor, final URL sealBase) throws IllegalArgumentException {
            throw new RuntimeException("define package has been disabled for jigsaw");
        }
    }
    
    private static class Java7 extends Helper
    {
        private final SecurityActions stack;
        private final MethodHandle definePackage;
        
        private Java7() {
            this.stack = SecurityActions.stack;
            this.definePackage = this.getDefinePackageMethodHandle();
        }
        
        private MethodHandle getDefinePackageMethodHandle() {
            if (this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getMethodHandle(ClassLoader.class, "definePackage", new Class[] { String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class });
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException("cannot initialize", e);
            }
        }
        
        @Override
        Package definePackage(final ClassLoader loader, final String name, final String specTitle, final String specVersion, final String specVendor, final String implTitle, final String implVersion, final String implVendor, final URL sealBase) throws IllegalArgumentException {
            if (this.stack.getCallerClass() != DefinePackageHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return (Package)this.definePackage.invokeWithArguments(loader, name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
            }
            catch (Throwable e) {
                if (e instanceof IllegalArgumentException) {
                    throw (IllegalArgumentException)e;
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                }
                return null;
            }
        }
    }
    
    private static class JavaOther extends Helper
    {
        private final SecurityActions stack;
        private final Method definePackage;
        
        private JavaOther() {
            this.stack = SecurityActions.stack;
            this.definePackage = this.getDefinePackageMethod();
        }
        
        private Method getDefinePackageMethod() {
            if (this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getDeclaredMethod(ClassLoader.class, "definePackage", new Class[] { String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class });
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException("cannot initialize", e);
            }
        }
        
        @Override
        Package definePackage(final ClassLoader loader, final String name, final String specTitle, final String specVersion, final String specVendor, final String implTitle, final String implVersion, final String implVendor, final URL sealBase) throws IllegalArgumentException {
            if (this.stack.getCallerClass() != DefinePackageHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                this.definePackage.setAccessible(true);
                return (Package)this.definePackage.invoke(loader, name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
            }
            catch (Throwable e) {
                if (e instanceof InvocationTargetException) {
                    final Throwable t = ((InvocationTargetException)e).getTargetException();
                    if (t instanceof IllegalArgumentException) {
                        throw (IllegalArgumentException)t;
                    }
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException)e;
                }
                return null;
            }
        }
    }
}
