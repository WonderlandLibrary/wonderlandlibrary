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
    public static final AbstractTexture unwrap(IAbstractTexture iAbstractTexture) {
        boolean bl = false;
        return ((AbstractTextureImpl)iAbstractTexture).getWrapped();
    }

    public static final IAbstractTexture wrap(AbstractTexture abstractTexture) {
        boolean bl = false;
        return new AbstractTextureImpl(abstractTexture);
    }
}

