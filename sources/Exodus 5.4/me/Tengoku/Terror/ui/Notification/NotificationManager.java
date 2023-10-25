/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.ui.Notification;

import java.util.concurrent.LinkedBlockingQueue;
import me.Tengoku.Terror.ui.Notification.Notification;

public class NotificationManager {
    private static Notification currentNotification;
    private static LinkedBlockingQueue<Notification> pendingNotifications;

    public static void update() {
        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }
        if (currentNotification == null && !pendingNotifications.isEmpty()) {
            currentNotification = pendingNotifications.poll();
            currentNotification.show();
        }
    }

    public static void show(Notification notification) {
        pendingNotifications.add(notification);
    }

    public static void render() {
        NotificationManager.update();
        if (currentNotification != null) {
            currentNotification.render();
        }
    }

    static {
        pendingNotifications = new LinkedBlockingQueue();
        currentNotification = null;
    }
}

