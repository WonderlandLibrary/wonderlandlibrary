/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.jmx;

import java.lang.reflect.Method;

class MethodUnion {
    Method readMethod;
    Method writeMethod;

    MethodUnion(Method readMethod, Method writeMethod) {
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }
}

