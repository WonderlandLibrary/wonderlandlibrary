package net.smoothboot.client.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.smoothboot.client.Client;

import java.util.Objects;
import java.util.stream.Stream;

public enum ChunkUtil
{
    ;

    private static final MinecraftClient mc = Client.mc;

    public static Stream<BlockEntity> getLoadedBlockEntities()
    {
        return getLoadedChunks()
                .flatMap(chunk -> chunk.getBlockEntities().values().stream());
    }

    public static int getManhattanDistance(ChunkPos a, ChunkPos b)
    {
        return Math.abs(a.x - b.x) + Math.abs(a.z - b.z);
    }

     public static Stream<WorldChunk> getLoadedChunks()
    {
        int radius = Math.max(2, mc.options.getClampedViewDistance()) + 3;
        int diameter = radius * 2 + 1;

        ChunkPos center = mc.player.getChunkPos();
        ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
        ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);

        Stream<WorldChunk> stream = Stream.<ChunkPos> iterate(min, pos -> {

                    int x = pos.x;
                    int z = pos.z;

                    x++;

                    if(x > max.x)
                    {
                        x = min.x;
                        z++;
                    }

                    if(z > max.z)
                        throw new IllegalStateException("Stream limit didn't work.");

                    return new ChunkPos(x, z);

                }).limit(diameter * diameter)
                .filter(c -> mc.world.isChunkLoaded(c.x, c.z))
                .map(c -> mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);

        return stream;
    }

    public static int getHighestNonEmptySectionYOffset(Chunk chunk)
    {
        int i = chunk.getHighestNonEmptySection();
        if(i == -1)
            return chunk.getBottomY();

        return ChunkSectionPos.getBlockCoord(chunk.sectionIndexToCoord(i));
    }
}