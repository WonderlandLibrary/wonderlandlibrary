package me.nyan.flush.notifications;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.Timer;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Notification {
    private final Type type;
    private final String title;
    private final String message;
    private final Timer timer = new Timer();
    private final int time;
    private int stage;
    private float level;
    private float offset;

    public Notification(Type type, String title, String message, int time) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.time = time;
        stage = -1;
    }

    public Notification(Type type, String title, String message) {
        this(type, title, message, 800);
    }

    public void draw(float x, float y, int offset) {
        if (this.offset < offset) {
            this.offset += 0.01 * Flush.getFrameTime();
            if (this.offset > offset) {
                this.offset = offset;
            }
        } else if (this.offset > offset) {
            this.offset -= 0.01 * Flush.getFrameTime();
            if (this.offset < offset) {
                this.offset = offset;
            }
        }

        switch (stage) {
            case 0:
                level += 0.01 * Flush.getFrameTime();
                if (level >= 1) {
                    level = 1;
                    stage++;
                }
                break;

            case 1:
                if (timer.hasTimeElapsed(time, false)) {
                    stage++;
                }
                break;

            case 2:
                level -= 0.01 * Flush.getFrameTime();
                if (level <= 0) {
                    level = 0;
                    stage++;
                }
                break;
        }

        String image = null;
        int color = 0;

        switch (type) {
            case ERROR:
                image = "flush/icons/notifications/error.png";
                color = 0xFFD40000;
                break;
            case INFO:
                image = "flush/icons/notifications/info.png";
                color = 0xFF00E200;
                break;
            case WARNING:
                image = "flush/icons/notifications/warning.png";
                color = 0xFF00C4FF;
                break;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -(this.offset * (getHeight() + 2)), 0);

        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();

        Gui.drawRect(
                x,
                y,
                x + getWidth(),
                y + getHeight(),
                ColorUtils.alpha(0xFF000000, (int) (level * 136))
        );

        Gui.drawRect(
                x,
                y + getHeight() - 2,
                x + getWidth() * Math.min(((System.currentTimeMillis() - timer.lastMS) / (float) time), 1),
                y + getHeight(),
                ColorUtils.alpha(color, (int) (level * 255))
        );

        RenderUtils.glColor(-1);

        if (image != null) {
            double scale = 0.5;
            GlStateManager.pushMatrix();
            GlStateManager.color(1, 1, 1, level);
            GlStateManager.scale(scale, scale, 1);
            RenderUtils.drawImage(
                    new ResourceLocation(image),
                    (x + getHeight() * scale) / scale - 28,
                    (y + getHeight() * scale) / scale - 28,
                    28 / scale,
                    28 / scale
            );
            RenderUtils.glColor(-1);
            GlStateManager.popMatrix();
        }

        Flush.getFont("GoogleSansDisplay Medium", 22).drawString(
                title,
                x + getHeight() / 2 + 14 + 4,
                y + 8,
                ColorUtils.alpha(type == Type.ERROR ? 0xFFF00000 : -1, (int) (level * 255)),
                true
        );
        getFont().drawString(
                message,
                x + getHeight() / 2 + 14 + 4,
                y + 8 + Flush.getFont("GoogleSansDisplay Medium", 22).getFontHeight(),
                ColorUtils.alpha(-1, (int) (level * 255)),
                true
        );
        GlStateManager.popMatrix();
    }

    public float getWidth() {
        return Math.max(100, 46 + Math.max(getFont().getStringWidth(message), Flush.getFont("GoogleSansDisplay Medium", 22).getStringWidth(title)));
    }

    public float getHeight() {
        return 40;
    }

    public GlyphPageFontRenderer getFont() {
        return Flush.getFont("GoogleSansDisplay", 18);
    }

    public void show() {
        stage = 0;
    }

    public boolean isShowing() {
        return stage < 3;
    }

    public boolean hasStarted() {
        return stage > -1;
    }

    public float getLevel() {
        return level;
    }

    public float getOffset() {
        return offset;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        INFO, WARNING, ERROR
    }
}