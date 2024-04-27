/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.AbstractCallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;

class UnnamedDynCallSiteDescriptor
extends AbstractCallSiteDescriptor {
    private final MethodType methodType;
    private final String op;

    UnnamedDynCallSiteDescriptor(String op, MethodType methodType) {
        this.op = op;
        this.methodType = methodType;
    }

    @Override
    public int getNameTokenCount() {
        return 2;
    }

    String getOp() {
        return this.op;
    }

    @Override
    public String getNameToken(int i) {
        switch (i) {
            case 0: {
                return "dyn";
            }
            case 1: {
                return this.op;
            }
        }
        throw new IndexOutOfBoundsException(String.valueOf(i));
    }

    @Override
    public MethodType getMethodType() {
        return this.methodType;
    }

    @Override
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new UnnamedDynCallSiteDescriptor(this.op, newMethodType));
    }
}

