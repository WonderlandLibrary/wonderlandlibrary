/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.coretext.CGAffineTransform;
import com.sun.javafx.font.coretext.CGRect;
import com.sun.javafx.font.coretext.CGSize;
import com.sun.javafx.font.coretext.CTFontFile;
import com.sun.javafx.font.coretext.CTFontStrike;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

class CTGlyph
implements Glyph {
    private CTFontStrike strike;
    private int glyphCode;
    private CGRect bounds;
    private double xAdvance;
    private double yAdvance;
    private boolean drawShapes;
    private static boolean LCD_CONTEXT = true;
    private static boolean CACHE_CONTEXT = true;
    private static long cachedContextRef;
    private static final int BITMAP_WIDTH = 256;
    private static final int BITMAP_HEIGHT = 256;
    private static final int MAX_SIZE = 320;
    private static final long GRAY_COLORSPACE;
    private static final long RGB_COLORSPACE;

    CTGlyph(CTFontStrike cTFontStrike, int n, boolean bl) {
        this.strike = cTFontStrike;
        this.glyphCode = n;
        this.drawShapes = bl;
    }

    @Override
    public int getGlyphCode() {
        return this.glyphCode;
    }

    @Override
    public RectBounds getBBox() {
        CGRect cGRect = this.strike.getBBox(this.glyphCode);
        if (cGRect == null) {
            return new RectBounds();
        }
        return new RectBounds((float)cGRect.origin.x, (float)cGRect.origin.y, (float)(cGRect.origin.x + cGRect.size.width), (float)(cGRect.origin.y + cGRect.size.height));
    }

    private void checkBounds() {
        if (this.bounds != null) {
            return;
        }
        this.bounds = new CGRect();
        if (this.strike.getSize() == 0.0f) {
            return;
        }
        long l = this.strike.getFontRef();
        if (l == 0L) {
            return;
        }
        int n = 0;
        CGSize cGSize = new CGSize();
        OS.CTFontGetAdvancesForGlyphs(l, n, (short)this.glyphCode, cGSize);
        this.xAdvance = cGSize.width;
        this.yAdvance = -cGSize.height;
        if (this.drawShapes) {
            return;
        }
        CTFontFile cTFontFile = (CTFontFile)this.strike.getFontResource();
        float[] arrf = new float[4];
        cTFontFile.getGlyphBoundingBox((short)this.glyphCode, this.strike.getSize(), arrf);
        this.bounds.origin.x = arrf[0];
        this.bounds.origin.y = arrf[1];
        this.bounds.size.width = arrf[2] - arrf[0];
        this.bounds.size.height = arrf[3] - arrf[1];
        if (this.strike.matrix != null) {
            OS.CGRectApplyAffineTransform(this.bounds, this.strike.matrix);
        }
        if (this.bounds.size.width < 0.0 || this.bounds.size.height < 0.0 || this.bounds.size.width > 320.0 || this.bounds.size.height > 320.0) {
            this.bounds.size.height = 0.0;
            this.bounds.size.width = 0.0;
            this.bounds.origin.y = 0.0;
            this.bounds.origin.x = 0.0;
        } else {
            this.bounds.origin.x = (int)Math.floor(this.bounds.origin.x) - 1;
            this.bounds.origin.y = (int)Math.floor(this.bounds.origin.y) - 1;
            this.bounds.size.width = (int)Math.ceil(this.bounds.size.width) + 1 + 1 + 1;
            this.bounds.size.height = (int)Math.ceil(this.bounds.size.height) + 1 + 1 + 1;
        }
    }

    @Override
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.glyphCode);
    }

    private long createContext(boolean bl, int n, int n2) {
        int n3;
        int n4;
        long l;
        int n5 = 8;
        if (bl) {
            l = RGB_COLORSPACE;
            n4 = n * 4;
            n3 = OS.kCGBitmapByteOrder32Host | 2;
        } else {
            l = GRAY_COLORSPACE;
            n4 = n;
            n3 = 0;
        }
        long l2 = OS.CGBitmapContextCreate(0L, n, n2, n5, n4, l, n3);
        boolean bl2 = this.strike.isSubPixelGlyph();
        OS.CGContextSetAllowsFontSmoothing(l2, bl);
        OS.CGContextSetAllowsAntialiasing(l2, true);
        OS.CGContextSetAllowsFontSubpixelPositioning(l2, bl2);
        OS.CGContextSetAllowsFontSubpixelQuantization(l2, bl2);
        return l2;
    }

    private long getCachedContext(boolean bl) {
        if (cachedContextRef == 0L) {
            cachedContextRef = this.createContext(bl, 256, 256);
        }
        return cachedContextRef;
    }

    private synchronized byte[] getImage(double d, double d2, int n, int n2, int n3) {
        byte[] arrby;
        long l;
        if (n == 0 || n2 == 0) {
            return new byte[0];
        }
        long l2 = this.strike.getFontRef();
        boolean bl = this.isLCDGlyph();
        boolean bl2 = LCD_CONTEXT || bl;
        CGAffineTransform cGAffineTransform = this.strike.matrix;
        boolean bl3 = CACHE_CONTEXT & 256 >= n & 256 >= n2;
        long l3 = l = bl3 ? this.getCachedContext(bl2) : this.createContext(bl2, n, n2);
        if (l == 0L) {
            return new byte[0];
        }
        OS.CGContextSetRGBFillColor(l, 1.0, 1.0, 1.0, 1.0);
        CGRect cGRect = new CGRect();
        cGRect.size.width = n;
        cGRect.size.height = n2;
        OS.CGContextFillRect(l, cGRect);
        double d3 = 0.0;
        double d4 = 0.0;
        if (cGAffineTransform != null) {
            OS.CGContextTranslateCTM(l, -d, -d2);
        } else {
            d3 = d - (double)this.strike.getSubPixelPosition(n3);
            d4 = d2;
        }
        OS.CGContextSetRGBFillColor(l, 0.0, 0.0, 0.0, 1.0);
        OS.CTFontDrawGlyphs(l2, (short)this.glyphCode, -d3, -d4, l);
        if (cGAffineTransform != null) {
            OS.CGContextTranslateCTM(l, d, d2);
        }
        if ((arrby = bl ? OS.CGBitmapContextGetData(l, n, n2, 24) : OS.CGBitmapContextGetData(l, n, n2, 8)) == null) {
            this.bounds = new CGRect();
            arrby = new byte[]{};
        }
        if (!bl3) {
            OS.CGContextRelease(l);
        }
        return arrby;
    }

    @Override
    public byte[] getPixelData() {
        return this.getPixelData(0);
    }

    @Override
    public byte[] getPixelData(int n) {
        this.checkBounds();
        return this.getImage(this.bounds.origin.x, this.bounds.origin.y, (int)this.bounds.size.width, (int)this.bounds.size.height, n);
    }

    @Override
    public float getAdvance() {
        this.checkBounds();
        return (float)this.xAdvance;
    }

    @Override
    public float getPixelXAdvance() {
        this.checkBounds();
        return (float)this.xAdvance;
    }

    @Override
    public float getPixelYAdvance() {
        this.checkBounds();
        return (float)this.yAdvance;
    }

    @Override
    public int getWidth() {
        this.checkBounds();
        int n = (int)this.bounds.size.width;
        return this.isLCDGlyph() ? n * 3 : n;
    }

    @Override
    public int getHeight() {
        this.checkBounds();
        return (int)this.bounds.size.height;
    }

    @Override
    public int getOriginX() {
        this.checkBounds();
        return (int)this.bounds.origin.x;
    }

    @Override
    public int getOriginY() {
        this.checkBounds();
        int n = (int)this.bounds.size.height;
        int n2 = (int)this.bounds.origin.y;
        return -n - n2;
    }

    @Override
    public boolean isLCDGlyph() {
        return this.strike.getAAMode() == 1;
    }

    static {
        GRAY_COLORSPACE = OS.CGColorSpaceCreateDeviceGray();
        RGB_COLORSPACE = OS.CGColorSpaceCreateDeviceRGB();
    }
}

