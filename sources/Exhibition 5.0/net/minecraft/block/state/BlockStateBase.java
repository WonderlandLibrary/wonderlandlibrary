// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state;

import java.util.Map;
import com.google.common.collect.Iterables;
import net.minecraft.block.Block;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Function;
import com.google.common.base.Joiner;

public abstract class BlockStateBase implements IBlockState
{
    private static final Joiner COMMA_JOINER;
    private static final Function field_177233_b;
    private static final String __OBFID = "CL_00002032";
    
    @Override
    public IBlockState cycleProperty(final IProperty property) {
        return this.withProperty(property, (Comparable)cyclePropertyValue(property.getAllowedValues(), this.getValue(property)));
    }
    
    protected static Object cyclePropertyValue(final Collection values, final Object currentValue) {
        final Iterator var2 = values.iterator();
        while (var2.hasNext()) {
            if (var2.next().equals(currentValue)) {
                if (var2.hasNext()) {
                    return var2.next();
                }
                return values.iterator().next();
            }
        }
        return var2.next();
    }
    
    @Override
    public String toString() {
        final StringBuilder var1 = new StringBuilder();
        var1.append(Block.blockRegistry.getNameForObject(this.getBlock()));
        if (!this.getProperties().isEmpty()) {
            var1.append("[");
            BlockStateBase.COMMA_JOINER.appendTo(var1, Iterables.transform((Iterable)this.getProperties().entrySet(), BlockStateBase.field_177233_b));
            var1.append("]");
        }
        return var1.toString();
    }
    
    static {
        COMMA_JOINER = Joiner.on(',');
        field_177233_b = (Function)new Function() {
            private static final String __OBFID = "CL_00002031";
            
            public String func_177225_a(final Map.Entry p_177225_1_) {
                if (p_177225_1_ == null) {
                    return "<NULL>";
                }
                final IProperty var2 = p_177225_1_.getKey();
                return var2.getName() + "=" + var2.getName((Comparable)p_177225_1_.getValue());
            }
            
            public Object apply(final Object p_apply_1_) {
                return this.func_177225_a((Map.Entry)p_apply_1_);
            }
        };
    }
}
