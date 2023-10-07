/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.dynalink.beans.FacetIntrospector;
import jdk.internal.dynalink.beans.StaticClass;

class StaticClassIntrospector
extends FacetIntrospector {
    StaticClassIntrospector(Class<?> clazz) {
        super(clazz, false);
    }

    @Override
    Map<String, MethodHandle> getInnerClassGetters() {
        HashMap<String, MethodHandle> map = new HashMap<String, MethodHandle>();
        for (Class<?> innerClass : this.membersLookup.getInnerClasses()) {
            map.put(innerClass.getSimpleName(), this.editMethodHandle(MethodHandles.constant(StaticClass.class, StaticClass.forClass(innerClass))));
        }
        return map;
    }

    @Override
    MethodHandle editMethodHandle(MethodHandle mh) {
        return StaticClassIntrospector.editStaticMethodHandle(mh);
    }

    static MethodHandle editStaticMethodHandle(MethodHandle mh) {
        return StaticClassIntrospector.dropReceiver(mh, Object.class);
    }

    static MethodHandle editConstructorMethodHandle(MethodHandle cmh) {
        return StaticClassIntrospector.dropReceiver(cmh, StaticClass.class);
    }

    private static MethodHandle dropReceiver(MethodHandle mh, Class<?> receiverClass) {
        MethodHandle newHandle = MethodHandles.dropArguments(mh, 0, new Class[]{receiverClass});
        if (mh.isVarargsCollector() && !newHandle.isVarargsCollector()) {
            MethodType type = mh.type();
            newHandle = newHandle.asVarargsCollector((Class<?>)type.parameterType(type.parameterCount() - 1));
        }
        return newHandle;
    }
}

