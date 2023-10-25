package fun.expensive.client.feature.impl.player;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.client.gui.GuiGameOver;

public class DeathCoordinates
        extends Feature {
    public DeathCoordinates() {
        super("DeathCoordinates", "ѕоказывает координаты смерти игрока", FeatureCategory.Misc);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 1.0f && mc.currentScreen instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (mc.player.deathTime < 1) {
                NotificationRenderer.queue("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 10, NotificationMode.INFO);
                ChatUtils.addChatMessage("Death Coordinates: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}
