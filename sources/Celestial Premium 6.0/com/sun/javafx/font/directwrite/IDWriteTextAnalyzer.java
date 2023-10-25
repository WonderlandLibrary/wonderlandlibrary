/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.DWRITE_SCRIPT_ANALYSIS;
import com.sun.javafx.font.directwrite.IDWriteFontFace;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.JFXTextAnalysisSink;
import com.sun.javafx.font.directwrite.OS;

class IDWriteTextAnalyzer
extends IUnknown {
    IDWriteTextAnalyzer(long l) {
        super(l);
    }

    int AnalyzeScript(JFXTextAnalysisSink jFXTextAnalysisSink, int n, int n2, JFXTextAnalysisSink jFXTextAnalysisSink2) {
        return OS.AnalyzeScript(this.ptr, jFXTextAnalysisSink.ptr, n, n2, jFXTextAnalysisSink2.ptr);
    }

    int GetGlyphs(char[] arrc, int n, int n2, IDWriteFontFace iDWriteFontFace, boolean bl, boolean bl2, DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS, String string, long l, long[] arrl, int[] arrn, int n3, int n4, short[] arrs, short[] arrs2, short[] arrs3, short[] arrs4, int[] arrn2) {
        return OS.GetGlyphs(this.ptr, arrc, n, n2, iDWriteFontFace.ptr, bl, bl2, dWRITE_SCRIPT_ANALYSIS, string != null ? (string + "\u0000").toCharArray() : (char[])null, l, arrl, arrn, n3, n4, arrs, arrs2, arrs3, arrs4, arrn2);
    }

    int GetGlyphPlacements(char[] arrc, short[] arrs, short[] arrs2, int n, int n2, short[] arrs3, short[] arrs4, int n3, IDWriteFontFace iDWriteFontFace, float f, boolean bl, boolean bl2, DWRITE_SCRIPT_ANALYSIS dWRITE_SCRIPT_ANALYSIS, String string, long[] arrl, int[] arrn, int n4, float[] arrf, float[] arrf2) {
        return OS.GetGlyphPlacements(this.ptr, arrc, arrs, arrs2, n, n2, arrs3, arrs4, n3, iDWriteFontFace.ptr, f, bl, bl2, dWRITE_SCRIPT_ANALYSIS, string != null ? (string + "\u0000").toCharArray() : (char[])null, arrl, arrn, n4, arrf, arrf2);
    }
}

