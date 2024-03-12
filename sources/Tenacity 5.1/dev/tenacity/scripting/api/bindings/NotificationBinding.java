package dev.tenacity.scripting.api.bindings;

import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude(Strategy.NAME_REMAPPING)
public class NotificationBinding {

    public NotificationType success = NotificationType.SUCCESS;
    public NotificationType disable = NotificationType.DISABLE;
    public NotificationType info = NotificationType.INFO;
    public NotificationType warning = NotificationType.WARNING;

    public void post(NotificationType notificationType, String title, String description) {
        NotificationManager.post(notificationType, title, description);
    }

    public void post(NotificationType notificationType, String title, String description, long time) {
        NotificationManager.post(notificationType, title, description, time);
    }

}
