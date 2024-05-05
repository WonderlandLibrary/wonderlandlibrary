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

    @Override
    public void deleteGlBuffers() {
        this.wrapped.func_177362_c();
    }

    @Override
    public void bindBuffer() {
        this.wrapped.func_177359_a();
    }

    @Override
    public void drawArrays(int mode) {
        this.wrapped.func_177358_a(mode);
    }

    @Override
    public void unbindBuffer() {
        this.wrapped.func_177361_b();
    }

    @Override
    public void bufferData(ByteBuffer buffer) {
        this.wrapped.func_181722_a(buffer);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof VertexBufferImpl && ((VertexBufferImpl)other).wrapped.equals(this.wrapped);
    }

    public final VertexBuffer getWrapped() {
        return this.wrapped;
    }

    public VertexBufferImpl(VertexBuffer wrapped) {
        this.wrapped = wrapped;
    }
}

