/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.nio.ByteBuffer;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.vertex.IVertexBuffer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import org.jetbrains.annotations.Nullable;

public final class VertexBufferImpl
implements IVertexBuffer {
    private final VertexBuffer wrapped;

    public final VertexBuffer getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof VertexBufferImpl && ((VertexBufferImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void deleteGlBuffers() {
        this.wrapped.func_177362_c();
    }

    @Override
    public void unbindBuffer() {
        this.wrapped.func_177361_b();
    }

    @Override
    public void bufferData(ByteBuffer byteBuffer) {
        this.wrapped.func_181722_a(byteBuffer);
    }

    @Override
    public void drawArrays(int n) {
        this.wrapped.func_177358_a(n);
    }

    public VertexBufferImpl(VertexBuffer vertexBuffer) {
        this.wrapped = vertexBuffer;
    }

    @Override
    public void bindBuffer() {
        this.wrapped.func_177359_a();
    }
}

