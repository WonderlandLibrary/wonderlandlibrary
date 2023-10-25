/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;

public class ModuleHelper {
    private static final Method getModuleMethod;
    private static final Method addReadsMethod;
    private static final Method addExportsMethod;
    private static final boolean verbose;

    public static Object getModule(Class class_) {
        if (getModuleMethod != null) {
            try {
                return getModuleMethod.invoke(class_, new Object[0]);
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                throw new RuntimeException(reflectiveOperationException);
            }
        }
        return null;
    }

    public static void addReads(Object object, Object object2) {
        if (addReadsMethod != null) {
            try {
                addReadsMethod.invoke(object, object2);
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                throw new RuntimeException(reflectiveOperationException);
            }
        }
    }

    public static void addExports(Object object, String string, Object object2) {
        if (addExportsMethod != null) {
            try {
                addExportsMethod.invoke(object, string, object2);
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                throw new RuntimeException(reflectiveOperationException);
            }
        }
    }

    static {
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.verbose"));
        verbose = bl;
        if (verbose) {
            System.err.println(ModuleHelper.class.getName() + " : <clinit>");
        }
        Method method = null;
        Method method2 = null;
        Method method3 = null;
        try {
            method = Class.class.getMethod("getModule", new Class[0]);
            Class<?> class_ = method.getReturnType();
            method2 = class_.getMethod("addReads", class_);
            method3 = class_.getMethod("addExports", String.class, class_);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
        }
        getModuleMethod = method;
        addReadsMethod = method2;
        addExportsMethod = method3;
        if (verbose) {
            System.err.println("getModuleMethod = " + getModuleMethod);
            System.err.println("addReadsMethod = " + addReadsMethod);
            System.err.println("addExportsMethod = " + addExportsMethod);
        }
    }
}

