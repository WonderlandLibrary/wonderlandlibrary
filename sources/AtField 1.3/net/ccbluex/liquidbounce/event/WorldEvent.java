/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.event;

import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

public final class WorldEvent
extends Event {
    private final IWorldClient worldClient;

    public final IWorldClient getWorldClient() {
        return this.worldClient;
    }

    public WorldEvent(@Nullable IWorldClient iWorldClient) {
        this.worldClient = iWorldClient;
    }
}

