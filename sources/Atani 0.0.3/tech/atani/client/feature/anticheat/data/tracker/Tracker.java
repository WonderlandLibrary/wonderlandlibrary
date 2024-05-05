package tech.atani.client.feature.anticheat.data.tracker;

import net.minecraft.network.Packet;
import tech.atani.client.feature.anticheat.data.PlayerData;

public abstract class Tracker {
    private final PlayerData data;

    public Tracker(PlayerData data) {
        this.data = data;
    }

    public abstract void handle(Packet<?> packet);

    public PlayerData getData() {
        return data;
    }
}