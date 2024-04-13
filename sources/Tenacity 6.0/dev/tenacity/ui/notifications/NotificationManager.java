package dev.tenacity.ui.notifications;

import dev.tenacity.util.animations.Animation;
import dev.tenacity.util.animations.Direction;
import dev.tenacity.util.animations.impl.DecelerateAnimation;
import dev.tenacity.util.font.FontUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    private final float spacing = 10;
    private final float widthSpacing = 25;
    private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();
    Animation downAnimation = null;

    public void drawNotifications(ScaledResolution sr) {
        int count = 0;

        for (Notification notification : notifications) {
            if (notification.timerUtil.hasTimeElapsed((long) notification.getMaxTime(), false)) {
                if (notification.getAnimation() != null) {
                    if (notification.getAnimation().isDone()) {
                        notifications.remove(notification);
                        downAnimation = new DecelerateAnimation(225, 1, Direction.FORWARDS);
                        continue;
                    }
                } else vanish(notification);
            } else {
                if (notification.getAnimation() != null) {
                    if (notification.getAnimation().isDone()) notification.stopAnimation();
                }
            }

            float notifWidth = notification.getWidth() + widthSpacing;
            float notifX = sr.getScaledWidth() - (notifWidth + 5);
            if (count == 0) notification.notificationY = sr.getScaledHeight();
            notification.notificationY = notifications.get(Math.max(count - 1, 0)).notificationY - spacing - notification.getHeight();

            if (notification.isAnimating()) notifX += notifWidth * notification.getAnimation().getOutput();

            if (downAnimation != null) {
                if (downAnimation.isDone()) {
                    downAnimation = null;
                    return;
                }

                float newY = sr.getScaledHeight() - (spacing + notification.getHeight()) * (count + 2);
                notification.notificationY = (float) (newY + ((notification.getHeight() + spacing) * downAnimation.getOutput()));
            }

            notificationDraw(notifX, notification.notificationY, notifWidth, notification.getHeight(), notification);

            count++;
        }
    }

    public void notificationDraw(float x, float y, float width, float height, Notification notification) {
        int color = -1;
        String iconText = "";
        float yOffset = 8;
        float xOffset = 5;
        switch (notification.getNotificationType()) {
            case SUCCESS:
                color = new Color(20, 250, 90).getRGB();
                iconText = FontUtil.CHECKMARK;
                break;
            case WARNING:
                color = new Color(255, 255, 0).getRGB();
                iconText = FontUtil.WARNING;
                break;
            case DISABLE:
                color = new Color(255, 30, 30).getRGB();
                iconText = FontUtil.XMARK;
                yOffset = 9;
                break;
            case INFO:
                color = new Color(255, 255, 255).getRGB();
                iconText = FontUtil.INFO;
                xOffset = 7;
                break;
        }

        Color baseColor = new Color(20, 20, 20, 110);
       // Color colorr = ColorUtil.interpolateColorC(baseColor, new Color(ColorUtil.applyOpacity(color, .3f)), NotificationsMod.colorInterpolation.getCurentValue());

     //   RoundedUtil.drawRound(x, y, width, height, 4, colorr);

        notification.titleFont.drawString(notification.getTitle(), x + 28, y + 3, -1);
        notification.iconFont.drawString(iconText, x + xOffset, y + yOffset, color);
        notification.descriptionFont.drawString(notification.getDescription(), x + 28, y + 16, -1);
    }

    public void blurNotifs(ScaledResolution sr) {
        int count = 0;
        for (Notification notification : notifications) {
            float notifWidth = notification.getWidth() + widthSpacing;
            float notifX = sr.getScaledWidth() - (notifWidth + 5);
            if (count == 0) notification.notificationY = sr.getScaledHeight(); //- Watermark.y;
            notification.notificationY = notifications.get(Math.max(count - 1, 0)).notificationY - spacing - notification.getHeight();

            if (notification.isAnimating()) notifX += notifWidth * notification.getAnimation().getOutput();

            if (downAnimation != null) {
                if (downAnimation.isDone()) {
                    downAnimation = null;
                    return;
                }
                float newY = sr.getScaledHeight() - (/*Watermark.y +*/ ((spacing + notification.getHeight()) * (count + 2)));
                notification.notificationY = (float) (newY + ((notification.getHeight() + spacing) * downAnimation.getOutput()));
            }

            Color baseColor = new Color(50, 50, 44, 80);
      //      Color colorr = ColorUtil.interpolateColorC(baseColor, new Color(ColorUtil.applyOpacity(-1, .3f)),  NotificationsMod.colorInterpolation.getValue().floatValue());

      //      RoundedUtil.drawRound(notifX, notification.notificationY, notifWidth, notification.getHeight(), 4.75f, true, colorr);
            count++;
        }
    }



    public static void post(NotificationType type, String title, String description) {
        post(new Notification(type, title, description));
    }

    public static void post(NotificationType type, String title, String description, float time) {
        post(new Notification(type, title, description, time));
    }

    private static void post(Notification notification) {
        notifications.add(notification);
        notification.startAnimation(new DecelerateAnimation(225, 1, Direction.BACKWARDS));
    }

    public static void vanish(Notification notification) {
        notification.startAnimation(new DecelerateAnimation(225, 1, Direction.FORWARDS));
    }


}
