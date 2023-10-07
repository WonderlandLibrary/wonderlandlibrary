/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import jdk.internal.dynalink.GuardedInvocationFilter;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;

public class DefaultPrelinkFilter
implements GuardedInvocationFilter {
    @Override
    public GuardedInvocation filter(GuardedInvocation inv, LinkRequest request, LinkerServices linkerServices) {
        return inv.asType(linkerServices, request.getCallSiteDescriptor().getMethodType());
    }
}

