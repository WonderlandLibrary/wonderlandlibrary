/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.coretext.CGAffineTransform;
import com.sun.javafx.font.coretext.CGRect;
import com.sun.javafx.font.coretext.CTFontFile;
import com.sun.javafx.font.coretext.CTGlyph;
import com.sun.javafx.font.coretext.CTStrikeDisposer;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;

class CTFontStrike
extends PrismFontStrike<CTFontFile> {
    private long fontRef;
    CGAffineTransform matrix;
    static final float SUBPIXEL4_SIZE = 12.0f;
    static final float SUBPIXEL3_SIZE = 18.0f;
    static final float SUBPIXEL2_SIZE = 34.0f;
    private static final boolean SUBPIXEL;

    CTFontStrike(CTFontFile cTFontFile, float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        super(cTFontFile, f, baseTransform, n, fontStrikeDesc);
        float f2 = PrismFontFactory.getFontSizeLimit();
        if (baseTransform.isTranslateOrIdentity()) {
            this.drawShapes = f > f2;
        } else {
            BaseTransform baseTransform2 = this.getTransform();
            this.matrix = new CGAffineTransform();
            this.matrix.a = baseTransform2.getMxx();
            this.matrix.b = -baseTransform2.getMyx();
            this.matrix.c = -baseTransform2.getMxy();
            this.matrix.d = baseTransform2.getMyy();
            if (Math.abs(this.matrix.a * (double)f) > (double)f2 || Math.abs(this.matrix.b * (double)f) > (double)f2 || Math.abs(this.matrix.c * (double)f) > (double)f2 || Math.abs(this.matrix.d * (double)f) > (double)f2) {
                this.drawShapes = true;
            }
        }
        if (cTFontFile.isEmbeddedFont()) {
            long l = cTFontFile.getCGFontRef();
            if (l != 0L) {
                this.fontRef = OS.CTFontCreateWithGraphicsFont(l, f, this.matrix, 0L);
            }
        } else {
            long l = OS.CFStringCreate(cTFontFile.getPSName());
            if (l != 0L) {
                this.fontRef = OS.CTFontCreateWithName(l, f, this.matrix);
                OS.CFRelease(l);
            }
        }
        if (this.fontRef == 0L && PrismFontFactory.debugFonts) {
            System.err.println("Failed to create CTFont for " + this);
        }
    }

    long getFontRef() {
        return this.fontRef;
    }

    @Override
    protected DisposerRecord createDisposer(FontStrikeDesc fontStrikeDesc) {
        CTFontFile cTFontFile = (CTFontFile)this.getFontResource();
        return new CTStrikeDisposer(cTFontFile, fontStrikeDesc, this.fontRef);
    }

    @Override
    protected Glyph createGlyph(int n) {
        return new CTGlyph(this, n, this.drawShapes);
    }

    @Override
    public int getQuantizedPosition(Point2D point2D) {
        if (SUBPIXEL && this.matrix == null) {
            if (this.getSize() < 12.0f) {
                float f = point2D.x;
                point2D.x = (int)point2D.x;
                f -= point2D.x;
                point2D.y = Math.round(point2D.y);
                if (f >= 0.75f) {
                    return 3;
                }
                if (f >= 0.5f) {
                    return 2;
                }
                if (f >= 0.25f) {
                    return 1;
                }
                return 0;
            }
            if (this.getAAMode() == 0) {
                if (this.getSize() < 18.0f) {
                    float f = point2D.x;
                    point2D.x = (int)point2D.x;
                    f -= point2D.x;
                    point2D.y = Math.round(point2D.y);
                    if (f >= 0.66f) {
                        return 2;
                    }
                    if (f >= 0.33f) {
                        return 1;
                    }
                    return 0;
                }
                if (this.getSize() < 34.0f) {
                    float f = point2D.x;
                    point2D.x = (int)point2D.x;
                    f -= point2D.x;
                    point2D.y = Math.round(point2D.y);
                    if (f >= 0.5f) {
                        return 1;
                    }
                }
                return 0;
            }
        }
        return super.getQuantizedPosition(point2D);
    }

    float getSubPixelPosition(int n) {
        if (n == 0) {
            return 0.0f;
        }
        float f = this.getSize();
        if (f < 12.0f) {
            if (n == 3) {
                return 0.75f;
            }
            if (n == 2) {
                return 0.5f;
            }
            if (n == 1) {
                return 0.25f;
            }
            return 0.0f;
        }
        if (this.getAAMode() == 1) {
            return 0.0f;
        }
        if (f < 18.0f) {
            if (n == 2) {
                return 0.66f;
            }
            if (n == 1) {
                return 0.33f;
            }
            return 0.0f;
        }
        if (f < 34.0f && n == 1) {
            return 0.5f;
        }
        return 0.0f;
    }

    boolean isSubPixelGlyph() {
        return SUBPIXEL && this.matrix == null;
    }

    @Override
    protected Path2D createGlyphOutline(int n) {
        CTFontFile cTFontFile = (CTFontFile)this.getFontResource();
        return cTFontFile.getGlyphOutline(n, this.getSize());
    }

    CGRect getBBox(int n) {
        CTFontFile cTFontFile = (CTFontFile)this.getFontResource();
        return cTFontFile.getBBox(n, this.getSize());
    }

    static {
        int n = PrismFontFactory.getFontFactory().getSubPixelMode();
        SUBPIXEL = (n & 1) != 0;
    }
}

