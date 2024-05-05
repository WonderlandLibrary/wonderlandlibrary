/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.SingleDynamicMethod;
import jdk.internal.dynalink.linker.LinkerServices;

abstract class DynamicMethod {
    private final String name;

    DynamicMethod(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    abstract MethodHandle getInvocation(CallSiteDescriptor var1, LinkerServices var2);

    abstract SingleDynamicMethod getMethodForExactParamTypes(String var1);

    abstract boolean contains(SingleDynamicMethod var1);

    static String getClassAndMethodName(Class<?> clazz, String name) {
        String clazzName = clazz.getCanonicalName();
        return (clazzName == null ? clazz.getName() : clazzName) + "." + name;
    }

    public String toString() {
        return "[" + this.getClass().getName() + " " + this.getName() + "]";
    }

    boolean isConstructor() {
        return false;
    }
}

