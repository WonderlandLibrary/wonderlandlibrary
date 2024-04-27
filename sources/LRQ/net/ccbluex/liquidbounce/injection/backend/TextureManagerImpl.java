/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.util.ResourceLocation
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IAbstractTexture;
import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.injection.backend.AbstractTextureImpl;
import net.ccbluex.liquidbounce.injection.backend.ResourceLocationImpl;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class TextureManagerImpl
implements ITextureManager {
    private final TextureManager wrapped;

    @Override
    public boolean loadTexture(IResourceLocation textureLocation, IAbstractTexture textureObj) {
        IAbstractTexture $this$unwrap$iv;
        IResourceLocation iResourceLocation = textureLocation;
        TextureManager textureManager = this.wrapped;
        boolean $i$f$unwrap = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)((Object)$this$unwrap$iv)).getWrapped();
        $this$unwrap$iv = textureObj;
        $i$f$unwrap = false;
        Object t = ((AbstractTextureImpl)$this$unwrap$iv).getWrapped();
        return textureManager.func_110579_a(resourceLocation, (ITextureObject)t);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void bindTexture(IResourceLocation image2) {
        void $this$unwrap$iv;
        IResourceLocation iResourceLocation = image2;
        TextureManager textureManager = this.wrapped;
        boolean $i$f$unwrap = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)$this$unwrap$iv).getWrapped();
        textureManager.func_110577_a(resourceLocation);
    }

    @Override
    public void bindTexture2(ResourceLocation image2) {
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
        return other instanceof TextureManagerImpl && ((TextureManagerImpl)other).wrapped.equals(this.wrapped);
    }

    public final TextureManager getWrapped() {
        return this.wrapped;
    }

    public TextureManagerImpl(TextureManager wrapped) {
        this.wrapped = wrapped;
    }
}

