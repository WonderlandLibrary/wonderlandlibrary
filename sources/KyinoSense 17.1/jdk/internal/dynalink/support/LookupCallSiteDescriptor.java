/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.DefaultCallSiteDescriptor;

class LookupCallSiteDescriptor
extends DefaultCallSiteDescriptor {
    private final MethodHandles.Lookup lookup;

    LookupCallSiteDescriptor(String[] tokenizedName, MethodType methodType, MethodHandles.Lookup lookup) {
        super(tokenizedName, methodType);
        this.lookup = lookup;
    }

    @Override
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }

    @Override
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return new LookupCallSiteDescriptor(this.getTokenizedName(), newMethodType, this.lookup);
    }
}

