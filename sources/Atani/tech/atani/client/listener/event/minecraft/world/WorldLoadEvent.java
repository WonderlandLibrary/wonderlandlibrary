package tech.atani.client.listener.event.minecraft.world;

import net.minecraft.client.multiplayer.WorldClient;
import tech.atani.client.listener.event.Event;

public class WorldLoadEvent extends Event {

    private final WorldClient worldClient;

    public WorldLoadEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

    public WorldClient getWorldClient() {
        return worldClient;
    }
}
