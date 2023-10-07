// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.block.statemap;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;

public class DefaultStateMapper extends StateMapperBase
{
    private static final String __OBFID = "CL_00002477";
    
    @Override
    protected ModelResourceLocation func_178132_a(final IBlockState p_178132_1_) {
        return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(p_178132_1_.getBlock()), this.func_178131_a((Map)p_178132_1_.getProperties()));
    }
}
