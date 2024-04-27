/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.BufferBuilder
 *  net.minecraft.client.renderer.vertex.VertexFormat
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.nio.ByteBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.render.IWorldRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.render.vertex.IVertexFormat;
import net.ccbluex.liquidbounce.injection.backend.VertexFormatImpl;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\nH\u0016J(\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0015H\u0016J\b\u0010\u0019\u001a\u00020\u0010H\u0016J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0096\u0002J\b\u0010\u001e\u001a\u00020\u0010H\u0016J \u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020!H\u0016J\b\u0010$\u001a\u00020\u0010H\u0016J\u0018\u0010%\u001a\u00020\u00012\u0006\u0010&\u001a\u00020!2\u0006\u0010'\u001a\u00020!H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006("}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/WorldRendererImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/IWorldRenderer;", "wrapped", "Lnet/minecraft/client/renderer/BufferBuilder;", "(Lnet/minecraft/client/renderer/BufferBuilder;)V", "byteBuffer", "Ljava/nio/ByteBuffer;", "getByteBuffer", "()Ljava/nio/ByteBuffer;", "vertexFormat", "Lnet/ccbluex/liquidbounce/api/minecraft/client/render/vertex/IVertexFormat;", "getVertexFormat", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/render/vertex/IVertexFormat;", "getWrapped", "()Lnet/minecraft/client/renderer/BufferBuilder;", "begin", "", "mode", "", "color", "red", "", "green", "blue", "alpha", "endVertex", "equals", "", "other", "", "finishDrawing", "pos", "x", "", "y", "z", "reset", "tex", "u", "v", "LiKingSense"})
public final class WorldRendererImpl
implements IWorldRenderer {
    @NotNull
    private final BufferBuilder wrapped;

    @Override
    @NotNull
    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = this.wrapped.func_178966_f();
        Intrinsics.checkExpressionValueIsNotNull((Object)byteBuffer, (String)"wrapped.byteBuffer");
        return byteBuffer;
    }

    @Override
    @NotNull
    public IVertexFormat getVertexFormat() {
        VertexFormat vertexFormat = this.wrapped.func_178973_g();
        Intrinsics.checkExpressionValueIsNotNull((Object)vertexFormat, (String)"wrapped.vertexFormat");
        VertexFormat $this$wrap$iv = vertexFormat;
        boolean $i$f$wrap = false;
        return new VertexFormatImpl($this$wrap$iv);
    }

    @Override
    public void begin(int mode, @NotNull IVertexFormat vertexFormat) {
        Intrinsics.checkParameterIsNotNull((Object)vertexFormat, (String)"vertexFormat");
        this.wrapped.func_181668_a(mode, ((VertexFormatImpl)vertexFormat).getWrapped());
    }

    @Override
    @NotNull
    public IWorldRenderer pos(double x, double y, double z) {
        this.wrapped.func_181662_b(x, y, z);
        return this;
    }

    @Override
    public void endVertex() {
        this.wrapped.func_181675_d();
    }

    @Override
    @NotNull
    public IWorldRenderer tex(double u, double v) {
        this.wrapped.func_187315_a(u, v);
        return this;
    }

    @Override
    @NotNull
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
        return other instanceof WorldRendererImpl && Intrinsics.areEqual((Object)((WorldRendererImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final BufferBuilder getWrapped() {
        return this.wrapped;
    }

    public WorldRendererImpl(@NotNull BufferBuilder wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

