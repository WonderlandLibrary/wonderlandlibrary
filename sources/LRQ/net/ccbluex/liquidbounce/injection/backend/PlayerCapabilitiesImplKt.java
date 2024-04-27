/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.PlayerCapabilities
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.entity.player.IPlayerCapabilities;
import net.ccbluex.liquidbounce.injection.backend.PlayerCapabilitiesImpl;
import net.minecraft.entity.player.PlayerCapabilities;

public final class PlayerCapabilitiesImplKt {
    public static final PlayerCapabilities unwrap(IPlayerCapabilities $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((PlayerCapabilitiesImpl)$this$unwrap).getWrapped();
    }

    public static final IPlayerCapabilities wrap(PlayerCapabilities $this$wrap) {
        int $i$f$wrap = 0;
        return new PlayerCapabilitiesImpl($this$wrap);
    }
}

