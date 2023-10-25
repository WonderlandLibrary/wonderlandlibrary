package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

class BlockModelShapes.3
extends StateMapperBase {
    BlockModelShapes.3() {
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap map = Maps.newLinkedHashMap((Map)state.getProperties());
        if (state.getValue((IProperty)BlockStem.FACING) != EnumFacing.UP) {
            map.remove((Object)BlockStem.AGE);
        }
        return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject((Object)state.getBlock()), this.getPropertyString((Map)map));
    }
}
