/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFile;

public class PrismMetrics
implements Metrics {
    PrismFontFile fontResource;
    float ascent;
    float descent;
    float linegap;
    private float[] styleMetrics;
    float size;
    static final int XHEIGHT = 0;
    static final int CAPHEIGHT = 1;
    static final int TYPO_ASCENT = 2;
    static final int TYPO_DESCENT = 3;
    static final int TYPO_LINEGAP = 4;
    static final int STRIKETHROUGH_THICKNESS = 5;
    static final int STRIKETHROUGH_OFFSET = 6;
    static final int UNDERLINE_THICKESS = 7;
    static final int UNDERLINE_OFFSET = 8;
    static final int METRICS_TOTAL = 9;

    PrismMetrics(float f, float f2, float f3, PrismFontFile prismFontFile, float f4) {
        this.ascent = f;
        this.descent = f2;
        this.linegap = f3;
        this.fontResource = prismFontFile;
        this.size = f4;
    }

    @Override
    public float getAscent() {
        return this.ascent;
    }

    @Override
    public float getDescent() {
        return this.descent;
    }

    @Override
    public float getLineGap() {
        return this.linegap;
    }

    @Override
    public float getLineHeight() {
        return -this.ascent + this.descent + this.linegap;
    }

    private void checkStyleMetrics() {
        if (this.styleMetrics == null) {
            this.styleMetrics = this.fontResource.getStyleMetrics(this.size);
        }
    }

    @Override
    public float getTypoAscent() {
        this.checkStyleMetrics();
        return this.styleMetrics[2];
    }

    @Override
    public float getTypoDescent() {
        this.checkStyleMetrics();
        return this.styleMetrics[3];
    }

    @Override
    public float getTypoLineGap() {
        this.checkStyleMetrics();
        return this.styleMetrics[4];
    }

    @Override
    public float getCapHeight() {
        this.checkStyleMetrics();
        return this.styleMetrics[1];
    }

    @Override
    public float getXHeight() {
        this.checkStyleMetrics();
        return this.styleMetrics[0];
    }

    @Override
    public float getStrikethroughOffset() {
        this.checkStyleMetrics();
        return this.styleMetrics[6];
    }

    @Override
    public float getStrikethroughThickness() {
        this.checkStyleMetrics();
        return this.styleMetrics[5];
    }

    @Override
    public float getUnderLineOffset() {
        this.checkStyleMetrics();
        return this.styleMetrics[8];
    }

    @Override
    public float getUnderLineThickness() {
        this.checkStyleMetrics();
        return this.styleMetrics[7];
    }

    public String toString() {
        return "ascent = " + this.getAscent() + " descent = " + this.getDescent() + " linegap = " + this.getLineGap() + " lineheight = " + this.getLineHeight();
    }
}

