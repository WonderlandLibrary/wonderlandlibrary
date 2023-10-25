/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public interface ISchematic {
    default public boolean inSchematic(int x, int y, int z, IBlockState currentState) {
        return x >= 0 && x < this.widthX() && y >= 0 && y < this.heightY() && z >= 0 && z < this.lengthZ();
    }

    default public int size(EnumFacing.Axis axis) {
        switch (axis) {
            case X: {
                return this.widthX();
            }
            case Y: {
                return this.heightY();
            }
            case Z: {
                return this.lengthZ();
            }
        }
        throw new UnsupportedOperationException(axis + "");
    }

    public IBlockState desiredState(int var1, int var2, int var3, IBlockState var4, List<IBlockState> var5);

    default public void reset() {
    }

    public int widthX();

    public int heightY();

    public int lengthZ();
}

