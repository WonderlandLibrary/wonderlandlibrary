/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.text;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.TextSpan;

public interface GlyphList {
    public int getGlyphCount();

    public int getGlyphCode(int var1);

    public float getPosX(int var1);

    public float getPosY(int var1);

    public float getWidth();

    public float getHeight();

    public RectBounds getLineBounds();

    public Point2D getLocation();

    public int getCharOffset(int var1);

    public boolean isComplex();

    public TextSpan getTextSpan();
}

