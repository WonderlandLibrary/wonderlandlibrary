/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 */
package net.minecraft.client.multiplayer;

import baritone.utils.accessor.IChunkProviderClient;
import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderClient
implements IChunkProvider,
IChunkProviderClient {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Chunk blankChunk;
    public final Long2ObjectMap<Chunk> chunkMapping = new Long2ObjectOpenHashMap<Chunk>(8192){

        protected void rehash(int p_rehash_1_) {
            if (p_rehash_1_ > this.key.length) {
                super.rehash(p_rehash_1_);
            }
        }
    };
    private final World worldObj;

    public ChunkProviderClient(World worldIn) {
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.worldObj = worldIn;
    }

    public void unloadChunk(int x, int z) {
        Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isEmpty()) {
            chunk.onChunkUnload();
        }
        this.chunkMapping.remove(ChunkPos.asLong(x, z));
    }

    @Override
    @Nullable
    public Chunk getLoadedChunk(int x, int z) {
        return (Chunk)this.chunkMapping.get(ChunkPos.asLong(x, z));
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);
        this.chunkMapping.put(ChunkPos.asLong(chunkX, chunkZ), (Object)chunk);
        chunk.setChunkLoaded(true);
        return chunk;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        return MoreObjects.firstNonNull(this.getLoadedChunk(x, z), this.blankChunk);
    }

    @Override
    public boolean unloadQueuedChunks() {
        long i = System.currentTimeMillis();
        for (Chunk chunk : this.chunkMapping.values()) {
            chunk.onTick(System.currentTimeMillis() - i > 5L);
        }
        if (System.currentTimeMillis() - i > 100L) {
            LOGGER.info("Warning: Clientside chunk ticking took {} ms", System.currentTimeMillis() - i);
        }
        return false;
    }

    @Override
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.size() + ", " + this.chunkMapping.size();
    }

    @Override
    public boolean isChunkGeneratedAt(int x, int y) {
        return this.chunkMapping.containsKey(ChunkPos.asLong(x, y));
    }

    @Override
    public Long2ObjectMap<Chunk> loadedChunks() {
        return this.chunkMapping;
    }
}

