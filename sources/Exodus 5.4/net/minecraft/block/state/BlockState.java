/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Joiner
 *  com.google.common.base.Objects
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableTable
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Table
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Cartesian;
import net.minecraft.util.MapPopulator;

public class BlockState {
    private static final Function<IProperty, String> GET_NAME_FUNC;
    private final ImmutableList<IProperty> properties;
    private final ImmutableList<IBlockState> validStates;
    private final Block block;
    private static final Joiner COMMA_JOINER;

    public String toString() {
        return Objects.toStringHelper((Object)this).add("block", Block.blockRegistry.getNameForObject(this.block)).add("properties", (Object)Iterables.transform(this.properties, GET_NAME_FUNC)).toString();
    }

    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }

    public BlockState(Block block, IProperty ... iPropertyArray) {
        this.block = block;
        Arrays.sort(iPropertyArray, new Comparator<IProperty>(){

            @Override
            public int compare(IProperty iProperty, IProperty iProperty2) {
                return iProperty.getName().compareTo(iProperty2.getName());
            }
        });
        this.properties = ImmutableList.copyOf((Object[])iPropertyArray);
        LinkedHashMap linkedHashMap = Maps.newLinkedHashMap();
        ArrayList arrayList = Lists.newArrayList();
        for (List<Comparable> object : Cartesian.cartesianProduct(this.getAllowedValues())) {
            Map<IProperty, Comparable> map = MapPopulator.createMap(this.properties, object);
            StateImplementation stateImplementation = new StateImplementation(block, ImmutableMap.copyOf(map));
            linkedHashMap.put(map, stateImplementation);
            arrayList.add(stateImplementation);
        }
        for (StateImplementation stateImplementation : arrayList) {
            stateImplementation.buildPropertyValueTable(linkedHashMap);
        }
        this.validStates = ImmutableList.copyOf((Collection)arrayList);
    }

    public IBlockState getBaseState() {
        return (IBlockState)this.validStates.get(0);
    }

    static {
        COMMA_JOINER = Joiner.on((String)", ");
        GET_NAME_FUNC = new Function<IProperty, String>(){

            public String apply(IProperty iProperty) {
                return iProperty == null ? "<NULL>" : iProperty.getName();
            }
        };
    }

    public Block getBlock() {
        return this.block;
    }

    private List<Iterable<Comparable>> getAllowedValues() {
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < this.properties.size()) {
            arrayList.add(((IProperty)this.properties.get(n)).getAllowedValues());
            ++n;
        }
        return arrayList;
    }

    public Collection<IProperty> getProperties() {
        return this.properties;
    }

    static class StateImplementation
    extends BlockStateBase {
        private ImmutableTable<IProperty, Comparable, IBlockState> propertyValueTable;
        private final Block block;
        private final ImmutableMap<IProperty, Comparable> properties;

        public void buildPropertyValueTable(Map<Map<IProperty, Comparable>, StateImplementation> map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            }
            HashBasedTable hashBasedTable = HashBasedTable.create();
            for (IProperty iProperty : this.properties.keySet()) {
                for (Comparable comparable : iProperty.getAllowedValues()) {
                    if (comparable == this.properties.get((Object)iProperty)) continue;
                    hashBasedTable.put((Object)iProperty, (Object)comparable, (Object)map.get(this.getPropertiesWithValue(iProperty, comparable)));
                }
            }
            this.propertyValueTable = ImmutableTable.copyOf((Table)hashBasedTable);
        }

        @Override
        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> iProperty, V v) {
            if (!this.properties.containsKey(iProperty)) {
                throw new IllegalArgumentException("Cannot set property " + iProperty + " as it does not exist in " + this.block.getBlockState());
            }
            if (!iProperty.getAllowedValues().contains(v)) {
                throw new IllegalArgumentException("Cannot set property " + iProperty + " to " + v + " on block " + Block.blockRegistry.getNameForObject(this.block) + ", it is not an allowed value");
            }
            return this.properties.get(iProperty) == v ? this : (IBlockState)this.propertyValueTable.get(iProperty, v);
        }

        @Override
        public Block getBlock() {
            return this.block;
        }

        public boolean equals(Object object) {
            return this == object;
        }

        private StateImplementation(Block block, ImmutableMap<IProperty, Comparable> immutableMap) {
            this.block = block;
            this.properties = immutableMap;
        }

        @Override
        public ImmutableMap<IProperty, Comparable> getProperties() {
            return this.properties;
        }

        @Override
        public Collection<IProperty> getPropertyNames() {
            return Collections.unmodifiableCollection(this.properties.keySet());
        }

        @Override
        public <T extends Comparable<T>> T getValue(IProperty<T> iProperty) {
            if (!this.properties.containsKey(iProperty)) {
                throw new IllegalArgumentException("Cannot get property " + iProperty + " as it does not exist in " + this.block.getBlockState());
            }
            return (T)((Comparable)iProperty.getValueClass().cast(this.properties.get(iProperty)));
        }

        public int hashCode() {
            return this.properties.hashCode();
        }

        private Map<IProperty, Comparable> getPropertiesWithValue(IProperty iProperty, Comparable comparable) {
            HashMap hashMap = Maps.newHashMap(this.properties);
            hashMap.put(iProperty, comparable);
            return hashMap;
        }
    }
}

