/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.paint;

import com.sun.prism.paint.Color;

public class Stop {
    private final Color color;
    private final float offset;

    public Stop(Color color, float f) {
        this.color = color;
        this.offset = f;
    }

    public Color getColor() {
        return this.color;
    }

    public float getOffset() {
        return this.offset;
    }
}

