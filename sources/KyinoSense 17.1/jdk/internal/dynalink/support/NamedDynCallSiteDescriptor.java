/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.support.UnnamedDynCallSiteDescriptor;

class NamedDynCallSiteDescriptor
extends UnnamedDynCallSiteDescriptor {
    private final String name;

    NamedDynCallSiteDescriptor(String op, String name, MethodType methodType) {
        super(op, methodType);
        this.name = name;
    }

    @Override
    public int getNameTokenCount() {
        return 3;
    }

    @Override
    public String getNameToken(int i) {
        switch (i) {
            case 0: {
                return "dyn";
            }
            case 1: {
                return this.getOp();
            }
            case 2: {
                return this.name;
            }
        }
        throw new IndexOutOfBoundsException(String.valueOf(i));
    }

    @Override
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new NamedDynCallSiteDescriptor(this.getOp(), this.name, newMethodType));
    }
}

