/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.shader.Framebuffer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.shader.IFramebuffer;
import net.minecraft.client.shader.Framebuffer;
import org.jetbrains.annotations.Nullable;

public final class FramebufferImpl
implements IFramebuffer {
    private final Framebuffer wrapped;

    @Override
    public void bindFramebuffer(boolean b) {
        this.wrapped.func_147610_a(b);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof FramebufferImpl && ((FramebufferImpl)other).wrapped.equals(this.wrapped);
    }

    public final Framebuffer getWrapped() {
        return this.wrapped;
    }

    public FramebufferImpl(Framebuffer wrapped) {
        this.wrapped = wrapped;
    }
}

