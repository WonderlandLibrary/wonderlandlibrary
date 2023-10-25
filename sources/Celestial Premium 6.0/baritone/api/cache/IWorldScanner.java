/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.cache;

import baritone.api.utils.BlockOptionalMetaLookup;
import baritone.api.utils.IPlayerContext;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public interface IWorldScanner {
    public List<BlockPos> scanChunkRadius(IPlayerContext var1, BlockOptionalMetaLookup var2, int var3, int var4, int var5);

    default public List<BlockPos> scanChunkRadius(IPlayerContext ctx, List<Block> filter, int max, int yLevelThreshold, int maxSearchRadius) {
        return this.scanChunkRadius(ctx, new BlockOptionalMetaLookup(filter.toArray(new Block[0])), max, yLevelThreshold, maxSearchRadius);
    }

    public List<BlockPos> scanChunk(IPlayerContext var1, BlockOptionalMetaLookup var2, ChunkPos var3, int var4, int var5);

    default public List<BlockPos> scanChunk(IPlayerContext ctx, List<Block> blocks, ChunkPos pos, int max, int yLevelThreshold) {
        return this.scanChunk(ctx, new BlockOptionalMetaLookup(blocks), pos, max, yLevelThreshold);
    }

    public int repack(IPlayerContext var1);

    public int repack(IPlayerContext var1, int var2);
}

