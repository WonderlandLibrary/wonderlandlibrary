package com.masterof13fps.manager.notificationmanager;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
    private static LinkedBlockingQueue<Notification> pendingNotifications = new LinkedBlockingQueue<>();
    private static Notification currentNotification = null;

    public static void show(Notification notification) {
        pendingNotifications.add(notification);
    }

    public static void update() {
        if (currentNotification != null && !currentNotification.isShown()) {
            currentNotification = null;
        }

        if (currentNotification == null && !pendingNotifications.isEmpty()) {
            currentNotification = pendingNotifications.poll();
            currentNotification.show();
        }

    }

    public static void render() {
        update();

        if (currentNotification != null)
            currentNotification.render();
    }

    public static LinkedBlockingQueue<Notification> getPendingNotifications() {
        return pendingNotifications;
    }

    public static void setPendingNotifications(LinkedBlockingQueue<Notification> pendingNotifications) {
        NotificationManager.pendingNotifications = pendingNotifications;
    }

    public static Notification getCurrentNotification() {
        return currentNotification;
    }

    public static void setCurrentNotification(Notification currentNotification) {
        NotificationManager.currentNotification = currentNotification;
    }
}