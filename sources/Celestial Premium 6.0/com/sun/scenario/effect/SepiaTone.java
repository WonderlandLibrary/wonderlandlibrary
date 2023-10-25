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

public class SepiaTone
extends CoreEffect<RenderState> {
    private float level;

    public SepiaTone() {
        this(DefaultInput);
    }

    public SepiaTone(Effect effect) {
        super(effect);
        this.setLevel(1.0f);
        this.updatePeerKey("SepiaTone");
    }

    public final Effect getInput() {
        return this.getInputs().get(0);
    }

    public void setInput(Effect effect) {
        this.setInput(0, effect);
    }

    public float getLevel() {
        return this.level;
    }

    public void setLevel(float f) {
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Level must be in the range [0,1]");
        }
        float f2 = this.level;
        this.level = f;
    }

    @Override
    public RenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override
    public boolean reducesOpaquePixels() {
        Effect effect = this.getInput();
        return effect != null && effect.reducesOpaquePixels();
    }
}

