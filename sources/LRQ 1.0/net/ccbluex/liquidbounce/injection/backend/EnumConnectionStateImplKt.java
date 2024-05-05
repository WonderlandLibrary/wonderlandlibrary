/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.EnumConnectionState
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.network.IEnumConnectionState;
import net.ccbluex.liquidbounce.injection.backend.EnumConnectionStateImpl;
import net.minecraft.network.EnumConnectionState;

public final class EnumConnectionStateImplKt {
    public static final EnumConnectionState unwrap(IEnumConnectionState $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((EnumConnectionStateImpl)$this$unwrap).getWrapped();
    }

    public static final IEnumConnectionState wrap(EnumConnectionState $this$wrap) {
        int $i$f$wrap = 0;
        return new EnumConnectionStateImpl($this$wrap);
    }
}

