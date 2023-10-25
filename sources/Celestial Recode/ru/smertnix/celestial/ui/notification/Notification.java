package ru.smertnix.celestial.ui.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import ru.smertnix.celestial.ui.clickgui.ScreenHelper;
import ru.smertnix.celestial.ui.font.MCFontRenderer;
import ru.smertnix.celestial.utils.math.TimerHelper;
public class Notification {
    public final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationMode type;
    private final TimerHelper timer;
    private final MCFontRenderer fontRenderer;
    public double x = sr.getScaledWidth();
    public double y = sr.getScaledHeight()- 35;
    public double notificationTimeBarWidth;
    public float anim;
    private final ScreenHelper screenHelper;

    public Notification(String title, String content, NotificationMode type, int second, MCFontRenderer fontRenderer) {
        this.title = title;
        this.content = content;
        this.time = second;
        this.type = type;
        this.timer = new TimerHelper();
        this.fontRenderer = fontRenderer;
        this.screenHelper = new ScreenHelper((sr.getScaledWidth() - getWidth() + getWidth()), (sr.getScaledHeight() - 60));

    }

    public final int getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 90);
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
    public final int getY() {
        return (int) this.y;
    }

    public final NotificationMode getType() {
        return this.type;
    }

    public final TimerHelper getTimer() {
        return this.timer;
    }
    public ScreenHelper getTranslate() {
        return screenHelper;
    }
}