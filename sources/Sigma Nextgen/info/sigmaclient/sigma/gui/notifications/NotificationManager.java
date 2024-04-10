package info.sigmaclient.sigma.gui.notifications;


import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.gui.Notifications;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.sigmaclient.sigma.minimap.minimap.Minimap.mc;
public class NotificationManager {

    private static final List<Notification> notifications = new CopyOnWriteArrayList<>();


    public static void publicity(String title, String content, int second, Notification.Type type) {
        if(!SigmaNG.getSigmaNG().moduleManager.getModule(Notifications.class).enabled) return;
        notifications.add(new Notification(title, content, type, second));
    }

    public static void onRender2D() {
        if (notifications.size() > 8)
            notifications.remove(0);
        float startY = new ScaledResolution(mc).getScaledHeight() - 36f;
        for (int i = 0; i < notifications.size(); i++) {
            Notification notification = notifications.get(i);
            notifications.removeIf(Notification::shouldDelete);
            notification.render(startY);
            startY -= (float) (notification.getHeight() + 5);
        }
    }
}