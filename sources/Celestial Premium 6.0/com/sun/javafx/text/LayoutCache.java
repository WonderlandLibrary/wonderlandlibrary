/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.font.PGFont;
import com.sun.javafx.text.TextLine;
import com.sun.javafx.text.TextRun;

class LayoutCache {
    int[] glyphs;
    float[] advances;
    boolean valid;
    int analysis;
    char[] text;
    PGFont font;
    TextRun[] runs;
    int runCount;
    TextLine[] lines;
    float layoutWidth;
    float layoutHeight;

    LayoutCache() {
    }
}

