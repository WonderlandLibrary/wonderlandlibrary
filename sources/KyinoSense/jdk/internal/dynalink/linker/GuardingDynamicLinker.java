/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.linker;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;

public interface GuardingDynamicLinker {
    public GuardedInvocation getGuardedInvocation(LinkRequest var1, LinkerServices var2) throws Exception;
}

