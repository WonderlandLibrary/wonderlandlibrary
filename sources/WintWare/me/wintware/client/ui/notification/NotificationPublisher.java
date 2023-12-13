/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.wintware.client.ui.notification;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.wintware.client.Main;
import me.wintware.client.ui.notification.Notification;
import me.wintware.client.ui.notification.NotificationType;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.animation.Translate;
import me.wintware.client.utils.font.FontRenderer;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public final class NotificationPublisher {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList<Notification>();

    public static void publish(ScaledResolution sr) {
        int srScaledHeight = sr.getScaledHeight();
        int scaledWidth = sr.getScaledWidth();
        int y = srScaledHeight - 50;
        for (Notification notification : NOTIFICATIONS) {
            Translate translate = notification.getTranslate();
            int width = notification.getWidth();
            if (!notification.getTimer().elapsed(notification.getTime())) {
                notification.scissorBoxWidth = AnimationUtil.animate(width, notification.scissorBoxWidth, 0.05 * Minecraft.frameTime / 2.0);
                translate.interpolate(scaledWidth - width, y, 0.03);
            } else {
                notification.scissorBoxWidth = AnimationUtil.animate(0.0, notification.scissorBoxWidth, 0.05 * Minecraft.frameTime / 4.0);
                if (notification.scissorBoxWidth < 1.0) {
                    NOTIFICATIONS.remove(notification);
                }
                y += 30;
            }
            float translateX = (float)translate.getX();
            float translateY = (float)translate.getY();
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            NotificationPublisher.prepareScissorBox((float)((double)scaledWidth - notification.scissorBoxWidth), translateY, scaledWidth, translateY + 30.0f);
            RenderUtil.drawNewRect(translateX, translateY, scaledWidth, translateY + 28.0f, new Color(10, 10, 10, 180).getRGB());
            RenderUtil.drawNewRect(translateX, translateY + 28.0f - 2.0f, scaledWidth, translateY + 28.0f, new Color(10, 10, 10, 180).getRGB());
            RenderUtil.drawNewRect(translateX, translateY + 28.0f - 2.0f, translateX + (float)((long)width * ((long)notification.getTime() - notification.getTimer().getElapsedTime()) / (long)notification.getTime()), translateY + 28.0f, Main.getClientColor().getRGB());
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(notification.getTitle(), translateX + 4.0f, translateY + 4.0f, -1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(notification.getContent(), translateX + 4.0f, translateY + 16.0f, -1);
            GL11.glDisable(3089);
            GL11.glPopMatrix();
            y -= 33;
            y -= 10;
        }
    }

    public static void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = ScaledResolution.getScaleFactor();
        GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
    }

    public static void queue(String title, String content, NotificationType type) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = mc.fontRenderer;
        NOTIFICATIONS.add(new Notification(title, content, type, fr));
    }
}

