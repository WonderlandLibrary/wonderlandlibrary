/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class StateMap
extends StateMapperBase {
    private final IProperty<?> name;
    private final String suffix;
    private final List<IProperty<?>> ignored;

    private StateMap(@Nullable IProperty<?> name, @Nullable String suffix, List<IProperty<?>> ignored) {
        this.name = name;
        this.suffix = suffix;
        this.ignored = ignored;
    }

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap<IProperty<?>, Comparable<?>> map = Maps.newLinkedHashMap(state.getProperties());
        String s = this.name == null ? Block.REGISTRY.getNameForObject(state.getBlock()).toString() : this.removeName(this.name, map);
        if (this.suffix != null) {
            s = s + this.suffix;
        }
        for (IProperty<?> iproperty : this.ignored) {
            map.remove(iproperty);
        }
        return new ModelResourceLocation(s, this.getPropertyString(map));
    }

    private <T extends Comparable<T>> String removeName(IProperty<T> p_187490_1_, Map<IProperty<?>, Comparable<?>> p_187490_2_) {
        return p_187490_1_.getName(p_187490_2_.remove(this.name));
    }

    public static class Builder {
        private IProperty<?> name;
        private String suffix;
        private final List<IProperty<?>> ignored = Lists.newArrayList();

        public Builder withName(IProperty<?> builderPropertyIn) {
            this.name = builderPropertyIn;
            return this;
        }

        public Builder withSuffix(String builderSuffixIn) {
            this.suffix = builderSuffixIn;
            return this;
        }

        public Builder ignore(IProperty<?> ... p_178442_1_) {
            Collections.addAll(this.ignored, p_178442_1_);
            return this;
        }

        public StateMap build() {
            return new StateMap(this.name, this.suffix, this.ignored);
        }
    }
}

