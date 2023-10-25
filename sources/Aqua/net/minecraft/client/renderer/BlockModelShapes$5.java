package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

class BlockModelShapes.5
extends StateMapperBase {
    BlockModelShapes.5() {
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        LinkedHashMap map = Maps.newLinkedHashMap((Map)state.getProperties());
        String s = BlockDirt.VARIANT.getName((Enum)((BlockDirt.DirtType)map.remove((Object)BlockDirt.VARIANT)));
        if (BlockDirt.DirtType.PODZOL != state.getValue((IProperty)BlockDirt.VARIANT)) {
            map.remove((Object)BlockDirt.SNOWY);
        }
        return new ModelResourceLocation(s, this.getPropertyString((Map)map));
    }
}
