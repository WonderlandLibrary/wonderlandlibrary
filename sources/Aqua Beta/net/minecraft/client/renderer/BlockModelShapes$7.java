package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

class BlockModelShapes.7
extends StateMapperBase {
    BlockModelShapes.7() {
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap map = Maps.newLinkedHashMap((Map)state.getProperties());
        String s = BlockStoneSlabNew.VARIANT.getName((Enum)((BlockStoneSlabNew.EnumType)map.remove((Object)BlockStoneSlabNew.VARIANT)));
        map.remove((Object)BlockStoneSlab.SEAMLESS);
        String s1 = (Boolean)state.getValue((IProperty)BlockStoneSlabNew.SEAMLESS) != false ? "all" : "normal";
        return new ModelResourceLocation(s + "_double_slab", s1);
    }
}
