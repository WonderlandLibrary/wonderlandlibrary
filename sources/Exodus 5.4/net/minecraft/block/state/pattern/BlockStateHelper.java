/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Maps
 */
package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class BlockStateHelper
implements Predicate<IBlockState> {
    private final BlockState blockstate;
    private final Map<IProperty, Predicate> propertyPredicates = Maps.newHashMap();

    private BlockStateHelper(BlockState blockState) {
        this.blockstate = blockState;
    }

    public static BlockStateHelper forBlock(Block block) {
        return new BlockStateHelper(block.getBlockState());
    }

    public <V extends Comparable<V>> BlockStateHelper where(IProperty<V> iProperty, Predicate<? extends V> predicate) {
        if (!this.blockstate.getProperties().contains(iProperty)) {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + iProperty);
        }
        this.propertyPredicates.put(iProperty, predicate);
        return this;
    }

    public boolean apply(IBlockState iBlockState) {
        if (iBlockState != null && iBlockState.getBlock().equals(this.blockstate.getBlock())) {
            for (Map.Entry<IProperty, Predicate> entry : this.propertyPredicates.entrySet()) {
                Object t = iBlockState.getValue(entry.getKey());
                if (entry.getValue().apply(t)) continue;
                return false;
            }
            return true;
        }
        return false;
    }
}

