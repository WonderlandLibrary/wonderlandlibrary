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
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChunkProviderServer implements IChunkProvider {
   private List loadedChunks = Lists.newArrayList();
   private WorldServer worldObj;
   private Chunk dummyChunk;
   public boolean chunkLoadOverride = true;
   private Set droppedChunksSet = Collections.newSetFromMap(new ConcurrentHashMap());
   private IChunkProvider serverChunkGenerator;
   private IChunkLoader chunkLoader;
   private static final Logger logger = LogManager.getLogger();
   private LongHashMap id2ChunkMap = new LongHashMap();

   public int getLoadedChunkCount() {
      return this.id2ChunkMap.getNumHashElements();
   }

   public boolean chunkExists(int var1, int var2) {
      return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
   }

   public Chunk provideChunk(int var1, int var2) {
      Chunk var3 = (Chunk)this.id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
      return var3 == null ? (!this.worldObj.isFindingSpawnPoint() && !this.chunkLoadOverride ? this.dummyChunk : this.loadChunk(var1, var2)) : var3;
   }

   public void recreateStructures(Chunk var1, int var2, int var3) {
   }

   public void populate(IChunkProvider var1, int var2, int var3) {
      Chunk var4 = this.provideChunk(var2, var3);
      if (!var4.isTerrainPopulated()) {
         var4.func_150809_p();
         if (this.serverChunkGenerator != null) {
            this.serverChunkGenerator.populate(var1, var2, var3);
            var4.setChunkModified();
         }
      }

   }

   private void saveChunkExtraData(Chunk var1) {
      if (this.chunkLoader != null) {
         try {
            this.chunkLoader.saveExtraChunkData(this.worldObj, var1);
         } catch (Exception var3) {
            logger.error((String)"Couldn't save entities", (Throwable)var3);
         }
      }

   }

   public boolean canSave() {
      return !this.worldObj.disableLevelSaving;
   }

   public Chunk loadChunk(int var1, int var2) {
      long var3 = ChunkCoordIntPair.chunkXZ2Int(var1, var2);
      this.droppedChunksSet.remove(var3);
      Chunk var5 = (Chunk)this.id2ChunkMap.getValueByKey(var3);
      if (var5 == null) {
         var5 = this.loadChunkFromFile(var1, var2);
         if (var5 == null) {
            if (this.serverChunkGenerator == null) {
               var5 = this.dummyChunk;
            } else {
               try {
                  var5 = this.serverChunkGenerator.provideChunk(var1, var2);
               } catch (Throwable var9) {
                  CrashReport var7 = CrashReport.makeCrashReport(var9, "Exception generating new chunk");
                  CrashReportCategory var8 = var7.makeCategory("Chunk to be generated");
                  var8.addCrashSection("Location", String.format("%d,%d", var1, var2));
                  var8.addCrashSection("Position hash", var3);
                  var8.addCrashSection("Generator", this.serverChunkGenerator.makeString());
                  throw new ReportedException(var7);
               }
            }
         }

         this.id2ChunkMap.add(var3, var5);
         this.loadedChunks.add(var5);
         var5.onChunkLoad();
         var5.populateChunk(this, this, var1, var2);
      }

      return var5;
   }

   public List func_152380_a() {
      return this.loadedChunks;
   }

   public boolean unloadQueuedChunks() {
      if (!this.worldObj.disableLevelSaving) {
         for(int var1 = 0; var1 < 100; ++var1) {
            if (!this.droppedChunksSet.isEmpty()) {
               Long var2 = (Long)this.droppedChunksSet.iterator().next();
               Chunk var3 = (Chunk)this.id2ChunkMap.getValueByKey(var2);
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

   public void saveExtraData() {
      if (this.chunkLoader != null) {
         this.chunkLoader.saveExtraData();
      }

   }

   public BlockPos getStrongholdGen(World var1, String var2, BlockPos var3) {
      return this.serverChunkGenerator.getStrongholdGen(var1, var2, var3);
   }

   private void saveChunkData(Chunk var1) {
      if (this.chunkLoader != null) {
         try {
            var1.setLastSaveTime(this.worldObj.getTotalWorldTime());
            this.chunkLoader.saveChunk(this.worldObj, var1);
         } catch (IOException var3) {
            logger.error((String)"Couldn't save chunk", (Throwable)var3);
         } catch (MinecraftException var4) {
            logger.error((String)"Couldn't save chunk; already in use by another instance of Minecraft?", (Throwable)var4);
         }
      }

   }

   public boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4) {
      if (this.serverChunkGenerator != null && this.serverChunkGenerator.func_177460_a(var1, var2, var3, var4)) {
         Chunk var5 = this.provideChunk(var3, var4);
         var5.setChunkModified();
         return true;
      } else {
         return false;
      }
   }

   private Chunk loadChunkFromFile(int var1, int var2) {
      if (this.chunkLoader == null) {
         return null;
      } else {
         try {
            Chunk var3 = this.chunkLoader.loadChunk(this.worldObj, var1, var2);
            if (var3 != null) {
               var3.setLastSaveTime(this.worldObj.getTotalWorldTime());
               if (this.serverChunkGenerator != null) {
                  this.serverChunkGenerator.recreateStructures(var3, var1, var2);
               }
            }

            return var3;
         } catch (Exception var4) {
            logger.error((String)"Couldn't load chunk", (Throwable)var4);
            return null;
         }
      }
   }

   public String makeString() {
      return "ServerChunkCache: " + this.id2ChunkMap.getNumHashElements() + " Drop: " + this.droppedChunksSet.size();
   }

   public List getPossibleCreatures(EnumCreatureType var1, BlockPos var2) {
      return this.serverChunkGenerator.getPossibleCreatures(var1, var2);
   }

   public Chunk provideChunk(BlockPos var1) {
      return this.provideChunk(var1.getX() >> 4, var1.getZ() >> 4);
   }

   public boolean saveChunks(boolean var1, IProgressUpdate var2) {
      int var3 = 0;
      ArrayList var4 = Lists.newArrayList((Iterable)this.loadedChunks);

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         Chunk var6 = (Chunk)var4.get(var5);
         if (var1) {
            this.saveChunkExtraData(var6);
         }

         if (var6.needsSaving(var1)) {
            this.saveChunkData(var6);
            var6.setModified(false);
            ++var3;
            if (var3 == 24 && !var1) {
               return false;
            }
         }
      }

      return true;
   }

   public void dropChunk(int var1, int var2) {
      if (this.worldObj.provider.canRespawnHere()) {
         if (!this.worldObj.isSpawnChunk(var1, var2)) {
            this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
         }
      } else {
         this.droppedChunksSet.add(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
      }

   }

   public void unloadAllChunks() {
      Iterator var2 = this.loadedChunks.iterator();

      while(var2.hasNext()) {
         Chunk var1 = (Chunk)var2.next();
         this.dropChunk(var1.xPosition, var1.zPosition);
      }

   }

   public ChunkProviderServer(WorldServer var1, IChunkLoader var2, IChunkProvider var3) {
      this.dummyChunk = new EmptyChunk(var1, 0, 0);
      this.worldObj = var1;
      this.chunkLoader = var2;
      this.serverChunkGenerator = var3;
   }
}
