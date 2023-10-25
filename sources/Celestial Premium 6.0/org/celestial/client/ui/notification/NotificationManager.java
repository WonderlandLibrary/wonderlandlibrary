/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.notification;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.celestial.client.feature.impl.hud.ClientFont;
import org.celestial.client.feature.impl.hud.Notifications;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.render.ScreenHelper;
import org.celestial.client.helpers.render.rect.RectHelper;
import org.celestial.client.ui.notification.Notification;
import org.celestial.client.ui.notification.NotificationType;

public class NotificationManager
implements Helper {
    private static final List<Notification> notifications = new CopyOnWriteArrayList<Notification>();
    public static float y;

    public static void publicity(String title, String content, int second, NotificationType type) {
        FontRenderer fontRenderer = NotificationManager.mc.fontRendererObj;
        notifications.add(new Notification(title, content, type, second * 1000, fontRenderer));
    }

    public static void renderNotification(ScaledResolution sr) {
        if (Notifications.backGroundMode.currentMode.equals("Blur") && NotificationManager.mc.gameSettings.ofFastRender) {
            NotificationManager.mc.gameSettings.ofFastRender = false;
        }
        if (!notifications.isEmpty()) {
            int srScaledHeight = sr.getScaledHeight();
            y = srScaledHeight - 60;
            int scaledWidth = sr.getScaledWidth();
            for (Notification notification : notifications) {
                if (notification == null) continue;
                ScreenHelper screenHelper = notification.getTranslate();
                int width = NotificationManager.mc.latoFontRender.getStringWidth(notification.getContent()) + (Notifications.timePeriod.getCurrentValue() ? 80 : 60);
                if (!notification.getTimer().hasReached(notification.getTime() - 100)) {
                    screenHelper.calculateCompensation((float)(scaledWidth - width), y, 0.55f, 0.55f);
                } else {
                    screenHelper.calculateCompensation((float)(scaledWidth + 30), notification.getTranslate().getY(), 0.55f, 0.55f);
                    if (NotificationManager.mc.player != null && NotificationManager.mc.world != null && notification.getTimer().getTime() > (long)(notification.getTime() + 500)) {
                        notifications.remove(notification);
                    }
                }
                if (notification.getTimer().getTime() <= (long)notification.getTime()) {
                    y -= 35.0f;
                }
                float translateX = screenHelper.getX();
                float translateY = screenHelper.getY();
                GlStateManager.pushMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
                if (Notifications.backGroundMode.currentMode.equals("Blur")) {
                    RenderHelper.blurAreaBoarder((int)translateX, (int)translateY, scaledWidth, 28.0f, 120.0f, 0.0f, 1.0f);
                } else {
                    RectHelper.drawSmoothRect(translateX, translateY, scaledWidth, translateY + 28.0f, new Color(50, 50, 50, 255).getRGB());
                }
                RenderHelper.renderBlurredShadow(new Color(notification.getType().getColor()).brighter(), (double)((int)translateX), (double)((int)translateY), 25.0, 25.0, 20);
                RenderHelper.renderBlurredShadow(Color.BLACK, (double)((int)translateX), (double)((int)translateY + 2), 25.0, 25.0, 30);
                GlStateManager.pushMatrix();
                GlStateManager.translate(translateX + 24.0f, translateY + 14.0f, 0.0f);
                GlStateManager.scale(3.0f, 3.0f, 3.0f);
                RenderHelper.renderTriangle(notification.getType().getColor());
                GlStateManager.popMatrix();
                RectHelper.drawSmoothRect(translateX, translateY, translateX + 28.0f, translateY + 28.0f, notification.getType().getColor());
                if (notification.getType() == NotificationType.ERROR) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/error.png"), translateX + 2.0f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                } else if (notification.getType() == NotificationType.INFO) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/info.png"), translateX + 2.0f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                } else if (notification.getType() == NotificationType.SUCCESS) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/success.png"), translateX + 2.0f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                } else if (notification.getType() == NotificationType.WARNING) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/warning.png"), translateX + 2.0f, translateY + 2.0f, 24.0f, 24.0f, new Color(0, 0, 0, 100));
                }
                if (notification.getType() == NotificationType.ERROR) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/error.png"), translateX + 2.0f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                } else if (notification.getType() == NotificationType.INFO) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/info.png"), translateX + 2.0f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                } else if (notification.getType() == NotificationType.SUCCESS) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/success.png"), translateX + 2.0f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                } else if (notification.getType() == NotificationType.WARNING) {
                    RenderHelper.drawImage(new ResourceLocation("celestial/notification/warning.png"), translateX + 2.0f, translateY + 2.0f, 23.0f, 23.0f, Color.WHITE);
                }
                String time = " (" + MathematicHelper.round((float)((int)((long)notification.getTime() - notification.getTimer().getTime())) / 1000.0f, 1) + "s)";
                if (!ClientFont.minecraftFont.getCurrentValue()) {
                    NotificationManager.mc.latoBig.drawStringWithShadow((Object)((Object)TextFormatting.BOLD) + notification.getTitle(), translateX + 40.0f, translateY + 3.0f, notification.getType().getColor());
                    NotificationManager.mc.latoFontRender.drawStringWithShadow(notification.getContent() + (Notifications.timePeriod.getCurrentValue() ? time : ""), translateX + 40.0f, translateY + 17.0f, new Color(245, 245, 245).getRGB());
                } else {
                    NotificationManager.mc.fontRendererObj.drawStringWithShadow((Object)((Object)TextFormatting.BOLD) + notification.getTitle(), translateX + 40.0f, translateY + 3.0f, notification.getType().getColor());
                    NotificationManager.mc.fontRendererObj.drawStringWithShadow(notification.getContent() + (Notifications.timePeriod.getCurrentValue() ? time : ""), translateX + 40.0f, translateY + 15.0f, new Color(245, 245, 245).getRGB());
                }
                RenderHelper.renderBlurredShadow(new Color(notification.getType().getColor()), (double)((int)translateX + 40), (double)((int)translateY + 6), ClientFont.minecraftFont.getCurrentValue() ? (double)(NotificationManager.mc.fontRendererObj.getStringWidth(notification.getTitle()) + 6) : (double)(NotificationManager.mc.latoBig.getStringWidth(notification.getTitle()) + 2), 5.0, 20);
                GlStateManager.popMatrix();
            }
        }
    }
}

