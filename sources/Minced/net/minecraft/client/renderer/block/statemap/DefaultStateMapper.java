// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.statemap;

import net.minecraft.block.properties.IProperty;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;

public class DefaultStateMapper extends StateMapperBase
{
    @Override
    protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
        return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), this.getPropertyString((Map<IProperty<?>, Comparable<?>>)state.getProperties()));
    }
}
