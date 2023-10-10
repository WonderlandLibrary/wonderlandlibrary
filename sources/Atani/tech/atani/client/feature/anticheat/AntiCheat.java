package tech.atani.client.feature.anticheat;

import net.minecraft.client.Minecraft;
import tech.atani.client.feature.anticheat.data.PlayerData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AntiCheat {

    public static final AntiCheat INSTANCE = new AntiCheat();

    private final Map<UUID, PlayerData> players = new ConcurrentHashMap<>();

    public void handlePlayers() {
        players.values().stream().filter(this::hasEntity).forEach(PlayerData::updateTicks);
        players.values().stream().filter(d -> !hasEntity(d)).forEach(d -> players.remove(d.getPlayer().getUniqueID()));
    }

    private boolean hasEntity(PlayerData data) {
        return Minecraft.getMinecraft().theWorld.playerEntities.contains(data.getPlayer());
    }

    public Map<UUID, PlayerData> getPlayers() {
        return players;
    }
}
