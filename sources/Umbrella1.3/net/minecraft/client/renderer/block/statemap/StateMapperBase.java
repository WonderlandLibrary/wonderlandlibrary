/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;

public abstract class StateMapperBase
implements IStateMapper {
    protected Map field_178133_b = Maps.newLinkedHashMap();
    private static final String __OBFID = "CL_00002479";

    public String func_178131_a(Map p_178131_1_) {
        StringBuilder var2 = new StringBuilder();
        for (Map.Entry var4 : p_178131_1_.entrySet()) {
            if (var2.length() != 0) {
                var2.append(",");
            }
            IProperty var5 = (IProperty)var4.getKey();
            Comparable var6 = (Comparable)var4.getValue();
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
    public Map func_178130_a(Block p_178130_1_) {
        for (IBlockState var3 : p_178130_1_.getBlockState().getValidStates()) {
            this.field_178133_b.put(var3, this.func_178132_a(var3));
        }
        return this.field_178133_b;
    }

    protected abstract ModelResourceLocation func_178132_a(IBlockState var1);
}

