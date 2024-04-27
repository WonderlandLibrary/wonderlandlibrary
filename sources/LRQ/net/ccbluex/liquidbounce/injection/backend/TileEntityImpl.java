/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.math.BlockPos
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public final class TileEntityImpl
implements ITileEntity {
    private final TileEntity wrapped;

    @Override
    public WBlockPos getPos() {
        BlockPos $this$wrap$iv = this.wrapped.func_174877_v();
        boolean $i$f$wrap = false;
        return new WBlockPos($this$wrap$iv.func_177958_n(), $this$wrap$iv.func_177956_o(), $this$wrap$iv.func_177952_p());
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof TileEntityImpl && ((TileEntityImpl)other).wrapped.equals(this.wrapped);
    }

    public final TileEntity getWrapped() {
        return this.wrapped;
    }

    public TileEntityImpl(TileEntity wrapped) {
        this.wrapped = wrapped;
    }
}

