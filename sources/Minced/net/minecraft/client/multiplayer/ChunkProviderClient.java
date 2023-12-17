// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.google.common.base.MoreObjects;
import javax.annotation.Nullable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.EmptyChunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.world.World;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderClient implements IChunkProvider
{
    private static final Logger LOGGER;
    private final Chunk blankChunk;
    private final Long2ObjectMap<Chunk> loadedChunks;
    private final World world;
    
    public ChunkProviderClient(final World worldIn) {
        this.loadedChunks = (Long2ObjectMap<Chunk>)new Long2ObjectOpenHashMap<Chunk>(8192) {
            protected void rehash(final int p_rehash_1_) {
                if (p_rehash_1_ > this.key.length) {
                    super.rehash(p_rehash_1_);
                }
            }
        };
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.world = worldIn;
    }
    
    public void unloadChunk(final int x, final int z) {
        final Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isEmpty()) {
            chunk.onUnload();
        }
        this.loadedChunks.remove(ChunkPos.asLong(x, z));
    }
    
    @Nullable
    @Override
    public Chunk getLoadedChunk(final int x, final int z) {
        return (Chunk)this.loadedChunks.get(ChunkPos.asLong(x, z));
    }
    
    public Chunk loadChunk(final int chunkX, final int chunkZ) {
        final Chunk chunk = new Chunk(this.world, chunkX, chunkZ);
        this.loadedChunks.put(ChunkPos.asLong(chunkX, chunkZ), (Object)chunk);
        chunk.markLoaded(true);
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        return (Chunk)MoreObjects.firstNonNull((Object)this.getLoadedChunk(x, z), (Object)this.blankChunk);
    }
    
    @Override
    public boolean tick() {
        final long i = System.currentTimeMillis();
        for (final Chunk chunk : this.loadedChunks.values()) {
            chunk.onTick(System.currentTimeMillis() - i > 5L);
        }
        if (System.currentTimeMillis() - i > 100L) {
            ChunkProviderClient.LOGGER.info("Warning: Clientside chunk ticking took {} ms", (Object)(System.currentTimeMillis() - i));
        }
        return false;
    }
    
    @Override
    public String makeString() {
        return "MultiplayerChunkCache: " + this.loadedChunks.size() + ", " + this.loadedChunks.size();
    }
    
    @Override
    public boolean isChunkGeneratedAt(final int x, final int z) {
        return this.loadedChunks.containsKey(ChunkPos.asLong(x, z));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
