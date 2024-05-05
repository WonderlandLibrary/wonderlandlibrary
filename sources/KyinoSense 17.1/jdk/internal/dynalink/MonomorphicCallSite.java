/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.support.AbstractRelinkableCallSite;

public class MonomorphicCallSite
extends AbstractRelinkableCallSite {
    public MonomorphicCallSite(CallSiteDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    public void relink(GuardedInvocation guardedInvocation, MethodHandle relink) {
        this.setTarget(guardedInvocation.compose(relink));
    }

    @Override
    public void resetAndRelink(GuardedInvocation guardedInvocation, MethodHandle relink) {
        this.relink(guardedInvocation, relink);
    }
}

