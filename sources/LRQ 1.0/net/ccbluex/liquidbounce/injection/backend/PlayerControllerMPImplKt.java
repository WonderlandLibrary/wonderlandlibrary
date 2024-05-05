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
    public static final PlayerControllerMP unwrap(IPlayerControllerMP $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((PlayerControllerMPImpl)$this$unwrap).getWrapped();
    }

    public static final IPlayerControllerMP wrap(PlayerControllerMP $this$wrap) {
        int $i$f$wrap = 0;
        return new PlayerControllerMPImpl($this$wrap);
    }
}

