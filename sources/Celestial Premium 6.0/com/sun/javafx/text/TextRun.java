/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.text;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.scene.text.TextSpan;
import com.sun.javafx.text.TextLine;

public class TextRun
implements GlyphList {
    int glyphCount;
    int[] gids;
    float[] positions;
    int[] charIndices;
    int start;
    int length;
    float width = -1.0f;
    byte level;
    int script;
    TextSpan span;
    TextLine line;
    Point2D location;
    private float ascent;
    private float descent;
    private float leading;
    int flags = 0;
    int slot = 0;
    static final int FLAGS_TAB = 1;
    static final int FLAGS_LINEBREAK = 2;
    static final int FLAGS_SOFTBREAK = 4;
    static final int FLAGS_NO_LINK_BEFORE = 8;
    static final int FLAGS_NO_LINK_AFTER = 16;
    static final int FLAGS_COMPLEX = 32;
    static final int FLAGS_EMBEDDED = 64;
    static final int FLAGS_SPLIT = 128;
    static final int FLAGS_SPLIT_LAST = 256;
    static final int FLAGS_LEFT_BEARING = 512;
    static final int FLAGS_RIGHT_BEARING = 1024;
    static final int FLAGS_CANONICAL = 2048;
    static final int FLAGS_COMPACT = 4096;
    float cacheWidth = 0.0f;
    int cacheIndex = 0;

    public TextRun(int n, int n2, byte by, boolean bl, int n3, TextSpan textSpan, int n4, boolean bl2) {
        this.start = n;
        this.length = n2;
        this.level = by;
        this.script = n3;
        this.span = textSpan;
        this.slot = n4;
        if (bl) {
            this.flags |= 0x20;
        }
        if (bl2) {
            this.flags |= 0x800;
        }
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.start + this.length;
    }

    public int getLength() {
        return this.length;
    }

    public byte getLevel() {
        return this.level;
    }

    @Override
    public RectBounds getLineBounds() {
        return this.line.getBounds();
    }

    public void setLine(TextLine textLine) {
        this.line = textLine;
    }

    public int getScript() {
        return this.script;
    }

    @Override
    public TextSpan getTextSpan() {
        return this.span;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isLinebreak() {
        return (this.flags & 2) != 0;
    }

    public boolean isCanonical() {
        return (this.flags & 0x800) != 0;
    }

    public boolean isSoftbreak() {
        return (this.flags & 4) != 0;
    }

    public boolean isBreak() {
        return (this.flags & 6) != 0;
    }

    public boolean isTab() {
        return (this.flags & 1) != 0;
    }

    public boolean isEmbedded() {
        return (this.flags & 0x40) != 0;
    }

    public boolean isNoLinkBefore() {
        return (this.flags & 8) != 0;
    }

    public boolean isNoLinkAfter() {
        return (this.flags & 0x10) != 0;
    }

    public boolean isSplit() {
        return (this.flags & 0x80) != 0;
    }

    public boolean isSplitLast() {
        return (this.flags & 0x100) != 0;
    }

    @Override
    public boolean isComplex() {
        return (this.flags & 0x20) != 0;
    }

    public boolean isLeftBearing() {
        return (this.flags & 0x200) != 0;
    }

    public boolean isRightBearing() {
        return (this.flags & 0x400) != 0;
    }

    public boolean isLeftToRight() {
        return (this.level & 1) == 0;
    }

    public void setComplex(boolean bl) {
        this.flags = bl ? (this.flags |= 0x20) : (this.flags &= 0xFFFFFFDF);
    }

    @Override
    public float getWidth() {
        if (this.width != -1.0f) {
            return this.width;
        }
        if (this.positions != null) {
            if ((this.flags & 0x1000) != 0) {
                this.width = 0.0f;
                for (int i = 0; i < this.glyphCount; ++i) {
                    this.width += this.positions[this.start + i];
                }
                return this.width;
            }
            return this.positions[this.glyphCount << 1];
        }
        return 0.0f;
    }

    @Override
    public float getHeight() {
        return -this.ascent + this.descent + this.leading;
    }

    public void setWidth(float f) {
        this.width = f;
    }

    public void setMetrics(float f, float f2, float f3) {
        this.ascent = f;
        this.descent = f2;
        this.leading = f3;
    }

    public float getAscent() {
        return this.ascent;
    }

    public float getDescent() {
        return this.descent;
    }

    public float getLeading() {
        return this.leading;
    }

    public void setLocation(float f, float f2) {
        this.location = new Point2D(f, f2);
    }

    @Override
    public Point2D getLocation() {
        return this.location;
    }

    public void setTab() {
        this.flags |= 1;
    }

    public void setEmbedded(RectBounds rectBounds, int n) {
        this.width = rectBounds.getWidth() * (float)n;
        this.ascent = rectBounds.getMinY();
        this.descent = rectBounds.getHeight() + this.ascent;
        this.length = n;
        this.flags |= 0x40;
    }

    public void setLinebreak() {
        this.flags |= 2;
    }

    public void setSoftbreak() {
        this.flags |= 4;
    }

    public void setLeftBearing() {
        this.flags |= 0x200;
    }

    public void setRightBearing() {
        this.flags |= 0x400;
    }

    public int getWrapIndex(float f) {
        if (this.glyphCount == 0) {
            return 0;
        }
        if (this.isLeftToRight()) {
            int n;
            if ((this.flags & 0x1000) != 0) {
                float f2 = 0.0f;
                for (n = 0; n < this.glyphCount; ++n) {
                    if (!((f2 += this.positions[this.start + n]) > f)) continue;
                    return this.getCharOffset(n);
                }
            } else {
                while (n < this.glyphCount) {
                    if (this.positions[n + 1 << 1] > f) {
                        return this.getCharOffset(n);
                    }
                    ++n;
                }
            }
        } else {
            int n = 0;
            float f3 = this.positions[this.glyphCount << 1];
            while (f3 > f) {
                float f4 = this.positions[n + 1 << 1] - this.positions[n << 1];
                if (f3 - f4 <= f) {
                    return this.getCharOffset(n);
                }
                f3 -= f4;
                ++n;
            }
        }
        return 0;
    }

    @Override
    public int getGlyphCount() {
        return this.glyphCount;
    }

    @Override
    public int getGlyphCode(int n) {
        if (0 <= n && n < this.glyphCount) {
            if ((this.flags & 0x1000) != 0) {
                return this.gids[this.start + n];
            }
            return this.gids[n];
        }
        return 65535;
    }

    @Override
    public float getPosX(int n) {
        if (0 <= n && n <= this.glyphCount) {
            if ((this.flags & 0x1000) != 0) {
                if (this.cacheIndex == n) {
                    return this.cacheWidth;
                }
                float f = 0.0f;
                if (this.cacheIndex + 1 == n) {
                    f = this.cacheWidth + this.positions[this.start + n - 1];
                } else {
                    for (int i = 0; i < n; ++i) {
                        f += this.positions[this.start + i];
                    }
                }
                this.cacheIndex = n;
                this.cacheWidth = f;
                return f;
            }
            return this.positions[n << 1];
        }
        return n == 0 ? 0.0f : this.getWidth();
    }

    @Override
    public float getPosY(int n) {
        if ((this.flags & 0x1000) != 0) {
            return 0.0f;
        }
        if (0 <= n && n <= this.glyphCount) {
            return this.positions[(n << 1) + 1];
        }
        return 0.0f;
    }

    public float getAdvance(int n) {
        if ((this.flags & 0x1000) != 0) {
            return this.positions[this.start + n];
        }
        return this.positions[n + 1 << 1] - this.positions[n << 1];
    }

    public void shape(int n, int[] arrn, float[] arrf, int[] arrn2) {
        this.glyphCount = n;
        this.gids = arrn;
        this.positions = arrf;
        this.charIndices = arrn2;
    }

    public void shape(int n, int[] arrn, float[] arrf) {
        this.glyphCount = n;
        this.gids = arrn;
        this.positions = arrf;
        this.charIndices = null;
        this.flags |= 0x1000;
    }

    public float getXAtOffset(int n, boolean bl) {
        boolean bl2 = this.isLeftToRight();
        if (n == this.length) {
            return bl2 ? this.getWidth() : 0.0f;
        }
        if (this.glyphCount > 0) {
            int n2 = this.getGlyphIndex(n);
            if (bl2) {
                return this.getPosX(n2 + (bl ? 0 : 1));
            }
            return this.getPosX(n2 + (bl ? 1 : 0));
        }
        if (this.isTab()) {
            if (bl2) {
                return bl ? 0.0f : this.getWidth();
            }
            return bl ? this.getWidth() : 0.0f;
        }
        return 0.0f;
    }

    public int getGlyphAtX(float f, int[] arrn) {
        boolean bl = this.isLeftToRight();
        float f2 = 0.0f;
        for (int i = 0; i < this.glyphCount; ++i) {
            float f3 = this.getAdvance(i);
            if (f2 + f3 > f) {
                if (arrn != null) {
                    arrn[0] = f - f2 > f3 / 2.0f ? (bl ? 1 : 0) : (bl ? 0 : 1);
                }
                return i;
            }
            f2 += f3;
        }
        if (arrn != null) {
            arrn[0] = bl ? 1 : 0;
        }
        return Math.max(0, this.glyphCount - 1);
    }

    public int getOffsetAtX(float f, int[] arrn) {
        if (this.glyphCount > 0) {
            int n = this.getGlyphAtX(f, arrn);
            return this.getCharOffset(n);
        }
        if (this.width != -1.0f && this.length > 0 && arrn != null && f > this.width / 2.0f) {
            arrn[0] = 1;
        }
        return 0;
    }

    private void reset() {
        this.positions = null;
        this.charIndices = null;
        this.gids = null;
        this.width = -1.0f;
        this.leading = 0.0f;
        this.descent = 0.0f;
        this.ascent = 0.0f;
        this.glyphCount = 0;
    }

    public TextRun split(int n) {
        int n2 = this.length - n;
        this.length = n;
        boolean bl = this.isComplex();
        TextRun textRun = new TextRun(this.start + this.length, n2, this.level, bl, this.script, this.span, this.slot, this.isCanonical());
        this.flags |= 0x10;
        textRun.flags |= 8;
        this.flags |= 0x80;
        this.flags &= 0xFFFFFEFF;
        textRun.flags |= 0x100;
        textRun.setMetrics(this.ascent, this.descent, this.leading);
        if (!bl) {
            this.glyphCount = this.length;
            if ((this.flags & 0x1000) != 0) {
                textRun.shape(n2, this.gids, this.positions);
                if (this.width != -1.0f) {
                    if (n2 > this.length) {
                        float f = this.width;
                        this.width = -1.0f;
                        textRun.setWidth(f - this.getWidth());
                    } else {
                        this.width -= textRun.getWidth();
                    }
                }
            } else {
                int[] arrn = new int[n2];
                float[] arrf = new float[n2 + 1 << 1];
                System.arraycopy(this.gids, n, arrn, 0, n2);
                float f = this.getWidth();
                int n3 = n << 1;
                for (int i = 2; i < arrf.length; i += 2) {
                    arrf[i] = this.positions[i + n3] - f;
                }
                textRun.shape(n2, arrn, arrf, null);
            }
        } else {
            this.reset();
        }
        return textRun;
    }

    public void merge(TextRun textRun) {
        if (textRun != null) {
            this.length += textRun.length;
            this.glyphCount += textRun.glyphCount;
            this.width = this.width != -1.0f && textRun.width != -1.0f ? (this.width += textRun.width) : -1.0f;
        }
        this.flags &= 0xFFFFFF7F;
        this.flags &= 0xFFFFFEFF;
    }

    public TextRun unwrap() {
        TextRun textRun = new TextRun(this.start, this.length, this.level, this.isComplex(), this.script, this.span, this.slot, this.isCanonical());
        textRun.shape(this.glyphCount, this.gids, this.positions);
        textRun.setWidth(this.width);
        textRun.setMetrics(this.ascent, this.descent, this.leading);
        int n = 28;
        textRun.flags = this.flags & ~n;
        return textRun;
    }

    public void justify(int n, float f) {
        if (this.positions != null) {
            int n2 = this.getGlyphIndex(n);
            if (n2 != -1) {
                for (int i = n2 + 1; i <= this.glyphCount; ++i) {
                    int n3 = i << 1;
                    this.positions[n3] = this.positions[n3] + f;
                }
                this.width = -1.0f;
            }
            this.setComplex(true);
        }
    }

    public int getGlyphIndex(int n) {
        if (this.charIndices == null) {
            return n;
        }
        for (int i = 0; i < this.charIndices.length && i < this.glyphCount; ++i) {
            if (this.charIndices[i] != n) continue;
            return i;
        }
        if (this.isLeftToRight()) {
            if (n > 0) {
                return this.getGlyphIndex(n - 1);
            }
        } else if (n + 1 < this.length) {
            return this.getGlyphIndex(n + 1);
        }
        return 0;
    }

    @Override
    public int getCharOffset(int n) {
        return this.charIndices == null ? n : this.charIndices[n];
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("TextRun start=");
        stringBuffer.append(this.start);
        stringBuffer.append(", length=");
        stringBuffer.append(this.length);
        stringBuffer.append(", script=");
        stringBuffer.append(this.script);
        stringBuffer.append(", linebreak=");
        stringBuffer.append(this.isLinebreak());
        stringBuffer.append(", softbreak=");
        stringBuffer.append(this.isSoftbreak());
        stringBuffer.append(", complex=");
        stringBuffer.append(this.isComplex());
        stringBuffer.append(", tab=");
        stringBuffer.append(this.isTab());
        stringBuffer.append(", compact=");
        stringBuffer.append((this.flags & 0x1000) != 0);
        stringBuffer.append(", ltr=");
        stringBuffer.append(this.isLeftToRight());
        stringBuffer.append(", split=");
        stringBuffer.append(this.isSplit());
        return stringBuffer.toString();
    }
}

