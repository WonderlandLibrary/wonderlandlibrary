/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.injection.backend.BlockImpl;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/IBlockStateImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "wrapped", "Lnet/minecraft/block/state/IBlockState;", "(Lnet/minecraft/block/state/IBlockState;)V", "block", "Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "getBlock", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/block/IBlock;", "getWrapped", "()Lnet/minecraft/block/state/IBlockState;", "equals", "", "other", "", "LiKingSense"})
public final class IBlockStateImpl
implements IIBlockState {
    @NotNull
    private final IBlockState wrapped;

    @Override
    @NotNull
    public IBlock getBlock() {
        Block block = this.wrapped.func_177230_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)block, (String)"wrapped.block");
        return new BlockImpl(block);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof IBlockStateImpl && Intrinsics.areEqual((Object)((IBlockStateImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final IBlockState getWrapped() {
        return this.wrapped;
    }

    public IBlockStateImpl(@NotNull IBlockState wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

