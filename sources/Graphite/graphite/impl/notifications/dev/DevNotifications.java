package graphite.impl.notifications.dev;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DevNotifications {
    private static DevNotifications instance = new DevNotifications();
    private final List<DevNotification> notifications = new CopyOnWriteArrayList<>();
    private final DevNotificationRenderer renderer = new DevNotificationRenderer();

    private DevNotifications() {
        instance = this;
    }

    public static DevNotifications getManager() {
        return instance;
    }

    public void post(String text) {
        this.notifications.add(new DevNotification(text));
    }

    public void updateAndRender() {
        if (!this.notifications.isEmpty())
            this.renderer.draw(this.notifications);
    }
}