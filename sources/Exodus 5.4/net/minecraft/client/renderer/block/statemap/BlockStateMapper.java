/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.client.renderer.block.statemap;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class BlockStateMapper {
    private Set<Block> setBuiltInBlocks;
    private Map<Block, IStateMapper> blockStateMap = Maps.newIdentityHashMap();

    public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations() {
        IdentityHashMap identityHashMap = Maps.newIdentityHashMap();
        for (Block block : Block.blockRegistry) {
            if (this.setBuiltInBlocks.contains(block)) continue;
            identityHashMap.putAll(((IStateMapper)Objects.firstNonNull((Object)this.blockStateMap.get(block), (Object)new DefaultStateMapper())).putStateModelLocations(block));
        }
        return identityHashMap;
    }

    public void registerBuiltInBlocks(Block ... blockArray) {
        Collections.addAll(this.setBuiltInBlocks, blockArray);
    }

    public BlockStateMapper() {
        this.setBuiltInBlocks = Sets.newIdentityHashSet();
    }

    public void registerBlockStateMapper(Block block, IStateMapper iStateMapper) {
        this.blockStateMap.put(block, iStateMapper);
    }
}

