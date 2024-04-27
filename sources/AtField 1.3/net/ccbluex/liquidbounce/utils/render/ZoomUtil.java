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
    private float width;
    private final float zoomFactor;
    private float height;
    private final float originalX;
    private float y;
    protected static final Minecraft mc = Minecraft.func_71410_x();
    private final float speed;
    private final float originalWidth;
    private final float originalHeight;
    private final TimeUtils timer = new TimeUtils();
    private final long nextUpdateTime;
    private float x;
    private final float originalY;

    public void setPosition(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
    }

    public float getX() {
        return this.x;
    }

    public float getSpeed() {
        return this.speed;
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

    public float getHeight() {
        return this.height;
    }

    public long getNextUpdateTime() {
        return this.nextUpdateTime;
    }

    public TimeUtils getTimer() {
        return this.timer;
    }

    public ZoomUtil(float f, float f2, float f3, float f4, long l, float f5, float f6) {
        this.originalX = f;
        this.originalY = f2;
        this.originalWidth = f3;
        this.originalHeight = f4;
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
        this.speed = f5;
        this.zoomFactor = f6;
        this.nextUpdateTime = l;
    }

    public void update(int n, int n2) {
        if (RenderUtils.isHovered(this.x, this.y, this.width, this.height, n, n2)) {
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

    public float getY() {
        return this.y;
    }

    public float getOriginalHeight() {
        return this.originalHeight;
    }

    public float getWidth() {
        return this.width;
    }

    public float getZoomFactor() {
        return this.zoomFactor;
    }
}

