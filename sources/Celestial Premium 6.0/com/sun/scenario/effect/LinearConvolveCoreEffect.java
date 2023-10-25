/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.CoreEffect;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

public abstract class LinearConvolveCoreEffect
extends CoreEffect<LinearConvolveRenderState> {
    public LinearConvolveCoreEffect(Effect effect) {
        super(effect);
    }

    @Override
    public final LinearConvolveRenderState getRenderState(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, Object object, Effect effect) {
        return this.getState().getRenderState(baseTransform);
    }

    @Override
    abstract LinearConvolveKernel getState();

    @Override
    public ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, LinearConvolveRenderState linearConvolveRenderState, ImageData ... arrimageData) {
        ImageData imageData = arrimageData[0];
        imageData.addref();
        if (linearConvolveRenderState.isNop()) {
            return imageData;
        }
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        int n = rectangle2.width;
        int n2 = rectangle2.height;
        Rectangle rectangle3 = rectangle;
        Renderer renderer = Renderer.getRenderer(filterContext, this, n, n2);
        for (int i = 0; i < 2; ++i) {
            imageData = linearConvolveRenderState.validatePassInput(imageData, i);
            EffectPeer<? extends LinearConvolveRenderState> effectPeer = linearConvolveRenderState.getPassPeer(renderer, filterContext);
            if (effectPeer == null) continue;
            effectPeer.setPass(i);
            ImageData imageData2 = effectPeer.filter(this, linearConvolveRenderState, baseTransform, rectangle3, imageData);
            imageData.unref();
            imageData = imageData2;
            if (imageData.validate(filterContext)) continue;
            imageData.unref();
            return imageData;
        }
        return imageData;
    }
}

