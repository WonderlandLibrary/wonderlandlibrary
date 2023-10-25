/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.scene.text;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLine;
import com.sun.javafx.scene.text.TextSpan;
import javafx.scene.shape.PathElement;

public interface TextLayout {
    public static final int FLAGS_LINES_VALID = 1;
    public static final int FLAGS_ANALYSIS_VALID = 2;
    public static final int FLAGS_HAS_TABS = 4;
    public static final int FLAGS_HAS_BIDI = 8;
    public static final int FLAGS_HAS_COMPLEX = 16;
    public static final int FLAGS_HAS_EMBEDDED = 32;
    public static final int FLAGS_HAS_CJK = 64;
    public static final int FLAGS_WRAPPED = 128;
    public static final int FLAGS_RTL_BASE = 256;
    public static final int FLAGS_CACHED_UNDERLINE = 512;
    public static final int FLAGS_CACHED_STRIKETHROUGH = 1024;
    public static final int FLAGS_LAST = 2048;
    public static final int ANALYSIS_MASK = 2047;
    public static final int ALIGN_LEFT = 262144;
    public static final int ALIGN_CENTER = 524288;
    public static final int ALIGN_RIGHT = 0x100000;
    public static final int ALIGN_JUSTIFY = 0x200000;
    public static final int ALIGN_MASK = 0x3C0000;
    public static final int DIRECTION_LTR = 1024;
    public static final int DIRECTION_RTL = 2048;
    public static final int DIRECTION_DEFAULT_LTR = 4096;
    public static final int DIRECTION_DEFAULT_RTL = 8192;
    public static final int DIRECTION_MASK = 15360;
    public static final int BOUNDS_CENTER = 16384;
    public static final int BOUNDS_MASK = 16384;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_UNDERLINE = 2;
    public static final int TYPE_STRIKETHROUGH = 4;
    public static final int TYPE_BASELINE = 8;
    public static final int TYPE_TOP = 16;
    public static final int TYPE_BEARINGS = 32;
    public static final int DEFAULT_TAB_SIZE = 8;

    public boolean setContent(TextSpan[] var1);

    public boolean setContent(String var1, Object var2);

    public boolean setAlignment(int var1);

    public boolean setWrapWidth(float var1);

    public boolean setLineSpacing(float var1);

    public boolean setDirection(int var1);

    public boolean setBoundsType(int var1);

    public BaseBounds getBounds();

    public BaseBounds getBounds(TextSpan var1, BaseBounds var2);

    public BaseBounds getVisualBounds(int var1);

    public TextLine[] getLines();

    public GlyphList[] getRuns();

    public Shape getShape(int var1, TextSpan var2);

    public boolean setTabSize(int var1);

    public Hit getHitInfo(float var1, float var2);

    public PathElement[] getCaretShape(int var1, boolean var2, float var3, float var4);

    public PathElement[] getRange(int var1, int var2, int var3, float var4, float var5);

    public static class Hit {
        int charIndex;
        int insertionIndex;
        boolean leading;

        public Hit(int n, int n2, boolean bl) {
            this.charIndex = n;
            this.insertionIndex = n2;
            this.leading = bl;
        }

        public int getCharIndex() {
            return this.charIndex;
        }

        public int getInsertionIndex() {
            return this.insertionIndex;
        }

        public boolean isLeading() {
            return this.leading;
        }
    }
}

