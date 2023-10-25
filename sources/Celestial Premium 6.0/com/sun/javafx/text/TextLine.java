/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.text.TextRun;

public class TextLine
implements com.sun.javafx.scene.text.TextLine {
    TextRun[] runs;
    RectBounds bounds;
    float lsb;
    float rsb;
    float leading;
    int start;
    int length;

    public TextLine(int n, int n2, TextRun[] arrtextRun, float f, float f2, float f3, float f4) {
        this.start = n;
        this.length = n2;
        this.bounds = new RectBounds(0.0f, f2, f, f3 + f4);
        this.leading = f4;
        this.runs = arrtextRun;
    }

    @Override
    public RectBounds getBounds() {
        return this.bounds;
    }

    public float getLeading() {
        return this.leading;
    }

    public TextRun[] getRuns() {
        return this.runs;
    }

    @Override
    public int getStart() {
        return this.start;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    public void setSideBearings(float f, float f2) {
        this.lsb = f;
        this.rsb = f2;
    }

    @Override
    public float getLeftSideBearing() {
        return this.lsb;
    }

    @Override
    public float getRightSideBearing() {
        return this.rsb;
    }

    public void setAlignment(float f) {
        this.bounds.setMinX(f);
        this.bounds.setMaxX(f + this.bounds.getMaxX());
    }

    public void setWidth(float f) {
        this.bounds.setMaxX(this.bounds.getMinX() + f);
    }
}

