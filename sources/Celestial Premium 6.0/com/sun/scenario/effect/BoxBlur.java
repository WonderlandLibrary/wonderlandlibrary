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
import com.sun.scenario.effect.impl.state.BoxBlurState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

public class BoxBlur
extends LinearConvolveCoreEffect {
    private final BoxBlurState state = new BoxBlurState();

    public BoxBlur() {
        this(1, 1);
    }

    public BoxBlur(int n, int n2) {
        this(n, n2, 1, DefaultInput);
    }

    public BoxBlur(int n, int n2, int n3) {
        this(n, n2, n3, DefaultInput);
    }

    public BoxBlur(int n, int n2, int n3, Effect effect) {
        super(effect);
        this.setHorizontalSize(n);
        this.setVerticalSize(n2);
        this.setPasses(n3);
    }

    @Override
    LinearConvolveKernel getState() {
        return this.state;
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

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
        return BoxBlur.transformBounds(baseTransform, rectBounds);
    }

    @Override
    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = arrimageData[0].getTransformedBounds(null);
        rectangle2 = this.state.getResultBounds(rectangle2, 0);
        rectangle2 = this.state.getResultBounds(rectangle2, 1);
        rectangle2.intersectWith(rectangle);
        return rectangle2;
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
        dirtyRegionContainer.grow(this.state.getKernelSize(0) / 2, this.state.getKernelSize(1) / 2);
        return dirtyRegionContainer;
    }
}

