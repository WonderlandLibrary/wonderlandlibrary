// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.statemap;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.Maps;
import java.util.Map;

public abstract class StateMapperBase implements IStateMapper
{
    protected Map field_178133_b;
    private static final String __OBFID = "CL_00002479";
    
    public StateMapperBase() {
        this.field_178133_b = Maps.newLinkedHashMap();
    }
    
    public String func_178131_a(final Map p_178131_1_) {
        final StringBuilder var2 = new StringBuilder();
        for (final Map.Entry var4 : p_178131_1_.entrySet()) {
            if (var2.length() != 0) {
                var2.append(",");
            }
            final IProperty var5 = var4.getKey();
            final Comparable var6 = var4.getValue();
            var2.append(var5.getName());
            var2.append("=");
            var2.append(var5.getName(var6));
        }
        if (var2.length() == 0) {
            var2.append("normal");
        }
        return var2.toString();
    }
    
    @Override
    public Map func_178130_a(final Block p_178130_1_) {
        for (final IBlockState var3 : p_178130_1_.getBlockState().getValidStates()) {
            this.field_178133_b.put(var3, this.func_178132_a(var3));
        }
        return this.field_178133_b;
    }
    
    protected abstract ModelResourceLocation func_178132_a(final IBlockState p0);
}
