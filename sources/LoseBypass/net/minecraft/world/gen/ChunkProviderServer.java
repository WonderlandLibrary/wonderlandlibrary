/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer
implements IChunkProvider {
    private static final Logger logger = LogManager.getLogger();
    private Set<Long> droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap());
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;
    public boolean chunkLoadOverride = true;
    private LongHashMap<Chunk> id2ChunkMap = new LongHashMap();
    private List<Chunk> loadedChunks = Lists.newArrayList();
    private WorldServer worldObj;

    public ChunkProviderServer(WorldServer p_i1520_1_, IChunkLoader p_i1520_2_, IChunkProvider p_i1520_3_) {
        this.dummyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
        this.worldObj = p_i1520_1_;
        this.chunkLoader = p_i1520_2_;
        this.serverChunkGenerator = p_i1520_3_;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(x, z));
    }

    public List<Chunk> func_152380_a() {
        return this.loadedChunks;
    }

    public void dropChunk(int p_73241_1_, int p_73241_2_) {
        if (this.worldObj.provider.canRespawnHere()) {
            if (this.worldObj.isSpawnChunk(p_73241_1_, p_73241_2_)) return;
            this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
            return;
        }
        this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
    }

    public void unloadAllChunks() {
        Iterator<Chunk> iterator = this.loadedChunks.iterator();
        while (iterator.hasNext()) {
            Chunk chunk = iterator.next();
            this.dropChunk(chunk.xPosition, chunk.zPosition);
        }
    }

    public Chunk loadChunk(int p_73158_1_, int p_73158_2_) {
        long i = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
        this.droppedChunksSet.remove(i);
        Chunk chunk = this.id2ChunkMap.getValueByKey(i);
        if (chunk != null) return chunk;
        chunk = this.loadChunkFromFile(p_73158_1_, p_73158_2_);
        if (chunk == null) {
            if (this.serverChunkGenerator == null) {
                chunk = this.dummyChunk;
            } else {
                try {
                    chunk = this.serverChunkGenerator.provideChunk(p_73158_1_, p_73158_2_);
                }
                catch (Throwable throwable) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                    crashreportcategory.addCrashSection("Location", String.format("%d,%d", p_73158_1_, p_73158_2_));
                    crashreportcategory.addCrashSection("Position hash", i);
                    crashreportcategory.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                    throw new ReportedException(crashreport);
                }
            }
        }
        this.id2ChunkMap.add(i, chunk);
        this.loadedChunks.add(chunk);
        chunk.onChunkLoad();
        chunk.populateChunk(this, this, p_73158_1_, p_73158_2_);
        return chunk;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        Chunk chunk;
        Chunk chunk2 = this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(x, z));
        if (chunk2 != null) {
            chunk = chunk2;
            return chunk;
        }
        if (!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride) {
            chunk = this.dummyChunk;
            return chunk;
        }
        chunk = this.loadChunk(x, z);
        return chunk;
    }

    private Chunk loadChunkFromFile(int x, int z) {
        if (this.chunkLoader == null) {
            return null;
        }
        try {
            Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, x, z);
            if (chunk == null) return chunk;
            chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
            if (this.serverChunkGenerator == null) return chunk;
            this.serverChunkGenerator.recreateStructures(chunk, x, z);
            return chunk;
        }
        catch (Exception exception) {
            logger.error("Couldn't load chunk", (Throwable)exception);
            return null;
        }
    }

    private void saveChunkExtraData(Chunk p_73243_1_) {
        if (this.chunkLoader == null) return;
        try {
            this.chunkLoader.saveExtraChunkData(this.worldObj, p_73243_1_);
            return;
        }
        catch (Exception exception) {
            logger.error("Couldn't save entities", (Throwable)exception);
        }
    }

    private void saveChunkData(Chunk p_73242_1_) {
        if (this.chunkLoader == null) return;
        try {
            p_73242_1_.setLastSaveTime(this.worldObj.getTotalWorldTime());
            this.chunkLoader.saveChunk(this.worldObj, p_73242_1_);
            return;
        }
        catch (IOException ioexception) {
            logger.error("Couldn't save chunk", (Throwable)ioexception);
            return;
        }
        catch (MinecraftException minecraftexception) {
            logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)minecraftexception);
        }
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        Chunk chunk = this.provideChunk(p_73153_2_, p_73153_3_);
        if (chunk.isTerrainPopulated()) return;
        chunk.func_150809_p();
        if (this.serverChunkGenerator == null) return;
        this.serverChunkGenerator.populate(p_73153_1_, p_73153_2_, p_73153_3_);
        chunk.setChunkModified();
    }

    @Override
    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        if (this.serverChunkGenerator == null) return false;
        if (!this.serverChunkGenerator.func_177460_a(p_177460_1_, p_177460_2_, p_177460_3_, p_177460_4_)) return false;
        Chunk chunk = this.provideChunk(p_177460_3_, p_177460_4_);
        chunk.setChunkModified();
        return true;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback) {
        int i = 0;
        ArrayList<Chunk> list = Lists.newArrayList(this.loadedChunks);
        int j = 0;
        while (j < list.size()) {
            Chunk chunk = (Chunk)list.get(j);
            if (p_73151_1_) {
                this.saveChunkExtraData(chunk);
            }
            if (chunk.needsSaving(p_73151_1_)) {
                this.saveChunkData(chunk);
                chunk.setModified(false);
                if (++i == 24 && !p_73151_1_) {
                    return false;
                }
            }
            ++j;
        }
        return true;
    }

    @Override
    public void saveExtraData() {
        if (this.chunkLoader == null) return;
        this.chunkLoader.saveExtraData();
    }

    @Override
    public boolean unloadQueuedChunks() {
        if (this.worldObj.disableLevelSaving) return this.serverChunkGenerator.unloadQueuedChunks();
        int i = 0;
        while (true) {
            if (i >= 100) {
                if (this.chunkLoader == null) return this.serverChunkGenerator.unloadQueuedChunks();
                this.chunkLoader.chunkTick();
                return this.serverChunkGenerator.unloadQueuedChunks();
            }
            if (!this.droppedChunksSet.isEmpty()) {
                Long olong = this.droppedChunksSet.iterator().next();
                Chunk chunk = this.id2ChunkMap.getValueByKey(olong);
                if (chunk != null) {
                    chunk.onChunkUnload();
                    this.saveChunkData(chunk);
                    this.saveChunkExtraData(chunk);
                    this.id2ChunkMap.remove(olong);
                    this.loadedChunks.remove(chunk);
                }
                this.droppedChunksSet.remove(olong);
            }
            ++i;
        }
    }

    @Override
    public boolean canSave() {
        if (this.worldObj.disableLevelSaving) return false;
        return true;
    }

    @Override
    public String makeString() {
        return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.serverChunkGenerator.getPossibleCreatures(creatureType, pos);
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return this.serverChunkGenerator.getStrongholdGen(worldIn, structureName, position);
    }

    @Override
    public int getLoadedChunkCount() {
        return this.id2ChunkMap.getNumHashElements();
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
    }

    @Override
    public Chunk provideChunk(BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}

