/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.schematic.ISchematic;
import baritone.api.schematic.MaskSchematic;
import net.minecraft.block.state.IBlockState;

public class WallsSchematic
extends MaskSchematic {
    public WallsSchematic(ISchematic schematic) {
        super(schematic);
    }

    @Override
    protected boolean partOfMask(int x, int y, int z, IBlockState currentState) {
        return x == 0 || z == 0 || x == this.widthX() - 1 || z == this.lengthZ() - 1;
    }
}

