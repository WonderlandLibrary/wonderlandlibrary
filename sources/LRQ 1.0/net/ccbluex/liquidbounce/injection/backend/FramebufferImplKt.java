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
    public static final Framebuffer unwrap(IFramebuffer $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((FramebufferImpl)$this$unwrap).getWrapped();
    }

    public static final IFramebuffer wrap(Framebuffer $this$wrap) {
        int $i$f$wrap = 0;
        return new FramebufferImpl($this$wrap);
    }
}

