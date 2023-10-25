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
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;

public abstract class DelegateEffect
extends Effect {
    protected DelegateEffect(Effect effect) {
        super(effect);
    }

    protected DelegateEffect(Effect effect, Effect effect2) {
        super(effect, effect2);
    }

    protected abstract Effect getDelegate();

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        return this.getDelegate().getBounds(baseTransform, effect);
    }

    @Override
    public ImageData filter(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return this.getDelegate().filter(filterContext, baseTransform, rectangle, object, effect);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return this.getDelegate().untransform(point2D, effect);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return this.getDelegate().transform(point2D, effect);
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return this.getDelegate().getAccelType(filterContext);
    }

    @Override
    public boolean reducesOpaquePixels() {
        return this.getDelegate().reducesOpaquePixels();
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        return this.getDelegate().getDirtyRegions(effect, dirtyRegionPool);
    }
}

