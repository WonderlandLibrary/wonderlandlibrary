package ru.smertnix.celestial.feature.impl.player;

import java.awt.Color;

import net.minecraft.client.gui.GuiGameOver;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.StringSetting;

public class AutoRespawn extends Feature {

    private final StringSetting cmd;
    public AutoRespawn() {
        super("Auto Respawn", "Автоматически возрождает вас", FeatureCategory.Player);
        cmd = new StringSetting("", "/home home", "/home home", () -> true);
        addSettings(cmd);
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 0 || !mc.player.isEntityAlive() || mc.currentScreen instanceof GuiGameOver) {
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
            mc.player.sendChatMessage(cmd.getCurrentText());
        }
    }
}
