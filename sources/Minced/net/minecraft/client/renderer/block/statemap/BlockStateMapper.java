// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.MoreObjects;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.block.state.IBlockState;
import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.Set;
import net.minecraft.block.Block;
import java.util.Map;

public class BlockStateMapper
{
    private final Map<Block, IStateMapper> blockStateMap;
    private final Set<Block> setBuiltInBlocks;
    
    public BlockStateMapper() {
        this.blockStateMap = (Map<Block, IStateMapper>)Maps.newIdentityHashMap();
        this.setBuiltInBlocks = (Set<Block>)Sets.newIdentityHashSet();
    }
    
    public void registerBlockStateMapper(final Block blockIn, final IStateMapper stateMapper) {
        this.blockStateMap.put(blockIn, stateMapper);
    }
    
    public void registerBuiltInBlocks(final Block... blockIn) {
        Collections.addAll(this.setBuiltInBlocks, blockIn);
    }
    
    public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations() {
        final Map<IBlockState, ModelResourceLocation> map = (Map<IBlockState, ModelResourceLocation>)Maps.newIdentityHashMap();
        for (final Block block : Block.REGISTRY) {
            map.putAll(this.getVariants(block));
        }
        return map;
    }
    
    public Set<ResourceLocation> getBlockstateLocations(final Block blockIn) {
        if (this.setBuiltInBlocks.contains(blockIn)) {
            return Collections.emptySet();
        }
        final IStateMapper istatemapper = this.blockStateMap.get(blockIn);
        if (istatemapper == null) {
            return Collections.singleton(Block.REGISTRY.getNameForObject(blockIn));
        }
        final Set<ResourceLocation> set = (Set<ResourceLocation>)Sets.newHashSet();
        for (final ModelResourceLocation modelresourcelocation : istatemapper.putStateModelLocations(blockIn).values()) {
            set.add(new ResourceLocation(modelresourcelocation.getNamespace(), modelresourcelocation.getPath()));
        }
        return set;
    }
    
    public Map<IBlockState, ModelResourceLocation> getVariants(final Block blockIn) {
        return this.setBuiltInBlocks.contains(blockIn) ? Collections.emptyMap() : ((IStateMapper)MoreObjects.firstNonNull((Object)this.blockStateMap.get(blockIn), (Object)new DefaultStateMapper())).putStateModelLocations(blockIn);
    }
}
