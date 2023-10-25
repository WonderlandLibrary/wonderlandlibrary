/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.application;

import java.lang.module.ModuleDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public final class ModuleAccess {
    private final Object module;
    private static final Method bootLayerMethod;
    private static final Method getModuleNameMethod;
    private static final Method findModuleMethod;
    private static final Method getDescriptorMethod;
    private static final Method classForNameMethod;

    private ModuleAccess(Object object) {
        this.module = object;
    }

    ModuleDescriptor getDescriptor() {
        try {
            return (ModuleDescriptor)getDescriptorMethod.invoke(this.module, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            throw new InternalError(reflectiveOperationException);
        }
    }

    String getName() {
        try {
            return (String)getModuleNameMethod.invoke(this.module, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            throw new InternalError(reflectiveOperationException);
        }
    }

    Class<?> classForName(String string) {
        try {
            return (Class)classForNameMethod.invoke(null, this.module, string);
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            throw new InternalError(reflectiveOperationException);
        }
    }

    static ModuleAccess load(String string) {
        try {
            Object object = bootLayerMethod.invoke(null, new Object[0]);
            Optional optional = (Optional)findModuleMethod.invoke(object, string);
            if (!optional.isPresent()) {
                throw new InternalError("Module " + string + " not in boot Layer");
            }
            return new ModuleAccess(optional.get());
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            throw new InternalError(reflectiveOperationException);
        }
    }

    static {
        Class<?> class_ = null;
        Method method = null;
        Method method2 = null;
        Method method3 = null;
        Method method4 = null;
        Method method5 = null;
        Method method6 = null;
        Method method7 = null;
        try {
            method = Class.class.getMethod("getModule", new Class[0]);
            class_ = method.getReturnType();
            method2 = class_.getMethod("getLayer", new Class[0]);
            method3 = class_.getMethod("getDescriptor", new Class[0]);
            method4 = class_.getMethod("getName", new Class[0]);
            Class<?> class_2 = method2.getReturnType();
            method5 = class_2.getMethod("boot", new Class[0]);
            method6 = class_2.getMethod("findModule", String.class);
            method7 = Class.class.getMethod("forName", class_, String.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            throw new InternalError("Module reflection failed", noSuchMethodException);
        }
        bootLayerMethod = method5;
        getModuleNameMethod = method4;
        getDescriptorMethod = method3;
        findModuleMethod = method6;
        classForNameMethod = method7;
    }
}

