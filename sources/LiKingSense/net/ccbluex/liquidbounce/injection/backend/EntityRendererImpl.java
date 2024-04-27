/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.client.shader.ShaderGroup
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IEntityRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IShaderGroup;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.ccbluex.liquidbounce.injection.backend.ShaderGroupImpl;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000b\u001a\u00020\fH\u0016J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\b\u0010\u0011\u001a\u00020\u000eH\u0016J\u0010\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\fH\u0016J\b\u0010\u001b\u001a\u00020\fH\u0016R\u0016\u0010\u0005\u001a\u0004\u0018\u00010\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u001c"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/EntityRendererImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IEntityRenderer;", "wrapped", "Lnet/minecraft/client/renderer/EntityRenderer;", "(Lnet/minecraft/client/renderer/EntityRenderer;)V", "shaderGroup", "Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IShaderGroup;", "getShaderGroup", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IShaderGroup;", "getWrapped", "()Lnet/minecraft/client/renderer/EntityRenderer;", "disableLightmap", "", "equals", "", "other", "", "isShaderActive", "loadShader", "resourceLocation", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "setupCameraTransform", "partialTicks", "", "pass", "", "setupOverlayRendering", "stopUseShader", "LiKingSense"})
public final class EntityRendererImpl
implements IEntityRenderer {
    @NotNull
    private final EntityRenderer wrapped;

    @Override
    @Nullable
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
    public void loadShader(@NotNull IResourceLocation resourceLocation) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)resourceLocation, (String)"resourceLocation");
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
        return other instanceof EntityRendererImpl && Intrinsics.areEqual((Object)((EntityRendererImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final EntityRenderer getWrapped() {
        return this.wrapped;
    }

    public EntityRendererImpl(@NotNull EntityRenderer wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

