// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import java.io.IOException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.EmptyChunk;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.WorldServer;
import java.util.List;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.chunk.Chunk;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderServer implements IChunkProvider
{
    private static final Logger logger;
    private Set<Long> droppedChunksSet;
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;
    public boolean chunkLoadOverride;
    private LongHashMap<Chunk> id2ChunkMap;
    private List<Chunk> loadedChunks;
    private WorldServer worldObj;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ChunkProviderServer(final WorldServer p_i1520_1_, final IChunkLoader p_i1520_2_, final IChunkProvider p_i1520_3_) {
        this.droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap<Long, Boolean>());
        this.chunkLoadOverride = true;
        this.id2ChunkMap = new LongHashMap<Chunk>();
        this.loadedChunks = Lists.newArrayList();
        this.dummyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
        this.worldObj = p_i1520_1_;
        this.chunkLoader = p_i1520_2_;
        this.serverChunkGenerator = p_i1520_3_;
    }
    
    @Override
    public boolean chunkExists(final int x, final int z) {
        return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }
    
    public List<Chunk> func_152380_a() {
        return this.loadedChunks;
    }
    
    public void dropChunk(final int x, final int z) {
        if (this.worldObj.provider.canRespawnHere()) {
            if (!this.worldObj.isSpawnChunk(x, z)) {
                this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(x, z));
            }
        }
        else {
            this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(x, z));
        }
    }
    
    public void unloadAllChunks() {
        for (final Chunk chunk : this.loadedChunks) {
            this.dropChunk(chunk.xPosition, chunk.zPosition);
        }
    }
    
    public Chunk loadChunk(final int chunkX, final int chunkZ) {
        final long i = ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
        this.droppedChunksSet.remove(i);
        Chunk chunk = this.id2ChunkMap.getValueByKey(i);
        if (chunk == null) {
            chunk = this.loadChunkFromFile(chunkX, chunkZ);
            if (chunk == null) {
                if (this.serverChunkGenerator == null) {
                    chunk = this.dummyChunk;
                }
                else {
                    try {
                        chunk = this.serverChunkGenerator.provideChunk(chunkX, chunkZ);
                    }
                    catch (final Throwable throwable) {
                        final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                        final CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                        crashreportcategory.addCrashSection("Location", String.format("%d,%d", chunkX, chunkZ));
                        crashreportcategory.addCrashSection("Position hash", i);
                        crashreportcategory.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                        throw new ReportedException(crashreport);
                    }
                }
            }
            this.id2ChunkMap.add(i, chunk);
            this.loadedChunks.add(chunk);
            chunk.onChunkLoad();
            chunk.populateChunk(this, this, chunkX, chunkZ);
        }
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        final Chunk chunk = this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
        return (chunk == null) ? ((!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride) ? this.dummyChunk : this.loadChunk(x, z)) : chunk;
    }
    
    private Chunk loadChunkFromFile(final int x, final int z) {
        if (this.chunkLoader == null) {
            return null;
        }
        try {
            final Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, x, z);
            if (chunk != null) {
                chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
                if (this.serverChunkGenerator != null) {
                    this.serverChunkGenerator.recreateStructures(chunk, x, z);
                }
            }
            return chunk;
        }
        catch (final Exception exception) {
            ChunkProviderServer.logger.error("Couldn't load chunk", (Throwable)exception);
            return null;
        }
    }
    
    private void saveChunkExtraData(final Chunk chunkIn) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.worldObj, chunkIn);
            }
            catch (final Exception exception) {
                ChunkProviderServer.logger.error("Couldn't save entities", (Throwable)exception);
            }
        }
    }
    
    private void saveChunkData(final Chunk chunkIn) {
        if (this.chunkLoader != null) {
            try {
                chunkIn.setLastSaveTime(this.worldObj.getTotalWorldTime());
                this.chunkLoader.saveChunk(this.worldObj, chunkIn);
            }
            catch (final IOException ioexception) {
                ChunkProviderServer.logger.error("Couldn't save chunk", (Throwable)ioexception);
            }
            catch (final MinecraftException minecraftexception) {
                ChunkProviderServer.logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)minecraftexception);
            }
        }
    }
    
    @Override
    public void populate(final IChunkProvider chunkProvider, final int x, final int z) {
        final Chunk chunk = this.provideChunk(x, z);
        if (!chunk.isTerrainPopulated()) {
            chunk.func_150809_p();
            if (this.serverChunkGenerator != null) {
                this.serverChunkGenerator.populate(chunkProvider, x, z);
                chunk.setChunkModified();
            }
        }
    }
    
    @Override
    public boolean populateChunk(final IChunkProvider chunkProvider, final Chunk chunkIn, final int x, final int z) {
        if (this.serverChunkGenerator != null && this.serverChunkGenerator.populateChunk(chunkProvider, chunkIn, x, z)) {
            final Chunk chunk = this.provideChunk(x, z);
            chunk.setChunkModified();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean saveChunks(final boolean saveAllChunks, final IProgressUpdate progressCallback) {
        int i = 0;
        final List<Chunk> list = Lists.newArrayList((Iterable)this.loadedChunks);
        for (int j = 0; j < list.size(); ++j) {
            final Chunk chunk = list.get(j);
            if (saveAllChunks) {
                this.saveChunkExtraData(chunk);
            }
            if (chunk.needsSaving(saveAllChunks)) {
                this.saveChunkData(chunk);
                chunk.setModified(false);
                if (++i == 24 && !saveAllChunks) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public void saveExtraData() {
        if (this.chunkLoader != null) {
            this.chunkLoader.saveExtraData();
        }
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        if (!this.worldObj.disableLevelSaving) {
            for (int i = 0; i < 100; ++i) {
                if (!this.droppedChunksSet.isEmpty()) {
                    final Long olong = this.droppedChunksSet.iterator().next();
                    final Chunk chunk = this.id2ChunkMap.getValueByKey(olong);
                    if (chunk != null) {
                        chunk.onChunkUnload();
                        this.saveChunkData(chunk);
                        this.saveChunkExtraData(chunk);
                        this.id2ChunkMap.remove(olong);
                        this.loadedChunks.remove(chunk);
                    }
                    this.droppedChunksSet.remove(olong);
                }
            }
            if (this.chunkLoader != null) {
                this.chunkLoader.chunkTick();
            }
        }
        return this.serverChunkGenerator.unloadQueuedChunks();
    }
    
    @Override
    public boolean canSave() {
        return !this.worldObj.disableLevelSaving;
    }
    
    @Override
    public String makeString() {
        return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
    }
    
    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return this.serverChunkGenerator.getPossibleCreatures(creatureType, pos);
    }
    
    @Override
    public BlockPos getStrongholdGen(final World worldIn, final String structureName, final BlockPos position) {
        return this.serverChunkGenerator.getStrongholdGen(worldIn, structureName, position);
    }
    
    @Override
    public int getLoadedChunkCount() {
        return this.id2ChunkMap.getNumHashElements();
    }
    
    @Override
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
    }
    
    @Override
    public Chunk provideChunk(final BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}
