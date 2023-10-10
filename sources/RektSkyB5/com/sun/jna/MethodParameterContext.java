/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.FunctionParameterContext;
import java.lang.reflect.Method;

public class MethodParameterContext
extends FunctionParameterContext {
    private Method method;

    MethodParameterContext(Function f2, Object[] args, int index, Method m2) {
        super(f2, args, index);
        this.method = m2;
    }

    public Method getMethod() {
        return this.method;
    }
}

