package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Cartesian;
import net.minecraft.util.MapPopulator;

public class BlockState {
    private static final Joiner COMMA_JOINER = Joiner.on((String)", ");
    private static final Function<IProperty, String> GET_NAME_FUNC = new /* Unavailable Anonymous Inner Class!! */;
    private final Block block;
    private final ImmutableList<IProperty> properties;
    private final ImmutableList<IBlockState> validStates;

    public BlockState(Block blockIn, IProperty ... properties) {
        this.block = blockIn;
        Arrays.sort((Object[])properties, (Comparator)new /* Unavailable Anonymous Inner Class!! */);
        this.properties = ImmutableList.copyOf((Object[])properties);
        LinkedHashMap map = Maps.newLinkedHashMap();
        ArrayList list = Lists.newArrayList();
        for (List list1 : Cartesian.cartesianProduct(this.getAllowedValues())) {
            Map map1 = MapPopulator.createMap(this.properties, (Iterable)list1);
            StateImplementation blockstate$stateimplementation = new StateImplementation(blockIn, ImmutableMap.copyOf((Map)map1), null);
            map.put((Object)map1, (Object)blockstate$stateimplementation);
            list.add((Object)blockstate$stateimplementation);
        }
        for (StateImplementation blockstate$stateimplementation1 : list) {
            blockstate$stateimplementation1.buildPropertyValueTable((Map)map);
        }
        this.validStates = ImmutableList.copyOf((Collection)list);
    }

    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }

    private List<Iterable<Comparable>> getAllowedValues() {
        ArrayList list = Lists.newArrayList();
        for (int i = 0; i < this.properties.size(); ++i) {
            list.add((Object)((IProperty)this.properties.get(i)).getAllowedValues());
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
        return Objects.toStringHelper((Object)this).add("block", Block.blockRegistry.getNameForObject((Object)this.block)).add("properties", (Object)Iterables.transform(this.properties, GET_NAME_FUNC)).toString();
    }
}
