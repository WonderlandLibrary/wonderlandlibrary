// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block.state;

import com.google.common.collect.Iterables;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.ImmutableTable;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.properties.IProperty;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.base.Joiner;

public abstract class BlockStateBase implements IBlockState
{
    private static final Joiner COMMA_JOINER;
    private static final Function<Map.Entry<IProperty, Comparable>, String> MAP_ENTRY_TO_STRING;
    private int blockId;
    private int blockStateId;
    private int metadata;
    private ResourceLocation blockLocation;
    
    static {
        COMMA_JOINER = Joiner.on(',');
        MAP_ENTRY_TO_STRING = (Function)new Function<Map.Entry<IProperty, Comparable>, String>() {
            public String apply(final Map.Entry<IProperty, Comparable> p_apply_1_) {
                if (p_apply_1_ == null) {
                    return "<NULL>";
                }
                final IProperty iproperty = p_apply_1_.getKey();
                return String.valueOf(iproperty.getName()) + "=" + iproperty.getName(p_apply_1_.getValue());
            }
        };
    }
    
    public BlockStateBase() {
        this.blockId = -1;
        this.blockStateId = -1;
        this.metadata = -1;
        this.blockLocation = null;
    }
    
    public int getBlockId() {
        if (this.blockId < 0) {
            this.blockId = Block.getIdFromBlock(this.getBlock());
        }
        return this.blockId;
    }
    
    public int getBlockStateId() {
        if (this.blockStateId < 0) {
            this.blockStateId = Block.getStateId(this);
        }
        return this.blockStateId;
    }
    
    public int getMetadata() {
        if (this.metadata < 0) {
            this.metadata = this.getBlock().getMetaFromState(this);
        }
        return this.metadata;
    }
    
    public ResourceLocation getBlockLocation() {
        if (this.blockLocation == null) {
            this.blockLocation = Block.blockRegistry.getNameForObject(this.getBlock());
        }
        return this.blockLocation;
    }
    
    public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
        return null;
    }
    
    @Override
    public <T extends Comparable<T>> IBlockState cycleProperty(final IProperty<T> property) {
        return this.withProperty(property, (Comparable)cyclePropertyValue((Collection<V>)property.getAllowedValues(), (V)this.getValue((IProperty<T>)property)));
    }
    
    protected static <T> T cyclePropertyValue(final Collection<T> values, final T currentValue) {
        final Iterator<T> iterator = values.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(currentValue)) {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
                return values.iterator().next();
            }
        }
        return iterator.next();
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(Block.blockRegistry.getNameForObject(this.getBlock()));
        if (!this.getProperties().isEmpty()) {
            stringbuilder.append("[");
            BlockStateBase.COMMA_JOINER.appendTo(stringbuilder, Iterables.transform((Iterable)this.getProperties().entrySet(), (Function)BlockStateBase.MAP_ENTRY_TO_STRING));
            stringbuilder.append("]");
        }
        return stringbuilder.toString();
    }
}
