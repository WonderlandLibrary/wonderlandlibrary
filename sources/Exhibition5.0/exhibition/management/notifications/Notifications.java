// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.notifications;

import java.util.ArrayList;
import java.util.List;

public final class Notifications
{
    private static Notifications instance;
    private List<INotification> notifications;
    private NotificationRenderer renderer;
    
    private Notifications() {
        this.notifications = new ArrayList<INotification>();
        this.renderer = new NotificationRenderer();
        Notifications.instance = this;
    }
    
    public static Notifications getManager() {
        return Notifications.instance;
    }
    
    public void post(final String header, final String subtext) {
        this.post(header, subtext, 2500L);
    }
    
    public void post(final String header, final String subtext, final Type type) {
        this.post(header, subtext, 156L, 2500L, type);
    }
    
    public void post(final String header, final String subtext, final long displayTime) {
        this.post(header, subtext, (long)(displayTime / 16.0f), displayTime, Type.INFO);
    }
    
    public void post(final String header, final String subtext, final long fade, final long displayTime, final Type type) {
        this.notifications.add(new Notification(header, subtext, fade, displayTime, type));
    }
    
    public void updateAndRender() {
        if (this.notifications.isEmpty()) {
            return;
        }
        this.renderer.draw(this.notifications);
    }
    
    static {
        Notifications.instance = new Notifications();
    }
    
    public enum Type
    {
        NOTIFY, 
        WARNING, 
        INFO;
    }
}
