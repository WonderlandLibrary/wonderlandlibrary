package wtf.dawn.notifications;

import java.util.concurrent.LinkedBlockingQueue;
import wtf.dawn.notifications.Notification;

public class NotificationManager {

    public static String notifMode = "DARK";
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
}
