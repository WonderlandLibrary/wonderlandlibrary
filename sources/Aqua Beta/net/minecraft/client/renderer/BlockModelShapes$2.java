package net.minecraft.client.renderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;

class BlockModelShapes.2
extends StateMapperBase {
    BlockModelShapes.2() {
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation("dead_bush", "normal");
    }
}
