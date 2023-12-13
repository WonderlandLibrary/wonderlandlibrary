// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
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
    private LongHashMap chunkMapping;
    private List chunkListing;
    private World worldObj;
    private static final String __OBFID = "CL_00000880";
    
    public ChunkProviderClient(final World worldIn) {
        this.chunkMapping = new LongHashMap();
        this.chunkListing = Lists.newArrayList();
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.worldObj = worldIn;
    }
    
    @Override
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return true;
    }
    
    public void unloadChunk(final int p_73234_1_, final int p_73234_2_) {
        final Chunk var3 = this.provideChunk(p_73234_1_, p_73234_2_);
        if (!var3.isEmpty()) {
            var3.onChunkUnload();
        }
        this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(p_73234_1_, p_73234_2_));
        this.chunkListing.remove(var3);
    }
    
    public Chunk loadChunk(final int p_73158_1_, final int p_73158_2_) {
        final Chunk var3 = new Chunk(this.worldObj, p_73158_1_, p_73158_2_);
        this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_), var3);
        this.chunkListing.add(var3);
        var3.func_177417_c(true);
        return var3;
    }
    
    @Override
    public Chunk provideChunk(final int p_73154_1_, final int p_73154_2_) {
        final Chunk var3 = (Chunk)this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return (var3 == null) ? this.blankChunk : var3;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate p_73151_2_) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        final long var1 = System.currentTimeMillis();
        for (final Chunk var3 : this.chunkListing) {
            var3.func_150804_b(System.currentTimeMillis() - var1 > 5L);
        }
        if (System.currentTimeMillis() - var1 > 100L) {
            ChunkProviderClient.logger.info("Warning: Clientside chunk ticking took {} ms", new Object[] { System.currentTimeMillis() - var1 });
        }
        return false;
    }
    
    @Override
    public boolean canSave() {
        return false;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
    }
    
    @Override
    public String makeString() {
        return "MultiplayerChunkCache: " + this.chunkMapping.getNumHashElements() + ", " + this.chunkListing.size();
    }
    
    @Override
    public List func_177458_a(final EnumCreatureType p_177458_1_, final BlockPos p_177458_2_) {
        return null;
    }
    
    @Override
    public BlockPos func_180513_a(final World worldIn, final String p_180513_2_, final BlockPos p_180513_3_) {
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return this.chunkListing.size();
    }
    
    @Override
    public void func_180514_a(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
    }
    
    @Override
    public Chunk func_177459_a(final BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
