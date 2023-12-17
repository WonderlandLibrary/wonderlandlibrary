// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.MinecraftException;
import java.io.IOException;
import net.minecraft.world.World;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.util.math.ChunkPos;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.google.common.collect.Sets;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.world.chunk.storage.IChunkLoader;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderServer implements IChunkProvider
{
    private static final Logger LOGGER;
    private final Set<Long> droppedChunks;
    private final IChunkGenerator chunkGenerator;
    private final IChunkLoader chunkLoader;
    private final Long2ObjectMap<Chunk> loadedChunks;
    private final WorldServer world;
    
    public ChunkProviderServer(final WorldServer worldObjIn, final IChunkLoader chunkLoaderIn, final IChunkGenerator chunkGeneratorIn) {
        this.droppedChunks = (Set<Long>)Sets.newHashSet();
        this.loadedChunks = (Long2ObjectMap<Chunk>)new Long2ObjectOpenHashMap(8192);
        this.world = worldObjIn;
        this.chunkLoader = chunkLoaderIn;
        this.chunkGenerator = chunkGeneratorIn;
    }
    
    public Collection<Chunk> getLoadedChunks() {
        return (Collection<Chunk>)this.loadedChunks.values();
    }
    
    public void queueUnload(final Chunk chunkIn) {
        if (this.world.provider.canDropChunk(chunkIn.x, chunkIn.z)) {
            this.droppedChunks.add(ChunkPos.asLong(chunkIn.x, chunkIn.z));
            chunkIn.unloadQueued = true;
        }
    }
    
    public void queueUnloadAll() {
        for (final Chunk chunk : this.loadedChunks.values()) {
            this.queueUnload(chunk);
        }
    }
    
    @Nullable
    @Override
    public Chunk getLoadedChunk(final int x, final int z) {
        final long i = ChunkPos.asLong(x, z);
        final Chunk chunk = (Chunk)this.loadedChunks.get(i);
        if (chunk != null) {
            chunk.unloadQueued = false;
        }
        return chunk;
    }
    
    @Nullable
    public Chunk loadChunk(final int x, final int z) {
        Chunk chunk = this.getLoadedChunk(x, z);
        if (chunk == null) {
            chunk = this.loadChunkFromFile(x, z);
            if (chunk != null) {
                this.loadedChunks.put(ChunkPos.asLong(x, z), (Object)chunk);
                chunk.onLoad();
                chunk.populate(this, this.chunkGenerator);
            }
        }
        return chunk;
    }
    
    @Override
    public Chunk provideChunk(final int x, final int z) {
        Chunk chunk = this.loadChunk(x, z);
        if (chunk == null) {
            final long i = ChunkPos.asLong(x, z);
            try {
                chunk = this.chunkGenerator.generateChunk(x, z);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addCrashSection("Location", String.format("%d,%d", x, z));
                crashreportcategory.addCrashSection("Position hash", i);
                crashreportcategory.addCrashSection("Generator", this.chunkGenerator);
                throw new ReportedException(crashreport);
            }
            this.loadedChunks.put(i, (Object)chunk);
            chunk.onLoad();
            chunk.populate(this, this.chunkGenerator);
        }
        return chunk;
    }
    
    @Nullable
    private Chunk loadChunkFromFile(final int x, final int z) {
        try {
            final Chunk chunk = this.chunkLoader.loadChunk(this.world, x, z);
            if (chunk != null) {
                chunk.setLastSaveTime(this.world.getTotalWorldTime());
                this.chunkGenerator.recreateStructures(chunk, x, z);
            }
            return chunk;
        }
        catch (Exception exception) {
            ChunkProviderServer.LOGGER.error("Couldn't load chunk", (Throwable)exception);
            return null;
        }
    }
    
    private void saveChunkExtraData(final Chunk chunkIn) {
        try {
            this.chunkLoader.saveExtraChunkData(this.world, chunkIn);
        }
        catch (Exception exception) {
            ChunkProviderServer.LOGGER.error("Couldn't save entities", (Throwable)exception);
        }
    }
    
    private void saveChunkData(final Chunk chunkIn) {
        try {
            chunkIn.setLastSaveTime(this.world.getTotalWorldTime());
            this.chunkLoader.saveChunk(this.world, chunkIn);
        }
        catch (IOException ioexception) {
            ChunkProviderServer.LOGGER.error("Couldn't save chunk", (Throwable)ioexception);
        }
        catch (MinecraftException minecraftexception) {
            ChunkProviderServer.LOGGER.error("Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)minecraftexception);
        }
    }
    
    public boolean saveChunks(final boolean all) {
        int i = 0;
        final List<Chunk> list = (List<Chunk>)Lists.newArrayList((Iterable)this.loadedChunks.values());
        for (int j = 0; j < list.size(); ++j) {
            final Chunk chunk = list.get(j);
            if (all) {
                this.saveChunkExtraData(chunk);
            }
            if (chunk.needsSaving(all)) {
                this.saveChunkData(chunk);
                chunk.setModified(false);
                if (++i == 24 && !all) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void flushToDisk() {
        this.chunkLoader.flush();
    }
    
    @Override
    public boolean tick() {
        if (!this.world.disableLevelSaving) {
            if (!this.droppedChunks.isEmpty()) {
                final Iterator<Long> iterator = this.droppedChunks.iterator();
                int i = 0;
                while (i < 100 && iterator.hasNext()) {
                    final Long olong = iterator.next();
                    final Chunk chunk = (Chunk)this.loadedChunks.get((Object)olong);
                    if (chunk != null && chunk.unloadQueued) {
                        chunk.onUnload();
                        this.saveChunkData(chunk);
                        this.saveChunkExtraData(chunk);
                        this.loadedChunks.remove((Object)olong);
                        ++i;
                    }
                    iterator.remove();
                }
            }
            this.chunkLoader.chunkTick();
        }
        return false;
    }
    
    public boolean canSave() {
        return !this.world.disableLevelSaving;
    }
    
    @Override
    public String makeString() {
        return "ServerChunkCache: " + this.loadedChunks.size() + " Drop: " + this.droppedChunks.size();
    }
    
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return this.chunkGenerator.getPossibleCreatures(creatureType, pos);
    }
    
    @Nullable
    public BlockPos getNearestStructurePos(final World worldIn, final String structureName, final BlockPos position, final boolean findUnexplored) {
        return this.chunkGenerator.getNearestStructurePos(worldIn, structureName, position, findUnexplored);
    }
    
    public boolean isInsideStructure(final World worldIn, final String structureName, final BlockPos pos) {
        return this.chunkGenerator.isInsideStructure(worldIn, structureName, pos);
    }
    
    public int getLoadedChunkCount() {
        return this.loadedChunks.size();
    }
    
    public boolean chunkExists(final int x, final int z) {
        return this.loadedChunks.containsKey(ChunkPos.asLong(x, z));
    }
    
    @Override
    public boolean isChunkGeneratedAt(final int x, final int z) {
        return this.loadedChunks.containsKey(ChunkPos.asLong(x, z)) || this.chunkLoader.isChunkGeneratedAt(x, z);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
