/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;

public interface FontStrike {
    public FontResource getFontResource();

    public float getSize();

    public BaseTransform getTransform();

    public boolean drawAsShapes();

    public int getQuantizedPosition(Point2D var1);

    public Metrics getMetrics();

    public Glyph getGlyph(char var1);

    public Glyph getGlyph(int var1);

    public void clearDesc();

    public int getAAMode();

    public float getCharAdvance(char var1);

    public Shape getOutline(GlyphList var1, BaseTransform var2);
}

