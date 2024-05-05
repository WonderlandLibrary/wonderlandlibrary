package net.minecraft.block.state;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;

static class BlockState.StateImplementation
extends BlockStateBase {
    private final Block block;
    private final ImmutableMap<IProperty, Comparable> properties;
    private ImmutableTable<IProperty, Comparable, IBlockState> propertyValueTable;

    private BlockState.StateImplementation(Block blockIn, ImmutableMap<IProperty, Comparable> propertiesIn) {
        this.block = blockIn;
        this.properties = propertiesIn;
    }

    public Collection<IProperty> getPropertyNames() {
        return Collections.unmodifiableCollection((Collection)this.properties.keySet());
    }

    public <T extends Comparable<T>> T getValue(IProperty<T> property) {
        if (!this.properties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
        }
        return (T)((Comparable)property.getValueClass().cast(this.properties.get(property)));
    }

    public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
        if (!this.properties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
        }
        if (!property.getAllowedValues().contains(value)) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject((Object)this.block) + ", it is not an allowed value");
        }
        return this.properties.get(property) == value ? this : (IBlockState)this.propertyValueTable.get(property, value);
    }

    public ImmutableMap<IProperty, Comparable> getProperties() {
        return this.properties;
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean equals(Object p_equals_1_) {
        return this == p_equals_1_;
    }

    public int hashCode() {
        return this.properties.hashCode();
    }

    public void buildPropertyValueTable(Map<Map<IProperty, Comparable>, BlockState.StateImplementation> map) {
        if (this.propertyValueTable != null) {
            throw new IllegalStateException();
        }
        HashBasedTable table = HashBasedTable.create();
        for (IProperty iproperty : this.properties.keySet()) {
            for (Comparable comparable : iproperty.getAllowedValues()) {
                if (comparable == this.properties.get((Object)iproperty)) continue;
                table.put((Object)iproperty, (Object)comparable, map.get(this.getPropertiesWithValue(iproperty, comparable)));
            }
        }
        this.propertyValueTable = ImmutableTable.copyOf((Table)table);
    }

    private Map<IProperty, Comparable> getPropertiesWithValue(IProperty property, Comparable value) {
        HashMap map = Maps.newHashMap(this.properties);
        map.put((Object)property, (Object)value);
        return map;
    }
}
