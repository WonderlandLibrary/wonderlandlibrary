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
    public static final DynamicTexture unwrap(IDynamicTexture iDynamicTexture) {
        boolean bl = false;
        return (DynamicTexture)((DynamicTextureImpl)iDynamicTexture).getWrapped();
    }

    public static final IDynamicTexture wrap(DynamicTexture dynamicTexture) {
        boolean bl = false;
        return new DynamicTextureImpl(dynamicTexture);
    }
}

