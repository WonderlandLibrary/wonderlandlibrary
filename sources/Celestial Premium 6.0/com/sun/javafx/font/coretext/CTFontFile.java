/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.font.coretext.CGAffineTransform;
import com.sun.javafx.font.coretext.CGRect;
import com.sun.javafx.font.coretext.CTFontStrike;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

class CTFontFile
extends PrismFontFile {
    private final long cgFontRef;
    private static final CGAffineTransform tx = new CGAffineTransform();

    CTFontFile(String string, String string2, int n, boolean bl, boolean bl2, boolean bl3, boolean bl4) throws Exception {
        super(string, string2, n, bl, bl2, bl3, bl4);
        if (bl2) {
            this.cgFontRef = this.createCGFontForEmbeddedFont();
            Disposer.addRecord(this, new SelfDisposerRecord(this.cgFontRef));
        } else {
            this.cgFontRef = 0L;
        }
    }

    public static boolean registerFont(String string) {
        if (string == null) {
            return false;
        }
        long l = OS.kCFAllocatorDefault();
        boolean bl = false;
        long l2 = OS.CFStringCreate(string);
        if (l2 != 0L) {
            int n = 0;
            long l3 = OS.CFURLCreateWithFileSystemPath(l, l2, n, false);
            if (l3 != 0L) {
                int n2 = 1;
                bl = OS.CTFontManagerRegisterFontsForURL(l3, n2, 0L);
                OS.CFRelease(l3);
            }
            OS.CFRelease(l2);
        }
        return bl;
    }

    private long createCGFontForEmbeddedFont() {
        long l = 0L;
        long l2 = OS.CFStringCreate(this.getFileName());
        if (l2 != 0L) {
            long l3 = OS.CFURLCreateWithFileSystemPath(OS.kCFAllocatorDefault(), l2, 0L, false);
            if (l3 != 0L) {
                long l4 = OS.CGDataProviderCreateWithURL(l3);
                if (l4 != 0L) {
                    l = OS.CGFontCreateWithDataProvider(l4);
                    OS.CFRelease(l4);
                }
                OS.CFRelease(l3);
            }
            OS.CFRelease(l2);
        }
        return l;
    }

    long getCGFontRef() {
        return this.cgFontRef;
    }

    CGRect getBBox(int n, float f) {
        CTFontStrike cTFontStrike = (CTFontStrike)this.getStrike(f, BaseTransform.IDENTITY_TRANSFORM);
        long l = cTFontStrike.getFontRef();
        if (l == 0L) {
            return null;
        }
        long l2 = OS.CTFontCreatePathForGlyph(l, (short)n, tx);
        if (l2 == 0L) {
            return null;
        }
        CGRect cGRect = OS.CGPathGetPathBoundingBox(l2);
        OS.CGPathRelease(l2);
        return cGRect;
    }

    Path2D getGlyphOutline(int n, float f) {
        CTFontStrike cTFontStrike = (CTFontStrike)this.getStrike(f, BaseTransform.IDENTITY_TRANSFORM);
        long l = cTFontStrike.getFontRef();
        if (l == 0L) {
            return null;
        }
        long l2 = OS.CTFontCreatePathForGlyph(l, (short)n, tx);
        if (l2 == 0L) {
            return null;
        }
        Path2D path2D = OS.CGPathApply(l2);
        OS.CGPathRelease(l2);
        return path2D;
    }

    @Override
    protected int[] createGlyphBoundingBox(int n) {
        short s;
        float f = 12.0f;
        CTFontStrike cTFontStrike = (CTFontStrike)this.getStrike(f, BaseTransform.IDENTITY_TRANSFORM);
        long l = cTFontStrike.getFontRef();
        if (l == 0L) {
            return null;
        }
        int[] arrn = new int[4];
        if (!this.isCFF() && OS.CTFontGetBoundingRectForGlyphUsingTables(l, (short)n, s = this.getIndexToLocFormat(), arrn)) {
            return arrn;
        }
        long l2 = OS.CTFontCreatePathForGlyph(l, (short)n, null);
        if (l2 == 0L) {
            return null;
        }
        CGRect cGRect = OS.CGPathGetPathBoundingBox(l2);
        OS.CGPathRelease(l2);
        float f2 = (float)this.getUnitsPerEm() / f;
        arrn[0] = (int)Math.round(cGRect.origin.x * (double)f2);
        arrn[1] = (int)Math.round(cGRect.origin.y * (double)f2);
        arrn[2] = (int)Math.round((cGRect.origin.x + cGRect.size.width) * (double)f2);
        arrn[3] = (int)Math.round((cGRect.origin.y + cGRect.size.height) * (double)f2);
        return arrn;
    }

    @Override
    protected PrismFontStrike<CTFontFile> createStrike(float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        return new CTFontStrike(this, f, baseTransform, n, fontStrikeDesc);
    }

    static {
        CTFontFile.tx.a = 1.0;
        CTFontFile.tx.d = -1.0;
    }

    private static class SelfDisposerRecord
    implements DisposerRecord {
        private long cgFontRef;

        SelfDisposerRecord(long l) {
            this.cgFontRef = l;
        }

        @Override
        public synchronized void dispose() {
            if (this.cgFontRef != 0L) {
                OS.CFRelease(this.cgFontRef);
                this.cgFontRef = 0L;
            }
        }
    }
}

