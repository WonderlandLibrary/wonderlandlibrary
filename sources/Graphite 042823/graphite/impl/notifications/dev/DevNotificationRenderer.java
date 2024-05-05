package graphite.impl.notifications.dev;

import java.awt.Color;
import java.util.List;

import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;

public class DevNotificationRenderer implements IDevNotificationRenderer {
    @Override
    public void draw(List<DevNotification> notifications) {
        final TTFFontRenderer font = Graphite.INSTANCE.getFontManager().getFontRenderer("TAHOMA_9");

        float y = 3;
        for (DevNotification notification : notifications) {
            notification.opacity.interpolate(notification.targetOpacity, 20);
            notification.translate.interpolate(60.0F, y, 0.35F);
            font.drawStringWithShadow(notification.getMessage(), 60.0F, notification.translate.getY(), new Color(255, 255, 255, (int) notification.opacity.getOpacity()).getRGB());
            y += 5;
            if (notification.checkTime() >= notification.getDisplayTime() + notification.getInitializeTime()) {
                notification.targetOpacity = 0;
                if (notification.opacity.getOpacity() <= 0.0F) {
                    notifications.remove(notification);
                }
            }
        }
    }
}