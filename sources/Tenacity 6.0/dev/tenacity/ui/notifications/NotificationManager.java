// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.notifications;

import dev.tenacity.module.Module;
import dev.tenacity.module.impl.render.NotificationsMod;
import dev.tenacity.Tenacity;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager
{
    private static float toggleTime;
    private static final CopyOnWriteArrayList<Notification> notifications;
    
    public static void post(final NotificationType type, final String title, final String description) {
        post(new Notification(type, title, description));
    }
    
    public static void post(final NotificationType type, final String title, final String description, final float time) {
        post(new Notification(type, title, description, time));
    }
    
    private static void post(final Notification notification) {
        if (Tenacity.INSTANCE.isEnabled(NotificationsMod.class)) {
            NotificationManager.notifications.add(notification);
        }
    }
    
    public static float getToggleTime() {
        return NotificationManager.toggleTime;
    }
    
    public static void setToggleTime(final float toggleTime) {
        NotificationManager.toggleTime = toggleTime;
    }
    
    public static CopyOnWriteArrayList<Notification> getNotifications() {
        return NotificationManager.notifications;
    }
    
    static {
        NotificationManager.toggleTime = 2.0f;
        notifications = new CopyOnWriteArrayList<Notification>();
    }
}
