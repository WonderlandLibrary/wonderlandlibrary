/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Joiner
 *  com.google.common.collect.Iterables
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
    private static final Joiner COMMA_JOINER = Joiner.on((char)',');
    private static final Function<Map.Entry<IProperty, Comparable>, String> MAP_ENTRY_TO_STRING = new Function<Map.Entry<IProperty, Comparable>, String>(){

        public String apply(Map.Entry<IProperty, Comparable> entry) {
            if (entry == null) {
                return "<NULL>";
            }
            IProperty iProperty = entry.getKey();
            return String.valueOf(iProperty.getName()) + "=" + iProperty.getName(entry.getValue());
        }
    };

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Block.blockRegistry.getNameForObject(this.getBlock()));
        if (!this.getProperties().isEmpty()) {
            stringBuilder.append("[");
            COMMA_JOINER.appendTo(stringBuilder, Iterables.transform((Iterable)this.getProperties().entrySet(), MAP_ENTRY_TO_STRING));
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }

    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> iProperty) {
        return this.withProperty(iProperty, (Comparable)BlockStateBase.cyclePropertyValue(iProperty.getAllowedValues(), this.getValue(iProperty)));
    }

    protected static <T> T cyclePropertyValue(Collection<T> collection, T t) {
        Iterator<T> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().equals(t)) continue;
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return collection.iterator().next();
        }
        return iterator.next();
    }
}

