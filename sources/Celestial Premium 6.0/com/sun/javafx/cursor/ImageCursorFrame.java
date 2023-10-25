/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.cursor;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.CursorType;

public final class ImageCursorFrame
extends CursorFrame {
    private final Object platformImage;
    private final double width;
    private final double height;
    private final double hotspotX;
    private final double hotspotY;

    public ImageCursorFrame(Object object, double d, double d2, double d3, double d4) {
        this.platformImage = object;
        this.width = d;
        this.height = d2;
        this.hotspotX = d3;
        this.hotspotY = d4;
    }

    @Override
    public CursorType getCursorType() {
        return CursorType.IMAGE;
    }

    public Object getPlatformImage() {
        return this.platformImage;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getHotspotX() {
        return this.hotspotX;
    }

    public double getHotspotY() {
        return this.hotspotY;
    }
}

