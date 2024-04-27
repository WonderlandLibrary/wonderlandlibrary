/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ServerData
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IServerData;
import net.ccbluex.liquidbounce.injection.backend.ServerDataImpl;
import net.minecraft.client.multiplayer.ServerData;

public final class ServerDataImplKt {
    public static final ServerData unwrap(IServerData $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ServerDataImpl)$this$unwrap).getWrapped();
    }

    public static final IServerData wrap(ServerData $this$wrap) {
        int $i$f$wrap = 0;
        return new ServerDataImpl($this$wrap);
    }
}

