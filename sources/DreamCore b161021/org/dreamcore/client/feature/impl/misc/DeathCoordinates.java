package org.dreamcore.client.feature.impl.misc;

import net.minecraft.client.gui.GuiGameOver;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventUpdate;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class DeathCoordinates extends Feature {

    public DeathCoordinates() {
        super("DeathCoordinates", "Показывает координаты смерти игрока", Type.Misc);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 1 && mc.currentScreen instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (mc.player.ticksExisted % 20 == 0) {
                NotificationManager.publicity("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 10, NotificationType.INFO);
                ChatHelper.addChatMessage("Death Coordinates: " + "X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}