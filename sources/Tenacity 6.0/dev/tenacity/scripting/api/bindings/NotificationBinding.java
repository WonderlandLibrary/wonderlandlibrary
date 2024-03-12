// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class NotificationBinding
{
    public NotificationType success;
    public NotificationType disable;
    public NotificationType info;
    public NotificationType warning;
    
    public NotificationBinding() {
        this.success = NotificationType.SUCCESS;
        this.disable = NotificationType.DISABLE;
        this.info = NotificationType.INFO;
        this.warning = NotificationType.WARNING;
    }
    
    public void post(final NotificationType notificationType, final String title, final String description) {
        NotificationManager.post(notificationType, title, description);
    }
    
    public void post(final NotificationType notificationType, final String title, final String description, final long time) {
        NotificationManager.post(notificationType, title, description, (float)time);
    }
}
