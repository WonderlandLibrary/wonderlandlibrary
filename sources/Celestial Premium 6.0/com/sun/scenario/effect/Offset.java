/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.Translate2D;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;

public class Offset
extends Effect {
    private int xoff;
    private int yoff;

    public Offset(int n, int n2, Effect effect) {
        super(effect);
        this.xoff = n;
        this.yoff = n2;
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public int getX() {
        return this.xoff;
    }

    public void setX(int n) {
        int n2 = this.xoff;
        this.xoff = n;
    }

    public int getY() {
        return this.yoff;
    }

    public void setY(int n) {
        float f = this.yoff;
        this.yoff = n;
    }

    static BaseTransform getOffsetTransform(BaseTransform baseTransform, double d, double d2) {
        if (baseTransform == null || baseTransform.isIdentity()) {
            return Translate2D.getInstance(d, d2);
        }
        return baseTransform.copy().deriveWithTranslation(d, d2);
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        BaseTransform baseTransform2 = Offset.getOffsetTransform(baseTransform, this.xoff, this.yoff);
        Effect effect2 = this.getDefaultedInput(0, effect);
        return effect2.getBounds(baseTransform2, effect);
    }

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        BaseTransform baseTransform2 = Offset.getOffsetTransform(baseTransform, this.xoff, this.yoff);
        Effect effect2 = this.getDefaultedInput(0, effect);
        return effect2.filter(filterContext, baseTransform2, rectangle, object, effect);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        point2D = this.getDefaultedInput(0, effect).transform(point2D, effect);
        float f = point2D.x + (float)this.xoff;
        float f2 = point2D.y + (float)this.yoff;
        point2D = new Point2D(f, f2);
        return point2D;
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        float f = point2D.x - (float)this.xoff;
        float f2 = point2D.y - (float)this.yoff;
        point2D = new Point2D(f, f2);
        point2D = this.getDefaultedInput(0, effect).untransform(point2D, effect);
        return point2D;
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return this.getInputs().get(0).getAccelType(filterContext);
    }

    @Override
    public boolean reducesOpaquePixels() {
        return this.getX() != 0 || this.getY() != 0 || this.getInput() != null && this.getInput().reducesOpaquePixels();
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        if (this.xoff != 0 || this.yoff != 0) {
            for (int i = 0; i < dirtyRegionContainer.size(); ++i) {
                dirtyRegionContainer.getDirtyRegion(i).translate(this.xoff, this.yoff, 0.0f);
            }
        }
        return dirtyRegionContainer;
    }
}

