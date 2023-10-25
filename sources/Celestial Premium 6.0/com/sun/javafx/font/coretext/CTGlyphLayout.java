/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.coretext;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.coretext.CTFontStrike;
import com.sun.javafx.font.coretext.OS;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.TextRun;

class CTGlyphLayout
extends GlyphLayout {
    CTGlyphLayout() {
    }

    private long createCTLine(long l, char[] arrc, boolean bl, int n, int n2) {
        long l2 = OS.kCFAllocatorDefault();
        long l3 = OS.CFStringCreateWithCharacters(l2, arrc, n, n2);
        long l4 = 0L;
        if (l3 != 0L) {
            long l5 = OS.CFDictionaryCreateMutable(l2, 4L, OS.kCFTypeDictionaryKeyCallBacks(), OS.kCFTypeDictionaryValueCallBacks());
            if (l5 != 0L) {
                long l6;
                OS.CFDictionaryAddValue(l5, OS.kCTFontAttributeName(), l);
                if (bl && (l6 = OS.CTParagraphStyleCreate(1)) != 0L) {
                    OS.CFDictionaryAddValue(l5, OS.kCTParagraphStyleAttributeName(), l6);
                    OS.CFRelease(l6);
                }
                if ((l6 = OS.CFAttributedStringCreate(l2, l3, l5)) != 0L) {
                    l4 = OS.CTLineCreateWithAttributedString(l6);
                    OS.CFRelease(l6);
                }
                OS.CFRelease(l5);
            }
            OS.CFRelease(l3);
        }
        return l4;
    }

    private int getFontSlot(long l, CompositeFontResource compositeFontResource, String string, int n) {
        long l2 = OS.CTRunGetAttributes(l);
        if (l2 == 0L) {
            return -1;
        }
        long l3 = OS.CFDictionaryGetValue(l2, OS.kCTFontAttributeName());
        if (l3 == 0L) {
            return -1;
        }
        String string2 = OS.CTFontCopyAttributeDisplayName(l3);
        if (string2 == null) {
            return -1;
        }
        if (!string2.equalsIgnoreCase(string)) {
            if (compositeFontResource == null) {
                return -1;
            }
            n = compositeFontResource.getSlotForFont(string2);
            if (PrismFontFactory.debugFonts) {
                System.err.println("\tFallback font= " + string2 + " slot=" + n);
            }
        }
        return n;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        int n = 0;
        CompositeFontResource compositeFontResource = null;
        if (fontStrike instanceof CompositeStrike) {
            compositeFontResource = (CompositeFontResource)fontStrike.getFontResource();
            n = this.getInitialSlot(compositeFontResource);
            fontStrike = ((CompositeStrike)fontStrike).getStrikeSlot(n);
        }
        float f = fontStrike.getSize();
        String string = fontStrike.getFontResource().getFullName();
        long l = ((CTFontStrike)fontStrike).getFontRef();
        if (l == 0L) {
            return;
        }
        boolean bl = (textRun.getLevel() & 1) != 0;
        long l2 = this.createCTLine(l, arrc, bl, textRun.getStart(), textRun.getLength());
        if (l2 == 0L) {
            return;
        }
        long l3 = OS.CTLineGetGlyphRuns(l2);
        if (l3 != 0L) {
            int n2 = (int)OS.CTLineGetGlyphCount(l2);
            int[] arrn = new int[n2];
            float[] arrf = new float[n2 * 2 + 2];
            int[] arrn2 = new int[n2];
            long l4 = OS.CFArrayGetCount(l3);
            int n3 = 0;
            int n4 = 0;
            int n5 = 0;
            int n6 = 0;
            while ((long)n6 < l4) {
                long l5 = OS.CFArrayGetValueAtIndex(l3, n6);
                if (l5 != 0L) {
                    int n7 = this.getFontSlot(l5, compositeFontResource, string, n);
                    n3 = n7 != -1 ? (n3 += OS.CTRunGetGlyphs(l5, n7 << 24, n3, arrn)) : (n3 += OS.CTRunGetGlyphs(l5, 0, n3, arrn));
                    if (f > 0.0f) {
                        n4 += OS.CTRunGetPositions(l5, n4, arrf);
                    }
                    n5 += OS.CTRunGetStringIndices(l5, n5, arrn2);
                }
                ++n6;
            }
            if (f > 0.0f) {
                arrf[n4] = (float)OS.CTLineGetTypographicBounds(l2);
            }
            textRun.shape(n2, arrn, arrf, arrn2);
        }
        OS.CFRelease(l2);
    }
}

