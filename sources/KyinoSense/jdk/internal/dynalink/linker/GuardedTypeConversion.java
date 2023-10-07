/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.linker;

import jdk.internal.dynalink.linker.GuardedInvocation;

public class GuardedTypeConversion {
    private final GuardedInvocation conversionInvocation;
    private final boolean cacheable;

    public GuardedTypeConversion(GuardedInvocation conversionInvocation, boolean cacheable) {
        this.conversionInvocation = conversionInvocation;
        this.cacheable = cacheable;
    }

    public GuardedInvocation getConversionInvocation() {
        return this.conversionInvocation;
    }

    public boolean isCacheable() {
        return this.cacheable;
    }
}

