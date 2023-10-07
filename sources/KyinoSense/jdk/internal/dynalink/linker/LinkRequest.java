/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.linker;

import jdk.internal.dynalink.CallSiteDescriptor;

public interface LinkRequest {
    public CallSiteDescriptor getCallSiteDescriptor();

    public Object getCallSiteToken();

    public Object[] getArguments();

    public Object getReceiver();

    public int getLinkCount();

    public boolean isCallSiteUnstable();

    public LinkRequest withoutRuntimeContext();

    public LinkRequest replaceArguments(CallSiteDescriptor var1, Object[] var2);
}

