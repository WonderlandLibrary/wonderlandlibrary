package graphite.impl.notifications.dev;

import java.util.List;

public interface IDevNotificationRenderer {
    void draw(List<DevNotification> notifications);
}