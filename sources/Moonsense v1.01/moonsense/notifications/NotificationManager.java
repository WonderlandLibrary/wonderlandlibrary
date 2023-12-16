// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.notifications;

import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager
{
    public CopyOnWriteArrayList<Notification> notifications;
    public CopyOnWriteArrayList<Notification> notificationQueue;
    public int defaultTargetX;
    public int defaultTargetY;
    public int defaultStartingX;
    public int defaultStartingY;
    public int defaultSpeed;
    public static NotificationManager notificationManager;
    
    static {
        NotificationManager.notificationManager = null;
    }
    
    public NotificationManager() {
        this.notifications = new CopyOnWriteArrayList<Notification>();
        this.notificationQueue = new CopyOnWriteArrayList<Notification>();
        this.defaultTargetX = 0;
        this.defaultTargetY = 0;
        this.defaultStartingX = 0;
        this.defaultStartingY = 0;
        this.defaultSpeed = 4;
    }
    
    public Notification createNotification(final String title, final String text, final boolean showTimer, final long timeOnScreen, final NotificationType type, final NotificationColor color) {
        final Notification n = new Notification(title, text, showTimer, timeOnScreen, type, color, this.defaultTargetX, this.defaultTargetY, this.defaultStartingX, this.defaultStartingY, this.defaultSpeed);
        n.setDefaultY = true;
        this.notificationQueue.add(n);
        return n;
    }
    
    public void createNotification(final Notification notification) {
        this.notificationQueue.add(notification);
    }
    
    public void onRender() {
        final Minecraft mc = Minecraft.getMinecraft();
        final FontRenderer fr = mc.fontRendererObj;
        final ScaledResolution sr = new ScaledResolution(mc);
        for (final Notification n : this.notificationQueue) {
            n.targetY = (float)(sr.getScaledHeight() - 54 - 54 * this.notifications.size());
            this.notifications.add(n);
            this.notificationQueue.remove(n);
        }
        this.defaultStartingX = sr.getScaledWidth();
        this.defaultStartingY = sr.getScaledHeight();
        this.defaultTargetX = sr.getScaledWidth() - 180;
        this.defaultTargetY = 0;
        this.defaultTargetY = sr.getScaledHeight() - 54 - 54 * this.notifications.size();
        for (final Notification not : this.notifications) {
            try {
                not.onRender(this.notifications.indexOf(not));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static NotificationManager getNotificationManager() {
        if (NotificationManager.notificationManager == null) {
            NotificationManager.notificationManager = new NotificationManager();
        }
        return NotificationManager.notificationManager;
    }
    
    public double getDefaultTargetX() {
        return this.defaultTargetX;
    }
    
    public void setDefaultTargetX(final int defaultTargetX) {
        this.defaultTargetX = defaultTargetX;
    }
    
    public double getDefaultTargetY() {
        return this.defaultTargetY;
    }
    
    public void setDefaultTargetY(final int defaultTargetY) {
        this.defaultTargetY = defaultTargetY;
    }
    
    public double getDefaultStartingX() {
        return this.defaultStartingX;
    }
    
    public void setDefaultStartingX(final int defaultStartingX) {
        this.defaultStartingX = defaultStartingX;
    }
    
    public double getDefaultStartingY() {
        return this.defaultStartingY;
    }
    
    public void setDefaultStartingY(final int defaultStartingY) {
        this.defaultStartingY = defaultStartingY;
    }
    
    public double getDefaultSpeed() {
        return this.defaultSpeed;
    }
    
    public void setDefaultSpeed(final int defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }
}
