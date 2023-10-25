package net.minecraft.client.renderer;

import net.minecraft.block.BlockQuartz;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

class BlockModelShapes.1
extends StateMapperBase {
    BlockModelShapes.1() {
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        BlockQuartz.EnumType blockquartz$enumtype = (BlockQuartz.EnumType)state.getValue((IProperty)BlockQuartz.VARIANT);
        switch (BlockModelShapes.8.$SwitchMap$net$minecraft$block$BlockQuartz$EnumType[blockquartz$enumtype.ordinal()]) {
            default: {
                return new ModelResourceLocation("quartz_block", "normal");
            }
            case 2: {
                return new ModelResourceLocation("chiseled_quartz_block", "normal");
            }
            case 3: {
                return new ModelResourceLocation("quartz_column", "axis=y");
            }
            case 4: {
                return new ModelResourceLocation("quartz_column", "axis=x");
            }
            case 5: 
        }
        return new ModelResourceLocation("quartz_column", "axis=z");
    }
}
