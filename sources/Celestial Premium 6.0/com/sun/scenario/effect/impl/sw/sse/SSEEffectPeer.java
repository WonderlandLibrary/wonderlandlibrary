/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.sse;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class SSEEffectPeer<T extends RenderState>
extends EffectPeer<T> {
    protected SSEEffectPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }
}

