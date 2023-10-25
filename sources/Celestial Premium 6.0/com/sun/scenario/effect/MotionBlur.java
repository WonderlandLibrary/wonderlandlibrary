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
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.LinearConvolveCoreEffect;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.scenario.effect.impl.state.MotionBlurState;

public class MotionBlur
extends LinearConvolveCoreEffect {
    private MotionBlurState state = new MotionBlurState();

    public MotionBlur() {
        this(10.0f, 0.0f, DefaultInput);
    }

    public MotionBlur(float f, float f2) {
        this(f, f2, DefaultInput);
    }

    public MotionBlur(float f, float f2, Effect effect) {
        super(effect);
        this.setRadius(f);
        this.setAngle(f2);
    }

    @Override
    LinearConvolveKernel getState() {
        return this.state;
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        return Renderer.getRenderer(filterContext).getAccelType();
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getRadius() {
        return this.state.getRadius();
    }

    public void setRadius(float f) {
        this.state.setRadius(f);
    }

    public float getAngle() {
        return this.state.getAngle();
    }

    public void setAngle(float f) {
        this.state.setAngle(f);
    }

    @Override
    public BaseBounds getBounds(BaseTransform baseTransform, Effect effect) {
        BaseBounds baseBounds = super.getBounds(null, effect);
        int n = this.state.getHPad();
        int n2 = this.state.getVPad();
        RectBounds rectBounds = new RectBounds(baseBounds.getMinX(), baseBounds.getMinY(), baseBounds.getMaxX(), baseBounds.getMaxY());
        rectBounds.grow(n, n2);
        return MotionBlur.transformBounds(baseTransform, rectBounds);
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = super.getResultBounds(baseTransform, rectangle, arrimageData);
        int n = this.state.getHPad();
        int n2 = this.state.getVPad();
        Rectangle rectangle3 = new Rectangle(rectangle2);
        rectangle3.grow(n, n2);
        return rectangle3;
    }

    @Override
    public boolean reducesOpaquePixels() {
        if (!this.state.isNop()) {
            return true;
        }
        Effect effect = this.getInput();
        return effect != null && effect.reducesOpaquePixels();
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        dirtyRegionContainer.grow(this.state.getHPad(), this.state.getVPad());
        return dirtyRegionContainer;
    }
}

