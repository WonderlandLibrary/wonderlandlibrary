package dev.rise.notifications;

import dev.rise.font.CustomFont;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.render.RenderUtil;
import dev.rise.util.render.theme.ThemeType;
import dev.rise.util.render.theme.ThemeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.StringUtils;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
public final class Notification {

    private final String description;
    private final NotificationType type;
    private long delay, start, end;

    private final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

    private float xVisual = sr.getScaledWidth();
    public float yVisual = sr.getScaledHeight() - 50;
    public float y = sr.getScaledHeight() - 50;

    private final TimeUtil timer = new TimeUtil();

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public Notification(final String description, final long delay, final NotificationType type) {
        this.description = description;
        this.delay = delay;
        this.type = type;

        start = System.currentTimeMillis();
        end = start + delay;
    }

    public String getDescription() {
        return description;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(final long delay) {
        this.delay = delay;
    }

    public void setStart(final long start) {
        this.start = start;
    }

    public void setEnd(final long end) {
        this.end = end;
    }

    public void render() {
        final String name = StringUtils.capitalize(type.name().toLowerCase());

        final float screenWidth = sr.getScaledWidth();
        float x = (screenWidth) - (Math.max(CustomFont.getWidth(description), CustomFont.getWidth(name))) - 2;

        final float curr = System.currentTimeMillis() - getStart();
        final float percentageLeft = curr / getDelay();

        if (percentageLeft > 0.9) x = screenWidth;

        if (timer.hasReached(1000 / 60)) {
            xVisual = lerp(xVisual, x, 0.2f);
            yVisual = lerp(yVisual, y, 0.2f);

            timer.reset();
        }

        final Color c = ThemeUtil.getThemeColor(ThemeType.GENERAL);

        RenderUtil.roundedRectCustom(xVisual, yVisual - 3, sr.getScaledWidth() - xVisual, 25, 4f, new Color(0, 0, 0, 170), true, false, true, false);

        Gui.drawRect(xVisual + (percentageLeft * (CustomFont.getWidth(description)) + 8), yVisual + 20, screenWidth + 1, yVisual + 22, c.hashCode());

        final Color bright = new Color(Math.min(c.getRed() + 16, 255), Math.min(c.getGreen() + 35, 255), Math.min(c.getBlue() + 7, 255));

        CustomFont.drawStringBold(name, xVisual + 1, yVisual - 2, bright.getRGB());
        CustomFont.drawString(description, xVisual + 1, yVisual + 10, c.hashCode());
    }

    public final float lerp(final float a, final float b, final float c) {
        return a + c * (b - a);
    }
}
