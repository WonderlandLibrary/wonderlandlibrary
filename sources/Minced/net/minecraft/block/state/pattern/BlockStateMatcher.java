// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.state.pattern;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import com.google.common.collect.Maps;
import net.minecraft.block.properties.IProperty;
import java.util.Map;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Predicate;

public class BlockStateMatcher implements Predicate<IBlockState>
{
    public static final Predicate<IBlockState> ANY;
    private final BlockStateContainer blockstate;
    private final Map<IProperty<?>, Predicate<?>> propertyPredicates;
    
    private BlockStateMatcher(final BlockStateContainer blockStateIn) {
        this.propertyPredicates = (Map<IProperty<?>, Predicate<?>>)Maps.newHashMap();
        this.blockstate = blockStateIn;
    }
    
    public static BlockStateMatcher forBlock(final Block blockIn) {
        return new BlockStateMatcher(blockIn.getBlockState());
    }
    
    public boolean apply(@Nullable final IBlockState p_apply_1_) {
        if (p_apply_1_ == null || !p_apply_1_.getBlock().equals(this.blockstate.getBlock())) {
            return false;
        }
        if (this.propertyPredicates.isEmpty()) {
            return true;
        }
        for (final Map.Entry<IProperty<?>, Predicate<?>> entry : this.propertyPredicates.entrySet()) {
            if (!this.matches(p_apply_1_, entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    protected <T extends Comparable<T>> boolean matches(final IBlockState blockState, final IProperty<T> property, final Predicate<T> predicate) {
        return predicate.apply((Object)blockState.getValue(property));
    }
    
    public <V extends Comparable<V>> BlockStateMatcher where(final IProperty<V> property, final Predicate<? extends V> is) {
        if (!this.blockstate.getProperties().contains(property)) {
            throw new IllegalArgumentException(this.blockstate + " cannot support property " + property);
        }
        this.propertyPredicates.put(property, is);
        return this;
    }
    
    static {
        ANY = (Predicate)new Predicate<IBlockState>() {
            public boolean apply(@Nullable final IBlockState p_apply_1_) {
                return true;
            }
        };
    }
}
