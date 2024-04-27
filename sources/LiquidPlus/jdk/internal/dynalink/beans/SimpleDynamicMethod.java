/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.beans.SingleDynamicMethod;

class SimpleDynamicMethod
extends SingleDynamicMethod {
    private final MethodHandle target;
    private final boolean constructor;

    SimpleDynamicMethod(MethodHandle target, Class<?> clazz, String name) {
        this(target, clazz, name, false);
    }

    SimpleDynamicMethod(MethodHandle target, Class<?> clazz, String name, boolean constructor) {
        super(SimpleDynamicMethod.getName(target, clazz, name, constructor));
        this.target = target;
        this.constructor = constructor;
    }

    private static String getName(MethodHandle target, Class<?> clazz, String name, boolean constructor) {
        return SimpleDynamicMethod.getMethodNameWithSignature(target.type(), constructor ? name : SimpleDynamicMethod.getClassAndMethodName(clazz, name), !constructor);
    }

    @Override
    boolean isVarArgs() {
        return this.target.isVarargsCollector();
    }

    @Override
    MethodType getMethodType() {
        return this.target.type();
    }

    @Override
    MethodHandle getTarget(MethodHandles.Lookup lookup) {
        return this.target;
    }

    @Override
    boolean isConstructor() {
        return this.constructor;
    }
}

