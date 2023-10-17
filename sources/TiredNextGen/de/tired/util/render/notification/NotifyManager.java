package de.tired.util.render.notification;

import de.tired.base.interfaces.IHook;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

public class NotifyManager implements IHook {

    private static final ArrayList notifications = new ArrayList<>();


    public static void drawNotifications(boolean rect, boolean text) {
        final ScaledResolution res = new ScaledResolution(MC);
        double startY = res.getScaledHeight() - 25;
        double lastY = startY;

        for (int i = 0; i < notifications.size(); ++i) {
            Notify not = (Notify) notifications.get(i);
            if (not.shouldDelete()) {
                notifications.remove(i);
            }

            not.doRenderNotification(startY, lastY, rect);
            startY -= not.height + 5.0D;
        }
    }

    public static void sendClientMessage(String title, String message, Notify.NotificationType notificationType) {
        notifications.add(new Notify(title, message, notificationType));
    }


}