/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.WorldClient
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.injection.backend.WorldClientImpl;
import net.minecraft.client.multiplayer.WorldClient;

public final class WorldClientImplKt {
    public static final WorldClient unwrap(IWorldClient $this$unwrap) {
        int $i$f$unwrap = 0;
        return (WorldClient)((WorldClientImpl)$this$unwrap).getWrapped();
    }

    public static final IWorldClient wrap(WorldClient $this$wrap) {
        int $i$f$wrap = 0;
        return new WorldClientImpl($this$wrap);
    }
}

