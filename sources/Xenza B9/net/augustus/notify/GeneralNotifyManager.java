// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.notify;

import net.augustus.Augustus;
import net.augustus.notify.xenza.NotificationsManager;
import net.augustus.notify.xenza.Notification;
import net.augustus.utils.interfaces.MM;

public class GeneralNotifyManager implements MM
{
    public static void addNotification(final String content, final NotificationType type) {
        final String selected = GeneralNotifyManager.mm.notifications.mode.getSelected();
        switch (selected) {
            case "Xenza": {
                NotificationsManager.addNotification(new Notification(content, type));
                break;
            }
            case "Rise5": {
                Augustus.getInstance().getRise5notifyManager().registerNotification(content, type);
                break;
            }
        }
    }
}
