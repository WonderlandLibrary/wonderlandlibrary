/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.schematic.ISchematic;
import baritone.api.schematic.MaskSchematic;
import baritone.api.utils.BlockOptionalMetaLookup;
import net.minecraft.block.state.IBlockState;

public class ReplaceSchematic
extends MaskSchematic {
    private final BlockOptionalMetaLookup filter;
    private final Boolean[][][] cache;

    public ReplaceSchematic(ISchematic schematic, BlockOptionalMetaLookup filter) {
        super(schematic);
        this.filter = filter;
        this.cache = new Boolean[this.widthX()][this.heightY()][this.lengthZ()];
    }

    @Override
    public void reset() {
        for (int x = 0; x < this.cache.length; ++x) {
            for (int y = 0; y < this.cache[0].length; ++y) {
                for (int z = 0; z < this.cache[0][0].length; ++z) {
                    this.cache[x][y][z] = null;
                }
            }
        }
    }

    @Override
    protected boolean partOfMask(int x, int y, int z, IBlockState currentState) {
        if (this.cache[x][y][z] == null) {
            this.cache[x][y][z] = this.filter.has(currentState);
        }
        return this.cache[x][y][z];
    }
}

