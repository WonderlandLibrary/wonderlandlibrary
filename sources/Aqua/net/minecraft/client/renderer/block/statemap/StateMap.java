package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    private StateMap(IProperty<?> name, String suffix, List<IProperty<?>> ignored) {
        this.name = name;
        this.suffix = suffix;
        this.ignored = ignored;
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap map = Maps.newLinkedHashMap((Map)state.getProperties());
        String s = this.name == null ? ((ResourceLocation)Block.blockRegistry.getNameForObject((Object)state.getBlock())).toString() : this.name.getName((Comparable)map.remove(this.name));
        if (this.suffix != null) {
            s = s + this.suffix;
        }
        for (IProperty iproperty : this.ignored) {
            map.remove((Object)iproperty);
        }
        return new ModelResourceLocation(s, this.getPropertyString((Map)map));
    }
}
