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
    public IWorldRenderer pos(double d, double d2, double d3) {
        this.wrapped.func_181662_b(d, d2, d3);
        return this;
    }

    public WorldRendererImpl(BufferBuilder bufferBuilder) {
        this.wrapped = bufferBuilder;
    }

    @Override
    public IVertexFormat getVertexFormat() {
        VertexFormat vertexFormat = this.wrapped.func_178973_g();
        boolean bl = false;
        return new VertexFormatImpl(vertexFormat);
    }

    @Override
    public IWorldRenderer tex(double d, double d2) {
        this.wrapped.func_187315_a(d, d2);
        return this;
    }

    @Override
    public void endVertex() {
        this.wrapped.func_181675_d();
    }

    @Override
    public void reset() {
        this.wrapped.func_178965_a();
    }

    public final BufferBuilder getWrapped() {
        return this.wrapped;
    }

    @Override
    public IWorldRenderer color(float f, float f2, float f3, float f4) {
        this.wrapped.func_181666_a(f, f2, f3, f4);
        return this;
    }

    @Override
    public void begin(int n, IVertexFormat iVertexFormat) {
        this.wrapped.func_181668_a(n, ((VertexFormatImpl)iVertexFormat).getWrapped());
    }

    @Override
    public void finishDrawing() {
        this.wrapped.func_178977_d();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof WorldRendererImpl && ((WorldRendererImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return this.wrapped.func_178966_f();
    }
}

