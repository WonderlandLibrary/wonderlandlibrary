/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.math.BlockPos
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/TileEntityImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "wrapped", "Lnet/minecraft/tileentity/TileEntity;", "(Lnet/minecraft/tileentity/TileEntity;)V", "pos", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getPos", "()Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "getWrapped", "()Lnet/minecraft/tileentity/TileEntity;", "equals", "", "other", "", "LiKingSense"})
public final class TileEntityImpl
implements ITileEntity {
    @NotNull
    private final TileEntity wrapped;

    @Override
    @NotNull
    public WBlockPos getPos() {
        BlockPos blockPos = this.wrapped.func_174877_v();
        Intrinsics.checkExpressionValueIsNotNull((Object)blockPos, (String)"wrapped.pos");
        BlockPos $this$wrap$iv = blockPos;
        boolean $i$f$wrap = false;
        return new WBlockPos($this$wrap$iv.func_177958_n(), $this$wrap$iv.func_177956_o(), $this$wrap$iv.func_177952_p());
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof TileEntityImpl && Intrinsics.areEqual((Object)((TileEntityImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final TileEntity getWrapped() {
        return this.wrapped;
    }

    public TileEntityImpl(@NotNull TileEntity wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

