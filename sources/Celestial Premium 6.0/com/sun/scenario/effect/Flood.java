/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.state.RenderState;

public class Flood
extends CoreEffect<RenderState> {
    private Object paint;
    private RectBounds bounds = new RectBounds();

    public Flood(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Paint must be non-null");
        }
        this.paint = object;
        this.updatePeerKey("Flood");
    }

    public Flood(Object object, RectBounds rectBounds) {
        this(object);
        if (rectBounds == null) {
            throw new IllegalArgumentException("Bounds must be non-null");
        }
        this.bounds.setBounds(rectBounds);
    }

    public Object getPaint() {
        return this.paint;
    }

    public void setPaint(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Paint must be non-null");
        }
        Object object2 = this.paint;
        this.paint = object;
    }

    public RectBounds getFloodBounds() {
        return new RectBounds(this.bounds);
    }

    public void setFloodBounds(RectBounds rectBounds) {
        if (rectBounds == null) {
            throw new IllegalArgumentException("Bounds must be non-null");
        }
        RectBounds rectBounds2 = new RectBounds(this.bounds);
        this.bounds.setBounds(rectBounds);
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        return Flood.transformBounds(baseTransform, this.bounds);
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return new Point2D(Float.NaN, Float.NaN);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        return new Point2D(Float.NaN, Float.NaN);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        DirtyRegionContainer dirtyRegionContainer = dirtyRegionPool.checkOut();
        dirtyRegionContainer.reset();
        return dirtyRegionContainer;
    }
}

