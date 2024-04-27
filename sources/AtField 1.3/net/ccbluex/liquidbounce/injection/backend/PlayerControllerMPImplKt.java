/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IPlayerControllerMP;
import net.ccbluex.liquidbounce.injection.backend.PlayerControllerMPImpl;
import net.minecraft.client.multiplayer.PlayerControllerMP;

public final class PlayerControllerMPImplKt {
    public static final IPlayerControllerMP wrap(PlayerControllerMP playerControllerMP) {
        boolean bl = false;
        return new PlayerControllerMPImpl(playerControllerMP);
    }

    public static final PlayerControllerMP unwrap(IPlayerControllerMP iPlayerControllerMP) {
        boolean bl = false;
        return ((PlayerControllerMPImpl)iPlayerControllerMP).getWrapped();
    }
}

