/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.schematic;

import baritone.api.schematic.AbstractSchematic;
import baritone.api.schematic.ISchematic;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class SubstituteSchematic
extends AbstractSchematic {
    private final ISchematic schematic;
    private final Map<Block, List<Block>> substitutions;
    private final Map<IBlockState, Map<Block, IBlockState>> blockStateCache = new HashMap<IBlockState, Map<Block, IBlockState>>();

    public SubstituteSchematic(ISchematic schematic, Map<Block, List<Block>> substitutions) {
        super(schematic.widthX(), schematic.heightY(), schematic.lengthZ());
        this.schematic = schematic;
        this.substitutions = substitutions;
    }

    @Override
    public boolean inSchematic(int x, int y, int z, IBlockState currentState) {
        return this.schematic.inSchematic(x, y, z, currentState);
    }

    @Override
    public IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable) {
        IBlockState desired = this.schematic.desiredState(x, y, z, current, approxPlaceable);
        Block desiredBlock = desired.getBlock();
        if (!this.substitutions.containsKey(desiredBlock)) {
            return desired;
        }
        List<Block> substitutes = this.substitutions.get(desiredBlock);
        if (substitutes.contains(current.getBlock()) && !(current.getBlock() instanceof BlockAir)) {
            return this.withBlock(desired, current.getBlock());
        }
        for (Block substitute : substitutes) {
            if (substitute instanceof BlockAir) {
                return current.getBlock() instanceof BlockAir ? current : Blocks.AIR.getDefaultState();
            }
            for (IBlockState placeable : approxPlaceable) {
                if (!substitute.equals(placeable.getBlock())) continue;
                return this.withBlock(desired, placeable.getBlock());
            }
        }
        return substitutes.get(0).getDefaultState();
    }

    private IBlockState withBlock(IBlockState state, Block block) {
        if (this.blockStateCache.containsKey(state) && this.blockStateCache.get(state).containsKey(block)) {
            return this.blockStateCache.get(state).get(block);
        }
        Collection<IProperty<?>> properties = state.getPropertyKeys();
        IBlockState newState = block.getDefaultState();
        for (IProperty<?> property : properties) {
            try {
                newState = this.copySingleProp(state, newState, property);
            }
            catch (IllegalArgumentException illegalArgumentException) {}
        }
        this.blockStateCache.computeIfAbsent(state, s -> new HashMap()).put(block, newState);
        return newState;
    }

    private <T extends Comparable<T>> IBlockState copySingleProp(IBlockState fromState, IBlockState toState, IProperty<T> prop) {
        return toState.withProperty(prop, fromState.getValue(prop));
    }
}

