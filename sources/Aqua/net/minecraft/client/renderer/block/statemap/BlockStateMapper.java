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
    private Map<Block, IStateMapper> blockStateMap = Maps.newIdentityHashMap();
    private Set<Block> setBuiltInBlocks = Sets.newIdentityHashSet();

    public void registerBlockStateMapper(Block p_178447_1_, IStateMapper p_178447_2_) {
        this.blockStateMap.put((Object)p_178447_1_, (Object)p_178447_2_);
    }

    public void registerBuiltInBlocks(Block ... p_178448_1_) {
        Collections.addAll(this.setBuiltInBlocks, (Object[])p_178448_1_);
    }

    public Map<IBlockState, ModelResourceLocation> putAllStateModelLocations() {
        IdentityHashMap map = Maps.newIdentityHashMap();
        for (Block block : Block.blockRegistry) {
            if (this.setBuiltInBlocks.contains((Object)block)) continue;
            map.putAll(((IStateMapper)Objects.firstNonNull((Object)this.blockStateMap.get((Object)block), (Object)new DefaultStateMapper())).putStateModelLocations(block));
        }
        return map;
    }
}
