/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.shader.Framebuffer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.shader.IFramebuffer;
import net.minecraft.client.shader.Framebuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0013\u0010\u000b\u001a\u00020\n2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0096\u0002R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000e"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/FramebufferImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/shader/IFramebuffer;", "wrapped", "Lnet/minecraft/client/shader/Framebuffer;", "(Lnet/minecraft/client/shader/Framebuffer;)V", "getWrapped", "()Lnet/minecraft/client/shader/Framebuffer;", "bindFramebuffer", "", "b", "", "equals", "other", "", "LiKingSense"})
public final class FramebufferImpl
implements IFramebuffer {
    @NotNull
    private final Framebuffer wrapped;

    @Override
    public void bindFramebuffer(boolean b) {
        this.wrapped.func_147610_a(b);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof FramebufferImpl && Intrinsics.areEqual((Object)((FramebufferImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Framebuffer getWrapped() {
        return this.wrapped;
    }

    public FramebufferImpl(@NotNull Framebuffer wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

