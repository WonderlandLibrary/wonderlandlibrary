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
import com.sun.scenario.effect.BoxShadow;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.GaussianShadowState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public class GaussianShadow
extends AbstractShadow {
    private GaussianShadowState state = new GaussianShadowState();

    public GaussianShadow() {
        this(10.0f);
    }

    public GaussianShadow(float f) {
        this(f, Color4f.BLACK);
    }

    public GaussianShadow(float f, Color4f color4f) {
        this(f, color4f, DefaultInput);
    }

    public GaussianShadow(float f, Color4f color4f, Effect effect) {
        super(effect);
        this.state.setRadius(f);
        this.state.setShadowColor(color4f);
    }

    @Override
    LinearConvolveKernel getState() {
        return this.state;
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return Renderer.getRenderer(filterContext).getAccelType();
    }

    @Override
    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    @Override
    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getRadius() {
        return this.state.getRadius();
    }

    public void setRadius(float f) {
        float f2 = this.state.getRadius();
        this.state.setRadius(f);
    }

    public float getHRadius() {
        return this.state.getHRadius();
    }

    public void setHRadius(float f) {
        float f2 = this.state.getHRadius();
        this.state.setHRadius(f);
    }

    public float getVRadius() {
        return this.state.getVRadius();
    }

    public void setVRadius(float f) {
        float f2 = this.state.getVRadius();
        this.state.setVRadius(f);
    }

    @Override
    public float getSpread() {
        return this.state.getSpread();
    }

    @Override
    public void setSpread(float f) {
        float f2 = this.state.getSpread();
        this.state.setSpread(f);
    }

    @Override
    public Color4f getColor() {
        return this.state.getShadowColor();
    }

    @Override
    public void setColor(Color4f color4f) {
        Color4f color4f2 = this.state.getShadowColor();
        this.state.setShadowColor(color4f);
    }

    @Override
    public float getGaussianRadius() {
        return this.getRadius();
    }

    @Override
    public float getGaussianWidth() {
        return this.getHRadius() * 2.0f + 1.0f;
    }

    @Override
    public float getGaussianHeight() {
        return this.getVRadius() * 2.0f + 1.0f;
    }

    @Override
    public void setGaussianRadius(float f) {
        this.setRadius(f);
    }

    @Override
    public void setGaussianWidth(float f) {
        this.setHRadius(f < 1.0f ? 0.0f : (f - 1.0f) / 2.0f);
    }

    @Override
    public void setGaussianHeight(float f) {
        this.setVRadius(f < 1.0f ? 0.0f : (f - 1.0f) / 2.0f);
    }

    @Override
    public AbstractShadow.ShadowMode getMode() {
        return AbstractShadow.ShadowMode.GAUSSIAN;
    }

    @Override
    public AbstractShadow implFor(AbstractShadow.ShadowMode shadowMode) {
        int n = 0;
        switch (shadowMode) {
            case GAUSSIAN: {
                return this;
            }
            case ONE_PASS_BOX: {
                n = 1;
                break;
            }
            case TWO_PASS_BOX: {
                n = 2;
                break;
            }
            case THREE_PASS_BOX: {
                n = 3;
            }
        }
        BoxShadow boxShadow = new BoxShadow();
        boxShadow.setInput(this.getInput());
        boxShadow.setGaussianWidth(this.getGaussianWidth());
        boxShadow.setGaussianHeight(this.getGaussianHeight());
        boxShadow.setColor(this.getColor());
        boxShadow.setPasses(n);
        boxShadow.setSpread(this.getSpread());
        return boxShadow;
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        BaseBounds baseBounds = super.getBounds(null, effect);
        int n = this.state.getPad(0);
        int n2 = this.state.getPad(1);
        RectBounds rectBounds = new RectBounds(baseBounds.getMinX(), baseBounds.getMinY(), baseBounds.getMaxX(), baseBounds.getMaxY());
        rectBounds.grow(n, n2);
        return GaussianShadow.transformBounds(baseTransform, rectBounds);
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = super.getResultBounds(baseTransform, rectangle, arrimageData);
        int n = this.state.getPad(0);
        int n2 = this.state.getPad(1);
        Rectangle rectangle3 = new Rectangle(rectangle2);
        rectangle3.grow(n, n2);
        return rectangle3;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        dirtyRegionContainer.grow(this.state.getPad(0), this.state.getPad(1));
        return dirtyRegionContainer;
    }
}

