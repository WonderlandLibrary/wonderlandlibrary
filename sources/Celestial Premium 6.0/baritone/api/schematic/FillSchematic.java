/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.schematic.AbstractSchematic;
import baritone.api.utils.BlockOptionalMeta;
import java.util.List;
import net.minecraft.block.state.IBlockState;

public class FillSchematic
extends AbstractSchematic {
    private final BlockOptionalMeta bom;

    public FillSchematic(int x, int y, int z, BlockOptionalMeta bom) {
        super(x, y, z);
        this.bom = bom;
    }

    public FillSchematic(int x, int y, int z, IBlockState state) {
        this(x, y, z, new BlockOptionalMeta(state.getBlock(), state.getBlock().getMetaFromState(state)));
    }

    public BlockOptionalMeta getBom() {
        return this.bom;
    }

    @Override
    public IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable) {
        if (this.bom.matches(current)) {
            return current;
        }
        for (IBlockState placeable : approxPlaceable) {
            if (!this.bom.matches(placeable)) continue;
            return placeable;
        }
        return this.bom.getAnyBlockState();
    }
}

