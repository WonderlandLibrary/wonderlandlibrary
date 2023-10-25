/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;

public class InvertMask
extends CoreEffect<RenderState> {
    private int pad;
    private int xoff;
    private int yoff;

    public InvertMask() {
        this(10);
    }

    public InvertMask(Effect effect) {
        this(10, effect);
    }

    public InvertMask(int n) {
        this(n, DefaultInput);
    }

    public InvertMask(int n, Effect effect) {
        super(effect);
        this.setPad(n);
        this.updatePeerKey("InvertMask");
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public int getPad() {
        return this.pad;
    }

    public void setPad(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Pad value must be non-negative");
        }
        int n2 = this.pad;
        this.pad = n;
    }

    public int getOffsetX() {
        return this.xoff;
    }

    public void setOffsetX(int n) {
        int n2 = this.xoff;
        this.xoff = n;
    }

    public int getOffsetY() {
        return this.yoff;
    }

    public void setOffsetY(int n) {
        float f = this.yoff;
        this.yoff = n;
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        BaseBounds baseBounds = super.getBounds(BaseTransform.IDENTITY_TRANSFORM, effect);
        BaseBounds baseBounds2 = new RectBounds(baseBounds.getMinX(), baseBounds.getMinY(), baseBounds.getMaxX(), baseBounds.getMaxY());
        baseBounds2.grow(this.pad, this.pad);
        if (!baseTransform.isIdentity()) {
            baseBounds2 = InvertMask.transformBounds(baseTransform, baseBounds2);
        }
        return baseBounds2;
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = super.getResultBounds(baseTransform, rectangle, arrimageData);
        Rectangle rectangle3 = new Rectangle(rectangle2);
        rectangle3.grow(this.pad, this.pad);
        return rectangle3;
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return new RenderState(){

            @Override
            public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
                return RenderState.EffectCoordinateSpace.UserSpace;
            }

            @Override
            public BaseTransform getInputTransform(BaseTransform baseTransform) {
                return BaseTransform.IDENTITY_TRANSFORM;
            }

            @Override
            public BaseTransform getResultTransform(BaseTransform baseTransform) {
                return baseTransform;
            }

            @Override
            public Rectangle getInputClip(int n, Rectangle rectangle) {
                if (rectangle != null && InvertMask.this.pad != 0) {
                    rectangle = new Rectangle(rectangle);
                    rectangle.grow(InvertMask.this.pad, InvertMask.this.pad);
                }
                return rectangle;
            }
        };
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
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

