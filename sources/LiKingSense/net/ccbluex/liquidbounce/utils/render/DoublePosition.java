/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.render;

public class DoublePosition {
    private float x;
    private float y;

    public DoublePosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public DoublePosition() {
        this.x = 0.0f;
        this.y = 0.0f;
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

