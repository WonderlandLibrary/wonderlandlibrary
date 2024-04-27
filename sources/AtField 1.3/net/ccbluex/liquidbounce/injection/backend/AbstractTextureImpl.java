/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.texture.IAbstractTexture;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.jetbrains.annotations.Nullable;

public class AbstractTextureImpl
implements IAbstractTexture {
    private final AbstractTexture wrapped;

    public AbstractTextureImpl(AbstractTexture abstractTexture) {
        this.wrapped = abstractTexture;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof AbstractTextureImpl && ((AbstractTextureImpl)object).wrapped.equals(this.wrapped);
    }

    public final AbstractTexture getWrapped() {
        return this.wrapped;
    }
}

