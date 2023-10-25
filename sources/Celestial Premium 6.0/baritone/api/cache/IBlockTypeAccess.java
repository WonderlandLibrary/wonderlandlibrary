/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IBlockTypeAccess {
    public IBlockState getBlock(int var1, int var2, int var3);

    default public IBlockState getBlock(BlockPos pos) {
        return this.getBlock(pos.getX(), pos.getY(), pos.getZ());
    }
}

