// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.notify.xenza;

import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;

public class NotificationsManager
{
    public static ArrayList<Notification> notifications;
    
    public static ArrayList<Notification> getNotifications() {
        return NotificationsManager.notifications;
    }
    
    public static void addNotification(final Notification n) {
        NotificationsManager.notifications.add(n);
    }
    
    public static void renderNotifications() {
        final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float y = (float)(sr.getScaledHeight() - 30);
        final Iterator<Notification> it = NotificationsManager.notifications.iterator();
        while (it.hasNext()) {
            it.next().render(y);
            y -= 25.0f;
        }
    }
    
    public static void update() {
        final ArrayList<Notification> temp = new ArrayList<Notification>();
        for (final Notification no : NotificationsManager.notifications) {
            temp.add(no);
            no.update();
            if (no.timer != null && no.timer.reached((long)(no.lastTime * 1000.0f))) {
                no.setBack = true;
                if (no.x > 0.1f) {
                    continue;
                }
                temp.remove(no);
            }
        }
        NotificationsManager.notifications.clear();
        for (final Notification no : temp) {
            NotificationsManager.notifications.add(no);
        }
    }
    
    static {
        NotificationsManager.notifications = new ArrayList<Notification>();
    }
}
