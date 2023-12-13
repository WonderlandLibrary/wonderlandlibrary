/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.ui.notification;

import me.wintware.client.ui.notification.NotificationType;
import me.wintware.client.utils.animation.Stopwatch;
import me.wintware.client.utils.animation.Translate;
import me.wintware.client.utils.font.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public final class Notification {
    public static final int HEIGHT = 30;
    private final String title;
    public float y;
    private final String content;
    private final int time;
    private final NotificationType type;
    private final Stopwatch timer;
    private final Translate translate;
    private final FontRenderer fontRenderer;
    public double scissorBoxWidth;

    public Notification(String title, String content, NotificationType type, FontRenderer fontRenderer) {
        this.title = title;
        this.content = content;
        this.time = 1900;
        this.type = type;
        this.timer = new Stopwatch();
        this.fontRenderer = fontRenderer;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.translate = new Translate(sr.getScaledWidth() - this.getWidth(), sr.getScaledHeight() - 30);
    }

    public final int getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 10);
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getContent() {
        return this.content;
    }

    public final int getTime() {
        return this.time;
    }

    public final NotificationType getType() {
        return this.type;
    }

    public final Stopwatch getTimer() {
        return this.timer;
    }

    public final Translate getTranslate() {
        return this.translate;
    }
}

