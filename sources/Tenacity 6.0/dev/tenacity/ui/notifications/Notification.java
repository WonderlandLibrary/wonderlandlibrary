// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.notifications;

import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.font.CustomFont;
import net.minecraft.client.gui.Gui;
import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.render.PostProcessing;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import java.awt.Color;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.utils.Utils;

public class Notification implements Utils
{
    private final NotificationType notificationType;
    private final String title;
    private final String description;
    private final float time;
    private final TimerUtil timerUtil;
    private final Animation animation;
    
    public Notification(final NotificationType type, final String title, final String description) {
        this(type, title, description, NotificationManager.getToggleTime());
    }
    
    public Notification(final NotificationType type, final String title, final String description, final float time) {
        this.title = title;
        this.description = description;
        this.time = (float)(long)(time * 1000.0f);
        this.timerUtil = new TimerUtil();
        this.notificationType = type;
        this.animation = new DecelerateAnimation(250, 1.0);
    }
    
    public void drawDefault(final float x, final float y, final float width, final float height, final float alpha, final boolean onlyTitle) {
        final Color color = ColorUtil.applyOpacity(ColorUtil.interpolateColorC(Color.BLACK, this.getNotificationType().getColor(), 0.65f), 0.7f * alpha);
        RoundedUtil.drawRound(x, y, width, height, 4.0f, color);
        final Color notificationColor = ColorUtil.applyOpacity(this.getNotificationType().getColor(), alpha);
        final Color textColor = ColorUtil.applyOpacity(Color.WHITE, alpha);
        final String icon = this.getNotificationType().getIcon();
        FontUtil.iconFont35.drawString(this.getNotificationType().getIcon(), x + 5.0f, y + FontUtil.iconFont35.getMiddleOfBox(height) + 1.0f, notificationColor);
        if (onlyTitle) {
            Notification.tenacityBoldFont22.drawString(this.getTitle(), x + 10.0f + FontUtil.iconFont35.getStringWidth(this.getNotificationType().getIcon()), y + Notification.tenacityBoldFont22.getMiddleOfBox(height), textColor);
        }
        else {
            Notification.tenacityBoldFont22.drawString(this.getTitle(), x + 10.0f + FontUtil.iconFont35.getStringWidth(this.getNotificationType().getIcon()), y + 4.0f, textColor);
            Notification.tenacityFont18.drawString(this.getDescription(), x + 10.0f + FontUtil.iconFont35.getStringWidth(this.getNotificationType().getIcon()), y + 7.0f + Notification.tenacityBoldFont22.getHeight(), textColor);
        }
    }
    
    public void blurDefault(final float x, final float y, final float width, final float height, final float alpha, final boolean glow) {
        final Color color = ColorUtil.applyOpacity(ColorUtil.interpolateColorC(Color.BLACK, this.getNotificationType().getColor(), glow ? 0.65f : 0.0f), alpha);
        RoundedUtil.drawRound(x, y, width, height, 4.0f, color);
    }
    
    public void drawExhi(final float x, final float y, final float width, final float height) {
        final boolean lowerAlpha = Tenacity.INSTANCE.getModuleCollection().getModule(PostProcessing.class).isEnabled();
        Gui.drawRect2(x, y, width, height, new Color(0.1f, 0.1f, 0.1f, lowerAlpha ? 0.4f : 0.75f).getRGB());
        final float percentage = Math.min(this.timerUtil.getTime() / this.getTime(), 1.0f);
        Gui.drawRect2(x + width * percentage, y + height - 1.0f, width - width * percentage, 1.0, this.getNotificationType().getColor().getRGB());
        FontUtil.iconFont40.drawString(this.getNotificationType().getIcon(), x + 3.0f, y + FontUtil.iconFont40.getMiddleOfBox(height) + 1.0f, this.getNotificationType().getColor());
        final CustomFont tahomaFont18 = Notification.tahomaFont.size(18);
        tahomaFont18.drawString(this.getTitle(), x + 7.0f + FontUtil.iconFont40.getStringWidth(this.getNotificationType().getIcon()), y + 4.0f, Color.WHITE);
        Notification.tahomaFont.size(14).drawString(this.getDescription(), x + 7.0f + FontUtil.iconFont40.getStringWidth(this.getNotificationType().getIcon()), y + 8.5f + tahomaFont18.getHeight(), Color.WHITE);
    }
    
    public void blurExhi(final float x, final float y, final float width, final float height) {
        Gui.drawRect2(x, y, width, height, Color.BLACK.getRGB());
        RenderUtil.resetColor();
    }
    
    public void drawSuicideX(final float x, final float y, final float width, final float height, final float animation) {
        final float heightVal = (height * animation <= 6.0f) ? 0.0f : (height * animation);
        final float yVal = y + height - heightVal;
        final String editTitle = this.getTitle() + ((this.getTitle().endsWith(".") || this.getTitle().endsWith("/")) ? " " : ". ") + this.getDescription();
        Notification.tenacityBoldFont22.drawCenteredString(editTitle, x + width / 2.0f, yVal + Notification.tenacityBoldFont22.getMiddleOfBox(heightVal), ColorUtil.applyOpacity(Color.WHITE, animation - 0.5f));
    }
    
    public void blurSuicideX(final float x, final float y, final float width, final float height, final float animation) {
        final float heightVal = (height * animation <= 6.0f) ? 0.0f : (height * animation);
        final float yVal = y + height - heightVal;
        RoundedUtil.drawRound(x, yVal, width, heightVal, 4.0f, Color.BLACK);
    }
    
    public NotificationType getNotificationType() {
        return this.notificationType;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public float getTime() {
        return this.time;
    }
    
    public TimerUtil getTimerUtil() {
        return this.timerUtil;
    }
    
    public Animation getAnimation() {
        return this.animation;
    }
}
