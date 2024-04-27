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

    public final EntityRenderer getWrapped() {
        return this.wrapped;
    }

    @Override
    public void loadShader(IResourceLocation iResourceLocation) {
        IResourceLocation iResourceLocation2 = iResourceLocation;
        EntityRenderer entityRenderer = this.wrapped;
        boolean bl = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)iResourceLocation2).getWrapped();
        entityRenderer.func_175069_a(resourceLocation);
    }

    @Override
    public void disableLightmap() {
        this.wrapped.func_175072_h();
    }

    @Override
    public void setupCameraTransform(float f, int n) {
        this.wrapped.func_78479_a(f, n);
    }

    @Override
    public boolean isShaderActive() {
        return this.wrapped.func_147702_a();
    }

    @Override
    public void setupOverlayRendering() {
        this.wrapped.func_78478_c();
    }

    @Override
    public IShaderGroup getShaderGroup() {
        IShaderGroup iShaderGroup;
        ShaderGroup shaderGroup = this.wrapped.func_147706_e();
        if (shaderGroup != null) {
            ShaderGroup shaderGroup2 = shaderGroup;
            boolean bl = false;
            iShaderGroup = new ShaderGroupImpl(shaderGroup2);
        } else {
            iShaderGroup = null;
        }
        return iShaderGroup;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof EntityRendererImpl && ((EntityRendererImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void stopUseShader() {
        this.wrapped.func_181022_b();
    }

    public EntityRendererImpl(EntityRenderer entityRenderer) {
        this.wrapped = entityRenderer;
    }
}

