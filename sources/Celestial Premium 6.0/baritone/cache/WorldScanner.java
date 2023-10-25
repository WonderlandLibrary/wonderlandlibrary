/*
 * Decompiled with CFR 0.150.
 */
package baritone.cache;

import baritone.api.cache.ICachedWorld;
import baritone.api.cache.IWorldScanner;
import baritone.api.utils.BetterBlockPos;
import baritone.api.utils.BlockOptionalMetaLookup;
import baritone.api.utils.IPlayerContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public enum WorldScanner implements IWorldScanner
{
    INSTANCE;

    private static final int[] DEFAULT_COORDINATE_ITERATION_ORDER;

    @Override
    public List<BlockPos> scanChunkRadius(IPlayerContext ctx, BlockOptionalMetaLookup filter, int max, int yLevelThreshold, int maxSearchRadius) {
        ArrayList<BlockPos> res = new ArrayList<BlockPos>();
        if (filter.blocks().isEmpty()) {
            return res;
        }
        ChunkProviderClient chunkProvider = (ChunkProviderClient)ctx.world().getChunkProvider();
        int maxSearchRadiusSq = maxSearchRadius * maxSearchRadius;
        int playerChunkX = ctx.playerFeet().getX() >> 4;
        int playerChunkZ = ctx.playerFeet().getZ() >> 4;
        int playerY = ctx.playerFeet().getY();
        int playerYBlockStateContainerIndex = playerY >> 4;
        int[] coordinateIterationOrder = IntStream.range(0, 16).boxed().sorted(Comparator.comparingInt(y -> Math.abs(y - playerYBlockStateContainerIndex))).mapToInt(x -> x).toArray();
        int searchRadiusSq = 0;
        boolean foundWithinY = false;
        while (true) {
            boolean allUnloaded = true;
            boolean foundChunks = false;
            for (int xoff = -searchRadiusSq; xoff <= searchRadiusSq; ++xoff) {
                for (int zoff = -searchRadiusSq; zoff <= searchRadiusSq; ++zoff) {
                    int distance = xoff * xoff + zoff * zoff;
                    if (distance != searchRadiusSq) continue;
                    foundChunks = true;
                    int chunkX = xoff + playerChunkX;
                    int chunkZ = zoff + playerChunkZ;
                    Chunk chunk = chunkProvider.getLoadedChunk(chunkX, chunkZ);
                    if (chunk == null) continue;
                    allUnloaded = false;
                    if (!this.scanChunkInto(chunkX << 4, chunkZ << 4, chunk, filter, res, max, yLevelThreshold, playerY, coordinateIterationOrder)) continue;
                    foundWithinY = true;
                }
            }
            if (allUnloaded && foundChunks || res.size() >= max && (searchRadiusSq > maxSearchRadiusSq || searchRadiusSq > 1 && foundWithinY)) {
                return res;
            }
            ++searchRadiusSq;
        }
    }

    @Override
    public List<BlockPos> scanChunk(IPlayerContext ctx, BlockOptionalMetaLookup filter, ChunkPos pos, int max, int yLevelThreshold) {
        if (filter.blocks().isEmpty()) {
            return Collections.emptyList();
        }
        ChunkProviderClient chunkProvider = (ChunkProviderClient)ctx.world().getChunkProvider();
        Chunk chunk = chunkProvider.getLoadedChunk(pos.x, pos.z);
        int playerY = ctx.playerFeet().getY();
        if (chunk == null || chunk.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<BlockPos> res = new ArrayList<BlockPos>();
        this.scanChunkInto(pos.x << 4, pos.z << 4, chunk, filter, res, max, yLevelThreshold, playerY, DEFAULT_COORDINATE_ITERATION_ORDER);
        return res;
    }

    @Override
    public int repack(IPlayerContext ctx) {
        return this.repack(ctx, 40);
    }

    @Override
    public int repack(IPlayerContext ctx, int range) {
        IChunkProvider chunkProvider = ctx.world().getChunkProvider();
        ICachedWorld cachedWorld = ctx.worldData().getCachedWorld();
        BetterBlockPos playerPos = ctx.playerFeet();
        int playerChunkX = playerPos.getX() >> 4;
        int playerChunkZ = playerPos.getZ() >> 4;
        int minX = playerChunkX - range;
        int minZ = playerChunkZ - range;
        int maxX = playerChunkX + range;
        int maxZ = playerChunkZ + range;
        int queued = 0;
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                Chunk chunk = chunkProvider.getLoadedChunk(x, z);
                if (chunk == null || chunk.isEmpty()) continue;
                ++queued;
                cachedWorld.queueForPacking(chunk);
            }
        }
        return queued;
    }

    private boolean scanChunkInto(int chunkX, int chunkZ, Chunk chunk, BlockOptionalMetaLookup filter, Collection<BlockPos> result, int max, int yLevelThreshold, int playerY, int[] coordinateIterationOrder) {
        ExtendedBlockStorage[] chunkInternalStorageArray = chunk.getBlockStorageArray();
        boolean foundWithinY = false;
        for (int yIndex = 0; yIndex < 16; ++yIndex) {
            int y0 = coordinateIterationOrder[yIndex];
            ExtendedBlockStorage extendedblockstorage = chunkInternalStorageArray[y0];
            if (extendedblockstorage == null) continue;
            int yReal = y0 << 4;
            BlockStateContainer bsc = extendedblockstorage.getData();
            int[] storage = bsc.storageArray();
            int imax = 4096;
            for (int i = 0; i < 4096; ++i) {
                IBlockState state = bsc.getAtPalette(storage[i]);
                if (!filter.has(state)) continue;
                int y = yReal | i >> 8 & 0xF;
                if (result.size() >= max) {
                    if (Math.abs(y - playerY) < yLevelThreshold) {
                        foundWithinY = true;
                    } else if (foundWithinY) {
                        return true;
                    }
                }
                result.add(new BlockPos(chunkX | i & 0xF, y, chunkZ | i >> 4 & 0xF));
            }
        }
        return foundWithinY;
    }

    static {
        DEFAULT_COORDINATE_ITERATION_ORDER = IntStream.range(0, 16).toArray();
    }
}

