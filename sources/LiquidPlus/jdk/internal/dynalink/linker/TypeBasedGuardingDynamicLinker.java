/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.linker;

import jdk.internal.dynalink.linker.GuardingDynamicLinker;

public interface TypeBasedGuardingDynamicLinker
extends GuardingDynamicLinker {
    public boolean canLinkType(Class<?> var1);
}

