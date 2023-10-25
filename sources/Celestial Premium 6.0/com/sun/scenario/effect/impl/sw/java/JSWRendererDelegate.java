/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.sw.RendererDelegate;

public class JSWRendererDelegate
implements RendererDelegate {
    @Override
    public Effect.AccelType getAccelType() {
        return Effect.AccelType.NONE;
    }

    @Override
    public String getPlatformPeerName(String string, int n) {
        return "com.sun.scenario.effect.impl.sw.java.JSW" + string + "Peer";
    }
}

