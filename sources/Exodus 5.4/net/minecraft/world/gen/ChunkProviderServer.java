/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private Set<Long> droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap());
    private IChunkLoader chunkLoader;
    private WorldServer worldObj;
    public boolean chunkLoadOverride = true;
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private LongHashMap<Chunk> id2ChunkMap = new LongHashMap();
    private List<Chunk> loadedChunks = Lists.newArrayList();
    private static final Logger logger = LogManager.getLogger();

    public ChunkProviderServer(WorldServer worldServer, IChunkLoader iChunkLoader, IChunkProvider iChunkProvider) {
        this.dummyChunk = new EmptyChunk(worldServer, 0, 0);
        this.worldObj = worldServer;
        this.chunkLoader = iChunkLoader;
        this.serverChunkGenerator = iChunkProvider;
    }

    @Override
    public boolean canSave() {
        return !this.worldObj.disableLevelSaving;
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
            int n = 0;
            while (n < 100) {
                if (!this.droppedChunksSet.isEmpty()) {
                    Long l = this.droppedChunksSet.iterator().next();
                    Chunk chunk = this.id2ChunkMap.getValueByKey(l);
                    if (chunk != null) {
                        chunk.onChunkUnload();
                        this.saveChunkData(chunk);
                        this.saveChunkExtraData(chunk);
                        this.id2ChunkMap.remove(l);
                        this.loadedChunks.remove(chunk);
                    }
                    this.droppedChunksSet.remove(l);
                }
                ++n;
            }
            if (this.chunkLoader != null) {
                this.chunkLoader.chunkTick();
            }
        }
        return this.serverChunkGenerator.unloadQueuedChunks();
    }

    @Override
    public boolean func_177460_a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
        if (this.serverChunkGenerator != null && this.serverChunkGenerator.func_177460_a(iChunkProvider, chunk, n, n2)) {
            Chunk chunk2 = this.provideChunk(n, n2);
            chunk2.setChunkModified();
            return true;
        }
        return false;
    }

    @Override
    public Chunk provideChunk(int n, int n2) {
        Chunk chunk = this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(n, n2));
        return chunk == null ? (!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride ? this.dummyChunk : this.loadChunk(n, n2)) : chunk;
    }

    @Override
    public String makeString() {
        return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
    }

    @Override
    public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
        int n = 0;
        ArrayList arrayList = Lists.newArrayList(this.loadedChunks);
        int n2 = 0;
        while (n2 < arrayList.size()) {
            Chunk chunk = (Chunk)arrayList.get(n2);
            if (bl) {
                this.saveChunkExtraData(chunk);
            }
            if (chunk.needsSaving(bl)) {
                this.saveChunkData(chunk);
                chunk.setModified(false);
                if (++n == 24 && !bl) {
                    return false;
                }
            }
            ++n2;
        }
        return true;
    }

    private void saveChunkData(Chunk chunk) {
        if (this.chunkLoader != null) {
            try {
                chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
                this.chunkLoader.saveChunk(this.worldObj, chunk);
            }
            catch (IOException iOException) {
                logger.error("Couldn't save chunk", (Throwable)iOException);
            }
            catch (MinecraftException minecraftException) {
                logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)minecraftException);
            }
        }
    }

    private Chunk loadChunkFromFile(int n, int n2) {
        if (this.chunkLoader == null) {
            return null;
        }
        try {
            Chunk chunk = this.chunkLoader.loadChunk(this.worldObj, n, n2);
            if (chunk != null) {
                chunk.setLastSaveTime(this.worldObj.getTotalWorldTime());
                if (this.serverChunkGenerator != null) {
                    this.serverChunkGenerator.recreateStructures(chunk, n, n2);
                }
            }
            return chunk;
        }
        catch (Exception exception) {
            logger.error("Couldn't load chunk", (Throwable)exception);
            return null;
        }
    }

    @Override
    public Chunk provideChunk(BlockPos blockPos) {
        return this.provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    private void saveChunkExtraData(Chunk chunk) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.worldObj, chunk);
            }
            catch (Exception exception) {
                logger.error("Couldn't save entities", (Throwable)exception);
            }
        }
    }

    @Override
    public boolean chunkExists(int n, int n2) {
        return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(n, n2));
    }

    public Chunk loadChunk(int n, int n2) {
        long l = ChunkCoordIntPair.chunkXZ2Int(n, n2);
        this.droppedChunksSet.remove(l);
        Chunk chunk = this.id2ChunkMap.getValueByKey(l);
        if (chunk == null) {
            chunk = this.loadChunkFromFile(n, n2);
            if (chunk == null) {
                if (this.serverChunkGenerator == null) {
                    chunk = this.dummyChunk;
                } else {
                    try {
                        chunk = this.serverChunkGenerator.provideChunk(n, n2);
                    }
                    catch (Throwable throwable) {
                        CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                        CrashReportCategory crashReportCategory = crashReport.makeCategory("Chunk to be generated");
                        crashReportCategory.addCrashSection("Location", String.format("%d,%d", n, n2));
                        crashReportCategory.addCrashSection("Position hash", l);
                        crashReportCategory.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                        throw new ReportedException(crashReport);
                    }
                }
            }
            this.id2ChunkMap.add(l, chunk);
            this.loadedChunks.add(chunk);
            chunk.onChunkLoad();
            chunk.populateChunk(this, this, n, n2);
        }
        return chunk;
    }

    @Override
    public void recreateStructures(Chunk chunk, int n, int n2) {
    }

    @Override
    public int getLoadedChunkCount() {
        return this.id2ChunkMap.getNumHashElements();
    }

    public void dropChunk(int n, int n2) {
        if (this.worldObj.provider.canRespawnHere()) {
            if (!this.worldObj.isSpawnChunk(n, n2)) {
                this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(n, n2));
            }
        } else {
            this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(n, n2));
        }
    }

    @Override
    public BlockPos getStrongholdGen(World world, String string, BlockPos blockPos) {
        return this.serverChunkGenerator.getStrongholdGen(world, string, blockPos);
    }

    public void unloadAllChunks() {
        for (Chunk chunk : this.loadedChunks) {
            this.dropChunk(chunk.xPosition, chunk.zPosition);
        }
    }

    public List<Chunk> func_152380_a() {
        return this.loadedChunks;
    }

    @Override
    public void populate(IChunkProvider iChunkProvider, int n, int n2) {
        Chunk chunk = this.provideChunk(n, n2);
        if (!chunk.isTerrainPopulated()) {
            chunk.func_150809_p();
            if (this.serverChunkGenerator != null) {
                this.serverChunkGenerator.populate(iChunkProvider, n, n2);
                chunk.setChunkModified();
            }
        }
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos) {
        return this.serverChunkGenerator.getPossibleCreatures(enumCreatureType, blockPos);
    }
}

