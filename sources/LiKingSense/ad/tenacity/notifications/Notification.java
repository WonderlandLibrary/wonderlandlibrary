/*
 * Decompiled with CFR 0.152.
 */
package ad.tenacity.notifications;

import ad.novoline.font.FontRenderer;
import ad.novoline.font.Fonts;
import ad.tenacity.notifications.NotificationType;
import net.ccbluex.liquidbounce.utils.render.miku.animations.Animation;
import net.ccbluex.liquidbounce.utils.render.tenacity.normal.TimerUtil;

public class Notification {
    private final NotificationType notificationType;
    private final String title;
    private final String description;
    private final float height = 28.0f;
    private final float time;
    public float notificationY;
    public FontRenderer descriptionFont = Fonts.tenacity.tenacity18.tenacity18;
    public FontRenderer titleFont = Fonts.tenacityblod.tenacityblod22.tenacityblod22;
    public FontRenderer iconFont = Fonts.tenacityCheck.tenacitycheck35.tenacitycheck35;
    public final TimerUtil timerUtil;
    private Animation animation;

    public Notification(NotificationType type, String title, String description) {
        this.title = title;
        this.description = description;
        this.time = 1500.0f;
        this.timerUtil = new TimerUtil();
        this.notificationType = type;
    }

    public Notification(NotificationType type, String title, String description, float time) {
        this.title = title;
        this.description = description;
        this.time = (long)(time * 1000.0f);
        this.timerUtil = new TimerUtil();
        this.notificationType = type;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public float getWidth() {
        return 17.0f + (float)Math.max(this.descriptionFont.stringWidth(this.description), this.titleFont.stringWidth(this.title));
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public float getHeight() {
        return 28.0f;
    }

    public float getMaxTime() {
        return this.time;
    }

    public void startAnimation(Animation animation) {
        this.animation = animation;
    }

    public void stopAnimation() {
        this.animation = null;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public boolean isAnimating() {
        return this.animation != null && !this.animation.isDone();
    }
}

