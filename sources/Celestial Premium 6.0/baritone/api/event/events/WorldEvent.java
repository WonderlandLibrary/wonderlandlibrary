/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.events;

import baritone.api.event.events.type.EventState;
import net.minecraft.client.multiplayer.WorldClient;

public final class WorldEvent {
    private final WorldClient world;
    private final EventState state;

    public WorldEvent(WorldClient world, EventState state) {
        this.world = world;
        this.state = state;
    }

    public final WorldClient getWorld() {
        return this.world;
    }

    public final EventState getState() {
        return this.state;
    }
}

