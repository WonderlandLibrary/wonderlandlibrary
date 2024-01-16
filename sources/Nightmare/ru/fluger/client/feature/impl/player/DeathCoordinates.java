// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.player;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.helpers.misc.ChatHelper;
import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class DeathCoordinates extends Feature
{
    public DeathCoordinates() {
        super("DeathCoordinates", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u0441\u043c\u0435\u0440\u0442\u0438 \u0438\u0433\u0440\u043e\u043a\u0430", Type.Player);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (DeathCoordinates.mc.h.cd() < 1.0f && DeathCoordinates.mc.m instanceof bkv) {
            final int x = DeathCoordinates.mc.h.c().p();
            final int y = DeathCoordinates.mc.h.c().q();
            final int z = DeathCoordinates.mc.h.c().r();
            if (DeathCoordinates.mc.h.aB < 1) {
                NotificationManager.publicity("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 10, NotificationType.INFO);
                ChatHelper.addChatMessage("Death Coordinates: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}
