/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font;

import com.sun.javafx.font.DisposerRecord;
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
import java.util.HashMap;
import java.util.Map;

public abstract class PrismFontStrike<T extends PrismFontFile>
implements FontStrike {
    private DisposerRecord disposer;
    private T fontResource;
    private Map<Integer, Glyph> glyphMap = new HashMap<Integer, Glyph>();
    private PrismMetrics metrics;
    protected boolean drawShapes = false;
    private float size;
    private BaseTransform transform;
    private int aaMode;
    private FontStrikeDesc desc;
    private int hash;

    protected PrismFontStrike(T t, float f, BaseTransform baseTransform, int n, FontStrikeDesc fontStrikeDesc) {
        this.fontResource = t;
        this.size = f;
        this.desc = fontStrikeDesc;
        PrismFontFactory prismFontFactory = PrismFontFactory.getFontFactory();
        boolean bl = prismFontFactory.isLCDTextSupported();
        this.aaMode = bl ? n : 0;
        this.transform = baseTransform.isTranslateOrIdentity() ? BaseTransform.IDENTITY_TRANSFORM : new Affine2D(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMxy(), baseTransform.getMyy(), 0.0, 0.0);
    }

    DisposerRecord getDisposer() {
        if (this.disposer == null) {
            this.disposer = this.createDisposer(this.desc);
        }
        return this.disposer;
    }

    protected abstract DisposerRecord createDisposer(FontStrikeDesc var1);

    @Override
    public synchronized void clearDesc() {
        ((PrismFontFile)this.fontResource).getStrikeMap().remove(this.desc);
    }

    @Override
    public float getSize() {
        return this.size;
    }

    @Override
    public Metrics getMetrics() {
        if (this.metrics == null) {
            this.metrics = ((PrismFontFile)this.fontResource).getFontMetrics(this.size);
        }
        return this.metrics;
    }

    public T getFontResource() {
        return this.fontResource;
    }

    @Override
    public boolean drawAsShapes() {
        return this.drawShapes;
    }

    @Override
    public int getAAMode() {
        return this.aaMode;
    }

    @Override
    public BaseTransform getTransform() {
        return this.transform;
    }

    @Override
    public int getQuantizedPosition(Point2D point2D) {
        point2D.x = this.aaMode == 0 ? (float)Math.round(point2D.x) : (float)Math.round(3.0 * (double)point2D.x) / 3.0f;
        point2D.y = Math.round(point2D.y);
        return 0;
    }

    @Override
    public float getCharAdvance(char c) {
        int n = ((PrismFontFile)this.fontResource).getGlyphMapper().charToGlyph((int)c);
        return ((PrismFontFile)this.fontResource).getAdvance(n, this.size);
    }

    @Override
    public Glyph getGlyph(char c) {
        int n = ((PrismFontFile)this.fontResource).getGlyphMapper().charToGlyph((int)c);
        return this.getGlyph(n);
    }

    protected abstract Glyph createGlyph(int var1);

    @Override
    public Glyph getGlyph(int n) {
        Glyph glyph = this.glyphMap.get(n);
        if (glyph == null) {
            glyph = this.createGlyph(n);
            this.glyphMap.put(n, glyph);
        }
        return glyph;
    }

    protected abstract Path2D createGlyphOutline(int var1);

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
            Path2D path2D2;
            int n = glyphList.getGlyphCode(i);
            if (n == 65535 || (path2D2 = this.createGlyphOutline(n)) == null) continue;
            affine2D.setTransform(baseTransform);
            affine2D.translate(glyphList.getPosX(i), glyphList.getPosY(i));
            path2D.append(((Shape)path2D2).getPathIterator(affine2D), false);
        }
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof PrismFontStrike)) {
            return false;
        }
        PrismFontStrike prismFontStrike = (PrismFontStrike)object;
        return this.size == prismFontStrike.size && this.transform.getMxx() == prismFontStrike.transform.getMxx() && this.transform.getMxy() == prismFontStrike.transform.getMxy() && this.transform.getMyx() == prismFontStrike.transform.getMyx() && this.transform.getMyy() == prismFontStrike.transform.getMyy() && ((PrismFontFile)this.fontResource).equals(prismFontStrike.fontResource);
    }

    public int hashCode() {
        if (this.hash != 0) {
            return this.hash;
        }
        this.hash = Float.floatToIntBits(this.size) + Float.floatToIntBits((float)this.transform.getMxx()) + Float.floatToIntBits((float)this.transform.getMyx()) + Float.floatToIntBits((float)this.transform.getMxy()) + Float.floatToIntBits((float)this.transform.getMyy());
        this.hash = 71 * this.hash + ((PrismFontFile)this.fontResource).hashCode();
        return this.hash;
    }

    public String toString() {
        return "FontStrike: " + super.toString() + " font resource = " + this.fontResource + " size = " + this.size + " matrix = " + this.transform;
    }
}

