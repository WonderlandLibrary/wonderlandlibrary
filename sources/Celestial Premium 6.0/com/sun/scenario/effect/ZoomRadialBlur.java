/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.state.ZoomRadialBlurState;

public class ZoomRadialBlur
extends CoreEffect<RenderState> {
    private int r;
    private float centerX;
    private float centerY;
    private final ZoomRadialBlurState state = new ZoomRadialBlurState(this);

    public ZoomRadialBlur() {
        this(1);
    }

    public ZoomRadialBlur(int n) {
        this(n, DefaultInput);
    }

    public ZoomRadialBlur(int n, Effect effect) {
        super(effect);
        this.setRadius(n);
    }

    @Override
    Object getState() {
        return this.state;
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public int getRadius() {
        return this.r;
    }

    public void setRadius(int n) {
        if (n < 1 || n > 64) {
            throw new IllegalArgumentException("Radius must be in the range [1,64]");
        }
        int n2 = this.r;
        this.r = n;
        this.state.invalidateDeltas();
        this.updatePeer();
    }

    private void updatePeer() {
        int n = 4 + this.r - this.r % 4;
        this.updatePeerKey("ZoomRadialBlur", n);
    }

    public float getCenterX() {
        return this.centerX;
    }

    public void setCenterX(float f) {
        float f2 = this.centerX;
        this.centerX = f;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public void setCenterY(float f) {
        float f2 = this.centerY;
        this.centerY = f;
    }

    @Override
    public ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData ... arrimageData) {
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        this.state.updateDeltas(1.0f / (float)rectangle2.width, 1.0f / (float)rectangle2.height);
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, arrimageData);
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.UserSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override
    public DirtyRegionContainer getDirtyRegions(Effect effect, DirtyRegionPool dirtyRegionPool) {
        Effect effect2 = this.getDefaultedInput(0, effect);
        DirtyRegionContainer dirtyRegionContainer = effect2.getDirtyRegions(effect, dirtyRegionPool);
        int n = this.getRadius();
        dirtyRegionContainer.grow(n, n);
        return dirtyRegionContainer;
    }
}

