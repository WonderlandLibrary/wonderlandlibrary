/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockStateMatcher
implements Predicate<IBlockState> {
    public static final Predicate<IBlockState> ANY = new Predicate<IBlockState>(){

        @Override
        public boolean apply(@Nullable IBlockState p_apply_1_) {
            return true;
        }
    };
    private final BlockStateContainer blockstate;
    private final Map<IProperty<?>, Predicate<?>> propertyPredicates = Maps.newHashMap();

    private BlockStateMatcher(BlockStateContainer blockStateIn) {
        this.blockstate = blockStateIn;
    }

    public static BlockStateMatcher forBlock(Block blockIn) {
        return new BlockStateMatcher(blockIn.getBlockState());
    }

    @Override
    public boolean apply(@Nullable IBlockState p_apply_1_) {
        if (p_apply_1_ != null && p_apply_1_.getBlock().equals(this.blockstate.getBlock())) {
            if (this.propertyPredicates.isEmpty()) {
                return true;
            }
            for (Map.Entry<IProperty<?>, Predicate<?>> entry : this.propertyPredicates.entrySet()) {
                if (this.matches(p_apply_1_, entry.getKey(), entry.getValue())) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    protected <T extends Comparable<T>> boolean matches(IBlockState blockState, IProperty<T> property, Predicate<T> predicate) {
        return predicate.apply(blockState.getValue(property));
    }

    public <V extends Comparable<V>> BlockStateMatcher where(IProperty<V> property, Predicate<? extends V> is) {
        if (!this.blockstate.getProperties().contains(property)) {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
        }
        this.propertyPredicates.put(property, is);
        return this;
    }
}

