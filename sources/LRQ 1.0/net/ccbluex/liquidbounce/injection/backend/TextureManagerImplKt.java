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
    public static final TextureManager unwrap(ITextureManager $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((TextureManagerImpl)$this$unwrap).getWrapped();
    }

    public static final ITextureManager wrap(TextureManager $this$wrap) {
        int $i$f$wrap = 0;
        return new TextureManagerImpl($this$wrap);
    }
}

