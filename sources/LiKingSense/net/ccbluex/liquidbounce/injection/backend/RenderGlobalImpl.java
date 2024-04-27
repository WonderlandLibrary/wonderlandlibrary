/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.renderer.RenderGlobal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.renderer.IRenderGlobal;
import net.minecraft.client.renderer.RenderGlobal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0096\u0002J\b\u0010\u000b\u001a\u00020\fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/RenderGlobalImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/renderer/IRenderGlobal;", "wrapped", "Lnet/minecraft/client/renderer/RenderGlobal;", "(Lnet/minecraft/client/renderer/RenderGlobal;)V", "getWrapped", "()Lnet/minecraft/client/renderer/RenderGlobal;", "equals", "", "other", "", "loadRenderers", "", "LiKingSense"})
public final class RenderGlobalImpl
implements IRenderGlobal {
    @NotNull
    private final RenderGlobal wrapped;

    @Override
    public void loadRenderers() {
        this.wrapped.func_72712_a();
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof RenderGlobalImpl && Intrinsics.areEqual((Object)((RenderGlobalImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final RenderGlobal getWrapped() {
        return this.wrapped;
    }

    public RenderGlobalImpl(@NotNull RenderGlobal wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

