package dev.rise.anticheat;

import dev.rise.anticheat.alert.AlertManager;
import dev.rise.anticheat.check.manager.CheckManager;
import dev.rise.anticheat.data.PlayerData;
import dev.rise.anticheat.listener.RegistrationListener;
import lombok.Getter;
import net.minecraft.network.Packet;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class AntiCheat {

    private final Map<UUID, PlayerData> playerMap = new ConcurrentHashMap<>();

    private final RegistrationListener registrationListener = new RegistrationListener();
    private final AlertManager alertManager = new AlertManager();

    public AntiCheat() {
        CheckManager.setup();
    }

    public void handle(final Packet<?> packet) {
        this.playerMap.values().forEach(playerData -> playerData.handle(packet));
    }
}
