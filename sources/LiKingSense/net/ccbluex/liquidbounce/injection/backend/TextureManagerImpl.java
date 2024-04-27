/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IAbstractTexture;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.injection.backend.AbstractTextureImpl;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\fH\u0016J\u0013\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0096\u0002J\u0018\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/TextureManagerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/ITextureManager;", "wrapped", "Lnet/minecraft/client/renderer/texture/TextureManager;", "(Lnet/minecraft/client/renderer/texture/TextureManager;)V", "getWrapped", "()Lnet/minecraft/client/renderer/texture/TextureManager;", "bindTexture", "", "image", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IResourceLocation;", "bindTexture2", "Lnet/minecraft/util/ResourceLocation;", "equals", "", "other", "", "loadTexture", "textureLocation", "textureObj", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/texture/IAbstractTexture;", "LiKingSense"})
public final class TextureManagerImpl
implements ITextureManager {
    @NotNull
    private final TextureManager wrapped;

    @Override
    public boolean loadTexture(@NotNull IResourceLocation textureLocation, @NotNull IAbstractTexture textureObj) {
        IAbstractTexture $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)textureLocation, (String)"textureLocation");
        Intrinsics.checkParameterIsNotNull((Object)textureObj, (String)"textureObj");
        IResourceLocation iResourceLocation = textureLocation;
        TextureManager textureManager = this.wrapped;
        boolean $i$f$unwrap = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)((Object)$this$unwrap$iv)).getWrapped();
        $this$unwrap$iv = textureObj;
        $i$f$unwrap = false;
        Object t2 = ((AbstractTextureImpl)$this$unwrap$iv).getWrapped();
        return textureManager.func_110579_a(resourceLocation, (ITextureObject)t2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void bindTexture(@NotNull IResourceLocation image2) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)image2, (String)"image");
        IResourceLocation iResourceLocation = image2;
        TextureManager textureManager = this.wrapped;
        boolean $i$f$unwrap = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)$this$unwrap$iv).getWrapped();
        textureManager.func_110577_a(resourceLocation);
    }

    @Override
    public void bindTexture2(@NotNull ResourceLocation image2) {
        Intrinsics.checkParameterIsNotNull((Object)image2, (String)"image");
        ResourceLocation resourceLocation = image2;
        TextureManager textureManager = this.wrapped;
        boolean bl = false;
        boolean bl2 = false;
        ResourceLocation it = resourceLocation;
        boolean bl3 = false;
        TextureManagerImpl $this$unwrap$iv = this;
        boolean $i$f$unwrap = false;
        TextureManagerImpl textureManagerImpl = $this$unwrap$iv;
        if (textureManagerImpl == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.TextureManagerImpl");
        }
        textureManagerImpl.getWrapped();
        ResourceLocation resourceLocation2 = resourceLocation;
        textureManager.func_110577_a(resourceLocation2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof TextureManagerImpl && Intrinsics.areEqual((Object)((TextureManagerImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final TextureManager getWrapped() {
        return this.wrapped;
    }

    public TextureManagerImpl(@NotNull TextureManager wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

