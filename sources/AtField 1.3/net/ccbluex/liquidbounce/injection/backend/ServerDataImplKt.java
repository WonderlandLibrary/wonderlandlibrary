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
    public static final ServerData unwrap(IServerData iServerData) {
        boolean bl = false;
        return ((ServerDataImpl)iServerData).getWrapped();
    }

    public static final IServerData wrap(ServerData serverData) {
        boolean bl = false;
        return new ServerDataImpl(serverData);
    }
}

