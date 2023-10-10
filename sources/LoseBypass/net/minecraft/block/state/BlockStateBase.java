/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;

public abstract class BlockStateBase
implements IBlockState {
    private static final Joiner COMMA_JOINER = Joiner.on(',');
    private static final Function<Map.Entry<IProperty, Comparable>, String> MAP_ENTRY_TO_STRING = new Function<Map.Entry<IProperty, Comparable>, String>(){

        @Override
        public String apply(Map.Entry<IProperty, Comparable> p_apply_1_) {
            if (p_apply_1_ == null) {
                return "<NULL>";
            }
            IProperty iproperty = p_apply_1_.getKey();
            return iproperty.getName() + "=" + iproperty.getName(p_apply_1_.getValue());
        }
    };

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
        return this.withProperty(property, (Comparable)BlockStateBase.cyclePropertyValue(property.getAllowedValues(), this.getValue(property)));
    }

    protected static <T> T cyclePropertyValue(Collection<T> values, T currentValue) {
        Iterator<T> iterator = values.iterator();
        do {
            if (!iterator.hasNext()) return iterator.next();
        } while (!iterator.next().equals(currentValue));
        if (!iterator.hasNext()) return values.iterator().next();
        return iterator.next();
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(Block.blockRegistry.getNameForObject(this.getBlock()));
        if (this.getProperties().isEmpty()) return stringbuilder.toString();
        stringbuilder.append("[");
        COMMA_JOINER.appendTo(stringbuilder, Iterables.transform(this.getProperties().entrySet(), MAP_ENTRY_TO_STRING));
        stringbuilder.append("]");
        return stringbuilder.toString();
    }
}

