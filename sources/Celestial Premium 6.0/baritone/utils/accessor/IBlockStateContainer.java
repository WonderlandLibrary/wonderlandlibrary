/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.accessor;

import net.minecraft.block.state.IBlockState;

public interface IBlockStateContainer {
    public IBlockState getAtPalette(int var1);

    public int[] storageArray();
}

