/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import net.minecraft.client.gui.GuiGameOver;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class DeathCoordinates
extends Feature {
    public DeathCoordinates() {
        super("DeathCoordinates", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u0441\u043c\u0435\u0440\u0442\u0438 \u0438\u0433\u0440\u043e\u043a\u0430", Type.Misc);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (DeathCoordinates.mc.player.getHealth() < 1.0f && DeathCoordinates.mc.currentScreen instanceof GuiGameOver) {
            int x = DeathCoordinates.mc.player.getPosition().getX();
            int y = DeathCoordinates.mc.player.getPosition().getY();
            int z = DeathCoordinates.mc.player.getPosition().getZ();
            if (DeathCoordinates.mc.player.deathTime < 1) {
                NotificationManager.publicity("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 10, NotificationType.INFO);
                ChatHelper.addChatMessage("Death Coordinates: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}

