/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.shader.ShaderGroup
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.minecraft.client.shader.ShaderGroup;
import org.jetbrains.annotations.Nullable;

public final class ShaderGroupImpl
implements IShaderGroup {
    private final ShaderGroup wrapped;

    public final ShaderGroup getWrapped() {
        return this.wrapped;
    }

    @Override
    public String getShaderGroupName() {
        return this.wrapped.func_148022_b();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ShaderGroupImpl && ((ShaderGroupImpl)object).wrapped.equals(this.wrapped);
    }

    public ShaderGroupImpl(ShaderGroup shaderGroup) {
        this.wrapped = shaderGroup;
    }
}

