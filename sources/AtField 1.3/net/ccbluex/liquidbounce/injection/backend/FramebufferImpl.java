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
    public void bindFramebuffer(boolean bl) {
        this.wrapped.func_147610_a(bl);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof FramebufferImpl && ((FramebufferImpl)object).wrapped.equals(this.wrapped);
    }

    public FramebufferImpl(Framebuffer framebuffer) {
        this.wrapped = framebuffer;
    }

    public final Framebuffer getWrapped() {
        return this.wrapped;
    }
}

