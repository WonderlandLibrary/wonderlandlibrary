package org.dreamcore.client.ui.notification;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.feature.impl.hud.HUD;
import org.dreamcore.client.feature.impl.hud.Notifications;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.misc.ClientHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;
import org.dreamcore.client.helpers.render.rect.RectHelper;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager implements Helper {

    private static final List<Notification> notifications = new CopyOnWriteArrayList<>();

    public static void publicity(String title, String content, int second, NotificationType type) {
        FontRenderer fontRenderer = mc.fontRendererObj;
        notifications.add(new Notification(title, content, type, second * 1000, fontRenderer));
    }

    public static void renderNotification(ScaledResolution sr) {
        if (dreamcore.instance.featureManager.getFeatureByClass(Notifications.class).getState()) {
            if (!notifications.isEmpty()) {
                int srScaledHeight = sr.getScaledHeight();
                int scaledWidth = sr.getScaledWidth() + 10;
                int y = srScaledHeight - 60;
                for (Notification notification : notifications) {
                    ScreenHelper screenHelper = notification.getTranslate();
                    int width = notification.getWidth() + 40 + mc.fontRendererObj.getStringWidth(notification.getContent()) / 2;
                    if (!notification.getTimer().hasReached(notification.getTime() - 100)) {
                        screenHelper.calculateCompensation(scaledWidth - width, (float) y, 0.12F, 5F);
                    } else {
                        screenHelper.calculateCompensation(scaledWidth, notification.getTranslate().getY(), 0.12F, 5F);
                        if (mc.player != null && mc.world != null) {
                            if (notification.getTimer().getTime() > notification.getTime() + 500) {
                                notifications.remove(notification);
                            }
                        }
                    }
                    float translateX = screenHelper.getX();
                    float translateY = screenHelper.getY();
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    RectHelper.drawRect(translateX, translateY, translateX - 2, (translateY + 28), ClientHelper.getClientColor().getRGB());
                    RectHelper.drawRect(translateX, translateY, scaledWidth, (translateY + 28), new Color(35, 34, 34).getRGB());

                    //    String time = " (" + MathematicHelper.round((int) (notification.getTime() - notification.getTimer().getTime()) / 1000F, 1) + "s)";
                    if (!HUD.font.currentMode.equals("Minecraft")) {
                        mc.latoBig.drawStringWithShadow(TextFormatting.BOLD + notification.getTitle(), translateX + 5, translateY + 4, -1);
                        mc.latoFontRender.drawStringWithShadow(notification.getContent(), translateX + 5, translateY + 17, new Color(245, 245, 245).getRGB());
                    } else {
                        mc.fontRendererObj.drawStringWithShadow(TextFormatting.BOLD + notification.getTitle(), translateX + 5, translateY + 4, -1);
                        mc.fontRendererObj.drawStringWithShadow(notification.getContent(), translateX + 5, translateY + 15, new Color(245, 245, 245).getRGB());
                    }

                    GlStateManager.popMatrix();

                    if (notifications.size() > 1) {
                        y -= 35;
                    }
                }
            }
        }
    }
}