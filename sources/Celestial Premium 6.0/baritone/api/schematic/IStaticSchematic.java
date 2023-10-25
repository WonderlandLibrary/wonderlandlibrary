/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.schematic.ISchematic;
import net.minecraft.block.state.IBlockState;

public interface IStaticSchematic
extends ISchematic {
    public IBlockState getDirect(int var1, int var2, int var3);

    default public IBlockState[] getColumn(int x, int z) {
        IBlockState[] column = new IBlockState[this.heightY()];
        for (int i = 0; i < this.heightY(); ++i) {
            column[i] = this.getDirect(x, i, z);
        }
        return column;
    }
}

