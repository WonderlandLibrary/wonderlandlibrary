/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.ps.PPSRenderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class PPSEffectPeer<T extends RenderState>
extends EffectPeer<T> {
    protected PPSEffectPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public final ImageData filter(Effect effect, T t, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setEffect(effect);
        this.setRenderState(t);
        this.setDestBounds(this.getResultBounds(baseTransform, rectangle, arrimageData));
        return this.filterImpl(arrimageData);
    }

    abstract ImageData filterImpl(ImageData ... var1);

    protected abstract boolean isSamplerLinear(int var1);

    protected abstract Shader createShader();

    protected abstract void updateShader(Shader var1);

    @Override
    public abstract void dispose();

    @Override
    protected final PPSRenderer getRenderer() {
        return (PPSRenderer)super.getRenderer();
    }

    protected final String getShaderName() {
        return this.getUniqueName();
    }
}

