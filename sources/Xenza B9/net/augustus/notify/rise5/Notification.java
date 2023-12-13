// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.notify.rise5;

import java.awt.Font;
import net.augustus.ui.GuiIngameHook;
import net.minecraft.client.gui.Gui;
import net.augustus.utils.skid.rise5.RenderUtil;
import java.awt.Color;
import net.augustus.Augustus;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.client.Minecraft;
import net.augustus.utils.TimeUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.notify.NotificationType;
import net.augustus.font.UnicodeFontRenderer;

public final class Notification
{
    private static UnicodeFontRenderer riseFontRenderer;
    private final String description;
    private final NotificationType type;
    private long delay;
    private long start;
    private long end;
    private final ScaledResolution sr;
    private float xVisual;
    public float yVisual;
    public float y;
    private final TimeUtil timer;
    
    public long getStart() {
        return this.start;
    }
    
    public long getEnd() {
        return this.end;
    }
    
    public Notification(final String description, final long delay, final NotificationType type) {
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
        this.xVisual = (float)this.sr.getScaledWidth();
        this.yVisual = (float)(this.sr.getScaledHeight() - 50);
        this.y = (float)(this.sr.getScaledHeight() - 50);
        this.timer = new TimeUtil();
        this.description = description;
        this.delay = delay;
        this.type = type;
        this.start = System.currentTimeMillis();
        this.end = this.start + delay;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public long getDelay() {
        return this.delay;
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
        final String name = StringUtils.capitalize(this.type.name().toLowerCase());
        final float screenWidth = (float)this.sr.getScaledWidth();
        float x = screenWidth - Math.max(Notification.riseFontRenderer.getStringWidth(this.description), Notification.riseFontRenderer.getStringWidth(name)) - 2.0f;
        final float curr = (float)(System.currentTimeMillis() - this.getStart());
        final float percentageLeft = curr / this.getDelay();
        if (percentageLeft > 0.9) {
            x = screenWidth;
        }
        if (this.timer.hasReached(16L)) {
            this.xVisual = this.lerp(this.xVisual, x, 0.2f);
            this.yVisual = this.lerp(this.yVisual, this.y, 0.2f);
            this.timer.reset();
        }
        final Color c = Augustus.getInstance().getClientColor();
        RenderUtil.roundedRectCustom(this.xVisual, this.yVisual - 3.0f, this.sr.getScaledWidth() - this.xVisual, 25.0, 4.0, new Color(0, 0, 0, 170), true, false, true, false);
        Gui.drawRect(this.xVisual + (percentageLeft * Notification.riseFontRenderer.getStringWidth(this.description) + 8.0f), this.yVisual + 20.0f, screenWidth + 1.0f, this.yVisual + 22.0f, c.hashCode());
        final Color bright = new Color(Math.min(c.getRed() + 16, 255), Math.min(c.getGreen() + 35, 255), Math.min(c.getBlue() + 7, 255));
        Notification.riseFontRenderer.drawString(name, this.xVisual + 1.0f, this.yVisual - 2.0f, bright.getRGB());
        Notification.riseFontRenderer.drawString(this.description, this.xVisual + 1.0f, this.yVisual + 10.0f, c.hashCode());
    }
    
    public final float lerp(final float a, final float b, final float c) {
        return a + c * (b - a);
    }
    
    static {
        try {
            Notification.riseFontRenderer = new UnicodeFontRenderer(Font.createFont(0, GuiIngameHook.class.getResourceAsStream("/ressources/Light.ttf")).deriveFont(18.0f));
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
