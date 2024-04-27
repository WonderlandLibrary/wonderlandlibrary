package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;
import net.minecraft.client.multiplayer.WorldClient;

public class WorldEvent extends Event {
    private WorldClient worldClient;

    public WorldEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

    public WorldClient getWorldClient() {
        return this.worldClient;
    }
}
