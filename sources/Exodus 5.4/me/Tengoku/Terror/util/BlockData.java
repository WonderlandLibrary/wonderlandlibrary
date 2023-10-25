/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BlockData {
    public EnumFacing face;
    public BlockPos position;

    public BlockData(BlockPos blockPos, EnumFacing enumFacing) {
        this.position = blockPos;
        this.face = enumFacing;
    }
}

