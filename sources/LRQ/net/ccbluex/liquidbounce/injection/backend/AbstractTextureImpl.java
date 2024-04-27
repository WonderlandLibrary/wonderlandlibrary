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

public class AbstractTextureImpl<T extends AbstractTexture>
implements IAbstractTexture {
    private final T wrapped;

    public boolean equals(@Nullable Object other) {
        return other instanceof AbstractTextureImpl && ((AbstractTextureImpl)other).wrapped.equals(this.wrapped);
    }

    public final T getWrapped() {
        return this.wrapped;
    }

    public AbstractTextureImpl(T wrapped) {
        this.wrapped = wrapped;
    }
}

