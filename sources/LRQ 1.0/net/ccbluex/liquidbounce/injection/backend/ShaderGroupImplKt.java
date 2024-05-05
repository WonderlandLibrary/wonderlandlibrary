/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.shader.ShaderGroup
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.ccbluex.liquidbounce.injection.backend.ShaderGroupImpl;
import net.minecraft.client.shader.ShaderGroup;

public final class ShaderGroupImplKt {
    public static final ShaderGroup unwrap(IShaderGroup $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((ShaderGroupImpl)$this$unwrap).getWrapped();
    }

    public static final IShaderGroup wrap(ShaderGroup $this$wrap) {
        int $i$f$wrap = 0;
        return new ShaderGroupImpl($this$wrap);
    }
}

