/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.AbstractTexture
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IAbstractTexture;
import net.ccbluex.liquidbounce.injection.backend.AbstractTextureImpl;
import net.minecraft.client.renderer.texture.AbstractTexture;

public final class AbstractTextureImplKt {
    public static final AbstractTexture unwrap(IAbstractTexture $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((AbstractTextureImpl)$this$unwrap).getWrapped();
    }

    public static final IAbstractTexture wrap(AbstractTexture $this$wrap) {
        int $i$f$wrap = 0;
        return new AbstractTextureImpl<AbstractTexture>($this$wrap);
    }
}

