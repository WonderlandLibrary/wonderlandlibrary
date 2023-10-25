/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.shape.LineTo
 *  javafx.scene.shape.MoveTo
 *  javafx.scene.shape.PathElement
 */
package com.sun.javafx.text;

import com.sun.javafx.font.CharToGlyphMapper;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.CharArrayIterator;
import com.sun.javafx.text.GlyphLayout;
import com.sun.javafx.text.LayoutCache;
import com.sun.javafx.text.TextLine;
import com.sun.javafx.text.TextRun;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

public class PrismTextLayout
implements TextLayout {
    private static final BaseTransform IDENTITY = BaseTransform.IDENTITY_TRANSFORM;
    private static final int X_MIN_INDEX = 0;
    private static final int Y_MIN_INDEX = 1;
    private static final int X_MAX_INDEX = 2;
    private static final int Y_MAX_INDEX = 3;
    private static final Hashtable<Integer, LayoutCache> stringCache = new Hashtable();
    private static final Object CACHE_SIZE_LOCK = new Object();
    private static int cacheSize = 0;
    private static final int MAX_STRING_SIZE = 256;
    private static final int MAX_CACHE_SIZE = PrismFontFactory.cacheLayoutSize;
    private char[] text;
    private TextSpan[] spans;
    private PGFont font;
    private FontStrike strike;
    private Integer cacheKey;
    private TextLine[] lines;
    private TextRun[] runs;
    private int runCount;
    private BaseBounds logicalBounds = new RectBounds();
    private RectBounds visualBounds;
    private float layoutWidth;
    private float layoutHeight;
    private float wrapWidth;
    private float spacing;
    private LayoutCache layoutCache;
    private Shape shape;
    private int flags = 262144;
    private int tabSize = 8;

    private void reset() {
        this.layoutCache = null;
        this.runs = null;
        this.flags &= 0xFFFFF800;
        this.relayout();
    }

    private void relayout() {
        this.logicalBounds.makeEmpty();
        this.visualBounds = null;
        this.layoutHeight = 0.0f;
        this.layoutWidth = 0.0f;
        this.flags &= 0xFFFFF97F;
        this.lines = null;
        this.shape = null;
    }

    @Override
    public boolean setContent(TextSpan[] arrtextSpan) {
        if (arrtextSpan == null && this.spans == null) {
            return false;
        }
        if (arrtextSpan != null && this.spans != null && arrtextSpan.length == this.spans.length) {
            int n;
            for (n = 0; n < arrtextSpan.length && arrtextSpan[n] == this.spans[n]; ++n) {
            }
            if (n == arrtextSpan.length) {
                return false;
            }
        }
        this.reset();
        this.spans = arrtextSpan;
        this.font = null;
        this.strike = null;
        this.text = null;
        this.cacheKey = null;
        return true;
    }

    @Override
    public boolean setContent(String string, Object object) {
        int n;
        this.reset();
        this.spans = null;
        this.font = (PGFont)object;
        this.strike = ((PGFont)object).getStrike(IDENTITY);
        this.text = string.toCharArray();
        if (MAX_CACHE_SIZE > 0 && 0 < (n = string.length()) && n <= 256) {
            this.cacheKey = string.hashCode() * this.strike.hashCode();
        }
        return true;
    }

    @Override
    public boolean setDirection(int n) {
        if ((this.flags & 0x3C00) == n) {
            return false;
        }
        this.flags &= 0xFFFFC3FF;
        this.flags |= n & 0x3C00;
        this.reset();
        return true;
    }

    @Override
    public boolean setBoundsType(int n) {
        if ((this.flags & 0x4000) == n) {
            return false;
        }
        this.flags &= 0xFFFFBFFF;
        this.flags |= n & 0x4000;
        this.reset();
        return true;
    }

    @Override
    public boolean setAlignment(int n) {
        int n2 = 262144;
        switch (n) {
            case 0: {
                n2 = 262144;
                break;
            }
            case 1: {
                n2 = 524288;
                break;
            }
            case 2: {
                n2 = 0x100000;
                break;
            }
            case 3: {
                n2 = 0x200000;
            }
        }
        if ((this.flags & 0x3C0000) == n2) {
            return false;
        }
        if (n2 == 0x200000 || (this.flags & 0x200000) != 0) {
            this.reset();
        }
        this.flags &= 0xFFC3FFFF;
        this.flags |= n2;
        this.relayout();
        return true;
    }

    @Override
    public boolean setWrapWidth(float f) {
        if (Float.isInfinite(f)) {
            f = 0.0f;
        }
        if (Float.isNaN(f)) {
            f = 0.0f;
        }
        float f2 = this.wrapWidth;
        this.wrapWidth = Math.max(0.0f, f);
        boolean bl = true;
        if (this.lines != null && f2 != 0.0f && f != 0.0f && (this.flags & 0x40000) != 0) {
            if (f > f2) {
                if ((this.flags & 0x80) == 0) {
                    bl = false;
                }
            } else if (f >= this.layoutWidth) {
                bl = false;
            }
        }
        if (bl) {
            this.relayout();
        }
        return bl;
    }

    @Override
    public boolean setLineSpacing(float f) {
        if (this.spacing == f) {
            return false;
        }
        this.spacing = f;
        this.relayout();
        return true;
    }

    private void ensureLayout() {
        if (this.lines == null) {
            this.layout();
        }
    }

    @Override
    public com.sun.javafx.scene.text.TextLine[] getLines() {
        this.ensureLayout();
        return this.lines;
    }

    @Override
    public GlyphList[] getRuns() {
        this.ensureLayout();
        GlyphList[] arrglyphList = new GlyphList[this.runCount];
        int n = 0;
        for (int i = 0; i < this.lines.length; ++i) {
            TextRun[] arrtextRun = this.lines[i].getRuns();
            int n2 = arrtextRun.length;
            System.arraycopy(arrtextRun, 0, arrglyphList, n, n2);
            n += n2;
        }
        return arrglyphList;
    }

    @Override
    public BaseBounds getBounds() {
        this.ensureLayout();
        return this.logicalBounds;
    }

    @Override
    public BaseBounds getBounds(TextSpan textSpan, BaseBounds baseBounds) {
        this.ensureLayout();
        float f = Float.POSITIVE_INFINITY;
        float f2 = Float.POSITIVE_INFINITY;
        float f3 = Float.NEGATIVE_INFINITY;
        float f4 = Float.NEGATIVE_INFINITY;
        if (textSpan != null) {
            for (int i = 0; i < this.lines.length; ++i) {
                TextLine textLine = this.lines[i];
                TextRun[] arrtextRun = textLine.getRuns();
                for (int j = 0; j < arrtextRun.length; ++j) {
                    TextRun textRun = arrtextRun[j];
                    TextSpan textSpan2 = textRun.getTextSpan();
                    if (textSpan2 != textSpan) continue;
                    Point2D point2D = textRun.getLocation();
                    float f5 = point2D.x;
                    if (textRun.isLeftBearing()) {
                        f5 += textLine.getLeftSideBearing();
                    }
                    float f6 = point2D.x + textRun.getWidth();
                    if (textRun.isRightBearing()) {
                        f6 += textLine.getRightSideBearing();
                    }
                    float f7 = point2D.y;
                    float f8 = point2D.y + textLine.getBounds().getHeight() + this.spacing;
                    if (f5 < f) {
                        f = f5;
                    }
                    if (f7 < f2) {
                        f2 = f7;
                    }
                    if (f6 > f3) {
                        f3 = f6;
                    }
                    if (!(f8 > f4)) continue;
                    f4 = f8;
                }
            }
        } else {
            f4 = 0.0f;
            f2 = 0.0f;
            for (int i = 0; i < this.lines.length; ++i) {
                float f9;
                TextLine textLine = this.lines[i];
                RectBounds rectBounds = textLine.getBounds();
                float f10 = rectBounds.getMinX() + textLine.getLeftSideBearing();
                if (f10 < f) {
                    f = f10;
                }
                if ((f9 = rectBounds.getMaxX() + textLine.getRightSideBearing()) > f3) {
                    f3 = f9;
                }
                f4 += rectBounds.getHeight();
            }
            if (this.isMirrored()) {
                float f11 = this.getMirroringWidth();
                float f12 = f;
                f = f11 - f3;
                f3 = f11 - f12;
            }
        }
        return baseBounds.deriveWithNewBounds(f, f2, 0.0f, f3, f4, 0.0f);
    }

    @Override
    public PathElement[] getCaretShape(int n, boolean bl, float f, float f2) {
        Point2D point2D;
        int n2;
        int n3;
        TextLine textLine;
        int n4;
        int n5;
        this.ensureLayout();
        int n6 = this.getLineCount();
        for (n5 = 0; n5 < n6 - 1 && (n4 = (textLine = this.lines[n5]).getStart() + textLine.getLength()) <= n; ++n5) {
        }
        int n7 = -1;
        n4 = 0;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        TextLine textLine2 = this.lines[n5];
        TextRun[] arrtextRun = textLine2.getRuns();
        int n8 = arrtextRun.length;
        int n9 = -1;
        for (n3 = 0; n3 < n8; ++n3) {
            TextRun textRun = arrtextRun[n3];
            int n10 = textRun.getStart();
            n2 = textRun.getEnd();
            if (n10 > n || n >= n2) continue;
            if (textRun.isLinebreak()) break;
            n9 = n3;
            break;
        }
        if (n9 != -1) {
            TextRun textRun = arrtextRun[n9];
            int n11 = textRun.getStart();
            point2D = textRun.getLocation();
            f3 = point2D.x + textRun.getXAtOffset(n - n11, bl);
            f4 = point2D.y;
            f5 = textLine2.getBounds().getHeight();
            if (bl) {
                if (n9 > 0 && n == n11) {
                    n4 = textRun.getLevel();
                    n7 = n - 1;
                }
            } else {
                n2 = textRun.getEnd();
                if (n9 + 1 < arrtextRun.length && n + 1 == n2) {
                    n4 = textRun.getLevel();
                    n7 = n + 1;
                }
            }
        } else {
            n3 = 0;
            n9 = 0;
            for (int i = 0; i < n8; ++i) {
                TextRun textRun = arrtextRun[i];
                if (textRun.getStart() < n3 || textRun.isLinebreak()) continue;
                n3 = textRun.getStart();
                n9 = i;
            }
            TextRun textRun = arrtextRun[n9];
            point2D = textRun.getLocation();
            f3 = point2D.x + (textRun.isLeftToRight() ? textRun.getWidth() : 0.0f);
            f4 = point2D.y;
            f5 = textLine2.getBounds().getHeight();
        }
        if (this.isMirrored()) {
            f3 = this.getMirroringWidth() - f3;
        }
        f3 += f;
        f4 += f2;
        if (n7 != -1) {
            for (int i = 0; i < arrtextRun.length; ++i) {
                TextRun textRun = arrtextRun[i];
                int n12 = textRun.getStart();
                n2 = textRun.getEnd();
                if (n12 > n7 || n7 >= n2 || (textRun.getLevel() & 1) == (n4 & 1)) continue;
                Point2D point2D2 = textRun.getLocation();
                float f6 = point2D2.x;
                if (bl) {
                    if ((n4 & 1) != 0) {
                        f6 += textRun.getWidth();
                    }
                } else if ((n4 & 1) == 0) {
                    f6 += textRun.getWidth();
                }
                if (this.isMirrored()) {
                    f6 = this.getMirroringWidth() - f6;
                }
                PathElement[] arrpathElement = new PathElement[]{new MoveTo((double)f3, (double)f4), new LineTo((double)f3, (double)(f4 + f5 / 2.0f)), new MoveTo((double)(f6 += f), (double)(f4 + f5 / 2.0f)), new LineTo((double)f6, (double)(f4 + f5))};
                return arrpathElement;
            }
        }
        PathElement[] arrpathElement = new PathElement[]{new MoveTo((double)f3, (double)f4), new LineTo((double)f3, (double)(f4 + f5))};
        return arrpathElement;
    }

    @Override
    public TextLayout.Hit getHitInfo(float f, float f2) {
        int n = -1;
        boolean bl = false;
        this.ensureLayout();
        int n2 = this.getLineIndex(f2);
        if (n2 >= this.getLineCount()) {
            n = this.getCharCount();
        } else {
            if (this.isMirrored()) {
                f = this.getMirroringWidth() - f;
            }
            TextLine textLine = this.lines[n2];
            TextRun[] arrtextRun = textLine.getRuns();
            RectBounds rectBounds = textLine.getBounds();
            TextRun textRun = null;
            f -= rectBounds.getMinX();
            for (int i = 0; i < arrtextRun.length && !(f < (textRun = arrtextRun[i]).getWidth()); ++i) {
                if (i + 1 >= arrtextRun.length) continue;
                if (arrtextRun[i + 1].isLinebreak()) break;
                f -= textRun.getWidth();
            }
            if (textRun != null) {
                int[] arrn = new int[1];
                n = textRun.getStart() + textRun.getOffsetAtX(f, arrn);
                bl = arrn[0] == 0;
            } else {
                n = textLine.getStart();
                bl = true;
            }
        }
        return new TextLayout.Hit(n, -1, bl);
    }

    @Override
    public PathElement[] getRange(int n, int n2, int n3, float f, float f2) {
        this.ensureLayout();
        int n4 = this.getLineCount();
        ArrayList<Object> arrayList = new ArrayList<Object>();
        float f3 = 0.0f;
        for (int i = 0; i < n4; ++i) {
            TextLine textLine = this.lines[i];
            RectBounds rectBounds = textLine.getBounds();
            int n5 = textLine.getStart();
            if (n5 >= n2) break;
            int n6 = n5 + textLine.getLength();
            if (n > n6) {
                f3 += rectBounds.getHeight() + this.spacing;
                continue;
            }
            TextRun[] arrtextRun = textLine.getRuns();
            int n7 = Math.min(n6, n2) - Math.max(n5, n);
            float f4 = -1.0f;
            float f5 = -1.0f;
            float f6 = rectBounds.getMinX();
            for (int j = 0; n7 > 0 && j < arrtextRun.length; ++j) {
                TextRun textRun = arrtextRun[j];
                int n8 = textRun.getStart();
                int n9 = textRun.getEnd();
                float f7 = textRun.getWidth();
                int n10 = Math.max(n8, Math.min(n, n9));
                int n11 = Math.max(n8, Math.min(n2, n9));
                int n12 = n11 - n10;
                if (n12 != 0) {
                    float f8;
                    float f9;
                    boolean bl = textRun.isLeftToRight();
                    float f10 = n8 > n ? (bl ? f6 : f6 + f7) : f6 + textRun.getXAtOffset(n - n8, true);
                    if (f10 > (f9 = n9 < n2 ? (bl ? f6 + f7 : f6) : f6 + textRun.getXAtOffset(n2 - n8, true))) {
                        f8 = f10;
                        f10 = f9;
                        f9 = f8;
                    }
                    n7 -= n12;
                    f8 = 0.0f;
                    float f11 = 0.0f;
                    switch (n3) {
                        case 1: {
                            f8 = f3;
                            f11 = f3 + rectBounds.getHeight();
                            break;
                        }
                        case 2: 
                        case 4: {
                            Object object;
                            FontStrike fontStrike = null;
                            if (this.spans != null) {
                                object = textRun.getTextSpan();
                                PGFont pGFont = (PGFont)object.getFont();
                                if (pGFont == null) break;
                                fontStrike = pGFont.getStrike(IDENTITY);
                            } else {
                                fontStrike = this.strike;
                            }
                            f8 = f3 - textRun.getAscent();
                            object = fontStrike.getMetrics();
                            f11 = n3 == 2 ? (f8 += object.getUnderLineOffset()) + object.getUnderLineThickness() : (f8 += object.getStrikethroughOffset()) + object.getStrikethroughThickness();
                        }
                    }
                    if (f10 != f5) {
                        if (f4 != -1.0f && f5 != -1.0f) {
                            float f12 = f4;
                            float f13 = f5;
                            if (this.isMirrored()) {
                                float f14 = this.getMirroringWidth();
                                f12 = f14 - f12;
                                f13 = f14 - f13;
                            }
                            arrayList.add((Object)new MoveTo((double)(f + f12), (double)(f2 + f8)));
                            arrayList.add((Object)new LineTo((double)(f + f13), (double)(f2 + f8)));
                            arrayList.add((Object)new LineTo((double)(f + f13), (double)(f2 + f11)));
                            arrayList.add((Object)new LineTo((double)(f + f12), (double)(f2 + f11)));
                            arrayList.add((Object)new LineTo((double)(f + f12), (double)(f2 + f8)));
                        }
                        f4 = f10;
                        f5 = f9;
                    }
                    f5 = f9;
                    if (n7 == 0) {
                        float f15 = f4;
                        float f16 = f5;
                        if (this.isMirrored()) {
                            float f17 = this.getMirroringWidth();
                            f15 = f17 - f15;
                            f16 = f17 - f16;
                        }
                        arrayList.add((Object)new MoveTo((double)(f + f15), (double)(f2 + f8)));
                        arrayList.add((Object)new LineTo((double)(f + f16), (double)(f2 + f8)));
                        arrayList.add((Object)new LineTo((double)(f + f16), (double)(f2 + f11)));
                        arrayList.add((Object)new LineTo((double)(f + f15), (double)(f2 + f11)));
                        arrayList.add((Object)new LineTo((double)(f + f15), (double)(f2 + f8)));
                    }
                }
                f6 += f7;
            }
            f3 += rectBounds.getHeight() + this.spacing;
        }
        return arrayList.toArray((T[])new PathElement[arrayList.size()]);
    }

    @Override
    public Shape getShape(int n, TextSpan textSpan) {
        boolean bl;
        this.ensureLayout();
        boolean bl2 = (n & 1) != 0;
        boolean bl3 = (n & 2) != 0;
        boolean bl4 = (n & 4) != 0;
        boolean bl5 = bl = (n & 8) != 0;
        if (this.shape != null && bl2 && !bl3 && !bl4 && bl) {
            return this.shape;
        }
        Path2D path2D = new Path2D();
        Translate2D translate2D = new Translate2D(0.0, 0.0);
        float f = 0.0f;
        if (bl) {
            f = -this.lines[0].getBounds().getMinY();
        }
        for (int i = 0; i < this.lines.length; ++i) {
            TextLine textLine = this.lines[i];
            TextRun[] arrtextRun = textLine.getRuns();
            RectBounds rectBounds = textLine.getBounds();
            float f2 = -rectBounds.getMinY();
            for (int j = 0; j < arrtextRun.length; ++j) {
                Shape shape;
                Object object;
                TextRun textRun = arrtextRun[j];
                FontStrike fontStrike = null;
                if (this.spans != null) {
                    PGFont pGFont;
                    object = textRun.getTextSpan();
                    if (textSpan != null && object != textSpan || (pGFont = (PGFont)object.getFont()) == null) continue;
                    fontStrike = pGFont.getStrike(IDENTITY);
                } else {
                    fontStrike = this.strike;
                }
                object = textRun.getLocation();
                float f3 = ((Point2D)object).x;
                float f4 = ((Point2D)object).y + f2 - f;
                Metrics metrics = null;
                if (bl3 || bl4) {
                    metrics = fontStrike.getMetrics();
                }
                if (bl3) {
                    shape = new RoundRectangle2D();
                    shape.x = f3;
                    shape.y = f4 + metrics.getUnderLineOffset();
                    shape.width = textRun.getWidth();
                    shape.height = metrics.getUnderLineThickness();
                    path2D.append(shape, false);
                }
                if (bl4) {
                    shape = new RoundRectangle2D();
                    shape.x = f3;
                    shape.y = f4 + metrics.getStrikethroughOffset();
                    shape.width = textRun.getWidth();
                    shape.height = metrics.getStrikethroughThickness();
                    path2D.append(shape, false);
                }
                if (!bl2 || textRun.getGlyphCount() <= 0) continue;
                ((BaseTransform)translate2D).restoreTransform(1.0, 0.0, 0.0, 1.0, f3, f4);
                shape = (Path2D)fontStrike.getOutline(textRun, translate2D);
                path2D.append(shape, false);
            }
        }
        if (bl2 && !bl3 && !bl4) {
            this.shape = path2D;
        }
        return path2D;
    }

    @Override
    public boolean setTabSize(int n) {
        if (n < 1) {
            n = 1;
        }
        if (this.tabSize != n) {
            this.tabSize = n;
            this.relayout();
            return true;
        }
        return false;
    }

    private int getLineIndex(float f) {
        int n;
        float f2 = 0.0f;
        int n2 = this.getLineCount();
        for (n = 0; n < n2; ++n) {
            f2 += this.lines[n].getBounds().getHeight() + this.spacing;
            if (n + 1 == n2) {
                f2 -= this.lines[n].getLeading();
            }
            if (f2 > f) break;
        }
        return n;
    }

    private boolean copyCache() {
        int n = this.flags & 0x3C0000;
        int n2 = this.flags & 0x4000;
        return this.wrapWidth != 0.0f || n != 262144 || n2 == 0 || this.isMirrored();
    }

    private void initCache() {
        if (this.cacheKey != null) {
            LayoutCache layoutCache;
            if (this.layoutCache == null && (layoutCache = stringCache.get(this.cacheKey)) != null && layoutCache.font.equals(this.font) && Arrays.equals(layoutCache.text, this.text)) {
                this.layoutCache = layoutCache;
                this.runs = layoutCache.runs;
                this.runCount = layoutCache.runCount;
                this.flags |= layoutCache.analysis;
            }
            if (this.layoutCache != null) {
                if (this.copyCache()) {
                    if (this.layoutCache.runs == this.runs) {
                        this.runs = new TextRun[this.runCount];
                        System.arraycopy(this.layoutCache.runs, 0, this.runs, 0, this.runCount);
                    }
                } else if (this.layoutCache.lines != null) {
                    this.runs = this.layoutCache.runs;
                    this.runCount = this.layoutCache.runCount;
                    this.flags |= this.layoutCache.analysis;
                    this.lines = this.layoutCache.lines;
                    this.layoutWidth = this.layoutCache.layoutWidth;
                    this.layoutHeight = this.layoutCache.layoutHeight;
                    float f = this.lines[0].getBounds().getMinY();
                    this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0f, f, 0.0f, this.layoutWidth, this.layoutHeight + f, 0.0f);
                }
            }
        }
    }

    private int getLineCount() {
        return this.lines.length;
    }

    private int getCharCount() {
        if (this.text != null) {
            return this.text.length;
        }
        int n = 0;
        for (int i = 0; i < this.lines.length; ++i) {
            n += this.lines[i].getLength();
        }
        return n;
    }

    public TextSpan[] getTextSpans() {
        return this.spans;
    }

    public PGFont getFont() {
        return this.font;
    }

    public int getDirection() {
        if ((this.flags & 0x400) != 0) {
            return 0;
        }
        if ((this.flags & 0x800) != 0) {
            return 1;
        }
        if ((this.flags & 0x1000) != 0) {
            return -2;
        }
        if ((this.flags & 0x2000) != 0) {
            return -1;
        }
        return -2;
    }

    public void addTextRun(TextRun textRun) {
        if (this.runCount + 1 > this.runs.length) {
            TextRun[] arrtextRun = new TextRun[this.runs.length + 64];
            System.arraycopy(this.runs, 0, arrtextRun, 0, this.runs.length);
            this.runs = arrtextRun;
        }
        this.runs[this.runCount++] = textRun;
    }

    private void buildRuns(char[] arrc) {
        this.runCount = 0;
        if (this.runs == null) {
            int n = Math.max(4, Math.min(arrc.length / 16, 16));
            this.runs = new TextRun[n];
        }
        GlyphLayout glyphLayout = GlyphLayout.getInstance();
        this.flags = glyphLayout.breakRuns(this, arrc, this.flags);
        glyphLayout.dispose();
        for (int i = this.runCount; i < this.runs.length; ++i) {
            this.runs[i] = null;
        }
    }

    private void shape(TextRun textRun, char[] arrc, GlyphLayout glyphLayout) {
        float f;
        FontStrike fontStrike;
        PGFont pGFont;
        Object object;
        if (this.spans != null) {
            if (this.spans.length == 0) {
                return;
            }
            object = textRun.getTextSpan();
            pGFont = (PGFont)object.getFont();
            if (pGFont == null) {
                RectBounds rectBounds = object.getBounds();
                textRun.setEmbedded(rectBounds, object.getText().length());
                return;
            }
            fontStrike = pGFont.getStrike(IDENTITY);
        } else {
            pGFont = this.font;
            fontStrike = this.strike;
        }
        if (textRun.getAscent() == 0.0f) {
            object = fontStrike.getMetrics();
            if ((this.flags & 0x4000) == 16384) {
                float f2 = object.getAscent();
                if (pGFont.getFamilyName().equals("Segoe UI")) {
                    f2 = (float)((double)f2 * 0.8);
                }
                f2 = (int)((double)f2 - 0.75);
                float f3 = (int)((double)object.getDescent() + 0.75);
                f = (int)((double)object.getLineGap() + 0.75);
                float f4 = (int)((double)object.getCapHeight() + 0.75);
                float f5 = -f2 - f4;
                if (f5 > f3) {
                    f3 = f5;
                } else {
                    f2 += f5 - f3;
                }
                textRun.setMetrics(f2, f3, f);
            } else {
                textRun.setMetrics(object.getAscent(), object.getDescent(), object.getLineGap());
            }
        }
        if (textRun.isTab()) {
            return;
        }
        if (textRun.isLinebreak()) {
            return;
        }
        if (textRun.getGlyphCount() > 0) {
            return;
        }
        if (textRun.isComplex()) {
            glyphLayout.layout(textRun, pGFont, fontStrike, arrc);
        } else {
            object = fontStrike.getFontResource();
            int n = textRun.getStart();
            int n2 = textRun.getLength();
            if (this.layoutCache == null) {
                f = fontStrike.getSize();
                CharToGlyphMapper charToGlyphMapper = object.getGlyphMapper();
                int[] arrn = new int[n2];
                charToGlyphMapper.charsToGlyphs(n, n2, arrc, arrn);
                float[] arrf = new float[n2 + 1 << 1];
                float f6 = 0.0f;
                for (int i = 0; i < n2; ++i) {
                    float f7 = object.getAdvance(arrn[i], f);
                    arrf[i << 1] = f6;
                    f6 += f7;
                }
                arrf[n2 << 1] = f6;
                textRun.shape(n2, arrn, arrf, null);
            } else {
                if (!this.layoutCache.valid) {
                    f = fontStrike.getSize();
                    CharToGlyphMapper charToGlyphMapper = object.getGlyphMapper();
                    charToGlyphMapper.charsToGlyphs(n, n2, arrc, this.layoutCache.glyphs, n);
                    int n3 = n + n2;
                    float f8 = 0.0f;
                    for (int i = n; i < n3; ++i) {
                        float f9;
                        this.layoutCache.advances[i] = f9 = object.getAdvance(this.layoutCache.glyphs[i], f);
                        f8 += f9;
                    }
                    textRun.setWidth(f8);
                }
                textRun.shape(n2, this.layoutCache.glyphs, this.layoutCache.advances);
            }
        }
    }

    private TextLine createLine(int n, int n2, int n3) {
        int n4 = n2 - n + 1;
        TextRun[] arrtextRun = new TextRun[n4];
        if (n < this.runCount) {
            System.arraycopy(this.runs, n, arrtextRun, 0, n4);
        }
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        int n5 = 0;
        for (int i = 0; i < arrtextRun.length; ++i) {
            TextRun textRun = arrtextRun[i];
            f += textRun.getWidth();
            f2 = Math.min(f2, textRun.getAscent());
            f3 = Math.max(f3, textRun.getDescent());
            f4 = Math.max(f4, textRun.getLeading());
            n5 += textRun.getLength();
        }
        if (f > this.layoutWidth) {
            this.layoutWidth = f;
        }
        return new TextLine(n3, n5, arrtextRun, f, f2, f3, f4);
    }

    private void reorderLine(TextLine textLine) {
        Object[] arrobject = textLine.getRuns();
        int n = arrobject.length;
        if (n > 0 && arrobject[n - 1].isLinebreak()) {
            --n;
        }
        if (n < 2) {
            return;
        }
        byte[] arrby = new byte[n];
        for (int i = 0; i < n; ++i) {
            arrby[i] = arrobject[i].getLevel();
        }
        Bidi.reorderVisually(arrby, 0, arrobject, 0, n);
    }

    private char[] getText() {
        if (this.text == null) {
            int n;
            int n2 = 0;
            for (n = 0; n < this.spans.length; ++n) {
                n2 += this.spans[n].getText().length();
            }
            this.text = new char[n2];
            n = 0;
            for (int i = 0; i < this.spans.length; ++i) {
                String string = this.spans[i].getText();
                int n3 = string.length();
                string.getChars(0, n3, this.text, n);
                n += n3;
            }
        }
        return this.text;
    }

    private boolean isSimpleLayout() {
        int n = this.flags & 0x3C0000;
        boolean bl = this.wrapWidth > 0.0f && n == 0x200000;
        int n2 = 24;
        return (this.flags & n2) == 0 && !bl;
    }

    private boolean isMirrored() {
        boolean bl = false;
        switch (this.flags & 0x3C00) {
            case 2048: {
                bl = true;
                break;
            }
            case 1024: {
                bl = false;
                break;
            }
            case 4096: 
            case 8192: {
                bl = (this.flags & 0x100) != 0;
            }
        }
        return bl;
    }

    private float getMirroringWidth() {
        return this.wrapWidth != 0.0f ? this.wrapWidth : this.layoutWidth;
    }

    private void reuseRuns() {
        TextRun textRun;
        this.runCount = 0;
        int n = 0;
        block0: while (n < this.runs.length && (textRun = this.runs[n]) != null) {
            TextRun textRun2;
            this.runs[n] = null;
            ++n;
            this.runs[this.runCount++] = textRun = textRun.unwrap();
            if (!textRun.isSplit()) continue;
            textRun.merge(null);
            while (n < this.runs.length && (textRun2 = this.runs[n]) != null) {
                textRun.merge(textRun2);
                this.runs[n] = null;
                ++n;
                if (!textRun2.isSplitLast()) continue;
                continue block0;
            }
        }
    }

    private float getTabAdvance() {
        float f = 0.0f;
        if (this.spans != null) {
            for (int i = 0; i < this.spans.length; ++i) {
                TextSpan textSpan = this.spans[i];
                PGFont pGFont = (PGFont)textSpan.getFont();
                if (pGFont == null) continue;
                FontStrike fontStrike = pGFont.getStrike(IDENTITY);
                f = fontStrike.getCharAdvance(' ');
                break;
            }
        } else {
            f = this.strike.getCharAdvance(' ');
        }
        return (float)this.tabSize * f;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void layout() {
        TextRun[] arrtextRun;
        int n;
        int n2;
        float f;
        this.initCache();
        if (this.lines != null) {
            return;
        }
        char[] arrc = this.getText();
        if ((this.flags & 2) != 0 && this.isSimpleLayout()) {
            this.reuseRuns();
        } else {
            this.buildRuns(arrc);
        }
        GlyphLayout glyphLayout = null;
        if ((this.flags & 0x10) != 0) {
            glyphLayout = GlyphLayout.getInstance();
        }
        float f2 = 0.0f;
        if ((this.flags & 4) != 0) {
            f2 = this.getTabAdvance();
        }
        BreakIterator breakIterator = null;
        if (this.wrapWidth > 0.0f && (this.flags & 0x50) != 0) {
            breakIterator = BreakIterator.getLineInstance();
            breakIterator.setText(new CharArrayIterator(arrc));
        }
        int n3 = this.flags & 0x3C0000;
        if (this.isSimpleLayout()) {
            if (this.layoutCache == null) {
                this.layoutCache = new LayoutCache();
                this.layoutCache.glyphs = new int[arrc.length];
                this.layoutCache.advances = new float[arrc.length];
            }
        } else {
            this.layoutCache = null;
        }
        float f3 = 0.0f;
        int n4 = 0;
        int n5 = 0;
        ArrayList<TextLine> arrayList = new ArrayList<TextLine>();
        for (int i = 0; i < this.runCount; ++i) {
            TextRun textRun = this.runs[i];
            this.shape(textRun, arrc, glyphLayout);
            if (textRun.isTab()) {
                f = (float)((int)(f3 / f2) + 1) * f2;
                textRun.setWidth(f - f3);
            }
            f = textRun.getWidth();
            if (this.wrapWidth > 0.0f && f3 + f > this.wrapWidth && !textRun.isLinebreak()) {
                int n6;
                int n7;
                int n8;
                n2 = textRun.getStart() + textRun.getWrapIndex(this.wrapWidth - f3);
                int n9 = n2;
                if (n9 + 1 >= (n = textRun.getEnd()) || arrc[n9] == ' ') {
                    // empty if block
                }
                if (breakIterator != null) {
                    n8 = breakIterator.isBoundary(n9) || arrc[n9] == '\t' ? n9 : breakIterator.preceding(n9);
                } else {
                    n7 = Character.isWhitespace(arrc[n8]);
                    for (n8 = ++n9; n8 > n5; --n8) {
                        boolean bl = Character.isWhitespace(arrc[n8 - 1]);
                        if (n7 == 0 && bl) break;
                        n7 = bl ? 1 : 0;
                    }
                }
                if (n8 < n5) {
                    n8 = n5;
                }
                TextRun textRun2 = null;
                for (n7 = n4; n7 < this.runCount && (textRun2 = this.runs[n7]).getEnd() <= n8; ++n7) {
                }
                if (n8 == n5) {
                    textRun2 = textRun;
                    n7 = i;
                    n8 = n2;
                }
                if ((n6 = n8 - textRun2.getStart()) == 0 && n7 != n4) {
                    i = n7 - 1;
                } else {
                    i = n7;
                    if (n6 == 0) {
                        ++n6;
                    }
                    if (n6 < textRun2.getLength()) {
                        if (this.runCount >= this.runs.length) {
                            arrtextRun = new TextRun[this.runs.length + 64];
                            System.arraycopy(this.runs, 0, arrtextRun, 0, i + 1);
                            System.arraycopy(this.runs, i + 1, arrtextRun, i + 2, this.runs.length - i - 1);
                            this.runs = arrtextRun;
                        } else {
                            System.arraycopy(this.runs, i + 1, this.runs, i + 2, this.runCount - i - 1);
                        }
                        this.runs[i + 1] = textRun2.split(n6);
                        if (textRun2.isComplex()) {
                            this.shape(textRun2, arrc, glyphLayout);
                        }
                        ++this.runCount;
                    }
                }
                if (i + 1 < this.runCount && !this.runs[i + 1].isLinebreak()) {
                    textRun = this.runs[i];
                    textRun.setSoftbreak();
                    this.flags |= 0x80;
                }
            }
            f3 += f;
            if (!textRun.isBreak()) continue;
            TextLine textLine = this.createLine(n4, i, n5);
            arrayList.add(textLine);
            n4 = i + 1;
            n5 += textLine.getLength();
            f3 = 0.0f;
        }
        if (glyphLayout != null) {
            glyphLayout.dispose();
        }
        arrayList.add(this.createLine(n4, this.runCount - 1, n5));
        this.lines = new TextLine[arrayList.size()];
        arrayList.toArray(this.lines);
        float f4 = Math.max(this.wrapWidth, this.layoutWidth);
        float f5 = 0.0f;
        if (this.isMirrored()) {
            f = 1.0f;
            if (n3 == 0x100000) {
                f = 0.0f;
            }
        } else {
            f = 0.0f;
            if (n3 == 0x100000) {
                f = 1.0f;
            }
        }
        if (n3 == 524288) {
            f = 0.5f;
        }
        for (n2 = 0; n2 < this.lines.length; ++n2) {
            int n10;
            TextRun[] arrtextRun2;
            int n11;
            boolean bl;
            TextLine textLine = this.lines[n2];
            n = textLine.getStart();
            RectBounds rectBounds = textLine.getBounds();
            float f6 = (f4 - rectBounds.getWidth()) * f;
            textLine.setAlignment(f6);
            boolean bl2 = bl = this.wrapWidth > 0.0f && n3 == 0x200000;
            if (bl && (n11 = (arrtextRun2 = textLine.getRuns()).length) > 0 && arrtextRun2[n11 - 1].isSoftbreak()) {
                n10 = n + textLine.getLength();
                int n12 = 0;
                boolean bl3 = false;
                for (int i = n10 - 1; i >= n; --i) {
                    if (!bl3 && arrc[i] != ' ') {
                        bl3 = true;
                    }
                    if (!bl3 || arrc[i] != ' ') continue;
                    ++n12;
                }
                if (n12 != 0) {
                    float f7 = (f4 - rectBounds.getWidth()) / (float)n12;
                    block8: for (int i = 0; i < n11; ++i) {
                        TextRun textRun = arrtextRun2[i];
                        int n13 = textRun.getStart();
                        int n14 = textRun.getEnd();
                        for (int j = n13; j < n14; ++j) {
                            if (arrc[j] != ' ') continue;
                            textRun.justify(j - n13, f7);
                            if (--n12 == 0) break block8;
                        }
                    }
                    f6 = 0.0f;
                    textLine.setAlignment(f6);
                    textLine.setWidth(f4);
                }
            }
            if ((this.flags & 8) != 0) {
                this.reorderLine(textLine);
            }
            this.computeSideBearings(textLine);
            float f8 = f6;
            arrtextRun = textLine.getRuns();
            for (n10 = 0; n10 < arrtextRun.length; ++n10) {
                TextRun textRun = arrtextRun[n10];
                textRun.setLocation(f8, f5);
                textRun.setLine(textLine);
                f8 += textRun.getWidth();
            }
            if (n2 + 1 < this.lines.length) {
                f5 = Math.max(f5, f5 + rectBounds.getHeight() + this.spacing);
                continue;
            }
            f5 += rectBounds.getHeight() - textLine.getLeading();
        }
        float f9 = this.lines[0].getBounds().getMinY();
        this.layoutHeight = f5;
        this.logicalBounds = this.logicalBounds.deriveWithNewBounds(0.0f, f9, 0.0f, this.layoutWidth, this.layoutHeight + f9, 0.0f);
        if (this.layoutCache != null) {
            if (this.cacheKey != null && !this.layoutCache.valid && !this.copyCache()) {
                this.layoutCache.font = this.font;
                this.layoutCache.text = this.text;
                this.layoutCache.runs = this.runs;
                this.layoutCache.runCount = this.runCount;
                this.layoutCache.lines = this.lines;
                this.layoutCache.layoutWidth = this.layoutWidth;
                this.layoutCache.layoutHeight = this.layoutHeight;
                this.layoutCache.analysis = this.flags & 0x7FF;
                Object object = CACHE_SIZE_LOCK;
                synchronized (object) {
                    n = arrc.length;
                    if (cacheSize + n > MAX_CACHE_SIZE) {
                        stringCache.clear();
                        cacheSize = 0;
                    }
                    stringCache.put(this.cacheKey, this.layoutCache);
                    cacheSize += n;
                }
            }
            this.layoutCache.valid = true;
        }
    }

    @Override
    public BaseBounds getVisualBounds(int n) {
        boolean bl;
        this.ensureLayout();
        if (this.strike == null) {
            return null;
        }
        boolean bl2 = (n & 2) != 0;
        boolean bl3 = (this.flags & 0x200) != 0;
        boolean bl4 = (n & 4) != 0;
        boolean bl5 = bl = (this.flags & 0x400) != 0;
        if (this.visualBounds != null && bl2 == bl3 && bl4 == bl) {
            return this.visualBounds;
        }
        this.flags &= 0xFFFFF9FF;
        if (bl2) {
            this.flags |= 0x200;
        }
        if (bl4) {
            this.flags |= 0x400;
        }
        this.visualBounds = new RectBounds();
        float f = Float.POSITIVE_INFINITY;
        float f2 = Float.POSITIVE_INFINITY;
        float f3 = Float.NEGATIVE_INFINITY;
        float f4 = Float.NEGATIVE_INFINITY;
        float[] arrf = new float[4];
        FontResource fontResource = this.strike.getFontResource();
        Metrics metrics = this.strike.getMetrics();
        float f5 = this.strike.getSize();
        for (int i = 0; i < this.lines.length; ++i) {
            TextLine textLine = this.lines[i];
            TextRun[] arrtextRun = textLine.getRuns();
            for (int j = 0; j < arrtextRun.length; ++j) {
                float f6;
                float f7;
                TextRun textRun = arrtextRun[j];
                Point2D point2D = textRun.getLocation();
                if (textRun.isLinebreak()) continue;
                int n2 = textRun.getGlyphCount();
                for (int k = 0; k < n2; ++k) {
                    int n3 = textRun.getGlyphCode(k);
                    if (n3 == 65535) continue;
                    fontResource.getGlyphBoundingBox(textRun.getGlyphCode(k), f5, arrf);
                    if (arrf[0] == arrf[2]) continue;
                    f7 = point2D.x + textRun.getPosX(k);
                    f6 = point2D.y + textRun.getPosY(k);
                    float f8 = f7 + arrf[0];
                    float f9 = f6 - arrf[3];
                    float f10 = f7 + arrf[2];
                    float f11 = f6 - arrf[1];
                    if (f8 < f) {
                        f = f8;
                    }
                    if (f9 < f2) {
                        f2 = f9;
                    }
                    if (f10 > f3) {
                        f3 = f10;
                    }
                    if (!(f11 > f4)) continue;
                    f4 = f11;
                }
                if (bl2) {
                    float f12 = point2D.x;
                    float f13 = point2D.y + metrics.getUnderLineOffset();
                    f7 = f12 + textRun.getWidth();
                    f6 = f13 + metrics.getUnderLineThickness();
                    if (f12 < f) {
                        f = f12;
                    }
                    if (f13 < f2) {
                        f2 = f13;
                    }
                    if (f7 > f3) {
                        f3 = f7;
                    }
                    if (f6 > f4) {
                        f4 = f6;
                    }
                }
                if (!bl4) continue;
                float f14 = point2D.x;
                float f15 = point2D.y + metrics.getStrikethroughOffset();
                f7 = f14 + textRun.getWidth();
                f6 = f15 + metrics.getStrikethroughThickness();
                if (f14 < f) {
                    f = f14;
                }
                if (f15 < f2) {
                    f2 = f15;
                }
                if (f7 > f3) {
                    f3 = f7;
                }
                if (!(f6 > f4)) continue;
                f4 = f6;
            }
        }
        if (f < f3 && f2 < f4) {
            this.visualBounds.setBounds(f, f2, f3, f4);
        }
        return this.visualBounds;
    }

    private void computeSideBearings(TextLine textLine) {
        Object object;
        int n;
        TextRun[] arrtextRun = textLine.getRuns();
        if (arrtextRun.length == 0) {
            return;
        }
        float[] arrf = new float[4];
        FontResource fontResource = null;
        float f = 0.0f;
        if (this.strike != null) {
            fontResource = this.strike.getFontResource();
            f = this.strike.getSize();
        }
        float f2 = 0.0f;
        float f3 = 0.0f;
        block0: for (int i = 0; i < arrtextRun.length; ++i) {
            TextRun textRun = arrtextRun[i];
            int n2 = textRun.getGlyphCount();
            for (n = 0; n < n2; ++n) {
                int n3;
                float f4 = textRun.getAdvance(n);
                if (f4 != 0.0f && (n3 = textRun.getGlyphCode(n)) != 65535) {
                    FontResource fontResource2 = fontResource;
                    if (fontResource2 == null) {
                        TextSpan textSpan = textRun.getTextSpan();
                        object = (PGFont)textSpan.getFont();
                        f = object.getSize();
                        fontResource2 = object.getFontResource();
                    }
                    fontResource2.getGlyphBoundingBox(n3, f, arrf);
                    float f5 = arrf[0];
                    f2 = Math.min(0.0f, f5 + f3);
                    textRun.setLeftBearing();
                    break block0;
                }
                f3 += f4;
            }
            if (n2 != 0) continue;
            f3 += textRun.getWidth();
        }
        float f6 = 0.0f;
        f3 = 0.0f;
        block2: for (int i = arrtextRun.length - 1; i >= 0; --i) {
            TextRun textRun = arrtextRun[i];
            n = textRun.getGlyphCount();
            for (int j = n - 1; j >= 0; --j) {
                int n4;
                float f7 = textRun.getAdvance(j);
                if (f7 != 0.0f && (n4 = textRun.getGlyphCode(j)) != 65535) {
                    FontResource fontResource3 = fontResource;
                    if (fontResource3 == null) {
                        object = textRun.getTextSpan();
                        PGFont pGFont = (PGFont)object.getFont();
                        f = pGFont.getSize();
                        fontResource3 = pGFont.getFontResource();
                    }
                    fontResource3.getGlyphBoundingBox(n4, f, arrf);
                    float f8 = arrf[2] - f7;
                    f6 = Math.max(0.0f, f8 - f3);
                    textRun.setRightBearing();
                    break block2;
                }
                f3 += f7;
            }
            if (n != 0) continue;
            f3 += textRun.getWidth();
        }
        textLine.setSideBearings(f2, f6);
    }
}

