/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.schematic.AbstractSchematic;
import baritone.api.schematic.ISchematic;
import java.util.List;
import net.minecraft.block.state.IBlockState;

public abstract class MaskSchematic
extends AbstractSchematic {
    private final ISchematic schematic;

    public MaskSchematic(ISchematic schematic) {
        super(schematic.widthX(), schematic.heightY(), schematic.lengthZ());
        this.schematic = schematic;
    }

    protected abstract boolean partOfMask(int var1, int var2, int var3, IBlockState var4);

    @Override
    public boolean inSchematic(int x, int y, int z, IBlockState currentState) {
        return this.schematic.inSchematic(x, y, z, currentState) && this.partOfMask(x, y, z, currentState);
    }

    @Override
    public IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable) {
        return this.schematic.desiredState(x, y, z, current, approxPlaceable);
    }
}

