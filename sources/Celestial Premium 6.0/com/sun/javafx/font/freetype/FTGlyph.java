/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.freetype;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.freetype.FTFontFile;
import com.sun.javafx.font.freetype.FTFontStrike;
import com.sun.javafx.font.freetype.FT_Bitmap;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

class FTGlyph
implements Glyph {
    FTFontStrike strike;
    int glyphCode;
    byte[] buffer;
    FT_Bitmap bitmap;
    int bitmap_left;
    int bitmap_top;
    float advanceX;
    float advanceY;
    float userAdvance;
    boolean lcd;

    FTGlyph(FTFontStrike fTFontStrike, int n, boolean bl) {
        this.strike = fTFontStrike;
        this.glyphCode = n;
    }

    @Override
    public int getGlyphCode() {
        return this.glyphCode;
    }

    private void init() {
        if (this.bitmap != null) {
            return;
        }
        this.strike.initGlyph(this);
    }

    @Override
    public RectBounds getBBox() {
        float[] arrf = new float[4];
        FTFontFile fTFontFile = (FTFontFile)this.strike.getFontResource();
        fTFontFile.getGlyphBoundingBox(this.glyphCode, this.strike.getSize(), arrf);
        return new RectBounds(arrf[0], arrf[1], arrf[2], arrf[3]);
    }

    @Override
    public float getAdvance() {
        this.init();
        return this.userAdvance;
    }

    @Override
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.glyphCode);
    }

    @Override
    public byte[] getPixelData() {
        this.init();
        return this.buffer;
    }

    @Override
    public byte[] getPixelData(int n) {
        this.init();
        return this.buffer;
    }

    @Override
    public float getPixelXAdvance() {
        this.init();
        return this.advanceX;
    }

    @Override
    public float getPixelYAdvance() {
        this.init();
        return this.advanceY;
    }

    @Override
    public int getWidth() {
        this.init();
        return this.bitmap != null ? this.bitmap.width : 0;
    }

    @Override
    public int getHeight() {
        this.init();
        return this.bitmap != null ? this.bitmap.rows : 0;
    }

    @Override
    public int getOriginX() {
        this.init();
        return this.bitmap_left;
    }

    @Override
    public int getOriginY() {
        this.init();
        return -this.bitmap_top;
    }

    @Override
    public boolean isLCDGlyph() {
        return this.lcd;
    }
}

