// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.statemap;

import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.block.properties.IProperty;

public class StateMap extends StateMapperBase
{
    private final IProperty<?> name;
    private final String suffix;
    private final List<IProperty<?>> ignored;
    
    private StateMap(@Nullable final IProperty<?> name, @Nullable final String suffix, final List<IProperty<?>> ignored) {
        this.name = name;
        this.suffix = suffix;
        this.ignored = ignored;
    }
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        final Map<IProperty<?>, Comparable<?>> map = (Map<IProperty<?>, Comparable<?>>)Maps.newLinkedHashMap((Map)state.getProperties());
        String s;
        if (this.name == null) {
            s = Block.REGISTRY.getNameForObject(state.getBlock()).toString();
        }
        else {
            s = this.removeName(this.name, map);
        }
        if (this.suffix != null) {
            s += this.suffix;
        }
        for (final IProperty<?> iproperty : this.ignored) {
            map.remove(iproperty);
        }
        return new ModelResourceLocation(s, this.getPropertyString(map));
    }
    
    private <T extends Comparable<T>> String removeName(final IProperty<T> property, final Map<IProperty<?>, Comparable<?>> values) {
        return property.getName((T)values.remove(this.name));
    }
    
    public static class Builder
    {
        private IProperty<?> name;
        private String suffix;
        private final List<IProperty<?>> ignored;
        
        public Builder() {
            this.ignored = (List<IProperty<?>>)Lists.newArrayList();
        }
        
        public Builder withName(final IProperty<?> builderPropertyIn) {
            this.name = builderPropertyIn;
            return this;
        }
        
        public Builder withSuffix(final String builderSuffixIn) {
            this.suffix = builderSuffixIn;
            return this;
        }
        
        public Builder ignore(final IProperty<?>... ignores) {
            Collections.addAll(this.ignored, ignores);
            return this;
        }
        
        public StateMap build() {
            return new StateMap(this.name, this.suffix, this.ignored, null);
        }
    }
}
