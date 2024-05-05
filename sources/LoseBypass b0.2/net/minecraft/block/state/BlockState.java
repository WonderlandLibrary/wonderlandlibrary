/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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
    private static final Joiner COMMA_JOINER = Joiner.on(", ");
    private static final Function<IProperty, String> GET_NAME_FUNC = new Function<IProperty, String>(){

        @Override
        public String apply(IProperty p_apply_1_) {
            if (p_apply_1_ == null) {
                return "<NULL>";
            }
            String string = p_apply_1_.getName();
            return string;
        }
    };
    private final Block block;
    private final ImmutableList<IProperty> properties;
    private final ImmutableList<IBlockState> validStates;

    public BlockState(Block blockIn, IProperty ... properties) {
        this.block = blockIn;
        Arrays.sort(properties, new Comparator<IProperty>(){

            @Override
            public int compare(IProperty p_compare_1_, IProperty p_compare_2_) {
                return p_compare_1_.getName().compareTo(p_compare_2_.getName());
            }
        });
        this.properties = ImmutableList.copyOf(properties);
        LinkedHashMap<Map<IProperty, Comparable>, StateImplementation> map = Maps.newLinkedHashMap();
        ArrayList<StateImplementation> list = Lists.newArrayList();
        for (List<Comparable> list1 : Cartesian.cartesianProduct(this.getAllowedValues())) {
            Map<IProperty, Comparable> map1 = MapPopulator.createMap(this.properties, list1);
            StateImplementation blockstate$stateimplementation = new StateImplementation(blockIn, ImmutableMap.copyOf(map1));
            map.put(map1, blockstate$stateimplementation);
            list.add(blockstate$stateimplementation);
        }
        Iterator<List<Comparable<Object>>> iterator = list.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.validStates = ImmutableList.copyOf(list);
                return;
            }
            StateImplementation blockstate$stateimplementation1 = (StateImplementation)((Object)iterator.next());
            blockstate$stateimplementation1.buildPropertyValueTable(map);
        }
    }

    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }

    private List<Iterable<Comparable>> getAllowedValues() {
        ArrayList<Iterable<Comparable>> list = Lists.newArrayList();
        int i = 0;
        while (i < this.properties.size()) {
            list.add(((IProperty)this.properties.get(i)).getAllowedValues());
            ++i;
        }
        return list;
    }

    public IBlockState getBaseState() {
        return (IBlockState)this.validStates.get(0);
    }

    public Block getBlock() {
        return this.block;
    }

    public Collection<IProperty> getProperties() {
        return this.properties;
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("block", Block.blockRegistry.getNameForObject(this.block)).add("properties", Iterables.transform(this.properties, GET_NAME_FUNC)).toString();
    }

    static class StateImplementation
    extends BlockStateBase {
        private final Block block;
        private final ImmutableMap<IProperty, Comparable> properties;
        private ImmutableTable<IProperty, Comparable, IBlockState> propertyValueTable;

        private StateImplementation(Block blockIn, ImmutableMap<IProperty, Comparable> propertiesIn) {
            this.block = blockIn;
            this.properties = propertiesIn;
        }

        @Override
        public Collection<IProperty> getPropertyNames() {
            return Collections.unmodifiableCollection(this.properties.keySet());
        }

        @Override
        public <T extends Comparable<T>> T getValue(IProperty<T> property) {
            if (this.properties.containsKey(property)) return (T)((Comparable)property.getValueClass().cast(this.properties.get(property)));
            throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
        }

        @Override
        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
            IBlockState iBlockState;
            if (!this.properties.containsKey(property)) {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            if (!property.getAllowedValues().contains(value)) {
                throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.blockRegistry.getNameForObject(this.block) + ", it is not an allowed value");
            }
            if (this.properties.get(property) == value) {
                iBlockState = this;
                return iBlockState;
            }
            iBlockState = (IBlockState)this.propertyValueTable.get(property, value);
            return iBlockState;
        }

        @Override
        public ImmutableMap<IProperty, Comparable> getProperties() {
            return this.properties;
        }

        @Override
        public Block getBlock() {
            return this.block;
        }

        public boolean equals(Object p_equals_1_) {
            if (this != p_equals_1_) return false;
            return true;
        }

        public int hashCode() {
            return this.properties.hashCode();
        }

        public void buildPropertyValueTable(Map<Map<IProperty, Comparable>, StateImplementation> map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            }
            HashBasedTable<IProperty, Comparable, StateImplementation> table = HashBasedTable.create();
            Iterator iterator = ((ImmutableSet)this.properties.keySet()).iterator();
            block0: while (true) {
                if (!iterator.hasNext()) {
                    this.propertyValueTable = ImmutableTable.copyOf(table);
                    return;
                }
                IProperty iproperty = (IProperty)iterator.next();
                Iterator iterator2 = iproperty.getAllowedValues().iterator();
                while (true) {
                    if (!iterator2.hasNext()) continue block0;
                    Comparable comparable = (Comparable)iterator2.next();
                    if (comparable == this.properties.get(iproperty)) continue;
                    table.put(iproperty, comparable, map.get(this.getPropertiesWithValue(iproperty, comparable)));
                }
                break;
            }
        }

        private Map<IProperty, Comparable> getPropertiesWithValue(IProperty property, Comparable value) {
            HashMap<IProperty, Comparable> map = Maps.newHashMap(this.properties);
            map.put(property, value);
            return map;
        }
    }
}

