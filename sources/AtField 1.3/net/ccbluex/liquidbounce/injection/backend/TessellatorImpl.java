/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.Tessellator
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.render.ITessellator;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.injection.backend.WorldRendererImpl;
import net.minecraft.client.renderer.Tessellator;
import org.jetbrains.annotations.Nullable;

public final class TessellatorImpl
implements ITessellator {
    private final Tessellator wrapped;

    @Override
    public void draw() {
        this.wrapped.func_78381_a();
    }

    @Override
    public IWorldRenderer getWorldRenderer() {
        return new WorldRendererImpl(this.wrapped.func_178180_c());
    }

    public TessellatorImpl(Tessellator tessellator) {
        this.wrapped = tessellator;
    }

    public final Tessellator getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof TessellatorImpl && ((TessellatorImpl)object).wrapped.equals(this.wrapped);
    }
}

