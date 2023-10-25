/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw;

import com.sun.scenario.effect.Effect;

public interface RendererDelegate {
    public Effect.AccelType getAccelType();

    public String getPlatformPeerName(String var1, int var2);
}

