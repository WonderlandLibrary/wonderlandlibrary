/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.directwrite.DWDisposer;
import com.sun.javafx.font.directwrite.DWFactory;
import com.sun.javafx.font.directwrite.DWFontStrike;
import com.sun.javafx.font.directwrite.DWRITE_GLYPH_METRICS;
import com.sun.javafx.font.directwrite.IDWriteFactory;
import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IDWriteFontCollection;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFamily;
import com.sun.javafx.font.directwrite.IDWriteFontFile;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

class DWFontFile
extends PrismFontFile {
    private IDWriteFontFace fontFace = this.createFontFace();
    private DWDisposer disposer;

    DWFontFile(String string, String string2, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        super(string, string2, n, bl, bl2, bl3, bl4);
        if (PrismFontFactory.debugFonts && this.fontFace == null) {
            System.err.println("Failed to create IDWriteFontFace for " + this);
        }
        if (bl3) {
            this.disposer = new DWDisposer(this.fontFace);
            Disposer.addRecord(this, this.disposer);
        }
    }

    private IDWriteFontFace createEmbeddedFontFace() {
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteFontFile iDWriteFontFile = iDWriteFactory.CreateFontFileReference(this.getFileName());
        if (iDWriteFontFile == null) {
            return null;
        }
        boolean[] arrbl = new boolean[1];
        int[] arrn = new int[1];
        int[] arrn2 = new int[1];
        int[] arrn3 = new int[1];
        int n = iDWriteFontFile.Analyze(arrbl, arrn, arrn2, arrn3);
        IDWriteFontFace iDWriteFontFace = null;
        if (n == 0 && arrbl[0]) {
            int n2 = this.getFontIndex();
            int n3 = 0;
            iDWriteFontFace = iDWriteFactory.CreateFontFace(arrn2[0], iDWriteFontFile, n2, n3);
        }
        iDWriteFontFile.Release();
        return iDWriteFontFace;
    }

    private IDWriteFontFace createFontFace() {
        if (this.isEmbeddedFont()) {
            return this.createEmbeddedFontFace();
        }
        IDWriteFontCollection iDWriteFontCollection = DWFactory.getFontCollection();
        int n = iDWriteFontCollection.FindFamilyName(this.getFamilyName());
        if (n == -1) {
            return this.createEmbeddedFontFace();
        }
        IDWriteFontFamily iDWriteFontFamily = iDWriteFontCollection.GetFontFamily(n);
        if (iDWriteFontFamily == null) {
            return null;
        }
        int n2 = this.isBold() ? 700 : 400;
        int n3 = 5;
        int n4 = this.isItalic() ? 2 : 0;
        IDWriteFont iDWriteFont = iDWriteFontFamily.GetFirstMatchingFont(n2, n3, n4);
        iDWriteFontFamily.Release();
        if (iDWriteFont == null) {
            return null;
        }
        IDWriteFontFace iDWriteFontFace = iDWriteFont.CreateFontFace();
        iDWriteFont.Release();
        return iDWriteFontFace;
    }

    IDWriteFontFace getFontFace() {
        return this.fontFace;
    }

    Path2D getGlyphOutline(int n, float f) {
        if (this.fontFace == null) {
            return null;
        }
        if (f == 0.0f) {
            return new Path2D();
        }
        return this.fontFace.GetGlyphRunOutline(f, (short)n, false);
    }

    RectBounds getBBox(int n, float f) {
        float[] arrf = new float[4];
        this.getGlyphBoundingBox(n, f, arrf);
        return new RectBounds(arrf[0], arrf[1], arrf[2], arrf[3]);
    }

    @Override
    protected int[] createGlyphBoundingBox(int n) {
        if (this.fontFace == null) {
            return null;
        }
        DWRITE_GLYPH_METRICS dWRITE_GLYPH_METRICS = this.fontFace.GetDesignGlyphMetrics((short)n, false);
        if (dWRITE_GLYPH_METRICS == null) {
            return null;
        }
        int[] arrn = new int[]{dWRITE_GLYPH_METRICS.leftSideBearing, dWRITE_GLYPH_METRICS.verticalOriginY - dWRITE_GLYPH_METRICS.advanceHeight + dWRITE_GLYPH_METRICS.bottomSideBearing, dWRITE_GLYPH_METRICS.advanceWidth - dWRITE_GLYPH_METRICS.rightSideBearing, dWRITE_GLYPH_METRICS.verticalOriginY - dWRITE_GLYPH_METRICS.topSideBearing};
        return arrn;
    }

    @Override
    protected PrismFontStrike<DWFontFile> createStrike(float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        return new DWFontStrike(this, f, baseTransform, n, fontStrikeDesc);
    }

    @Override
    protected synchronized void disposeOnShutdown() {
        if (this.fontFace != null) {
            if (this.disposer != null) {
                this.disposer.dispose();
            } else {
                this.fontFace.Release();
                if (PrismFontFactory.debugFonts) {
                    System.err.println("null disposer for " + this.fontFace);
                }
            }
            if (PrismFontFactory.debugFonts) {
                System.err.println("fontFace freed: " + this.fontFace);
            }
            this.fontFace = null;
        }
        super.disposeOnShutdown();
    }
}

