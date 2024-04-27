/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.util.Collections;
import java.util.Map;
import jdk.internal.dynalink.beans.FacetIntrospector;

class BeanIntrospector
extends FacetIntrospector {
    BeanIntrospector(Class<?> clazz) {
        super(clazz, true);
    }

    @Override
    Map<String, MethodHandle> getInnerClassGetters() {
        return Collections.emptyMap();
    }

    @Override
    MethodHandle editMethodHandle(MethodHandle mh) {
        return mh;
    }
}

