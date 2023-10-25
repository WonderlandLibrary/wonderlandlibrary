/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.directwrite.DWFontFile;
import com.sun.javafx.font.directwrite.DWGlyph;
import com.sun.javafx.font.directwrite.DWRITE_MATRIX;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

class DWFontStrike
extends PrismFontStrike<DWFontFile> {
    DWRITE_MATRIX matrix;
    static final boolean SUBPIXEL_ON;
    static final boolean SUBPIXEL_Y;
    static final boolean SUBPIXEL_NATIVE;

    DWFontStrike(DWFontFile dWFontFile, float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        super(dWFontFile, f, baseTransform, n, fontStrikeDesc);
        float f2 = PrismFontFactory.getFontSizeLimit();
        if (baseTransform.isTranslateOrIdentity()) {
            this.drawShapes = f > f2;
        } else {
            BaseTransform baseTransform2 = this.getTransform();
            this.matrix = new DWRITE_MATRIX();
            this.matrix.m11 = (float)baseTransform2.getMxx();
            this.matrix.m12 = (float)baseTransform2.getMyx();
            this.matrix.m21 = (float)baseTransform2.getMxy();
            this.matrix.m22 = (float)baseTransform2.getMyy();
            if (Math.abs(this.matrix.m11 * f) > f2 || Math.abs(this.matrix.m12 * f) > f2 || Math.abs(this.matrix.m21 * f) > f2 || Math.abs(this.matrix.m22 * f) > f2) {
                this.drawShapes = true;
            }
        }
    }

    @Override
    protected DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc) {
        return null;
    }

    @Override
    public int getQuantizedPosition(Point2D point2D) {
        if (SUBPIXEL_ON && (this.matrix == null || SUBPIXEL_NATIVE) && (this.getAAMode() == 0 || SUBPIXEL_NATIVE)) {
            float f = point2D.x;
            point2D.x = (int)point2D.x;
            f -= point2D.x;
            int n = 0;
            if (f >= 0.66f) {
                n = 2;
            } else if (f >= 0.33f) {
                n = 1;
            }
            if (SUBPIXEL_Y) {
                f = point2D.y;
                point2D.y = (int)point2D.y;
                if ((f -= point2D.y) >= 0.66f) {
                    n += 6;
                } else if (f >= 0.33f) {
                    n += 3;
                }
            } else {
                point2D.y = Math.round(point2D.y);
            }
            return n;
        }
        return super.getQuantizedPosition(point2D);
    }

    IDWriteFontFace getFontFace() {
        DWFontFile dWFontFile = (DWFontFile)this.getFontResource();
        return dWFontFile.getFontFace();
    }

    RectBounds getBBox(int n) {
        DWFontFile dWFontFile = (DWFontFile)this.getFontResource();
        return dWFontFile.getBBox(n, this.getSize());
    }

    int getUpem() {
        return ((DWFontFile)this.getFontResource()).getUnitsPerEm();
    }

    @Override
    protected Path2D createGlyphOutline(int n) {
        DWFontFile dWFontFile = (DWFontFile)this.getFontResource();
        return dWFontFile.getGlyphOutline(n, this.getSize());
    }

    @Override
    protected Glyph createGlyph(int n) {
        return new DWGlyph(this, n, this.drawShapes);
    }

    static {
        int n = PrismFontFactory.getFontFactory().getSubPixelMode();
        SUBPIXEL_ON = (n & 1) != 0;
        SUBPIXEL_Y = (n & 2) != 0;
        SUBPIXEL_NATIVE = (n & 4) != 0;
    }
}

