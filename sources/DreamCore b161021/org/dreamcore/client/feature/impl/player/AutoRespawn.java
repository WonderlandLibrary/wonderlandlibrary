package org.dreamcore.client.feature.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;

public class AutoRespawn extends Feature {

    public AutoRespawn() {
        super("AutoRespawn", "Автоматический респавн при смерти", Type.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 0 || !mc.player.isEntityAlive() || mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }
}
