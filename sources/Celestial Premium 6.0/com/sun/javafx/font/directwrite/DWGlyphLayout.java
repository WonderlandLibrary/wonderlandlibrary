/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.directwrite.DWFactory;
import com.sun.javafx.font.directwrite.DWFontFile;
import com.sun.javafx.font.directwrite.DWRITE_SCRIPT_ANALYSIS;
import com.sun.javafx.font.directwrite.IDWriteFactory;
import com.sun.javafx.font.directwrite.IDWriteFont;
import com.sun.javafx.font.directwrite.IDWriteFontCollection;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IDWriteFontFamily;
import com.sun.javafx.font.directwrite.IDWriteLocalizedStrings;
import com.sun.javafx.font.directwrite.IDWriteTextAnalyzer;
import com.sun.javafx.font.directwrite.IDWriteTextFormat;
import com.sun.javafx.font.directwrite.IDWriteTextLayout;
import com.sun.javafx.font.directwrite.JFXTextAnalysisSink;
import com.sun.javafx.font.directwrite.JFXTextRenderer;
import com.sun.javafx.font.directwrite.OS;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.PrismTextLayout;
import com.sun.javafx.text.TextRun;
import java.util.Arrays;

public class DWGlyphLayout
extends GlyphLayout {
    private static final String LOCALE = "en-us";

    @Override
    protected TextRun addTextRun(PrismTextLayout prismTextLayout, char[] arrc, int n, int n2, PGFont pGFont, TextSpan textSpan, byte by) {
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteTextAnalyzer iDWriteTextAnalyzer = iDWriteFactory.CreateTextAnalyzer();
        if (iDWriteTextAnalyzer == null) {
            return new TextRun(n, n2, by, false, 0, textSpan, 0, false);
        }
        int n3 = (by & 1) != 0 ? 1 : 0;
        JFXTextAnalysisSink jFXTextAnalysisSink = OS.NewJFXTextAnalysisSink(arrc, n, n2, LOCALE, n3);
        if (jFXTextAnalysisSink == null) {
            return new TextRun(n, n2, by, false, 0, textSpan, 0, false);
        }
        jFXTextAnalysisSink.AddRef();
        TextRun textRun = null;
        int n4 = iDWriteTextAnalyzer.AnalyzeScript(jFXTextAnalysisSink, 0, n2, jFXTextAnalysisSink);
        if (n4 == 0) {
            while (jFXTextAnalysisSink.Next()) {
                int n5 = jFXTextAnalysisSink.GetStart();
                int n6 = jFXTextAnalysisSink.GetLength();
                DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS = jFXTextAnalysisSink.GetAnalysis();
                textRun = new TextRun(n + n5, n6, by, true, dWRITE_SCRIPT_ANALYSIS.script, textSpan, dWRITE_SCRIPT_ANALYSIS.shapes, false);
                prismTextLayout.addTextRun(textRun);
            }
        }
        iDWriteTextAnalyzer.Release();
        jFXTextAnalysisSink.Release();
        return textRun;
    }

    @Override
    public void layout(TextRun textRun, PGFont pGFont, FontStrike fontStrike, char[] arrc) {
        int n;
        IDWriteFontFace iDWriteFontFace;
        int n2 = 0;
        FontResource fontResource = pGFont.getFontResource();
        boolean bl = fontResource instanceof CompositeFontResource;
        if (bl) {
            n2 = this.getInitialSlot(fontResource);
            fontResource = ((CompositeFontResource)fontResource).getSlotResource(n2);
        }
        if ((iDWriteFontFace = ((DWFontFile)fontResource).getFontFace()) == null) {
            return;
        }
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteTextAnalyzer iDWriteTextAnalyzer = iDWriteFactory.CreateTextAnalyzer();
        if (iDWriteTextAnalyzer == null) {
            return;
        }
        long[] arrl = null;
        int[] arrn = null;
        int n3 = 0;
        int n4 = textRun.getLength();
        short[] arrs = new short[n4];
        short[] arrs2 = new short[n4];
        int n5 = n4 * 3 / 2 + 16;
        short[] arrs3 = new short[n5];
        short[] arrs4 = new short[n5];
        int[] arrn2 = new int[1];
        boolean bl2 = !textRun.isLeftToRight();
        DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS = new DWRITE_SCRIPT_ANALYSIS();
        dWRITE_SCRIPT_ANALYSIS.script = (short)textRun.getScript();
        dWRITE_SCRIPT_ANALYSIS.shapes = textRun.getSlot();
        int n6 = textRun.getStart();
        int n7 = iDWriteTextAnalyzer.GetGlyphs(arrc, n6, n4, iDWriteFontFace, false, bl2, dWRITE_SCRIPT_ANALYSIS, null, 0L, arrl, arrn, n3, n5, arrs, arrs2, arrs3, arrs4, arrn2);
        if (n7 == -2147024774) {
            arrs3 = new short[n5 *= 2];
            arrs4 = new short[n5];
            n7 = iDWriteTextAnalyzer.GetGlyphs(arrc, n6, n4, iDWriteFontFace, false, bl2, dWRITE_SCRIPT_ANALYSIS, null, 0L, arrl, arrn, n3, n5, arrs, arrs2, arrs3, arrs4, arrn2);
        }
        if (n7 != 0) {
            iDWriteTextAnalyzer.Release();
            return;
        }
        int n8 = arrn2[0];
        int n9 = bl2 ? -1 : 1;
        int[] arrn3 = new int[n8];
        int n10 = n2 << 24;
        boolean bl3 = false;
        int n11 = 0;
        int n12 = n = bl2 ? n8 - 1 : 0;
        while (n11 < n8) {
            if (arrs3[n11] == 0) {
                bl3 = true;
                if (bl) break;
            }
            arrn3[n11] = arrs3[n] & 0xFFFF | n10;
            ++n11;
            n += n9;
        }
        if (bl3 && bl) {
            iDWriteTextAnalyzer.Release();
            this.renderShape(arrc, textRun, pGFont, n2);
            return;
        }
        float f = pGFont.getSize();
        float[] arrf = new float[n8];
        float[] arrf2 = new float[n8 * 2];
        iDWriteTextAnalyzer.GetGlyphPlacements(arrc, arrs, arrs2, n6, n4, arrs3, arrs4, n8, iDWriteFontFace, f, false, bl2, dWRITE_SCRIPT_ANALYSIS, null, arrl, arrn, n3, arrf, arrf2);
        iDWriteTextAnalyzer.Release();
        float[] arrf3 = this.getPositions(arrf, arrf2, n8, bl2);
        int[] arrn4 = this.getIndices(arrs, n8, bl2);
        textRun.shape(n8, arrn3, arrf3, arrn4);
    }

    private float[] getPositions(float[] arrf, float[] arrf2, int n, boolean bl) {
        float[] arrf3 = new float[n * 2 + 2];
        int n2 = 0;
        int n3 = bl ? n - 1 : 0;
        int n4 = bl ? -1 : 1;
        float f = 0.0f;
        while (n2 < arrf3.length - 2) {
            int n5 = n3 << 1;
            arrf3[n2++] = (bl ? -arrf2[n5] : arrf2[n5]) + f;
            arrf3[n2++] = -arrf2[n5 + 1];
            f += arrf[n3];
            n3 += n4;
        }
        arrf3[n2++] = f;
        arrf3[n2++] = 0.0f;
        return arrf3;
    }

    private int[] getIndices(short[] arrs, int n, boolean bl) {
        int n2;
        int n3;
        int[] arrn = new int[n];
        Arrays.fill(arrn, -1);
        for (n3 = 0; n3 < arrs.length; ++n3) {
            n2 = arrs[n3];
            if (0 > n2 || n2 >= n || arrn[n2] != -1) continue;
            arrn[n2] = n3;
        }
        if (arrn.length > 0) {
            if (arrn[0] == -1) {
                arrn[0] = 0;
            }
            for (n3 = 1; n3 < arrn.length; ++n3) {
                if (arrn[n3] != -1) continue;
                arrn[n3] = arrn[n3 - 1];
            }
        }
        if (bl) {
            for (n3 = 0; n3 < arrn.length / 2; ++n3) {
                n2 = arrn[n3];
                arrn[n3] = arrn[arrn.length - n3 - 1];
                arrn[arrn.length - n3 - 1] = n2;
            }
        }
        return arrn;
    }

    private String getName(IDWriteLocalizedStrings iDWriteLocalizedStrings) {
        if (iDWriteLocalizedStrings == null) {
            return null;
        }
        int n = iDWriteLocalizedStrings.FindLocaleName(LOCALE);
        String string = null;
        if (n >= 0) {
            int n2 = iDWriteLocalizedStrings.GetStringLength(n);
            string = iDWriteLocalizedStrings.GetString(n, n2);
        }
        iDWriteLocalizedStrings.Release();
        return string;
    }

    private FontResource checkFontResource(FontResource fontResource, String string, String string2) {
        if (fontResource == null) {
            return null;
        }
        if (string != null && string.equals(fontResource.getPSName())) {
            return fontResource;
        }
        if (string2 != null) {
            if (string2.equals(fontResource.getFullName())) {
                return fontResource;
            }
            String string3 = fontResource.getFamilyName() + " " + fontResource.getStyleName();
            if (string2.equals(string3)) {
                return fontResource;
            }
        }
        return null;
    }

    private int getFontSlot(IDWriteFontFace iDWriteFontFace, CompositeFontResource compositeFontResource, String string, int n) {
        Object object;
        if (iDWriteFontFace == null) {
            return -1;
        }
        IDWriteFontCollection iDWriteFontCollection = DWFactory.getFontCollection();
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        IDWriteFont iDWriteFont = iDWriteFontCollection.GetFontFromFontFace(iDWriteFontFace);
        if (iDWriteFont == null) {
            return -1;
        }
        IDWriteFontFamily iDWriteFontFamily = iDWriteFont.GetFontFamily();
        String string2 = this.getName(iDWriteFontFamily.GetFamilyNames());
        iDWriteFontFamily.Release();
        boolean bl = iDWriteFont.GetStyle() != 0;
        boolean bl2 = iDWriteFont.GetWeight() > 400;
        int n2 = iDWriteFont.GetSimulations();
        int n3 = 17;
        String string3 = this.getName(iDWriteFont.GetInformationalStrings(n3));
        n3 = 11;
        String string4 = this.getName(iDWriteFont.GetInformationalStrings(n3));
        n3 = 12;
        String string5 = this.getName(iDWriteFont.GetInformationalStrings(n3));
        String string6 = string4 + " " + string5;
        if (PrismFontFactory.debugFonts) {
            object = this.getName(iDWriteFont.GetFaceNames());
            System.err.println("Mapping IDWriteFont=\"" + string2 + " " + (String)object + "\" Postscript name=\"" + string3 + "\" Win32 name=\"" + string6 + "\"");
        }
        iDWriteFont.Release();
        object = prismFontFactory.getFontResource(string2, bl2, bl, false);
        object = this.checkFontResource((FontResource)object, string3, string6);
        if (object == null) {
            object = prismFontFactory.getFontResource(string2, bl2 &= (n2 & 1) == 0, bl &= (n2 & 2) == 0, false);
            object = this.checkFontResource((FontResource)object, string3, string6);
        }
        if (object == null) {
            object = prismFontFactory.getFontResource(string6, null, false);
            object = this.checkFontResource((FontResource)object, string3, string6);
        }
        if (object == null) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("\t**** Failed to map IDWriteFont to Prism ****");
            }
            return -1;
        }
        String string7 = object.getFullName();
        if (!string.equalsIgnoreCase(string7)) {
            n = compositeFontResource.getSlotForFont(string7);
        }
        if (PrismFontFactory.debugFonts) {
            System.err.println("\tFallback full name=\"" + string7 + "\" Postscript name=\"" + object.getPSName() + "\" Style name=\"" + object.getStyleName() + "\" slot=" + n);
        }
        return n;
    }

    private void renderShape(char[] arrc, TextRun textRun, PGFont pGFont, int n) {
        int n2;
        IDWriteFontCollection iDWriteFontCollection;
        CompositeFontResource compositeFontResource = (CompositeFontResource)pGFont.getFontResource();
        FontResource fontResource = compositeFontResource.getSlotResource(n);
        String string = fontResource.getFamilyName();
        String string2 = fontResource.getFullName();
        int n3 = fontResource.isBold() ? 700 : 400;
        int n4 = 5;
        int n5 = fontResource.isItalic() ? 2 : 0;
        float f = pGFont.getSize();
        float f2 = f > 0.0f ? f : 1.0f;
        IDWriteFactory iDWriteFactory = DWFactory.getDWriteFactory();
        IDWriteTextFormat iDWriteTextFormat = iDWriteFactory.CreateTextFormat(string, iDWriteFontCollection = DWFactory.getFontCollection(), n3, n5, n4, f2, LOCALE);
        if (iDWriteTextFormat == null) {
            return;
        }
        int n6 = textRun.getStart();
        IDWriteTextLayout iDWriteTextLayout = iDWriteFactory.CreateTextLayout(arrc, n6, n2 = textRun.getLength(), iDWriteTextFormat, 100000.0f, 100000.0f);
        if (iDWriteTextLayout != null) {
            JFXTextRenderer jFXTextRenderer = OS.NewJFXTextRenderer();
            if (jFXTextRenderer != null) {
                boolean bl;
                int n7;
                jFXTextRenderer.AddRef();
                iDWriteTextLayout.Draw(0L, jFXTextRenderer, 0.0f, 0.0f);
                int n8 = jFXTextRenderer.GetTotalGlyphCount();
                int[] arrn = new int[n8];
                float[] arrf = new float[n8];
                float[] arrf2 = new float[n8 * 2];
                short[] arrs = new short[n2];
                int n9 = 0;
                int n10 = 0;
                while (jFXTextRenderer.Next()) {
                    IDWriteFontFace iDWriteFontFace = jFXTextRenderer.GetFontFace();
                    n7 = this.getFontSlot(iDWriteFontFace, compositeFontResource, string2, n);
                    if (n7 >= 0) {
                        jFXTextRenderer.GetGlyphIndices(arrn, n9, n7 << 24);
                        jFXTextRenderer.GetGlyphOffsets(arrf2, n9 * 2);
                    }
                    if (f > 0.0f) {
                        jFXTextRenderer.GetGlyphAdvances(arrf, n9);
                    }
                    jFXTextRenderer.GetClusterMap(arrs, n10, n9);
                    n9 += jFXTextRenderer.GetGlyphCount();
                    n10 += jFXTextRenderer.GetLength();
                }
                jFXTextRenderer.Release();
                boolean bl2 = bl = !textRun.isLeftToRight();
                if (bl) {
                    for (n7 = 0; n7 < n8 / 2; ++n7) {
                        int n11 = arrn[n7];
                        arrn[n7] = arrn[n8 - n7 - 1];
                        arrn[n8 - n7 - 1] = n11;
                    }
                }
                float[] arrf3 = this.getPositions(arrf, arrf2, n8, bl);
                int[] arrn2 = this.getIndices(arrs, n8, bl);
                textRun.shape(n8, arrn, arrf3, arrn2);
            }
            iDWriteTextLayout.Release();
        }
        iDWriteTextFormat.Release();
    }
}

