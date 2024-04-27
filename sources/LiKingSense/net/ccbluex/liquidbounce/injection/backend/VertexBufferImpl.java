/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.vertex.VertexBuffer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.nio.ByteBuffer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.vertex.IVertexBuffer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\bH\u0016J\u0010\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0096\u0002J\b\u0010\u0014\u001a\u00020\bH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/VertexBufferImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/vertex/IVertexBuffer;", "wrapped", "Lnet/minecraft/client/renderer/vertex/VertexBuffer;", "(Lnet/minecraft/client/renderer/vertex/VertexBuffer;)V", "getWrapped", "()Lnet/minecraft/client/renderer/vertex/VertexBuffer;", "bindBuffer", "", "bufferData", "buffer", "Ljava/nio/ByteBuffer;", "deleteGlBuffers", "drawArrays", "mode", "", "equals", "", "other", "", "unbindBuffer", "LiKingSense"})
public final class VertexBufferImpl
implements IVertexBuffer {
    @NotNull
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
    public void bufferData(@NotNull ByteBuffer buffer) {
        Intrinsics.checkParameterIsNotNull((Object)buffer, (String)"buffer");
        this.wrapped.func_181722_a(buffer);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof VertexBufferImpl && Intrinsics.areEqual((Object)((VertexBufferImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final VertexBuffer getWrapped() {
        return this.wrapped;
    }

    public VertexBufferImpl(@NotNull VertexBuffer wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

