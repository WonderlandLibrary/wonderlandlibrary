/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public interface MethodTypeConversionStrategy {
    public MethodHandle asType(MethodHandle var1, MethodType var2);
}

