/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.schematic;

import baritone.api.schematic.AbstractSchematic;
import baritone.api.schematic.IStaticSchematic;
import java.util.List;
import net.minecraft.block.state.IBlockState;

public class StaticSchematic
extends AbstractSchematic
implements IStaticSchematic {
    protected IBlockState[][][] states;

    @Override
    public IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable) {
        return this.states[x][z][y];
    }

    @Override
    public IBlockState getDirect(int x, int y, int z) {
        return this.states[x][z][y];
    }

    @Override
    public IBlockState[] getColumn(int x, int z) {
        return this.states[x][z];
    }
}

