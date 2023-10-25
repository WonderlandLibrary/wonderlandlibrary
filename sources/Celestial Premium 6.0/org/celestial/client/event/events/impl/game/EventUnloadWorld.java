/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.event.events.impl.game;

import net.minecraft.world.World;
import org.celestial.client.event.events.callables.EventCancellable;

public class EventUnloadWorld
extends EventCancellable {
    private final World world;

    public EventUnloadWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }
}

