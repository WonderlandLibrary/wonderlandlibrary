/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.support.LinkRequestImpl;

public class RuntimeContextLinkRequestImpl
extends LinkRequestImpl {
    private final int runtimeContextArgCount;
    private LinkRequestImpl contextStrippedRequest;

    public RuntimeContextLinkRequestImpl(CallSiteDescriptor callSiteDescriptor, Object callSiteToken, int linkCount, boolean callSiteUnstable, Object[] arguments, int runtimeContextArgCount) {
        super(callSiteDescriptor, callSiteToken, linkCount, callSiteUnstable, arguments);
        if (runtimeContextArgCount < 1) {
            throw new IllegalArgumentException("runtimeContextArgCount < 1");
        }
        this.runtimeContextArgCount = runtimeContextArgCount;
    }

    @Override
    public LinkRequest withoutRuntimeContext() {
        if (this.contextStrippedRequest == null) {
            this.contextStrippedRequest = new LinkRequestImpl(CallSiteDescriptorFactory.dropParameterTypes(this.getCallSiteDescriptor(), 1, this.runtimeContextArgCount + 1), this.getCallSiteToken(), this.getLinkCount(), this.isCallSiteUnstable(), this.getTruncatedArguments());
        }
        return this.contextStrippedRequest;
    }

    @Override
    public LinkRequest replaceArguments(CallSiteDescriptor callSiteDescriptor, Object[] arguments) {
        return new RuntimeContextLinkRequestImpl(callSiteDescriptor, this.getCallSiteToken(), this.getLinkCount(), this.isCallSiteUnstable(), arguments, this.runtimeContextArgCount);
    }

    private Object[] getTruncatedArguments() {
        Object[] args2 = this.getArguments();
        Object[] newargs = new Object[args2.length - this.runtimeContextArgCount];
        newargs[0] = args2[0];
        System.arraycopy(args2, this.runtimeContextArgCount + 1, newargs, 1, newargs.length - 1);
        return newargs;
    }
}

