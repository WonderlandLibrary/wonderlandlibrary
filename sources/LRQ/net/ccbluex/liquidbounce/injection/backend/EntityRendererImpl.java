/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IEntityRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.ccbluex.liquidbounce.injection.backend.ShaderGroupImpl;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class EntityRendererImpl
implements IEntityRenderer {
    private final EntityRenderer wrapped;

    @Override
    public IShaderGroup getShaderGroup() {
        IShaderGroup iShaderGroup;
        ShaderGroup shaderGroup = this.wrapped.func_147706_e();
        if (shaderGroup != null) {
            ShaderGroup $this$wrap$iv = shaderGroup;
            boolean $i$f$wrap = false;
            iShaderGroup = new ShaderGroupImpl($this$wrap$iv);
        } else {
            iShaderGroup = null;
        }
        return iShaderGroup;
    }

    @Override
    public void disableLightmap() {
        this.wrapped.func_175072_h();
    }

    @Override
    public boolean isShaderActive() {
        return this.wrapped.func_147702_a();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void loadShader(IResourceLocation resourceLocation) {
        void $this$unwrap$iv;
        IResourceLocation iResourceLocation = resourceLocation;
        EntityRenderer entityRenderer = this.wrapped;
        boolean $i$f$unwrap = false;
        ResourceLocation resourceLocation2 = ((ResourceLocationImpl)$this$unwrap$iv).getWrapped();
        entityRenderer.func_175069_a(resourceLocation2);
    }

    @Override
    public void stopUseShader() {
        this.wrapped.func_181022_b();
    }

    @Override
    public void setupCameraTransform(float partialTicks, int pass) {
        this.wrapped.func_78479_a(partialTicks, pass);
    }

    @Override
    public void setupOverlayRendering() {
        this.wrapped.func_78478_c();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof EntityRendererImpl && ((EntityRendererImpl)other).wrapped.equals(this.wrapped);
    }

    public final EntityRenderer getWrapped() {
        return this.wrapped;
    }

    public EntityRendererImpl(EntityRenderer wrapped) {
        this.wrapped = wrapped;
    }
}

