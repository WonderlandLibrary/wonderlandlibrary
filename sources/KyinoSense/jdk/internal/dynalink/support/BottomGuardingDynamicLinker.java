/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;

public class BottomGuardingDynamicLinker
implements TypeBasedGuardingDynamicLinker {
    public static final BottomGuardingDynamicLinker INSTANCE = new BottomGuardingDynamicLinker();

    private BottomGuardingDynamicLinker() {
    }

    @Override
    public boolean canLinkType(Class<?> type) {
        return false;
    }

    @Override
    public GuardedInvocation getGuardedInvocation(LinkRequest linkRequest, LinkerServices linkerServices) {
        return null;
    }
}

