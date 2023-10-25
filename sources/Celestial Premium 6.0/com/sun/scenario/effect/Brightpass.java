/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.state.RenderState;

public class Brightpass
extends CoreEffect<RenderState> {
    private float threshold;

    public Brightpass() {
        this(DefaultInput);
    }

    public Brightpass(Effect effect) {
        super(effect);
        this.setThreshold(0.3f);
        this.updatePeerKey("Brightpass");
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getThreshold() {
        return this.threshold;
    }

    public void setThreshold(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Threshold must be in the range [0,1]");
        }
        float f2 = this.threshold;
        this.threshold = f;
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        return true;
    }
}

