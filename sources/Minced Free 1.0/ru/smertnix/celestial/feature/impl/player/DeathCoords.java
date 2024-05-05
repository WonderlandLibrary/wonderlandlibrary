package ru.smertnix.celestial.feature.impl.player;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class DeathCoords
        extends Feature {
    public DeathCoords() {
        super("Death Coords", "Даёт информацию о вашей смерти", FeatureCategory.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 1.0f && mc.currentScreen instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (mc.player.deathTime < 1) {
                NotificationRenderer.queue("Death Coords", "Your death position - " + TextFormatting.RED + "X: " + TextFormatting.RESET + x + TextFormatting.RED + " Y: " + TextFormatting.RESET + y + TextFormatting.RED + " Z: " + TextFormatting.RESET + z, 10, NotificationMode.INFO);
                ChatUtils.addChatMessage("Your death position - " + TextFormatting.RED + "X: " + TextFormatting.RESET + x + TextFormatting.RED + " Y: " + TextFormatting.RESET + y + TextFormatting.RED + " Z: " + TextFormatting.RESET + z);
            }
        }
    }
}
