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
import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.GaussianShadow;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxShadowState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public class BoxShadow
extends AbstractShadow {
    private final BoxShadowState state = new BoxShadowState();

    public BoxShadow() {
        this(1, 1);
    }

    public BoxShadow(int n, int n2) {
        this(n, n2, 1, DefaultInput);
    }

    public BoxShadow(int n, int n2, int n3) {
        this(n, n2, n3, DefaultInput);
    }

    public BoxShadow(int n, int n2, int n3, Effect effect) {
        super(effect);
        this.setHorizontalSize(n);
        this.setVerticalSize(n2);
        this.setPasses(n3);
        this.setColor(Color4f.BLACK);
        this.setSpread(0.0f);
    }

    @Override
    LinearConvolveKernel getState() {
        return this.state;
    }

    @Override
    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    @Override
    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public int getHorizontalSize() {
        return this.state.getHsize();
    }

    public final void setHorizontalSize(int n) {
        this.state.setHsize(n);
    }

    public int getVerticalSize() {
        return this.state.getVsize();
    }

    public final void setVerticalSize(int n) {
        this.state.setVsize(n);
    }

    public int getPasses() {
        return this.state.getBlurPasses();
    }

    public final void setPasses(int n) {
        this.state.setBlurPasses(n);
    }

    @Override
    public Color4f getColor() {
        return this.state.getShadowColor();
    }

    @Override
    public final void setColor(Color4f color4f) {
        this.state.setShadowColor(color4f);
    }

    @Override
    public float getSpread() {
        return this.state.getSpread();
    }

    @Override
    public final void setSpread(float f) {
        this.state.setSpread(f);
    }

    @Override
    public float getGaussianRadius() {
        float f = (float)(this.getHorizontalSize() + this.getVerticalSize()) / 2.0f;
        return (f *= 3.0f) < 1.0f ? 0.0f : (f - 1.0f) / 2.0f;
    }

    @Override
    public float getGaussianWidth() {
        return (float)this.getHorizontalSize() * 3.0f;
    }

    @Override
    public float getGaussianHeight() {
        return (float)this.getVerticalSize() * 3.0f;
    }

    @Override
    public void setGaussianRadius(float f) {
        float f2 = f * 2.0f + 1.0f;
        this.setGaussianWidth(f2);
        this.setGaussianHeight(f2);
    }

    @Override
    public void setGaussianWidth(float f) {
        this.setHorizontalSize(Math.round(f /= 3.0f));
    }

    @Override
    public void setGaussianHeight(float f) {
        this.setVerticalSize(Math.round(f /= 3.0f));
    }

    @Override
    public AbstractShadow.ShadowMode getMode() {
        switch (this.getPasses()) {
            case 1: {
                return AbstractShadow.ShadowMode.ONE_PASS_BOX;
            }
            case 2: {
                return AbstractShadow.ShadowMode.TWO_PASS_BOX;
            }
        }
        return AbstractShadow.ShadowMode.THREE_PASS_BOX;
    }

    @Override
    public AbstractShadow implFor(AbstractShadow.ShadowMode shadowMode) {
        switch (shadowMode) {
            case GAUSSIAN: {
                GaussianShadow gaussianShadow = new GaussianShadow();
                gaussianShadow.setInput(this.getInput());
                gaussianShadow.setGaussianWidth(this.getGaussianWidth());
                gaussianShadow.setGaussianHeight(this.getGaussianHeight());
                gaussianShadow.setColor(this.getColor());
                gaussianShadow.setSpread(this.getSpread());
                return gaussianShadow;
            }
            case ONE_PASS_BOX: {
                this.setPasses(1);
                break;
            }
            case TWO_PASS_BOX: {
                this.setPasses(2);
                break;
            }
            case THREE_PASS_BOX: {
                this.setPasses(3);
            }
        }
        return this;
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return Renderer.getRenderer(filterContext).getAccelType();
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        BaseBounds baseBounds = super.getBounds(null, effect);
        int n = this.state.getKernelSize(0) / 2;
        int n2 = this.state.getKernelSize(1) / 2;
        RectBounds rectBounds = new RectBounds(baseBounds.getMinX(), baseBounds.getMinY(), baseBounds.getMaxX(), baseBounds.getMaxY());
        rectBounds.grow(n, n2);
        return BoxShadow.transformBounds(baseTransform, rectBounds);
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        rectangle2 = this.state.getResultBounds(rectangle2, 0);
        rectangle2 = this.state.getResultBounds(rectangle2, 1);
        rectangle2.intersectWith(rectangle);
        return rectangle2;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        dirtyRegionContainer.grow(this.state.getKernelSize(0) / 2, this.state.getKernelSize(1) / 2);
        return dirtyRegionContainer;
    }
}

