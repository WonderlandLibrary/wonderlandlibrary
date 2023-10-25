package ru.smertnix.celestial.ui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.feature.impl.hud.Hud;
import ru.smertnix.celestial.feature.impl.misc.Optimizer;
import ru.smertnix.celestial.feature.impl.visual.Notifications;
import ru.smertnix.celestial.ui.clickgui.ClickGuiScreen;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;
import ru.smertnix.celestial.utils.render.TextureEngine;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.animation.Interpolator;

public final class NotificationRenderer implements Helper {
    private static final List<Notification> NOTIFICATIONS = new CopyOnWriteArrayList<>();
    public static void queue(String title, String content, int second, NotificationMode type) {
        NOTIFICATIONS.add(new Notification(title, content, type, second * 1000, Minecraft.getMinecraft().neverlose500_18));
    }
    
    public static void publish(ScaledResolution sr) {
        if (Celestial.instance.featureManager.getFeature(Notifications.class).isEnabled() && !mc.gameSettings.showDebugInfo && mc.world != null && !(mc.currentScreen instanceof ClickGuiScreen)) {
            if (!NOTIFICATIONS.isEmpty()) {
                int y = sr.getScaledHeight() - 35;
                double better;
                for (Notification notification : NOTIFICATIONS) {
                    better = Minecraft.getMinecraft().neverlose500_18.getStringWidth(notification.getTitle() + " " + notification.getContent());

                    if (!notification.getTimer().hasReached(notification.getTime() / 2)) {
                        notification.notificationTimeBarWidth = 360;
                    } else {
                        notification.notificationTimeBarWidth = MathHelper.EaseOutBack((float) notification.notificationTimeBarWidth, 0, (float) (4 * Celestial.deltaTime()));
                    }

                    if (!notification.getTimer().hasReached(notification.getTime())) {
                        notification.x = sr.getScaledWidth() - 185;
                        notification.y = MathHelper.EaseOutBack((float) notification.y, (float) y, (float) (5 * Celestial.deltaTime()));
                    } else {
                        notification.x = sr.getScaledWidth() - 185;
                        if (notification.anim < 50) {
                        	 NOTIFICATIONS.remove(notification);
                        }
                    }
                    
                    notification.anim = (float)Interpolator.LINEAR.interpolate((double)notification.anim, !notification.getTimer().hasReached(notification.getTime()) ? 200 : 0, 0.08);

                    GlStateManager.pushMatrix();
                    RenderUtils.drawBlurredShadow((float) (notification.x + 18 + (110 - mc.mntsb_16.getStringWidth(notification.getContent()))), (float) (notification.y - 16 - 3), 10 + Minecraft.getMinecraft().mntsb_16.getStringWidth(notification.getContent()) - 7 + 47, 15.0f + 13, 10, new Color(25,25,25,(int) 100));
                    RoundedUtil.drawRound((float) (notification.x + 18 + (110 - mc.mntsb_16.getStringWidth(notification.getContent()))), (float) (notification.y - 16 - 3), 10 +  Minecraft.getMinecraft().mntsb_16.getStringWidth(notification.getContent()) - 7 + 47, 15.0f + 10 + 3, 4, new Color(25,25,25,(int) notification.anim));

                    RenderUtils.drawImage(new ResourceLocation("celestial/images/notification/" + notification.getType().getTitleString() + ".png"), (float) (notification.x + 21 + (110 - mc.mntsb_16.getStringWidth(notification.getContent()))), (float) (notification.y - 13 - 3), 19 + 3, 19 + 3, new Color(255,255,255,55 + (int) notification.anim));
                    Minecraft.getMinecraft().mntsb_18.drawString(notification.getTitle(), (float) (notification.x + 44 + 2 + (110 - mc.mntsb_16.getStringWidth(notification.getContent()))), (float) (notification.y - 13), new Color(230,230,230, 55 + (int) notification.anim).getRGB());
                    Minecraft.getMinecraft().mntsb_16.drawString(TextFormatting.GRAY + notification.getContent(), (float) (notification.x +  41 + 3 + (120 - mc.mntsb_16.getStringWidth(notification.getContent()))) - 7.5f, (float) (notification.y - 2), new Color(255,255,255,55 + (int) notification.anim).getRGB());
                    GlStateManager.popMatrix();
                    y -= 35;
                }
            }
        }
    }
}