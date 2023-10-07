// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state;

import java.util.HashMap;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.Collections;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.base.Objects;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.MapPopulator;
import java.util.List;
import net.minecraft.util.Cartesian;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import com.google.common.base.Function;
import com.google.common.base.Joiner;

public class BlockState
{
    private static final Joiner COMMA_JOINER;
    private static final Function GET_NAME_FUNC;
    private final Block block;
    private final ImmutableList properties;
    private final ImmutableList validStates;
    private static final String __OBFID = "CL_00002030";
    
    public BlockState(final Block blockIn, final IProperty... properties) {
        this.block = blockIn;
        Arrays.sort(properties, new Comparator() {
            private static final String __OBFID = "CL_00002028";
            
            public int compare(final IProperty left, final IProperty right) {
                return left.getName().compareTo(right.getName());
            }
            
            @Override
            public int compare(final Object p_compare_1_, final Object p_compare_2_) {
                return this.compare((IProperty)p_compare_1_, (IProperty)p_compare_2_);
            }
        });
        this.properties = ImmutableList.copyOf((Object[])properties);
        final LinkedHashMap var3 = Maps.newLinkedHashMap();
        final ArrayList var4 = Lists.newArrayList();
        final Iterable var5 = Cartesian.cartesianProduct(this.getAllowedValues());
        for (final List var7 : var5) {
            final Map var8 = MapPopulator.createMap((Iterable)this.properties, var7);
            final StateImplemenation var9 = new StateImplemenation(blockIn, ImmutableMap.copyOf(var8), null);
            var3.put(var8, var9);
            var4.add(var9);
        }
        for (final StateImplemenation var10 : var4) {
            var10.buildPropertyValueTable(var3);
        }
        this.validStates = ImmutableList.copyOf((Collection)var4);
    }
    
    public ImmutableList getValidStates() {
        return this.validStates;
    }
    
    private List getAllowedValues() {
        final ArrayList var1 = Lists.newArrayList();
        for (int var2 = 0; var2 < this.properties.size(); ++var2) {
            var1.add(((IProperty)this.properties.get(var2)).getAllowedValues());
        }
        return var1;
    }
    
    public IBlockState getBaseState() {
        return (IBlockState)this.validStates.get(0);
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public Collection getProperties() {
        return (Collection)this.properties;
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper((Object)this).add("block", Block.blockRegistry.getNameForObject(this.block)).add("properties", (Object)Iterables.transform((Iterable)this.properties, BlockState.GET_NAME_FUNC)).toString();
    }
    
    static {
        COMMA_JOINER = Joiner.on(", ");
        GET_NAME_FUNC = (Function)new Function() {
            private static final String __OBFID = "CL_00002029";
            
            public String apply(final IProperty property) {
                return (property == null) ? "<NULL>" : property.getName();
            }
            
            public Object apply(final Object p_apply_1_) {
                return this.apply((IProperty)p_apply_1_);
            }
        };
    }
    
    static class StateImplemenation extends BlockStateBase
    {
        private final Block block;
        private final ImmutableMap properties;
        private ImmutableTable propertyValueTable;
        private static final String __OBFID = "CL_00002027";
        
        private StateImplemenation(final Block p_i45660_1_, final ImmutableMap p_i45660_2_) {
            this.block = p_i45660_1_;
            this.properties = p_i45660_2_;
        }
        
        @Override
        public Collection getPropertyNames() {
            return Collections.unmodifiableCollection((Collection<?>)this.properties.keySet());
        }
        
        @Override
        public Comparable getValue(final IProperty property) {
            if (!this.properties.containsKey((Object)property)) {
                throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            return property.getValueClass().cast(this.properties.get((Object)property));
        }
        
        @Override
        public IBlockState withProperty(final IProperty property, final Comparable value) {
            if (!this.properties.containsKey((Object)property)) {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            if (!property.getAllowedValues().contains(value)) {
                throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject(this.block) + ", it is not an allowed value");
            }
            return (this.properties.get((Object)property) == value) ? this : ((IBlockState)this.propertyValueTable.get((Object)property, (Object)value));
        }
        
        @Override
        public ImmutableMap getProperties() {
            return this.properties;
        }
        
        @Override
        public Block getBlock() {
            return this.block;
        }
        
        @Override
        public boolean equals(final Object p_equals_1_) {
            return this == p_equals_1_;
        }
        
        @Override
        public int hashCode() {
            return this.properties.hashCode();
        }
        
        public void buildPropertyValueTable(final Map map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            }
            final HashBasedTable var2 = HashBasedTable.create();
            for (final IProperty var4 : this.properties.keySet()) {
                for (final Comparable var6 : var4.getAllowedValues()) {
                    if (var6 != this.properties.get((Object)var4)) {
                        var2.put((Object)var4, (Object)var6, map.get(this.setPropertyValue(var4, var6)));
                    }
                }
            }
            this.propertyValueTable = ImmutableTable.copyOf((Table)var2);
        }
        
        private Map setPropertyValue(final IProperty property, final Comparable value) {
            final HashMap var3 = Maps.newHashMap((Map)this.properties);
            var3.put(property, value);
            return var3;
        }
        
        StateImplemenation(final Block p_i45661_1_, final ImmutableMap p_i45661_2_, final Object p_i45661_3_) {
            this(p_i45661_1_, p_i45661_2_);
        }
    }
}
