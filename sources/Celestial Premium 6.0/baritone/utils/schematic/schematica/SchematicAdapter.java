/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.schematic.schematica;

import baritone.api.schematic.IStaticSchematic;
import com.github.lunatrius.schematica.client.world.SchematicWorld;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public final class SchematicAdapter
implements IStaticSchematic {
    private final SchematicWorld schematic;

    public SchematicAdapter(SchematicWorld schematicWorld) {
        this.schematic = schematicWorld;
    }

    @Override
    public IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable) {
        return this.getDirect(x, y, z);
    }

    @Override
    public IBlockState getDirect(int x, int y, int z) {
        return this.schematic.getSchematic().getBlockState(new BlockPos(x, y, z));
    }

    @Override
    public int widthX() {
        return this.schematic.getSchematic().getWidth();
    }

    @Override
    public int heightY() {
        return this.schematic.getSchematic().getHeight();
    }

    @Override
    public int lengthZ() {
        return this.schematic.getSchematic().getLength();
    }
}

