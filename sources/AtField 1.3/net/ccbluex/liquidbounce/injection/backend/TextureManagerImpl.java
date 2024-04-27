/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.client.renderer.texture.AbstractTexture
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
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class TextureManagerImpl
implements ITextureManager {
    private final TextureManager wrapped;

    public TextureManagerImpl(TextureManager textureManager) {
        this.wrapped = textureManager;
    }

    @Override
    public void bindTexture(IResourceLocation iResourceLocation) {
        IResourceLocation iResourceLocation2 = iResourceLocation;
        TextureManager textureManager = this.wrapped;
        boolean bl = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)iResourceLocation2).getWrapped();
        textureManager.func_110577_a(resourceLocation);
    }

    public final TextureManager getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof TextureManagerImpl && ((TextureManagerImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void bindTexture2(ResourceLocation resourceLocation) {
        ResourceLocation resourceLocation2 = resourceLocation;
        TextureManager textureManager = this.wrapped;
        boolean bl = false;
        boolean bl2 = false;
        ResourceLocation resourceLocation3 = resourceLocation2;
        boolean bl3 = false;
        TextureManagerImpl textureManagerImpl = this;
        boolean bl4 = false;
        TextureManagerImpl textureManagerImpl2 = textureManagerImpl;
        if (textureManagerImpl2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.TextureManagerImpl");
        }
        textureManagerImpl2.getWrapped();
        ResourceLocation resourceLocation4 = resourceLocation2;
        textureManager.func_110577_a(resourceLocation4);
    }

    @Override
    public boolean loadTexture(IResourceLocation iResourceLocation, IAbstractTexture iAbstractTexture) {
        Object object = iResourceLocation;
        TextureManager textureManager = this.wrapped;
        boolean bl = false;
        ResourceLocation resourceLocation = ((ResourceLocationImpl)object).getWrapped();
        object = iAbstractTexture;
        bl = false;
        AbstractTexture abstractTexture = ((AbstractTextureImpl)object).getWrapped();
        return textureManager.func_110579_a(resourceLocation, (ITextureObject)abstractTexture);
    }
}

