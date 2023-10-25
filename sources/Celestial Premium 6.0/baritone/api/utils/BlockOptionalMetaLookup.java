/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.utils.BlockOptionalMeta;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class BlockOptionalMetaLookup {
    private final BlockOptionalMeta[] boms;

    public BlockOptionalMetaLookup(BlockOptionalMeta ... boms) {
        this.boms = boms;
    }

    public BlockOptionalMetaLookup(Block ... blocks) {
        this.boms = (BlockOptionalMeta[])Stream.of(blocks).map(BlockOptionalMeta::new).toArray(BlockOptionalMeta[]::new);
    }

    public BlockOptionalMetaLookup(List<Block> blocks) {
        this.boms = (BlockOptionalMeta[])blocks.stream().map(BlockOptionalMeta::new).toArray(BlockOptionalMeta[]::new);
    }

    public BlockOptionalMetaLookup(String ... blocks) {
        this.boms = (BlockOptionalMeta[])Stream.of(blocks).map(BlockOptionalMeta::new).toArray(BlockOptionalMeta[]::new);
    }

    public boolean has(Block block) {
        for (BlockOptionalMeta bom : this.boms) {
            if (bom.getBlock() != block) continue;
            return true;
        }
        return false;
    }

    public boolean has(IBlockState state) {
        for (BlockOptionalMeta bom : this.boms) {
            if (!bom.matches(state)) continue;
            return true;
        }
        return false;
    }

    public boolean has(ItemStack stack) {
        for (BlockOptionalMeta bom : this.boms) {
            if (!bom.matches(stack)) continue;
            return true;
        }
        return false;
    }

    public List<BlockOptionalMeta> blocks() {
        return Arrays.asList(this.boms);
    }

    public String toString() {
        return String.format("BlockOptionalMetaLookup{%s}", Arrays.toString(this.boms));
    }
}

