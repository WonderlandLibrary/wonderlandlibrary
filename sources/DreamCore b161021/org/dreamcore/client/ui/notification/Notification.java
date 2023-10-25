package org.dreamcore.client.ui.notification;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.dreamcore.client.helpers.Helper;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.helpers.render.ScreenHelper;

public class Notification implements Helper {

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
        this.screenHelper = new ScreenHelper((sr.getScaledWidth() - getWidth() + getWidth()), (sr.getScaledHeight() - 60));
    }

    public int getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 40);
    }

    public String getTitle() {
        return title;
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
        return screenHelper;
    }
}
