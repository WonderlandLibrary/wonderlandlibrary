// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.statemap;

import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedHashMap;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import java.util.List;
import net.minecraft.block.properties.IProperty;

public class StateMap extends StateMapperBase
{
    private final IProperty field_178142_a;
    private final String field_178141_c;
    private final List field_178140_d;
    private static final String __OBFID = "CL_00002476";
    
    private StateMap(final IProperty p_i46210_1_, final String p_i46210_2_, final List p_i46210_3_) {
        this.field_178142_a = p_i46210_1_;
        this.field_178141_c = p_i46210_2_;
        this.field_178140_d = p_i46210_3_;
    }
    
    @Override
    protected ModelResourceLocation func_178132_a(final IBlockState p_178132_1_) {
        final LinkedHashMap var2 = Maps.newLinkedHashMap((Map)p_178132_1_.getProperties());
        String var3;
        if (this.field_178142_a == null) {
            var3 = ((ResourceLocation)Block.blockRegistry.getNameForObject(p_178132_1_.getBlock())).toString();
        }
        else {
            var3 = this.field_178142_a.getName((Comparable)var2.remove(this.field_178142_a));
        }
        if (this.field_178141_c != null) {
            var3 += this.field_178141_c;
        }
        for (final IProperty var5 : this.field_178140_d) {
            var2.remove(var5);
        }
        return new ModelResourceLocation(var3, this.func_178131_a(var2));
    }
    
    StateMap(final IProperty p_i46211_1_, final String p_i46211_2_, final List p_i46211_3_, final Object p_i46211_4_) {
        this(p_i46211_1_, p_i46211_2_, p_i46211_3_);
    }
    
    public static class Builder
    {
        private IProperty field_178445_a;
        private String field_178443_b;
        private final List field_178444_c;
        private static final String __OBFID = "CL_00002474";
        
        public Builder() {
            this.field_178444_c = Lists.newArrayList();
        }
        
        public Builder func_178440_a(final IProperty p_178440_1_) {
            this.field_178445_a = p_178440_1_;
            return this;
        }
        
        public Builder func_178439_a(final String p_178439_1_) {
            this.field_178443_b = p_178439_1_;
            return this;
        }
        
        public Builder func_178442_a(final IProperty... p_178442_1_) {
            Collections.addAll(this.field_178444_c, p_178442_1_);
            return this;
        }
        
        public StateMap build() {
            return new StateMap(this.field_178445_a, this.field_178443_b, this.field_178444_c, null);
        }
    }
}
