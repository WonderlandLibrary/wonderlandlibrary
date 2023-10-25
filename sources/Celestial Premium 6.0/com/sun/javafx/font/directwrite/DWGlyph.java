/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.directwrite.D2D1_COLOR_F;
import com.sun.javafx.font.directwrite.D2D1_MATRIX_3X2_F;
import com.sun.javafx.font.directwrite.D2D1_POINT_2F;
import com.sun.javafx.font.directwrite.D2D1_RENDER_TARGET_PROPERTIES;
import com.sun.javafx.font.directwrite.DWFactory;
import com.sun.javafx.font.directwrite.DWFontStrike;
import com.sun.javafx.font.directwrite.DWRITE_GLYPH_METRICS;
import com.sun.javafx.font.directwrite.DWRITE_GLYPH_RUN;
import com.sun.javafx.font.directwrite.DWRITE_MATRIX;
import com.sun.javafx.font.directwrite.ID2D1Brush;
import com.sun.javafx.font.directwrite.ID2D1Factory;
import com.sun.javafx.font.directwrite.ID2D1RenderTarget;
import com.sun.javafx.font.directwrite.IDWriteFactory;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteGlyphRunAnalysis;
import com.sun.javafx.font.directwrite.IWICBitmap;
import com.sun.javafx.font.directwrite.IWICBitmapLock;
import com.sun.javafx.font.directwrite.IWICImagingFactory;
import com.sun.javafx.font.directwrite.RECT;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

