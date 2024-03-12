// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.animations.Animation;
import java.util.Iterator;
import net.minecraft.client.gui.GuiChat;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.ui.notifications.Notification;
import dev.tenacity.ui.notifications.NotificationManager;
import net.minecraft.client.gui.ScaledResolution;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.Module;

public class NotificationsMod extends Module
{
    private final NumberSetting time;
    public static final ModeSetting mode;
    public static final BooleanSetting onlyTitle;
    public static final BooleanSetting toggleNotifications;
    
    public NotificationsMod() {
        super("Notifications", "Notifications", Category.RENDER, "Allows you to customize the client notifications");
        this.time = new NumberSetting("Time on Screen", 2.0, 10.0, 1.0, 0.5);
        NotificationsMod.onlyTitle.addParent(NotificationsMod.mode, modeSetting -> modeSetting.is("Default"));
        this.addSettings(this.time, NotificationsMod.mode, NotificationsMod.onlyTitle, NotificationsMod.toggleNotifications);
        if (!this.enabled) {
            this.toggleSilent();
        }
    }
    
    public void render() {
        float yOffset = 0.0f;
        int notificationHeight = 0;
        int actualOffset = 0;
        final ScaledResolution sr = new ScaledResolution(NotificationsMod.mc);
        NotificationManager.setToggleTime(this.time.getValue().floatValue());
        for (final Notification notification : NotificationManager.getNotifications()) {
            final Animation animation = notification.getAnimation();
            animation.setDirection(notification.getTimerUtil().hasTimeElapsed((long)notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);
            if (animation.finished(Direction.BACKWARDS)) {
                NotificationManager.getNotifications().remove(notification);
            }
            else {
                final String mode = NotificationsMod.mode.getMode();
                switch (mode) {
                    case "Default": {
                        animation.setDuration(250);
                        actualOffset = 8;
                        int notificationWidth;
                        if (NotificationsMod.onlyTitle.isEnabled()) {
                            notificationHeight = 19;
                            notificationWidth = (int)NotificationsMod.tenacityBoldFont22.getStringWidth(notification.getTitle()) + 35;
                        }
                        else {
                            notificationHeight = 28;
                            notificationWidth = (int)Math.max(NotificationsMod.tenacityBoldFont22.getStringWidth(notification.getTitle()), NotificationsMod.tenacityFont18.getStringWidth(notification.getDescription())) + 35;
                        }
                        final float x = sr.getScaledWidth() - (notificationWidth + 5) * animation.getOutput().floatValue();
                        final float y = sr.getScaledHeight() - (yOffset + 18.0f + HUDMod.offsetValue + notificationHeight + 15.0f * GuiChat.openingAnimation.getOutput().floatValue());
                        notification.drawDefault(x, y, (float)notificationWidth, (float)notificationHeight, animation.getOutput().floatValue(), NotificationsMod.onlyTitle.isEnabled());
                        break;
                    }
                    case "SuicideX": {
                        animation.setDuration(200);
                        actualOffset = 3;
                        notificationHeight = 16;
                        final String editTitle = notification.getTitle() + ((notification.getTitle().endsWith(".") || notification.getTitle().endsWith("/")) ? " " : ". ") + notification.getDescription();
                        final int notificationWidth = (int)NotificationsMod.tenacityBoldFont22.getStringWidth(editTitle) + 5;
                        final float x = (float)(sr.getScaledWidth() - (notificationWidth + 5));
                        final float y = sr.getScaledHeight() - (yOffset + 18.0f + HUDMod.offsetValue + notificationHeight + 15.0f * GuiChat.openingAnimation.getOutput().floatValue());
                        notification.drawSuicideX(x, y, (float)notificationWidth, (float)notificationHeight, animation.getOutput().floatValue());
                        break;
                    }
                    case "Exhibition": {
                        animation.setDuration(125);
                        actualOffset = 3;
                        notificationHeight = 25;
                        final int notificationWidth = (int)Math.max(NotificationsMod.tahomaFont.size(18).getStringWidth(notification.getTitle()), NotificationsMod.tahomaFont.size(14).getStringWidth(notification.getDescription())) + 30;
                        final float x = sr.getScaledWidth() - (sr.getScaledWidth() / 2.0f + notificationWidth / 2.0f) * animation.getOutput().floatValue();
                        final float y = sr.getScaledHeight() / 2.0f - notificationHeight / 2.0f + 40.0f + yOffset;
                        notification.drawExhi(x, y, (float)notificationWidth, (float)notificationHeight);
                        break;
                    }
                }
                yOffset += (notificationHeight + actualOffset) * animation.getOutput().floatValue();
            }
        }
    }
    
    public void renderEffects(final boolean glow) {
        float yOffset = 0.0f;
        int notificationHeight = 0;
        int actualOffset = 0;
        final ScaledResolution sr = new ScaledResolution(NotificationsMod.mc);
        for (final Notification notification : NotificationManager.getNotifications()) {
            final Animation animation = notification.getAnimation();
            animation.setDirection(notification.getTimerUtil().hasTimeElapsed((long)notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);
            if (animation.finished(Direction.BACKWARDS)) {
                NotificationManager.getNotifications().remove(notification);
            }
            else {
                final String mode = NotificationsMod.mode.getMode();
                switch (mode) {
                    case "Default": {
                        actualOffset = 8;
                        int notificationWidth;
                        if (NotificationsMod.onlyTitle.isEnabled()) {
                            notificationHeight = 19;
                            notificationWidth = (int)NotificationsMod.tenacityBoldFont22.getStringWidth(notification.getTitle()) + 35;
                        }
                        else {
                            notificationHeight = 28;
                            notificationWidth = (int)Math.max(NotificationsMod.tenacityBoldFont22.getStringWidth(notification.getTitle()), NotificationsMod.tenacityFont18.getStringWidth(notification.getDescription())) + 35;
                        }
                        final float x = sr.getScaledWidth() - (notificationWidth + 5) * animation.getOutput().floatValue();
                        final float y = sr.getScaledHeight() - (yOffset + 18.0f + HUDMod.offsetValue + notificationHeight + 15.0f * GuiChat.openingAnimation.getOutput().floatValue());
                        notification.blurDefault(x, y, (float)notificationWidth, (float)notificationHeight, animation.getOutput().floatValue(), glow);
                        break;
                    }
                    case "SuicideX": {
                        actualOffset = 3;
                        notificationHeight = 16;
                        final String editTitle = notification.getTitle() + ((notification.getTitle().endsWith(".") || notification.getTitle().endsWith("/")) ? " " : ". ") + notification.getDescription();
                        final int notificationWidth = (int)NotificationsMod.tenacityBoldFont22.getStringWidth(editTitle) + 5;
                        final float x = (float)(sr.getScaledWidth() - (notificationWidth + 5));
                        final float y = sr.getScaledHeight() - (yOffset + 18.0f + HUDMod.offsetValue + notificationHeight + 15.0f * GuiChat.openingAnimation.getOutput().floatValue());
                        notification.blurSuicideX(x, y, (float)notificationWidth, (float)notificationHeight, animation.getOutput().floatValue());
                        break;
                    }
                    case "Exhibition": {
                        actualOffset = 3;
                        notificationHeight = 25;
                        final int notificationWidth = (int)Math.max(NotificationsMod.tahomaFont.size(18).getStringWidth(notification.getTitle()), NotificationsMod.tahomaFont.size(14).getStringWidth(notification.getDescription())) + 30;
                        final float x = sr.getScaledWidth() - (sr.getScaledWidth() / 2.0f + notificationWidth / 2.0f) * animation.getOutput().floatValue();
                        final float y = sr.getScaledHeight() / 2.0f - notificationHeight / 2.0f + 40.0f + yOffset;
                        notification.blurExhi(x, y, (float)notificationWidth, (float)notificationHeight);
                        break;
                    }
                }
                yOffset += (notificationHeight + actualOffset) * animation.getOutput().floatValue();
            }
        }
    }
    
    static {
        mode = new ModeSetting("Mode", "Default", new String[] { "Default", "SuicideX", "Exhibition" });
        onlyTitle = new BooleanSetting("Only Title", false);
        toggleNotifications = new BooleanSetting("Show Toggle", true);
    }
}
