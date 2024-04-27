/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.renderer.vertex.IVertexBuffer;
import net.ccbluex.liquidbounce.injection.backend.VertexBufferImpl;
import net.minecraft.client.renderer.vertex.VertexBuffer;

public final class VertexBufferImplKt {
    public static final VertexBuffer unwrap(IVertexBuffer $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((VertexBufferImpl)$this$unwrap).getWrapped();
    }

    public static final IVertexBuffer wrap(VertexBuffer $this$wrap) {
        int $i$f$wrap = 0;
        return new VertexBufferImpl($this$wrap);
    }
}

