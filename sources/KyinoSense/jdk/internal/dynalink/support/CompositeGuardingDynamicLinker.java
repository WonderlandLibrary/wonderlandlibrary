/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.io.Serializable;
import java.util.LinkedList;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;

public class CompositeGuardingDynamicLinker
implements GuardingDynamicLinker,
Serializable {
    private static final long serialVersionUID = 1L;
    private final GuardingDynamicLinker[] linkers;

    public CompositeGuardingDynamicLinker(Iterable<? extends GuardingDynamicLinker> linkers) {
        LinkedList<GuardingDynamicLinker> l = new LinkedList<GuardingDynamicLinker>();
        for (GuardingDynamicLinker guardingDynamicLinker : linkers) {
            l.add(guardingDynamicLinker);
        }
        this.linkers = l.toArray(new GuardingDynamicLinker[l.size()]);
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) throws Exception {
        for (GuardingDynamicLinker linker : this.linkers) {
            GuardedInvocation invocation = linker.getGuardedInvocation(linkRequest, linkerServices);
            if (invocation == null) continue;
            return invocation;
        }
        return null;
    }
}

