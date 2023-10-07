/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;

public interface RelinkableCallSite {
    public void initialize(MethodHandle var1);

    public CallSiteDescriptor getDescriptor();

    public void relink(GuardedInvocation var1, MethodHandle var2);

    public void resetAndRelink(GuardedInvocation var1, MethodHandle var2);
}

