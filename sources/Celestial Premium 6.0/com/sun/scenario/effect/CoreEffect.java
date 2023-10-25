/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.FilterEffect;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

abstract class CoreEffect<T extends RenderState>
extends FilterEffect<T> {
    private String peerKey;
    private int peerCount = -1;

    CoreEffect() {
    }

    CoreEffect(Effect effect) {
        super(effect);
    }

    CoreEffect(Effect effect, Effect effect2) {
        super(effect, effect2);
    }

    final void updatePeerKey(String string) {
        this.updatePeerKey(string, -1);
    }

    final void updatePeerKey(String string, int n) {
        this.peerKey = string;
        this.peerCount = n;
    }

    private EffectPeer getPeer(FilterContext filterContext, int n, int n2) {
        return Renderer.getRenderer(filterContext, this, n, n2).getPeerInstance(filterContext, this.peerKey, this.peerCount);
    }

    final EffectPeer getPeer(FilterContext filterContext, ImageData[] arrimageData) {
        int n;
        int n2;
        if (arrimageData.length > 0) {
            Rectangle rectangle = arrimageData[0].getUntransformedBounds();
            n2 = rectangle.width;
            n = rectangle.height;
        } else {
            n = 500;
            n2 = 500;
        }
        return this.getPeer(filterContext, n2, n);
    }

    @Override
    public ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, T t, ImageData ... arrimageData) {
        return this.getPeer(filterContext, arrimageData).filter(this, t, baseTransform, rectangle, arrimageData);
    }

    @Override
    public Effect.AccelType getAccelType(FilterContext filterContext) {
        EffectPeer effectPeer = this.getPeer(filterContext, 1024, 1024);
        return effectPeer.getAccelType();
    }
}

