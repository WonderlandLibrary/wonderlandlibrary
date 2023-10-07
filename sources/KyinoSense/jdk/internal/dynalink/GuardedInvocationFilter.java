/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;

public interface GuardedInvocationFilter {
    public GuardedInvocation filter(GuardedInvocation var1, LinkRequest var2, LinkerServices var3);
}

