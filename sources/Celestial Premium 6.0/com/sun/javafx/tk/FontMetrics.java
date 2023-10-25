/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.text.Font
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.Toolkit;
import javafx.scene.text.Font;

public class FontMetrics {
    private float maxAscent;
    private float ascent;
    private float xheight;
    private int baseline;
    private float descent;
    private float maxDescent;
    private float leading;
    private float lineHeight;
    private Font font;

    public static FontMetrics createFontMetrics(float f, float f2, float f3, float f4, float f5, float f6, Font font) {
        return new FontMetrics(f, f2, f3, f4, f5, f6, font);
    }

    public final float getMaxAscent() {
        return this.maxAscent;
    }

    public final float getAscent() {
        return this.ascent;
    }

    public final float getXheight() {
        return this.xheight;
    }

    public final int getBaseline() {
        return this.baseline;
    }

    public final float getDescent() {
        return this.descent;
    }

    public final float getMaxDescent() {
        return this.maxDescent;
    }

    public final float getLeading() {
        return this.leading;
    }

    public final float getLineHeight() {
        return this.lineHeight;
    }

    public final Font getFont() {
        if (this.font == null) {
            this.font = Font.getDefault();
        }
        return this.font;
    }

    public FontMetrics(float f, float f2, float f3, float f4, float f5, float f6, Font font) {
        this.maxAscent = f;
        this.ascent = f2;
        this.xheight = f3;
        this.descent = f4;
        this.maxDescent = f5;
        this.leading = f6;
        this.font = font;
        this.lineHeight = f + f5 + f6;
    }

    public float getCharWidth(char c) {
        return Toolkit.getToolkit().getFontLoader().getCharWidth(c, this.getFont());
    }

    public String toString() {
        return "FontMetrics: [maxAscent=" + this.getMaxAscent() + ", ascent=" + this.getAscent() + ", xheight=" + this.getXheight() + ", baseline=" + this.getBaseline() + ", descent=" + this.getDescent() + ", maxDescent=" + this.getMaxDescent() + ", leading=" + this.getLeading() + ", lineHeight=" + this.getLineHeight() + ", font=" + this.getFont() + "]";
    }
}

