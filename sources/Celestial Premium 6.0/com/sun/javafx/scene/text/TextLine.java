/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.text;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;

public interface TextLine {
    public GlyphList[] getRuns();

    public RectBounds getBounds();

    public float getLeftSideBearing();

    public float getRightSideBearing();

    public int getStart();

    public int getLength();
}

