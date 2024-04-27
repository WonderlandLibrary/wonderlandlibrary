/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.DynamicTexture
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IDynamicTexture;
import net.ccbluex.liquidbounce.injection.backend.DynamicTextureImpl;
import net.minecraft.client.renderer.texture.DynamicTexture;

public final class DynamicTextureImplKt {
    public static final DynamicTexture unwrap(IDynamicTexture $this$unwrap) {
        int $i$f$unwrap = 0;
        return (DynamicTexture)((DynamicTextureImpl)$this$unwrap).getWrapped();
    }

    public static final IDynamicTexture wrap(DynamicTexture $this$wrap) {
        int $i$f$wrap = 0;
        return new DynamicTextureImpl<DynamicTexture>($this$wrap);
    }
}

