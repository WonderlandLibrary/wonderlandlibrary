package vip.radium.notification;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import vip.radium.gui.font.FontManager;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;
import vip.radium.utils.render.LockedResolution;
import vip.radium.utils.render.OGLUtils;
import vip.radium.utils.render.RenderingUtils;
import vip.radium.utils.render.Translate;

public final class Notification {

    private final String title;
    private final String body;
    private final Translate translate;
    private final float width;
    private final float height;
    private final long duration;
    private final int color;
    private final TimerUtil timer;
    private boolean dead;

    public Notification(String title, String body, long duration, NotificationType type) {
        this.title = title;
        this.body = body;
        if (body != null)
            this.width = Math.max(FontManager.FR.getWidth(title), FontManager.SMALL_FR.getWidth(body)) + 4;
        else
            this.width = FontManager.FR.getWidth(title) + 4;

        this.height = 27.0F;
        if (Wrapper.getCurrentScreen() == null) {
            LockedResolution lr = RenderingUtils.getLockedResolution();
            this.translate = new Translate(lr.getWidth(), lr.getHeight() - height - 2);
        } else {
            ScaledResolution sr = RenderingUtils.getScaledResolution();
            this.translate = new Translate(sr.getScaledWidth(), sr.getScaledHeight() - height - 2);
        }
        this.duration = duration;
        this.color = type.getColor();
        this.timer = new TimerUtil();
    }

    public Notification(String title, String body, NotificationType type) {
        this(title, body, (title.length() + body.length()) * 40L, type);
    }

    public Notification(String title, NotificationType type) {
        this(title, null, title.length() * 40L, type);
    }

    public Notification(String title, long duration, NotificationType type) {
        this(title, null, duration, type);
    }

    public void render(LockedResolution lockedResolution, ScaledResolution scaledResolution, int index, int yOffset) {
        final int width;
        final int height;
        if (lockedResolution != null) {
            width = lockedResolution.getWidth();
            height = lockedResolution.getHeight();
        } else {
            width = scaledResolution.getScaledWidth();
            height = scaledResolution.getScaledHeight();
        }

        float notificationY = height - ((this.height + 2) * index) - yOffset;
        float notificationX = width - this.width;

        if (timer.hasElapsed(duration))
            translate.animate(width, notificationY);
        else
            translate.animate(notificationX, notificationY);

        float x = (float) translate.getX();
        float y = (float) translate.getY();

        if (x >= width) {
            this.dead = true;
            return;
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        if (lockedResolution != null) {
            OGLUtils.startScissorBox(lockedResolution, (int) x, (int) y,
                    MathHelper.ceiling_float_int(this.width), (int) this.height);
        } else {
            OGLUtils.startScissorBox(scaledResolution, (int) x, (int) y,
                    MathHelper.ceiling_float_int(this.width), (int) this.height);
        }

        Gui.drawRect(notificationX, y, notificationX + this.width, y + this.height, 0x78000000);

        double progress = ((double) (System.currentTimeMillis() - timer.lastReset()) / duration) * this.width;

        Gui.drawRect(notificationX, y + this.height - 2, notificationX + this.width, y + this.height,
                RenderingUtils.darker(color, 0.4F));
        Gui.drawRect(notificationX, y + this.height - 2, notificationX + progress, y + this.height, color);

        if (body != null && body.length() > 0) {
            FontManager.MEDIUM_FR.drawStringWithShadow(title, notificationX + 2, y + 2, -1);
            FontManager.SMALL_FR.drawStringWithShadow(body, notificationX + 2, y + 14, -1);
        } else
            FontManager.MEDIUM_FR.drawStringWithShadow(title, notificationX + 2, y + 9, -1);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public boolean isDead() {
        return dead;
    }

}
