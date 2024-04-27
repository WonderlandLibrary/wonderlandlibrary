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
        BlockPos blockPos = this.wrapped.func_174877_v();
        boolean bl = false;
        return new WBlockPos(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p());
    }

    public final TileEntity getWrapped() {
        return this.wrapped;
    }

    public TileEntityImpl(TileEntity tileEntity) {
        this.wrapped = tileEntity;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof TileEntityImpl && ((TileEntityImpl)object).wrapped.equals(this.wrapped);
    }
}

