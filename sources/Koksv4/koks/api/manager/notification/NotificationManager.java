package koks.api.manager.notification;

import koks.api.Methods;
import koks.api.font.Fonts;
import koks.api.utils.RenderUtil;
import koks.api.utils.Resolution;
import lombok.Getter;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements Methods {
    private static NotificationManager instance;

    private final List<Notification> notificationList = new ArrayList<>();

    public void drawNotifications(Design design) {
        final Resolution resolution = Resolution.getResolution();
        final RenderUtil renderUtil = RenderUtil.getInstance();
        final int notificationsBottom = resolution.getHeight() - 20;
        final int notificationsRight = resolution.getWidth() - 10;
        switch (design) {
            default:
                final int notificationHeight = 35;
                for (int i = 0; i < notificationList.size(); i++) {
                    final Notification notification = notificationList.get(i);
                    final float notificationWidth = Math.max(Fonts.arial25.getStringWidth(notification.getTitle()), Fonts.arial18.getStringWidth(notification.getFurtherInformation())) + 10;
                    float notificationX = notificationsRight - (notificationWidth + 10) * Math.min(1, (System.currentTimeMillis() - notification.existsSince) / 500f);
                    final float notificationY = notificationsBottom - (notificationHeight + 5) * (i + 1);
                    if (notification.existsSince + notification.deletedAfter <= System.currentTimeMillis()) {
                        notificationX = notificationsRight - notificationWidth + (notificationWidth + 10) * (System.currentTimeMillis() - notification.existsSince - notification.deletedAfter) / 500f;
                    }
                    renderUtil.drawRect(notificationX, notificationY, notificationX + notificationWidth, notificationY + notificationHeight, new Color(25, 25, 25, 255).getRGB());
                    if (notification.existsSince + notification.deletedAfter > System.currentTimeMillis()) {
                        for (float j = 0; j < 50; j++) {
                            final float timeLineX = notificationX + 1 + ((notificationWidth - 2) / 50f) * j;
                            if (notificationX + notificationWidth * (System.currentTimeMillis() - notification.existsSince) / (float) notification.deletedAfter < timeLineX) {
                                renderUtil.drawRect(timeLineX, notificationY + notificationHeight - 3, timeLineX + (notificationWidth - 2) / 50f, notificationY + notificationHeight - 1, getRainbow((int) (10 * (50 - j)), 2000, 0.6f, 1).getRGB());
                            }
                        }
                    }
                    notification.progress = MathHelper.clamp_float(notification.progress + (0.005F * (notification.backwards ? -1 : 1)), 0, 1);
                    if(notification.progress == 1)
                        notification.backwards = true;
                    else if(notification.progress == 0)
                        notification.backwards = false;
                    Fonts.arial25.drawString(notification.title, notificationX + 5, notificationY + 5, notification.type == NotificationType.WARNING ? new Color(fadeBetween(Color.white.getRGB(), Color.red.getRGB(), notification.progress)) : Color.white, false);
                    Fonts.arial18.drawString(notification.furtherInformation, notificationX + 5, notificationY + 3 + Fonts.arial25.getStringHeight(notification.title), Color.white, false);
                    if (notificationX > resolution.getWidth()) {
                        notificationList.remove(notification);
                    }
                }
                //notificationList.clear();
                break;
        }
    }

    public void addNotification(String notificationTitle, String notificationText, NotificationType type, long timeToDisplayInMS) {
        notificationList.add(new Notification(type, notificationTitle, notificationText, timeToDisplayInMS));
    }

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    @Getter
    private class Notification {
        private final long existsSince, deletedAfter;
        private final NotificationType type;
        private final String title, furtherInformation;

        private float progress = 0;
        private boolean backwards = false;

        public Notification(NotificationType type, String title, String furtherInformation, long timeToExist) {
            this.type = type;
            this.title = title;
            this.furtherInformation = furtherInformation;
            existsSince = System.currentTimeMillis();
            deletedAfter = timeToExist;
        }
    }
}
