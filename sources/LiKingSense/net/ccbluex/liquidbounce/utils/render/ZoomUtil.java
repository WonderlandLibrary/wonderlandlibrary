/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.ccbluex.liquidbounce.utils.render;

import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.minecraft.client.Minecraft;

public class ZoomUtil {
    protected static final Minecraft mc = Minecraft.func_71410_x();
    private final float originalX;
    private final float originalY;
    private final float originalWidth;
    private final float originalHeight;
    private final float speed;
    private final float zoomFactor;
    private final long nextUpdateTime;
    private final TimeUtils timer = new TimeUtils();
    private float x;
    private float y;
    private float width;
    private float height;

    public ZoomUtil(float x, float y, float width, float height, long nextUpdateTime, float speed, float zoomFactor) {
        this.originalX = x;
        this.originalY = y;
        this.originalWidth = width;
        this.originalHeight = height;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.zoomFactor = zoomFactor;
        this.nextUpdateTime = nextUpdateTime;
    }

    public void update(int mouseX, int mouseY) {
        if (RenderUtils.isHovered(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            if (this.timer.hasElapsed(this.nextUpdateTime)) {
                this.x = RenderUtils.animate(this.originalX - this.zoomFactor / 2.0f, this.x, this.speed) - 0.1f;
                this.y = RenderUtils.animate(this.originalY - this.zoomFactor / 2.0f, this.y, this.speed) - 0.1f;
                this.width = RenderUtils.animate(this.originalWidth + this.zoomFactor, this.width, this.speed) - 0.1f;
                this.height = RenderUtils.animate(this.originalHeight + this.zoomFactor, this.height, this.speed) - 0.1f;
                this.timer.reset();
            }
        } else if (this.timer.hasElapsed(this.nextUpdateTime)) {
            this.x = RenderUtils.animate(this.originalX, this.x, this.speed) - 0.1f;
            this.y = RenderUtils.animate(this.originalY, this.y, this.speed) - 0.1f;
            this.width = RenderUtils.animate(this.originalWidth, this.width, this.speed) - 0.1f;
            this.height = RenderUtils.animate(this.originalHeight, this.height, this.speed) - 0.1f;
            this.timer.reset();
        }
    }

    public void setPosition(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getOriginalX() {
        return this.originalX;
    }

    public float getOriginalY() {
        return this.originalY;
    }

    public float getOriginalWidth() {
        return this.originalWidth;
    }

    public float getOriginalHeight() {
        return this.originalHeight;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getZoomFactor() {
        return this.zoomFactor;
    }

    public long getNextUpdateTime() {
        return this.nextUpdateTime;
    }

    public TimeUtils getTimer() {
        return this.timer;
    }
}

