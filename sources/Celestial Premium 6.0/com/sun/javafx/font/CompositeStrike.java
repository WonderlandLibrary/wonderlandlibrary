/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.CompositeFontResource;
import com.sun.javafx.font.CompositeStrikeDisposer;
import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismMetrics;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;

public class CompositeStrike
implements FontStrike {
    private CompositeFontResource fontResource;
    private float size;
    private int aaMode;
    BaseTransform transform;
    private FontStrike slot0Strike;
    private FontStrike[] strikeSlots;
    private FontStrikeDesc desc;
    DisposerRecord disposer;
    private PrismMetrics metrics;

    @Override
    public void clearDesc() {
        this.fontResource.getStrikeMap().remove(this.desc);
        if (this.slot0Strike != null) {
            this.slot0Strike.clearDesc();
        }
        if (this.strikeSlots != null) {
            for (int i = 1; i < this.strikeSlots.length; ++i) {
                if (this.strikeSlots[i] == null) continue;
                this.strikeSlots[i].clearDesc();
            }
        }
    }

    CompositeStrike(CompositeFontResource compositeFontResource, float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        this.fontResource = compositeFontResource;
        this.size = f;
        this.transform = baseTransform.isTranslateOrIdentity() ? BaseTransform.IDENTITY_TRANSFORM : baseTransform.copy();
        this.desc = fontStrikeDesc;
        this.aaMode = n;
        this.disposer = new CompositeStrikeDisposer(compositeFontResource, fontStrikeDesc);
    }

    @Override
    public int getAAMode() {
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        if (prismFontFactory.isLCDTextSupported()) {
            return this.aaMode;
        }
        return 0;
    }

    @Override
    public BaseTransform getTransform() {
        return this.transform;
    }

    public FontStrike getStrikeSlot(int n) {
        Object object;
        if (n == 0) {
            if (this.slot0Strike == null) {
                FontResource fontResource = this.fontResource.getSlotResource(0);
                this.slot0Strike = fontResource.getStrike(this.size, this.transform, this.getAAMode());
            }
            return this.slot0Strike;
        }
        if (this.strikeSlots == null) {
            this.strikeSlots = new FontStrike[this.fontResource.getNumSlots()];
        }
        if (n >= this.strikeSlots.length) {
            object = new FontStrike[this.fontResource.getNumSlots()];
            System.arraycopy(this.strikeSlots, 0, object, 0, this.strikeSlots.length);
            this.strikeSlots = object;
        }
        if (this.strikeSlots[n] == null) {
            object = this.fontResource.getSlotResource(n);
            this.strikeSlots[n] = object.getStrike(this.size, this.transform, this.getAAMode());
        }
        return this.strikeSlots[n];
    }

    @Override
    public FontResource getFontResource() {
        return this.fontResource;
    }

    public int getStrikeSlotForGlyph(int n) {
        return n >>> 24;
    }

    @Override
    public float getSize() {
        return this.size;
    }

    @Override
    public boolean drawAsShapes() {
        return this.getStrikeSlot(0).drawAsShapes();
    }

    @Override
    public Metrics getMetrics() {
        if (this.metrics == null) {
            PrismFontFile prismFontFile = (PrismFontFile)this.fontResource.getSlotResource(0);
            this.metrics = prismFontFile.getFontMetrics(this.size);
        }
        return this.metrics;
    }

    @Override
    public Glyph getGlyph(char c) {
        int n = this.fontResource.getGlyphMapper().charToGlyph(c);
        return this.getGlyph(n);
    }

    @Override
    public Glyph getGlyph(int n) {
        int n2 = n >>> 24;
        int n3 = n & 0xFFFFFF;
        return this.getStrikeSlot(n2).getGlyph(n3);
    }

    @Override
    public float getCharAdvance(char c) {
        int n = this.fontResource.getGlyphMapper().charToGlyph((int)c);
        return this.fontResource.getAdvance(n, this.size);
    }

    @Override
    public int getQuantizedPosition(Point2D point2D) {
        return this.getStrikeSlot(0).getQuantizedPosition(point2D);
    }

    @Override
    public Shape getOutline(GlyphList glyphList, BaseTransform baseTransform) {
        Path2D path2D = new Path2D();
        this.getOutline(glyphList, baseTransform, path2D);
        return path2D;
    }

    void getOutline(GlyphList glyphList, BaseTransform baseTransform, Path2D path2D) {
        path2D.reset();
        if (glyphList == null) {
            return;
        }
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        Affine2D affine2D = new Affine2D();
        for (int i = 0; i < glyphList.getGlyphCount(); ++i) {
            Glyph glyph;
            Shape shape;
            int n = glyphList.getGlyphCode(i);
            if (n == 65535 || (shape = (glyph = this.getGlyph(n)).getShape()) == null) continue;
            affine2D.setTransform(baseTransform);
            affine2D.translate(glyphList.getPosX(i), glyphList.getPosY(i));
            path2D.append(shape.getPathIterator(affine2D), false);
        }
    }
}

