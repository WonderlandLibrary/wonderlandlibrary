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
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;

public class DisplacementMap
extends CoreEffect<RenderState> {
    private FloatMap mapData;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    private boolean wrap;

    public DisplacementMap(FloatMap floatMap) {
        this(floatMap, DefaultInput);
    }

    public DisplacementMap(FloatMap floatMap, Effect effect) {
        super(effect);
        this.setMapData(floatMap);
        this.updatePeerKey("DisplacementMap");
    }

    public final FloatMap getMapData() {
        return this.mapData;
    }

    public void setMapData(FloatMap floatMap) {
        if (floatMap == null) {
            throw new IllegalArgumentException("Map data must be non-null");
        }
        FloatMap floatMap2 = this.mapData;
        this.mapData = floatMap;
    }

    public final Effect getContentInput() {
        return this.getInputs().get(0);
    }

    public void setContentInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public void setScaleX(float f) {
        float f2 = this.scaleX;
        this.scaleX = f;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public void setScaleY(float f) {
        float f2 = this.scaleY;
        this.scaleY = f;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float f) {
        float f2 = this.offsetX;
        this.offsetX = f;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float f) {
        float f2 = this.offsetY;
        this.offsetY = f;
    }

    public boolean getWrap() {
        return this.wrap;
    }

    public void setWrap(boolean bl) {
        boolean bl2 = this.wrap;
        this.wrap = bl;
    }

    @Override
    public Point2D transform(Point2D point2D, Effect effect) {
        return new Point2D(Float.NaN, Float.NaN);
    }

    @Override
    public Point2D untransform(Point2D point2D, Effect effect) {
        BaseBounds baseBounds = this.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        float f = baseBounds.getWidth();
        float f2 = baseBounds.getHeight();
        float f3 = (point2D.x - baseBounds.getMinX()) / f;
        float f4 = (point2D.y - baseBounds.getMinY()) / f2;
        if (f3 >= 0.0f && f4 >= 0.0f && f3 < 1.0f && f4 < 1.0f) {
            int n = (int)(f3 * (float)this.mapData.getWidth());
            int n2 = (int)(f4 * (float)this.mapData.getHeight());
            float f5 = this.mapData.getSample(n, n2, 0);
            float f6 = this.mapData.getSample(n, n2, 1);
            f3 += this.scaleX * (f5 + this.offsetX);
            f4 += this.scaleY * (f6 + this.offsetY);
            if (this.wrap) {
                f3 = (float)((double)f3 - Math.floor(f3));
                f4 = (float)((double)f4 - Math.floor(f4));
            }
            point2D = new Point2D(f3 * f + baseBounds.getMinX(), f4 * f2 + baseBounds.getMinY());
        }
        return this.getDefaultedInput(0, effect).untransform(point2D, effect);
    }

    @Override
    public ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData ... arrimageData) {
        return super.filterImageDatas(filterContext, baseTransform, null, renderState, arrimageData);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.UnclippedUserSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        DirtyRegionContainer dirtyRegionContainer = dirtyRegionPool.checkOut();
        dirtyRegionContainer.deriveWithNewRegion((RectBounds)this.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect));
        return dirtyRegionContainer;
    }
}

