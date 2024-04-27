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
    public static final IWorldClient wrap(WorldClient worldClient) {
        boolean bl = false;
        return new WorldClientImpl(worldClient);
    }

    public static final WorldClient unwrap(IWorldClient iWorldClient) {
        boolean bl = false;
        return (WorldClient)((WorldClientImpl)iWorldClient).getWrapped();
    }
}

