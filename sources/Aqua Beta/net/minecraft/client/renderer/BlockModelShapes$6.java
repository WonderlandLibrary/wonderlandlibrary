package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

class BlockModelShapes.6
extends StateMapperBase {
    BlockModelShapes.6() {
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap map = Maps.newLinkedHashMap((Map)state.getProperties());
        String s = BlockStoneSlab.VARIANT.getName((Enum)((BlockStoneSlab.EnumType)map.remove((Object)BlockStoneSlab.VARIANT)));
        map.remove((Object)BlockStoneSlab.SEAMLESS);
        String s1 = (Boolean)state.getValue((IProperty)BlockStoneSlab.SEAMLESS) != false ? "all" : "normal";
        return new ModelResourceLocation(s + "_double_slab", s1);
    }
}
