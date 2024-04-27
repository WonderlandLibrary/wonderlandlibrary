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
    public static final IShaderGroup wrap(ShaderGroup shaderGroup) {
        boolean bl = false;
        return new ShaderGroupImpl(shaderGroup);
    }

    public static final ShaderGroup unwrap(IShaderGroup iShaderGroup) {
        boolean bl = false;
        return ((ShaderGroupImpl)iShaderGroup).getWrapped();
    }
}

