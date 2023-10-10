package today.flux.gui.hud.notification;

import today.flux.module.Module;
import today.flux.module.implement.Render.Hud;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public static void doRender(float wid, float hei) {
        if (Hud.notification.getValue()) {
            float startY = hei - 23;
            for (Notification notification : notifications) {
                if (notification == null)
                    continue;

                notification.draw(wid, startY);
                if (Hud.notifMode.isCurrentMode("Classical"))
                    startY -= notification.getHeight() + 6;
                else
                    startY -= notification.getHeight() + 2;
            }
            notifications.removeIf(Notification::shouldDelete);
        } else {
            if (!notifications.isEmpty()) {
                notifications.clear();
            }
        }
    }

    public static void show(String title, String message, Notification.Type type) {
        notifications.add(new Notification(title, message, type, 2500L));
    }

    public static void show(String title, String message, Notification.Type type, long stayTime) {
        notifications.add(new Notification(title, message, type, stayTime));
    }

    public static void show(String title, String message, Module module) {
        notifications.add(new Notification(title, message, Notification.Type.MODULE, 2500L, module));
    }
}
