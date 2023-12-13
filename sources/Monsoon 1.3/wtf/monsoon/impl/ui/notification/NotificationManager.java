package wtf.monsoon.impl.ui.notification;


import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
    public static LinkedBlockingQueue<Notification> pendingNotifications = new LinkedBlockingQueue<>();
    public static Notification currentNotification = null;

    public static void show(Notification notification) {
        pendingNotifications.add(notification);
    }

    public static void update() {
        pendingNotifications.removeIf(notif -> notif.timer.hasTimeElapsed(notif.end, false));
    }

    public static void render() {
        update();

        int count = 0;

        for(Notification notif : pendingNotifications) {
            count++;
            notif.render(count);
        }
    }
}