/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.nio.ByteBuffer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.ccbluex.liquidbounce.injection.backend.VertexFormatImpl;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.jetbrains.annotations.Nullable;

public final class WorldRendererImpl
implements IWorldRenderer {
    private final BufferBuilder wrapped;

    @Override
    public ByteBuffer getByteBuffer() {
        return this.wrapped.func_178966_f();
    }

    @Override
    public IVertexFormat getVertexFormat() {
        VertexFormat $this$wrap$iv = this.wrapped.func_178973_g();
        boolean $i$f$wrap = false;
        return new VertexFormatImpl($this$wrap$iv);
    }

    @Override
    public void begin(int mode, IVertexFormat vertexFormat) {
        this.wrapped.func_181668_a(mode, ((VertexFormatImpl)vertexFormat).getWrapped());
    }

    @Override
    public IWorldRenderer pos(double x, double y, double z) {
        this.wrapped.func_181662_b(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        this.wrapped.func_181675_d();
    }

    @Override
    public IWorldRenderer tex(double u, double v) {
        this.wrapped.func_187315_a(u, v);
        return this;
    }

    @Override
    public IWorldRenderer color(float red, float green, float blue, float alpha) {
        this.wrapped.func_181666_a(red, green, blue, alpha);
        return this;
    }

    @Override
    public void finishDrawing() {
        this.wrapped.func_178977_d();
    }

    @Override
    public void reset() {
        this.wrapped.func_178965_a();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof WorldRendererImpl && ((WorldRendererImpl)other).wrapped.equals(this.wrapped);
    }

    public final BufferBuilder getWrapped() {
        return this.wrapped;
    }

    public WorldRendererImpl(BufferBuilder wrapped) {
        this.wrapped = wrapped;
    }
}

