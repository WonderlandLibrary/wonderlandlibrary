// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import java.util.Iterator;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.EmptyChunk;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import java.util.List;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderClient implements IChunkProvider
{
    private static final Logger logger;
    private Chunk blankChunk;
    private LongHashMap<Chunk> chunkMapping;
    private List<Chunk> chunkListing;
    private World worldObj;
    
    public ChunkProviderClient(final World worldIn) {
        this.chunkMapping = new LongHashMap<Chunk>();
        this.chunkListing = (List<Chunk>)Lists.newArrayList();
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.worldObj = worldIn;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return true;
    }
    
    public void unloadChunk(final int x, final int z) {
        final Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isEmpty()) {
            chunk.onChunkUnload();
        }
        this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(x, z));
        this.chunkListing.remove(chunk);
    }
    
    public Chunk loadChunk(final int chunkX, final int chunkZ) {
        final Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);
        this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), chunk);
        this.chunkListing.add(chunk);
        chunk.setChunkLoaded(true);
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        final Chunk chunk = this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return (chunk == null) ? this.blankChunk : chunk;
    }
    
    @Override
    public boolean saveChunks(final boolean saveAllChunks, final IProgressUpdate progressCallback) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        final long i = System.currentTimeMillis();
        for (final Chunk chunk : this.chunkListing) {
            chunk.func_150804_b(System.currentTimeMillis() - i > 5L);
        }
        if (System.currentTimeMillis() - i > 100L) {
            ChunkProviderClient.logger.info("Warning: Clientside chunk ticking took {} ms", new Object[] { System.currentTimeMillis() - i });
        }
        return false;
    }
    
    @Override
    public boolean canSave() {
        return false;
    }
    
    @Override
    public void populate(final IChunkProvider chunkProvider, final int x, final int z) {
    }
    
    @Override
    public boolean populateChunk(final IChunkProvider chunkProvider, final Chunk chunkIn, final int x, final int z) {
        return false;
    }
    
    @Override
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.getNumHashElements() + ", " + this.chunkListing.size();
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return null;
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return this.chunkListing.size();
    }
    
    @Override
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
