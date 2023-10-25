/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.ui.notification;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.celestial.client.feature.impl.hud.Notifications;
import org.celestial.client.helpers.Helper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.render.ScreenHelper;
import org.celestial.client.ui.notification.NotificationType;

public class Notification
implements Helper {
    private final ScreenHelper screenHelper;
    private final FontRenderer fontRenderer;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationType type;
    private final TimerHelper timer;

    public Notification(String title, String content, NotificationType type, int second, FontRenderer fontRenderer) {
        this.title = title;
        this.content = content;
        this.time = second;
        this.type = type;
        this.timer = new TimerHelper();
        this.fontRenderer = fontRenderer;
        ScaledResolution sr = new ScaledResolution(mc);
        this.screenHelper = new ScreenHelper(sr.getScaledWidth() + 100, sr.getScaledHeight() - 60);
    }

    public int getWidth() {
        return Notification.mc.fontRendererObj.getStringWidth(this.content) + (Notifications.timePeriod.getCurrentValue() ? 70 : 90);
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public int getTime() {
        return this.time;
    }

    public NotificationType getType() {
        return this.type;
    }

    public TimerHelper getTimer() {
        return this.timer;
    }

    public ScreenHelper getTranslate() {
        return this.screenHelper;
    }
}

