/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MutableCallSite;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.RelinkableCallSite;

public abstract class AbstractRelinkableCallSite
extends MutableCallSite
implements RelinkableCallSite {
    private final CallSiteDescriptor descriptor;

    protected AbstractRelinkableCallSite(CallSiteDescriptor descriptor) {
        super(descriptor.getMethodType());
        this.descriptor = descriptor;
    }

    @Override
    public CallSiteDescriptor getDescriptor() {
        return this.descriptor;
    }

    @Override
    public void initialize(MethodHandle relinkAndInvoke) {
        this.setTarget(relinkAndInvoke);
    }
}

