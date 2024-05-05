// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.notification;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import vip.Resolute.util.render.LockedResolution;
import vip.Resolute.util.font.MinecraftFontRenderer;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import vip.Resolute.util.font.FontUtil;
import vip.Resolute.util.misc.TimerUtil;
import vip.Resolute.util.render.Translate;

public final class Notification
{
    private final String title;
    private final String body;
    private final Translate translate;
    private final float width;
    private final float height;
    private final long duration;
    private final int color;
    private final TimerUtil timer;
    private boolean dead;
    private NotificationType type;
    
    public Notification(final String title, final String body, final long duration, final NotificationType type) {
        final MinecraftFontRenderer fontRenderer = FontUtil.moon;
        this.type = type;
        this.title = title;
        this.body = body;
        if (body != null) {
            this.width = (float)(Math.max(fontRenderer.getStringWidth(title), fontRenderer.getStringWidth(body)) + 4.0);
        }
        else {
            this.width = (float)(fontRenderer.getStringWidth(title) + 4.0);
        }
        this.height = 27.0f;
        if (Minecraft.getMinecraft().currentScreen == null) {
            final LockedResolution lr = RenderUtils.getLockedResolution();
            this.translate = new Translate(lr.getWidth(), lr.getHeight() - this.height - 2.0f);
        }
        else {
            final ScaledResolution sr = RenderUtils.getScaledResolution();
            this.translate = new Translate(sr.getScaledWidth(), sr.getScaledHeight() - this.height - 2.0f);
        }
        this.duration = duration;
        this.color = type.getColor();
        this.timer = new TimerUtil();
    }
    
    public Notification(final String title, final String body, final NotificationType type) {
        this(title, body, (title.length() + body.length()) * 40L, type);
    }
    
    public Notification(final String title, final NotificationType type) {
        this(title, null, title.length() * 40L, type);
    }
    
    public Notification(final String title, final long duration, final NotificationType type) {
        this(title, null, duration, type);
    }
    
    public void render(final LockedResolution lockedResolution, final ScaledResolution scaledResolution, final int index, final int yOffset) {
        final MinecraftFontRenderer fontRenderer = FontUtil.moon;
        int width;
        int height;
        if (lockedResolution != null) {
            width = lockedResolution.getWidth();
            height = lockedResolution.getHeight();
        }
        else {
            width = scaledResolution.getScaledWidth();
            height = scaledResolution.getScaledHeight();
        }
        final float notificationY = height - (this.height + 2.0f) * index - yOffset;
        final float notificationX = width - this.width;
        if (this.timer.hasElapsed(this.duration)) {
            this.translate.animate2(width, notificationY);
        }
        else {
            this.translate.animate2(notificationX, notificationY);
        }
        final float x = (float)this.translate.getX();
        final float y = (float)this.translate.getY();
        if (x >= width) {
            this.dead = true;
            return;
        }
        GL11.glEnable(3089);
        if (lockedResolution != null) {
            RenderUtils.startScissorBox(lockedResolution, (int)x, (int)y, MathHelper.ceiling_float_int(this.width), (int)this.height);
        }
        else {
            RenderUtils.startScissorBox(scaledResolution, (int)x, (int)y, MathHelper.ceiling_float_int(this.width), (int)this.height);
        }
        Gui.drawRect(x, y, x + this.width, y + this.height, 2132746527);
        final double progress = (System.currentTimeMillis() - this.timer.lastReset()) / (double)this.duration * this.width;
        Gui.drawRect(x, y + this.height - 2.0f, x + this.width, y + this.height, RenderUtils.darker(this.color, 0.4f));
        Gui.drawRect(x, y + this.height - 2.0f, x + progress, y + this.height, this.color);
        if (this.body != null && this.body.length() > 0) {
            fontRenderer.drawStringWithShadow(this.title, x + 2.0f, y + 2.0f, -1);
            fontRenderer.drawStringWithShadow(this.body, x + 2.0f, y + 14.0f, -1);
        }
        else {
            fontRenderer.drawStringWithShadow(this.title, x + 2.0f, y + 9.0f, -1);
        }
        GL11.glDisable(3089);
    }
    
    public boolean isDead() {
        return this.dead;
    }
}
