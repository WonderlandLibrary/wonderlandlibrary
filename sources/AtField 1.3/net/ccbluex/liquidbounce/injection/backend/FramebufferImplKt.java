/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.shader.Framebuffer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.shader.IFramebuffer;
import net.ccbluex.liquidbounce.injection.backend.FramebufferImpl;
import net.minecraft.client.shader.Framebuffer;

public final class FramebufferImplKt {
    public static final Framebuffer unwrap(IFramebuffer iFramebuffer) {
        boolean bl = false;
        return ((FramebufferImpl)iFramebuffer).getWrapped();
    }

    public static final IFramebuffer wrap(Framebuffer framebuffer) {
        boolean bl = false;
        return new FramebufferImpl(framebuffer);
    }
}

