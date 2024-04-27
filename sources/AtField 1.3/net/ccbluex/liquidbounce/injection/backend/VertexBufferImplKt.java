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
    public static final IVertexBuffer wrap(VertexBuffer vertexBuffer) {
        boolean bl = false;
        return new VertexBufferImpl(vertexBuffer);
    }

    public static final VertexBuffer unwrap(IVertexBuffer iVertexBuffer) {
        boolean bl = false;
        return ((VertexBufferImpl)iVertexBuffer).getWrapped();
    }
}

