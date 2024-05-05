/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderGlobal
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IRenderGlobal;
import net.minecraft.client.renderer.RenderGlobal;
import org.jetbrains.annotations.Nullable;

public final class RenderGlobalImpl
implements IRenderGlobal {
    private final RenderGlobal wrapped;

    @Override
    public void loadRenderers() {
        this.wrapped.func_72712_a();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof RenderGlobalImpl && ((RenderGlobalImpl)other).wrapped.equals(this.wrapped);
    }

    public final RenderGlobal getWrapped() {
        return this.wrapped;
    }

    public RenderGlobalImpl(RenderGlobal wrapped) {
        this.wrapped = wrapped;
    }
}

