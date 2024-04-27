/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.beans.BeanLinker;
import jdk.internal.dynalink.beans.GuardedInvocationComponent;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.support.Lookup;

class ClassLinker
extends BeanLinker {
    private static final MethodHandle FOR_CLASS = new Lookup(MethodHandles.lookup()).findStatic(StaticClass.class, "forClass", MethodType.methodType(StaticClass.class, Class.class));

    ClassLinker() {
        super(Class.class);
        this.setPropertyGetter("static", FOR_CLASS, GuardedInvocationComponent.ValidationType.EXACT_CLASS);
    }
}

