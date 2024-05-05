/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.linker;

import jdk.internal.dynalink.linker.GuardedTypeConversion;

public interface GuardingTypeConverterFactory {
    public GuardedTypeConversion convertToType(Class<?> var1, Class<?> var2) throws Exception;
}

