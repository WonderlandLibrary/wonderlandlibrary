/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.render;

import net.minecraft.client.Minecraft;
import org.celestial.client.helpers.render.AnimationHelper;

public class ScreenHelper {
    private float x;
    private float y;

    public ScreenHelper(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void calculateCompensation(float targetX, float targetY, double xSpeed, double ySpeed) {
        int deltaX = (int)((double)Math.abs(targetX - this.x) * xSpeed);
        int deltaY = (int)((double)Math.abs(targetY - this.y) * ySpeed);
        this.x = AnimationHelper.calculateCompensation(targetX, this.x, (float)(Minecraft.frameTime * 0.5), deltaX);
        this.y = AnimationHelper.calculateCompensation(targetY, this.y, (float)(Minecraft.frameTime * 0.5), deltaY);
    }

    public void calculateCompensation(float targetX, float targetY, float xSpeed, float ySpeed) {
        int deltaX = (int)(Math.abs(targetX - this.x) * xSpeed);
        int deltaY = (int)(Math.abs(targetY - this.y) * ySpeed);
        this.x = AnimationHelper.calculateCompensation(targetX, this.x, (float)(Minecraft.frameTime * 0.5), deltaX);
        this.y = AnimationHelper.calculateCompensation(targetY, this.y, (float)(Minecraft.frameTime * 0.5), deltaY);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

