/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.hw.d3d;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.hw.ShaderSource;
import java.io.InputStream;

public class D3DShaderSource
implements ShaderSource {
    @Override
    public InputStream loadSource(String string) {
        return D3DShaderSource.class.getResourceAsStream("hlsl/" + string + ".obj");
    }

    @Override
    public Effect.AccelType getAccelType() {
        return Effect.AccelType.DIRECT3D;
    }
}

