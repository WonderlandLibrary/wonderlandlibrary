/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

public class StateMap
extends StateMapperBase {
    private final IProperty<?> name;
    private final String suffix;
    private final List<IProperty<?>> ignored;

    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
        LinkedHashMap linkedHashMap = Maps.newLinkedHashMap(iBlockState.getProperties());
        String string = this.name == null ? ((ResourceLocation)Block.blockRegistry.getNameForObject(iBlockState.getBlock())).toString() : this.name.getName((Comparable)linkedHashMap.remove(this.name));
        if (this.suffix != null) {
            string = String.valueOf(string) + this.suffix;
        }
        for (IProperty<?> iProperty : this.ignored) {
            linkedHashMap.remove(iProperty);
        }
        return new ModelResourceLocation(string, this.getPropertyString(linkedHashMap));
    }

    /* synthetic */ StateMap(IProperty iProperty, String string, List list, StateMap stateMap) {
        this(iProperty, string, list);
    }

    private StateMap(IProperty<?> iProperty, String string, List<IProperty<?>> list) {
        this.name = iProperty;
        this.suffix = string;
        this.ignored = list;
    }

    public static class Builder {
        private String suffix;
        private final List<IProperty<?>> ignored = Lists.newArrayList();
        private IProperty<?> name;

        public Builder withName(IProperty<?> iProperty) {
            this.name = iProperty;
            return this;
        }

        public Builder withSuffix(String string) {
            this.suffix = string;
            return this;
        }

        public Builder ignore(IProperty<?> ... iPropertyArray) {
            Collections.addAll(this.ignored, iPropertyArray);
            return this;
        }

        public StateMap build() {
            return new StateMap(this.name, this.suffix, this.ignored, null);
        }
    }
}

