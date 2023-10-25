/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.hw;

import com.sun.scenario.effect.Effect;
import java.io.InputStream;

public interface ShaderSource {
    public InputStream loadSource(String var1);

    public Effect.AccelType getAccelType();
}

