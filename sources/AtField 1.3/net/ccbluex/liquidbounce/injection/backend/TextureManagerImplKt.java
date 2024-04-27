/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.TextureManager
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.ITextureManager;
import net.ccbluex.liquidbounce.injection.backend.TextureManagerImpl;
import net.minecraft.client.renderer.texture.TextureManager;

public final class TextureManagerImplKt {
    public static final TextureManager unwrap(ITextureManager iTextureManager) {
        boolean bl = false;
        return ((TextureManagerImpl)iTextureManager).getWrapped();
    }

    public static final ITextureManager wrap(TextureManager textureManager) {
        boolean bl = false;
        return new TextureManagerImpl(textureManager);
    }
}

