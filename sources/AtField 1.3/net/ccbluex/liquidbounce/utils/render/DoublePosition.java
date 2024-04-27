/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

public class DoublePosition {
    private float y;
    private float x;

    public DoublePosition(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public void setY(float f) {
        this.y = f;
    }

    public DoublePosition() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public float getY() {
        return this.y;
    }

    public void setX(float f) {
        this.x = f;
    }

    public float getX() {
        return this.x;
    }
}

