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
    public static final PlayerCapabilities unwrap(IPlayerCapabilities iPlayerCapabilities) {
        boolean bl = false;
        return ((PlayerCapabilitiesImpl)iPlayerCapabilities).getWrapped();
    }

    public static final IPlayerCapabilities wrap(PlayerCapabilities playerCapabilities) {
        boolean bl = false;
        return new PlayerCapabilitiesImpl(playerCapabilities);
    }
}

