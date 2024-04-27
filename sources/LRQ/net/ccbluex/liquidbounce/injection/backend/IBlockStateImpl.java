/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.client.block.IBlock;
import net.ccbluex.liquidbounce.injection.backend.BlockImpl;
import net.minecraft.block.state.IBlockState;
import org.jetbrains.annotations.Nullable;

public final class IBlockStateImpl
implements IIBlockState {
    private final IBlockState wrapped;

    @Override
    public IBlock getBlock() {
        return new BlockImpl(this.wrapped.func_177230_c());
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof IBlockStateImpl && ((IBlockStateImpl)other).wrapped.equals(this.wrapped);
    }

    public final IBlockState getWrapped() {
        return this.wrapped;
    }

    public IBlockStateImpl(IBlockState wrapped) {
        this.wrapped = wrapped;
    }
}

