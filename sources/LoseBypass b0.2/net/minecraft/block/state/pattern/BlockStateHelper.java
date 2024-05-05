/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class BlockStateHelper
implements Predicate<IBlockState> {
    private final BlockState blockstate;
    private final Map<IProperty, Predicate> propertyPredicates = Maps.newHashMap();

    private BlockStateHelper(BlockState blockStateIn) {
        this.blockstate = blockStateIn;
    }

    public static BlockStateHelper forBlock(Block blockIn) {
        return new BlockStateHelper(blockIn.getBlockState());
    }

    @Override
    public boolean apply(IBlockState p_apply_1_) {
        Object object;
        Map.Entry<IProperty, Predicate> entry;
        if (p_apply_1_ == null) return false;
        if (!p_apply_1_.getBlock().equals(this.blockstate.getBlock())) return false;
        Iterator<Map.Entry<IProperty, Predicate>> iterator = this.propertyPredicates.entrySet().iterator();
        do {
            if (!iterator.hasNext()) return true;
            entry = iterator.next();
            object = p_apply_1_.getValue(entry.getKey());
        } while (entry.getValue().apply(object));
        return false;
    }

    public <V extends Comparable<V>> BlockStateHelper where(IProperty<V> property, Predicate<? extends V> is) {
        if (!this.blockstate.getProperties().contains(property)) {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
        }
        this.propertyPredicates.put(property, is);
        return this;
    }
}

