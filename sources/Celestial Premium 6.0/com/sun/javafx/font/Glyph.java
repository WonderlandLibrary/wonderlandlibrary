/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

public interface Glyph {
    public int getGlyphCode();

    public RectBounds getBBox();

    public float getAdvance();

    public Shape getShape();

    public byte[] getPixelData();

    public byte[] getPixelData(int var1);

    public float getPixelXAdvance();

    public float getPixelYAdvance();

    public boolean isLCDGlyph();

    public int getWidth();

    public int getHeight();

    public int getOriginX();

    public int getOriginY();
}

