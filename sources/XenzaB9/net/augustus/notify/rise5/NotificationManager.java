// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.notify.rise5;

import java.util.ArrayDeque;
import java.awt.Font;
import net.augustus.ui.GuiIngameHook;
import net.augustus.notify.NotificationType;
import java.util.Deque;
import net.augustus.font.UnicodeFontRenderer;

public final class NotificationManager
{
    private static UnicodeFontRenderer riseFontRenderer;
    public static Deque<Notification> notifications;
    
    public void registerNotification(final String description, final long delay, final NotificationType type) {
        NotificationManager.notifications.add(new Notification(description, delay, type));
    }
    
    public void registerNotification(final String description, final NotificationType type) {
        NotificationManager.notifications.add(new Notification(description, NotificationManager.riseFontRenderer.getStringWidth(description) * 30L, type));
    }
    
    public void registerNotification(final String description) {
        NotificationManager.notifications.add(new Notification(description, NotificationManager.riseFontRenderer.getStringWidth(description) * 40L, NotificationType.Info));
    }
    
    static {
        try {
            NotificationManager.riseFontRenderer = new UnicodeFontRenderer(Font.createFont(0, GuiIngameHook.class.getResourceAsStream("/ressources/Light.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        NotificationManager.notifications = new ArrayDeque<Notification>();
    }
}
