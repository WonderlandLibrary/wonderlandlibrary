// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.ui.notification;

import java.util.concurrent.CopyOnWriteArrayList;
import ru.fluger.client.helpers.render.ScreenHelper;
import java.util.Iterator;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import ru.fluger.client.feature.impl.hud.Notifications;
import java.util.List;
import ru.fluger.client.helpers.Helper;

public class NotificationManager implements Helper
{
    private static final List<Notification> notifications;
    public static float y;
    
    public static void publicity(final String title, final String content, final int second, final NotificationType type) {
        final bip fontRenderer = NotificationManager.mc.k;
        NotificationManager.notifications.add(new Notification(title, content, type, second * 1000, fontRenderer));
    }
    
    public static void renderNotification(final bit sr) {
        if (!NotificationManager.notifications.isEmpty()) {
            final int srScaledHeight = sr.b();
            NotificationManager.y = (float)(srScaledHeight - 60);
            final int scaledWidth = sr.a();
            for (final Notification notification : NotificationManager.notifications) {
                if (notification == null) {
                    continue;
                }
                final ScreenHelper screenHelper = notification.getTranslate();
                final int width = NotificationManager.mc.latoFontRender.getStringWidth(notification.getContent()) + (Notifications.timePeriod.getCurrentValue() ? 80 : 60);
                if (!notification.getTimer().hasReached(notification.getTime() - 100)) {
                    screenHelper.calculateCompensation((float)(scaledWidth - width), NotificationManager.y, 0.3f, 0.3f);
                }
                else {
                    screenHelper.calculateCompensation((float)(scaledWidth + 30), notification.getTranslate().getY(), 0.3f, 0.3f);
                    if (NotificationManager.mc.h != null && NotificationManager.mc.f != null && notification.getTimer().getTime() > notification.getTime() + 500) {
                        NotificationManager.notifications.remove(notification);
                    }
                }
                if (notification.getTimer().getTime() <= notification.getTime()) {
                    NotificationManager.y -= 35.0f;
                }
                final float translateX = screenHelper.getX();
                final float translateY = screenHelper.getY();
                bus.G();
                bus.E();
                bus.l();
                bus.g();
                bus.j();
                bhz.a();
                RectHelper.drawGradientRect(translateX, translateY, scaledWidth, translateY + 28.0f, RenderHelper.injectAlpha(new Color(notification.getType().getColor()).darker(), 120).getRGB(), RenderHelper.injectAlpha(new Color(notification.getType().getColor()).darker().darker().darker(), 120).getRGB());
                RenderHelper.renderBlurredShadow(new Color(notification.getType().getColor()).brighter(), (int)translateX, (int)translateY, 25.0, 25.0, 20);
                RenderHelper.renderBlurredShadow(Color.BLACK, (int)translateX, (int)translateY + 2, 25.0, 25.0, 30);
                RectHelper.drawSmoothRect(translateX, translateY, translateX + 30.0f, translateY + 28.0f, notification.getType().getColor());
                if (notification.getType() == NotificationType.ERROR) {
                    RenderHelper.drawImage(new nf("nightmare/notification/error.png"), translateX + 3.5f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                }
                else if (notification.getType() == NotificationType.INFO) {
                    RenderHelper.drawImage(new nf("nightmare/notification/info.png"), translateX + 3.0f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                }
                else if (notification.getType() == NotificationType.SUCCESS) {
                    RenderHelper.drawImage(new nf("nightmare/notification/success.png"), translateX + 3.5f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                }
                else if (notification.getType() == NotificationType.WARNING) {
                    RenderHelper.drawImage(new nf("nightmare/notification/warning.png"), translateX + 3.5f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                }
                if (notification.getType() == NotificationType.ERROR) {
                    RenderHelper.drawImage(new nf("nightmare/notification/error.png"), translateX + 3.5f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                }
                else if (notification.getType() == NotificationType.INFO) {
                    RenderHelper.drawImage(new nf("nightmare/notification/info.png"), translateX + 3.5f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                }
                else if (notification.getType() == NotificationType.SUCCESS) {
                    RenderHelper.drawImage(new nf("nightmare/notification/success.png"), translateX + 3.5f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                }
                else if (notification.getType() == NotificationType.WARNING) {
                    RenderHelper.drawImage(new nf("nightmare/notification/warning.png"), translateX + 3.5f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                }
                final String time = " (" + MathematicHelper.round((int)(notification.getTime() - notification.getTimer().getTime()) / 1000.0f, 1) + "s)";
                NotificationManager.mc.rubik_25.drawStringWithShadow(a.r + notification.getTitle(), translateX + 40.0f, translateY + 3.0f, notification.getType().getColor());
                NotificationManager.mc.rubik_18.drawStringWithShadow(notification.getContent() + (Notifications.timePeriod.getCurrentValue() ? time : ""), translateX + 40.0f, translateY + 17.0f, new Color(245, 245, 245).getRGB());
                RenderHelper.renderBlurredShadow(new Color(notification.getType().getColor()), (int)translateX + 40, (int)translateY + 6, NotificationManager.mc.latoBig.getStringWidth(notification.getTitle()) + 2, 5.0, 20);
                bus.H();
            }
        }
    }
    
    static {
        notifications = new CopyOnWriteArrayList<Notification>();
    }
}
