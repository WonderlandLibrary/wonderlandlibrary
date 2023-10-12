package wtf.diablo.gui.notifications;

import net.minecraft.client.gui.ScaledResolution;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {

    public CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public void render(ScaledResolution sr){
        int count = 0;
        for(Notification notification : notifications){
            if(notification.startTime + notification.duration < System.currentTimeMillis()){
                removeNotification(notification);
                return;
            }
            notification.drawNotification(count, sr);
            if(!notification.hiding){
                count++;
            }
        }
    }

    public void removeNotification(Notification notification){
        notifications.remove(notification);
    }

}