public class DWGlyph
implements Glyph {
    private DWFontStrike strike;
    private DWRITE_GLYPH_METRICS metrics;
    private DWRITE_GLYPH_RUN run;
    private float pixelXAdvance;
    private float pixelYAdvance;
    private RECT rect;
    private boolean drawShapes;
    private byte[][] pixelData;
    private RECT[] rects;
    private static final boolean CACHE_TARGET = true;
    private static IWICBitmap cachedBitmap;
    private static ID2D1RenderTarget cachedTarget;
    private static final int BITMAP_WIDTH = 256;
    private static final int BITMAP_HEIGHT = 256;
    private static final int BITMAP_PIXEL_FORMAT = 8;
    private static D2D1_COLOR_F BLACK;
    private static D2D1_COLOR_F WHITE;
    private static D2D1_MATRIX_3X2_F D2D2_MATRIX_IDENTITY;
    public static final int SHORTMASK = 65535;

    DWGlyph(DWFontStrike dWFontStrike, int n, boolean bl) {
        this.strike = dWFontStrike;
        this.drawShapes = bl;
        int n2 = DWFontStrike.SUBPIXEL_Y ? 9 : 3;
        this.pixelData = new byte[n2][];
        this.rects = new RECT[n2];
        IDWriteFontFace iDWriteFontFace = dWFontStrike.getFontFace();
        this.run = new DWRITE_GLYPH_RUN();
        this.run.fontFace = iDWriteFontFace != null ? iDWriteFontFace.ptr : 0L;
        this.run.fontEmSize = dWFontStrike.getSize();
        this.run.glyphIndices = (short)n;
        this.run.glyphAdvances = 0.0f;
        this.run.advanceOffset = 0.0f;
        this.run.ascenderOffset = 0.0f;
        this.run.bidiLevel = 0;
        this.run.isSideways = false;
    }

    void checkMetrics() {
        if (this.metrics != null) {
            return;
        }
        IDWriteFontFace iDWriteFontFace = this.strike.getFontFace();
        if (iDWriteFontFace == null) {
            return;
        }
        this.metrics = iDWriteFontFace.GetDesignGlyphMetrics(this.run.glyphIndices, false);
        if (this.metrics != null) {
            float f = this.strike.getUpem();
            this.pixelXAdvance = (float)this.metrics.advanceWidth * this.strike.getSize() / f;
            this.pixelYAdvance = 0.0f;
            if (this.strike.matrix != null) {
                Point2D point2D = new Point2D(this.pixelXAdvance, this.pixelYAdvance);
                this.strike.getTransform().transform(point2D, point2D);
                this.pixelXAdvance = point2D.x;
                this.pixelYAdvance = point2D.y;
            }
        }
    }

    void checkBounds() {
        if (this.rect != null) {
            return;
        }
        int n = 1;
        IDWriteGlyphRunAnalysis iDWriteGlyphRunAnalysis = this.createAnalysis(0.0f, 0.0f);
        if (iDWriteGlyphRunAnalysis != null) {
            this.rect = iDWriteGlyphRunAnalysis.GetAlphaTextureBounds(n);
            if (this.rect == null || this.rect.right - this.rect.left == 0 || this.rect.bottom - this.rect.top == 0) {
                this.rect = iDWriteGlyphRunAnalysis.GetAlphaTextureBounds(0);
            }
            iDWriteGlyphRunAnalysis.Release();
        }
        if (this.rect == null) {
            this.rect = new RECT();
        } else {
            --this.rect.left;
            --this.rect.top;
            ++this.rect.right;
            ++this.rect.bottom;
        }
    }

    byte[] getLCDMask(float f, float f2) {
        IDWriteGlyphRunAnalysis iDWriteGlyphRunAnalysis = this.createAnalysis(f, f2);
        byte[] arrby = null;
        if (iDWriteGlyphRunAnalysis != null) {
            int n = 1;
            this.rect = iDWriteGlyphRunAnalysis.GetAlphaTextureBounds(n);
            if (this.rect != null && this.rect.right - this.rect.left != 0 && this.rect.bottom - this.rect.top != 0) {
                arrby = iDWriteGlyphRunAnalysis.CreateAlphaTexture(n, this.rect);
            } else {
                this.rect = iDWriteGlyphRunAnalysis.GetAlphaTextureBounds(0);
                if (this.rect != null && this.rect.right - this.rect.left != 0 && this.rect.bottom - this.rect.top != 0) {
                    arrby = this.getD2DMask(f, f2, true);
                }
            }
            iDWriteGlyphRunAnalysis.Release();
        }
        if (arrby == null) {
            arrby = new byte[]{};
            this.rect = new RECT();
        }
        return arrby;
    }

    byte[] getD2DMask(float f, float f2, boolean bl) {
        D2D1_MATRIX_3X2_F d2D1_MATRIX_3X2_F;
        ID2D1RenderTarget iD2D1RenderTarget;
        IWICBitmap iWICBitmap;
        boolean bl2;
        this.checkBounds();
        if (this.getWidth() == 0 || this.getHeight() == 0 || this.run.fontFace == 0L) {
            return new byte[0];
        }
        float f3 = this.rect.left;
        float f4 = this.rect.top;
        int n = this.rect.right - this.rect.left;
        int n2 = this.rect.bottom - this.rect.top;
        boolean bl3 = bl2 = 256 >= n && 256 >= n2;
        if (bl2) {
            iWICBitmap = this.getCachedBitmap();
            iD2D1RenderTarget = this.getCachedRenderingTarget();
        } else {
            iWICBitmap = this.createBitmap(n, n2);
            iD2D1RenderTarget = this.createRenderingTarget(iWICBitmap);
        }
        if (iWICBitmap == null || iD2D1RenderTarget == null) {
            return new byte[0];
        }
        DWRITE_MATRIX dWRITE_MATRIX = this.strike.matrix;
        if (dWRITE_MATRIX != null) {
            d2D1_MATRIX_3X2_F = new D2D1_MATRIX_3X2_F(dWRITE_MATRIX.m11, dWRITE_MATRIX.m12, dWRITE_MATRIX.m21, dWRITE_MATRIX.m22, -f3 + f, -f4 + f2);
            f4 = 0.0f;
            f3 = 0.0f;
        } else {
            d2D1_MATRIX_3X2_F = D2D2_MATRIX_IDENTITY;
            f3 -= f;
            f4 -= f2;
        }
        iD2D1RenderTarget.BeginDraw();
        iD2D1RenderTarget.SetTransform(d2D1_MATRIX_3X2_F);
        iD2D1RenderTarget.Clear(WHITE);
        D2D1_POINT_2F d2D1_POINT_2F = new D2D1_POINT_2F(-f3, -f4);
        ID2D1Brush iD2D1Brush = iD2D1RenderTarget.CreateSolidColorBrush(BLACK);
        if (!bl) {
            iD2D1RenderTarget.SetTextAntialiasMode(2);
        }
        iD2D1RenderTarget.DrawGlyphRun(d2D1_POINT_2F, this.run, iD2D1Brush, 0);
        int n3 = iD2D1RenderTarget.EndDraw();
        iD2D1Brush.Release();
        if (n3 != 0) {
            iWICBitmap.Release();
            cachedBitmap = null;
            iD2D1RenderTarget.Release();
            cachedTarget = null;
            if (PrismFontFactory.debugFonts) {
                System.err.println("Rendering failed=" + n3);
            }
            this.rect.bottom = 0;
            this.rect.right = 0;
            this.rect.top = 0;
            this.rect.left = 0;
            return null;
        }
        byte[] arrby = null;
        IWICBitmapLock iWICBitmapLock = iWICBitmap.Lock(0, 0, n, n2, 1);
        if (iWICBitmapLock != null) {
            byte[] arrby2 = iWICBitmapLock.GetDataPointer();
            if (arrby2 != null) {
                int n4 = iWICBitmapLock.GetStride();
                int n5 = 0;
                int n6 = 0;
                int n7 = -1;
                if (bl) {
                    arrby = new byte[n * n2 * 3];
                    for (int i = 0; i < n2; ++i) {
                        int n8 = n6;
                        for (int j = 0; j < n; ++j) {
                            arrby[n5++] = (byte)(n7 - arrby2[n8++]);
                            arrby[n5++] = (byte)(n7 - arrby2[n8++]);
                            arrby[n5++] = (byte)(n7 - arrby2[n8++]);
                            ++n8;
                        }
                        n6 += n4;
                    }
                } else {
                    arrby = new byte[n * n2];
                    for (int i = 0; i < n2; ++i) {
                        int n9 = n6;
                        for (int j = 0; j < n; ++j) {
                            arrby[n5++] = (byte)(n7 - arrby2[n9]);
                            n9 += 4;
                        }
                        n6 += n4;
                    }
                }
            }
            iWICBitmapLock.Release();
        }
        if (!bl2) {
            iWICBitmap.Release();
            iD2D1RenderTarget.Release();
        }
        return arrby;
    }

    IDWriteGlyphRunAnalysis createAnalysis(float f, float f2) {
        if (this.run.fontFace == 0L) {
            return null;
        }
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        int n = DWFontStrike.SUBPIXEL_Y ? 5 : 4;
        int n2 = 0;
        DWRITE_MATRIX dWRITE_MATRIX = this.strike.matrix;
        float f3 = 1.0f;
        return iDWriteFactory.CreateGlyphRunAnalysis(this.run, f3, dWRITE_MATRIX, n, n2, f, f2);
    }

    IWICBitmap getCachedBitmap() {
        if (cachedBitmap == null) {
            cachedBitmap = this.createBitmap(256, 256);
        }
        return cachedBitmap;
    }

    ID2D1RenderTarget getCachedRenderingTarget() {
        if (cachedTarget == null) {
            cachedTarget = this.createRenderingTarget(this.getCachedBitmap());
        }
        return cachedTarget;
    }

    IWICBitmap createBitmap(int n, int n2) {
        IWICImagingFactory iWICImagingFactory = DWFactory.getWICFactory();
        return iWICImagingFactory.CreateBitmap(n, n2, 8, 1);
    }

    ID2D1RenderTarget createRenderingTarget(IWICBitmap iWICBitmap) {
        D2D1_RENDER_TARGET_PROPERTIES d2D1_RENDER_TARGET_PROPERTIES = new D2D1_RENDER_TARGET_PROPERTIES();
        d2D1_RENDER_TARGET_PROPERTIES.type = 0;
        d2D1_RENDER_TARGET_PROPERTIES.pixelFormat.format = 0;
        d2D1_RENDER_TARGET_PROPERTIES.pixelFormat.alphaMode = 0;
        d2D1_RENDER_TARGET_PROPERTIES.dpiX = 0.0f;
        d2D1_RENDER_TARGET_PROPERTIES.dpiY = 0.0f;
        d2D1_RENDER_TARGET_PROPERTIES.usage = 0;
        d2D1_RENDER_TARGET_PROPERTIES.minLevel = 0;
        ID2D1Factory iD2D1Factory = DWFactory.getD2DFactory();
        return iD2D1Factory.CreateWicBitmapRenderTarget(iWICBitmap, d2D1_RENDER_TARGET_PROPERTIES);
    }

    @Override
    public int getGlyphCode() {
        return this.run.glyphIndices & 0xFFFF;
    }

    @Override
    public RectBounds getBBox() {
        return this.strike.getBBox(this.run.glyphIndices & 0xFFFF);
    }

    @Override
    public float getAdvance() {
        this.checkMetrics();
        if (this.metrics == null) {
            return 0.0f;
        }
        float f = this.strike.getUpem();
        return (float)this.metrics.advanceWidth * this.strike.getSize() / f;
    }

    @Override
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.run.glyphIndices & 0xFFFF);
    }

    @Override
    public byte[] getPixelData() {
        return this.getPixelData(0);
    }

    @Override
    public byte[] getPixelData(int n) {
        byte[] arrby = this.pixelData[n];
        if (arrby == null) {
            float f = 0.0f;
            float f2 = 0.0f;
            int n2 = n;
            if (n2 >= 6) {
                n2 -= 6;
                f2 = 0.66f;
            } else if (n2 >= 3) {
                n2 -= 3;
                f2 = 0.33f;
            }
            if (n2 == 1) {
                f = 0.33f;
            }
            if (n2 == 2) {
                f = 0.66f;
            }
            arrby = this.isLCDGlyph() ? this.getLCDMask(f, f2) : this.getD2DMask(f, f2, false);
            this.pixelData[n] = arrby;
            this.rects[n] = this.rect;
        } else {
            this.rect = this.rects[n];
        }
        return arrby;
    }

    @Override
    public float getPixelXAdvance() {
        this.checkMetrics();
        return this.pixelXAdvance;
    }

    @Override
    public float getPixelYAdvance() {
        this.checkMetrics();
        return this.pixelYAdvance;
    }

    @Override
    public int getWidth() {
        this.checkBounds();
        return (this.rect.right - this.rect.left) * (this.isLCDGlyph() ? 3 : 1);
    }

    @Override
    public int getHeight() {
        this.checkBounds();
        return this.rect.bottom - this.rect.top;
    }

    @Override
    public int getOriginX() {
        this.checkBounds();
        return this.rect.left;
    }

    @Override
    public int getOriginY() {
        this.checkBounds();
        return this.rect.top;
    }

    @Override
    public boolean isLCDGlyph() {
        return this.strike.getAAMode() == 1;
    }

    static {
        BLACK = new D2D1_COLOR_F(0.0f, 0.0f, 0.0f, 1.0f);
        WHITE = new D2D1_COLOR_F(1.0f, 1.0f, 1.0f, 1.0f);
        D2D2_MATRIX_IDENTITY = new D2D1_MATRIX_3X2_F(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);
    }
}

