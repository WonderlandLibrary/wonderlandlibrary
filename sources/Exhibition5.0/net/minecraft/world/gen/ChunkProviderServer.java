// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import org.apache.logging.log4j.LogManager;
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
    private Set droppedChunksSet;
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;
    public boolean chunkLoadOverride;
    private LongHashMap id2ChunkMap;
    private List loadedChunks;
    private WorldServer worldObj;
    private static final String __OBFID = "CL_00001436";
    
    public ChunkProviderServer(final WorldServer p_i1520_1_, final IChunkLoader p_i1520_2_, final IChunkProvider p_i1520_3_) {
        this.droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap<Object, Boolean>());
        this.chunkLoadOverride = true;
        this.id2ChunkMap = new LongHashMap();
        this.loadedChunks = Lists.newArrayList();
        this.dummyChunk = new EmptyChunk(p_i1520_1_, 0, 0);
        this.worldObj = p_i1520_1_;
        this.chunkLoader = p_i1520_2_;
        this.serverChunkGenerator = p_i1520_3_;
    }
    
    @Override
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_73149_1_, p_73149_2_));
    }
    
    public List func_152380_a() {
        return this.loadedChunks;
    }
    
    public void dropChunk(final int p_73241_1_, final int p_73241_2_) {
        if (this.worldObj.provider.canRespawnHere()) {
            if (!this.worldObj.chunkExists(p_73241_1_, p_73241_2_)) {
                this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
            }
        }
        else {
            this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(p_73241_1_, p_73241_2_));
        }
    }
    
    public void unloadAllChunks() {
        for (final Chunk var2 : this.loadedChunks) {
            this.dropChunk(var2.xPosition, var2.zPosition);
        }
    }
    
    public Chunk loadChunk(final int p_73158_1_, final int p_73158_2_) {
        final long var3 = ChunkCoordIntPair.chunkXZ2Int(p_73158_1_, p_73158_2_);
        this.droppedChunksSet.remove(var3);
        Chunk var4 = (Chunk)this.id2ChunkMap.getValueByKey(var3);
        if (var4 == null) {
            var4 = this.loadChunkFromFile(p_73158_1_, p_73158_2_);
            if (var4 == null) {
                if (this.serverChunkGenerator == null) {
                    var4 = this.dummyChunk;
                }
                else {
                    try {
                        var4 = this.serverChunkGenerator.provideChunk(p_73158_1_, p_73158_2_);
                    }
                    catch (Throwable var6) {
                        final CrashReport var5 = CrashReport.makeCrashReport(var6, "Exception generating new chunk");
                        final CrashReportCategory var7 = var5.makeCategory("Chunk to be generated");
                        var7.addCrashSection("Location", String.format("%d,%d", p_73158_1_, p_73158_2_));
                        var7.addCrashSection("Position hash", var3);
                        var7.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                        throw new ReportedException(var5);
                    }
                }
            }
            this.id2ChunkMap.add(var3, var4);
            this.loadedChunks.add(var4);
            var4.onChunkLoad();
            var4.populateChunk(this, this, p_73158_1_, p_73158_2_);
        }
        return var4;
    }
    
    @Override
    public Chunk provideChunk(final int p_73154_1_, final int p_73154_2_) {
        final Chunk var3 = (Chunk)this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(p_73154_1_, p_73154_2_));
        return (var3 == null) ? ((!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride) ? this.dummyChunk : this.loadChunk(p_73154_1_, p_73154_2_)) : var3;
    }
    
    private Chunk loadChunkFromFile(final int p_73239_1_, final int p_73239_2_) {
        if (this.chunkLoader == null) {
            return null;
        }
        try {
            final Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, p_73239_1_, p_73239_2_);
            if (var3 != null) {
                var3.setLastSaveTime(this.worldObj.getTotalWorldTime());
                if (this.serverChunkGenerator != null) {
                    this.serverChunkGenerator.func_180514_a(var3, p_73239_1_, p_73239_2_);
                }
            }
            return var3;
        }
        catch (Exception var4) {
            ChunkProviderServer.logger.error("Couldn't load chunk", (Throwable)var4);
            return null;
        }
    }
    
    private void saveChunkExtraData(final Chunk p_73243_1_) {
        if (this.chunkLoader != null) {
            try {
                this.chunkLoader.saveExtraChunkData(this.worldObj, p_73243_1_);
            }
            catch (Exception var3) {
                ChunkProviderServer.logger.error("Couldn't save entities", (Throwable)var3);
            }
        }
    }
    
    private void saveChunkData(final Chunk p_73242_1_) {
        if (this.chunkLoader != null) {
            try {
                p_73242_1_.setLastSaveTime(this.worldObj.getTotalWorldTime());
                this.chunkLoader.saveChunk(this.worldObj, p_73242_1_);
            }
            catch (IOException var3) {
                ChunkProviderServer.logger.error("Couldn't save chunk", (Throwable)var3);
            }
            catch (MinecraftException var4) {
                ChunkProviderServer.logger.error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)var4);
            }
        }
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        final Chunk var4 = this.provideChunk(p_73153_2_, p_73153_3_);
        if (!var4.isTerrainPopulated()) {
            var4.func_150809_p();
            if (this.serverChunkGenerator != null) {
                this.serverChunkGenerator.populate(p_73153_1_, p_73153_2_, p_73153_3_);
                var4.setChunkModified();
            }
        }
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        if (this.serverChunkGenerator != null && this.serverChunkGenerator.func_177460_a(p_177460_1_, p_177460_2_, p_177460_3_, p_177460_4_)) {
            final Chunk var5 = this.provideChunk(p_177460_3_, p_177460_4_);
            var5.setChunkModified();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate p_73151_2_) {
        int var3 = 0;
        for (int var4 = 0; var4 < this.loadedChunks.size(); ++var4) {
            final Chunk var5 = this.loadedChunks.get(var4);
            if (p_73151_1_) {
                this.saveChunkExtraData(var5);
            }
            if (var5.needsSaving(p_73151_1_)) {
                this.saveChunkData(var5);
                var5.setModified(false);
                if (++var3 == 24 && !p_73151_1_) {
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
            for (int var1 = 0; var1 < 100; ++var1) {
                if (!this.droppedChunksSet.isEmpty()) {
                    final Long var2 = this.droppedChunksSet.iterator().next();
                    final Chunk var3 = (Chunk)this.id2ChunkMap.getValueByKey(var2);
                    if (var3 != null) {
                        var3.onChunkUnload();
                        this.saveChunkData(var3);
                        this.saveChunkExtraData(var3);
                        this.id2ChunkMap.remove(var2);
                        this.loadedChunks.remove(var3);
                    }
                    this.droppedChunksSet.remove(var2);
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
    public List func_177458_a(final EnumCreatureType p_177458_1_, final BlockPos p_177458_2_) {
        return this.serverChunkGenerator.func_177458_a(p_177458_1_, p_177458_2_);
    }
    
    @Override
    public BlockPos func_180513_a(final World worldIn, final String p_180513_2_, final BlockPos p_180513_3_) {
        return this.serverChunkGenerator.func_180513_a(worldIn, p_180513_2_, p_180513_3_);
    }
    
    @Override
    public int getLoadedChunkCount() {
        return this.id2ChunkMap.getNumHashElements();
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
