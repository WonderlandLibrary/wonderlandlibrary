package sudo.utils.misc;

import java.util.ArrayList;

import sudo.module.ModuleManager;
import sudo.module.render.Notifications;

public class NotificationUtil {

    private static final ArrayList<Notification> active_notifications = new ArrayList<>();
    
    
    public static void send_notification (Notification message) {
        active_notifications.add(0, message);
    }

    public static ArrayList<Notification> get_notifications () {
        return active_notifications;
    }

    public static void update () {
        active_notifications.removeIf(notification -> (System.currentTimeMillis() - notification.getTimeCreated()) > 2000);

        int maxnotifications = ModuleManager.INSTANCE.getModule(Notifications.class).maxNotif.getValueInt();
        if (maxnotifications < active_notifications.size()) {
            active_notifications.remove(maxnotifications - 1);
        }
    }
}
