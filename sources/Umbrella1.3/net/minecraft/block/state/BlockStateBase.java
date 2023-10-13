/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.base.Joiner
 *  com.google.common.collect.ImmutableTable
 *  com.google.common.collect.Iterables
 */
package net.minecraft.block.state;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public abstract class BlockStateBase
implements IBlockState {
    private static final Joiner COMMA_JOINER = Joiner.on((char)',');
    private static final Function field_177233_b = new Function(){
        private static final String __OBFID = "CL_00002031";

        public String func_177225_a(Map.Entry p_177225_1_) {
            if (p_177225_1_ == null) {
                return "<NULL>";
            }
            IProperty var2 = (IProperty)p_177225_1_.getKey();
            return String.valueOf(var2.getName()) + "=" + var2.getName((Comparable)p_177225_1_.getValue());
        }

        public Object apply(Object p_apply_1_) {
            return this.func_177225_a((Map.Entry)p_apply_1_);
        }
    };
    private static final String __OBFID = "CL_00002032";
    private int blockId = -1;
    private int blockStateId = -1;
    private int metadata = -1;
    private ResourceLocation blockLocation = null;

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
            this.blockLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.getBlock());
        }
        return this.blockLocation;
    }

    @Override
    public IBlockState cycleProperty(IProperty property) {
        return this.withProperty(property, (Comparable)BlockStateBase.cyclePropertyValue(property.getAllowedValues(), this.getValue(property)));
    }

    protected static Object cyclePropertyValue(Collection values, Object currentValue) {
        Iterator var2 = values.iterator();
        while (var2.hasNext()) {
            if (!var2.next().equals(currentValue)) continue;
            if (var2.hasNext()) {
                return var2.next();
            }
            return values.iterator().next();
        }
        return var2.next();
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append(Block.blockRegistry.getNameForObject(this.getBlock()));
        if (!this.getProperties().isEmpty()) {
            var1.append("[");
            COMMA_JOINER.appendTo(var1, Iterables.transform((Iterable)this.getProperties().entrySet(), (Function)field_177233_b));
            var1.append("]");
        }
        return var1.toString();
    }

    public ImmutableTable<IProperty, Comparable, IBlockState> getPropertyValueTable() {
        return null;
    }
}

