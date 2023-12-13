package net.minecraft.world.chunk.storage;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilChunkLoader implements IThreadedFileIO, IChunkLoader {
   private final File chunkSaveLocation;
   private Set pendingAnvilChunksCoordinates = Collections.newSetFromMap(new ConcurrentHashMap());
   private static final Logger logger = LogManager.getLogger();
   private boolean field_183014_e = false;
   private Map chunksToRemove = new ConcurrentHashMap();

   private Chunk readChunkFromNBT(World var1, NBTTagCompound var2) {
      int var3 = var2.getInteger("xPos");
      int var4 = var2.getInteger("zPos");
      Chunk var5 = new Chunk(var1, var3, var4);
      var5.setHeightMap(var2.getIntArray("HeightMap"));
      var5.setTerrainPopulated(var2.getBoolean("TerrainPopulated"));
      var5.setLightPopulated(var2.getBoolean("LightPopulated"));
      var5.setInhabitedTime(var2.getLong("InhabitedTime"));
      NBTTagList var6 = var2.getTagList("Sections", 10);
      byte var7 = 16;
      ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];
      boolean var9 = !var1.provider.getHasNoSky();

      for(int var10 = 0; var10 < var6.tagCount(); ++var10) {
         NBTTagCompound var11 = var6.getCompoundTagAt(var10);
         byte var12 = var11.getByte("Y");
         ExtendedBlockStorage var13 = new ExtendedBlockStorage(var12 << 4, var9);
         byte[] var14 = var11.getByteArray("Blocks");
         NibbleArray var15 = new NibbleArray(var11.getByteArray("Data"));
         NibbleArray var16 = var11.hasKey("Add", 7) ? new NibbleArray(var11.getByteArray("Add")) : null;
         char[] var17 = new char[var14.length];

         for(int var18 = 0; var18 < var17.length; ++var18) {
            int var19 = var18 & 15;
            int var20 = var18 >> 8 & 15;
            int var21 = var18 >> 4 & 15;
            int var22 = var16 != null ? var16.get(var19, var20, var21) : 0;
            var17[var18] = (char)(var22 << 12 | (var14[var18] & 255) << 4 | var15.get(var19, var20, var21));
         }

         var13.setData(var17);
         var13.setBlocklightArray(new NibbleArray(var11.getByteArray("BlockLight")));
         if (var9) {
            var13.setSkylightArray(new NibbleArray(var11.getByteArray("SkyLight")));
         }

         var13.removeInvalidBlocks();
         var8[var12] = var13;
      }

      var5.setStorageArrays(var8);
      if (var2.hasKey("Biomes", 7)) {
         var5.setBiomeArray(var2.getByteArray("Biomes"));
      }

      NBTTagList var24 = var2.getTagList("Entities", 10);
      if (var24 != null) {
         for(int var25 = 0; var25 < var24.tagCount(); ++var25) {
            NBTTagCompound var27 = var24.getCompoundTagAt(var25);
            Entity var29 = EntityList.createEntityFromNBT(var27, var1);
            var5.setHasEntities(true);
            if (var29 != null) {
               var5.addEntity(var29);
               Entity var33 = var29;

               for(NBTTagCompound var35 = var27; var35.hasKey("Riding", 10); var35 = var35.getCompoundTag("Riding")) {
                  Entity var38 = EntityList.createEntityFromNBT(var35.getCompoundTag("Riding"), var1);
                  if (var38 != null) {
                     var5.addEntity(var38);
                     var33.mountEntity(var38);
                  }

                  var33 = var38;
               }
            }
         }
      }

      NBTTagList var26 = var2.getTagList("TileEntities", 10);
      if (var26 != null) {
         for(int var28 = 0; var28 < var26.tagCount(); ++var28) {
            NBTTagCompound var31 = var26.getCompoundTagAt(var28);
            TileEntity var34 = TileEntity.createAndLoadEntity(var31);
            if (var34 != null) {
               var5.addTileEntity(var34);
            }
         }
      }

      if (var2.hasKey("TileTicks", 9)) {
         NBTTagList var30 = var2.getTagList("TileTicks", 10);
         if (var30 != null) {
            for(int var32 = 0; var32 < var30.tagCount(); ++var32) {
               NBTTagCompound var36 = var30.getCompoundTagAt(var32);
               Block var37;
               if (var36.hasKey("i", 8)) {
                  var37 = Block.getBlockFromName(var36.getString("i"));
               } else {
                  var37 = Block.getBlockById(var36.getInteger("i"));
               }

               var1.scheduleBlockUpdate(new BlockPos(var36.getInteger("x"), var36.getInteger("y"), var36.getInteger("z")), var37, var36.getInteger("t"), var36.getInteger("p"));
            }
         }
      }

      return var5;
   }

   protected Chunk checkedReadChunkFromNBT(World var1, int var2, int var3, NBTTagCompound var4) {
      if (!var4.hasKey("Level", 10)) {
         logger.error("Chunk file at " + var2 + "," + var3 + " is missing level data, skipping");
         return null;
      } else {
         NBTTagCompound var5 = var4.getCompoundTag("Level");
         if (!var5.hasKey("Sections", 9)) {
            logger.error("Chunk file at " + var2 + "," + var3 + " is missing block data, skipping");
            return null;
         } else {
            Chunk var6 = this.readChunkFromNBT(var1, var5);
            if (!var6.isAtLocation(var2, var3)) {
               logger.error("Chunk file at " + var2 + "," + var3 + " is in the wrong location; relocating. (Expected " + var2 + ", " + var3 + ", got " + var6.xPosition + ", " + var6.zPosition + ")");
               var5.setInteger("xPos", var2);
               var5.setInteger("zPos", var3);
               var6 = this.readChunkFromNBT(var1, var5);
            }

            return var6;
         }
      }
   }

   public void saveExtraChunkData(World var1, Chunk var2) throws IOException {
   }

   private void func_183013_b(ChunkCoordIntPair var1, NBTTagCompound var2) throws IOException {
      DataOutputStream var3 = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, var1.chunkXPos, var1.chunkZPos);
      CompressedStreamTools.write(var2, (DataOutput)var3);
      var3.close();
   }

   public void saveChunk(World var1, Chunk var2) throws MinecraftException, IOException {
      var1.checkSessionLock();

      try {
         NBTTagCompound var3 = new NBTTagCompound();
         NBTTagCompound var4 = new NBTTagCompound();
         var3.setTag("Level", var4);
         this.writeChunkToNBT(var2, var1, var4);
         this.addChunkToPending(var2.getChunkCoordIntPair(), var3);
      } catch (Exception var5) {
         logger.error((String)"Failed to save chunk", (Throwable)var5);
      }

   }

   public void chunkTick() {
   }

   protected void addChunkToPending(ChunkCoordIntPair var1, NBTTagCompound var2) {
      if (!this.pendingAnvilChunksCoordinates.contains(var1)) {
         this.chunksToRemove.put(var1, var2);
      }

      ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
   }

   public AnvilChunkLoader(File var1) {
      this.chunkSaveLocation = var1;
   }

   public void saveExtraData() {
      this.field_183014_e = true;

      while(this == false) {
      }

   }

   public Chunk loadChunk(World var1, int var2, int var3) throws IOException {
      ChunkCoordIntPair var4 = new ChunkCoordIntPair(var2, var3);
      NBTTagCompound var5 = (NBTTagCompound)this.chunksToRemove.get(var4);
      if (var5 == null) {
         DataInputStream var6 = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, var2, var3);
         if (var6 == null) {
            return null;
         }

         var5 = CompressedStreamTools.read(var6);
      }

      return this.checkedReadChunkFromNBT(var1, var2, var3, var5);
   }

   private void writeChunkToNBT(Chunk var1, World var2, NBTTagCompound var3) {
      var3.setByte("V", (byte)1);
      var3.setInteger("xPos", var1.xPosition);
      var3.setInteger("zPos", var1.zPosition);
      var3.setLong("LastUpdate", var2.getTotalWorldTime());
      var3.setIntArray("HeightMap", var1.getHeightMap());
      var3.setBoolean("TerrainPopulated", var1.isTerrainPopulated());
      var3.setBoolean("LightPopulated", var1.isLightPopulated());
      var3.setLong("InhabitedTime", var1.getInhabitedTime());
      ExtendedBlockStorage[] var4 = var1.getBlockStorageArray();
      NBTTagList var5 = new NBTTagList();
      boolean var6 = !var2.provider.getHasNoSky();
      ExtendedBlockStorage[] var10 = var4;
      int var9 = var4.length;

      int var8;
      NBTTagCompound var11;
      for(var8 = 0; var8 < var9; ++var8) {
         ExtendedBlockStorage var7 = var10[var8];
         if (var7 != null) {
            var11 = new NBTTagCompound();
            var11.setByte("Y", (byte)(var7.getYLocation() >> 4 & 255));
            byte[] var12 = new byte[var7.getData().length];
            NibbleArray var13 = new NibbleArray();
            NibbleArray var14 = null;

            for(int var15 = 0; var15 < var7.getData().length; ++var15) {
               char var16 = var7.getData()[var15];
               int var17 = var15 & 15;
               int var18 = var15 >> 8 & 15;
               int var19 = var15 >> 4 & 15;
               if (var16 >> 12 != 0) {
                  if (var14 == null) {
                     var14 = new NibbleArray();
                  }

                  var14.set(var17, var18, var19, var16 >> 12);
               }

               var12[var15] = (byte)(var16 >> 4 & 255);
               var13.set(var17, var18, var19, var16 & 15);
            }

            var11.setByteArray("Blocks", var12);
            var11.setByteArray("Data", var13.getData());
            if (var14 != null) {
               var11.setByteArray("Add", var14.getData());
            }

            var11.setByteArray("BlockLight", var7.getBlocklightArray().getData());
            if (var6) {
               var11.setByteArray("SkyLight", var7.getSkylightArray().getData());
            } else {
               var11.setByteArray("SkyLight", new byte[var7.getBlocklightArray().getData().length]);
            }

            var5.appendTag(var11);
         }
      }

      var3.setTag("Sections", var5);
      var3.setByteArray("Biomes", var1.getBiomeArray());
      var1.setHasEntities(false);
      NBTTagList var20 = new NBTTagList();

      Iterator var25;
      for(var8 = 0; var8 < var1.getEntityLists().length; ++var8) {
         var25 = var1.getEntityLists()[var8].iterator();

         while(var25.hasNext()) {
            Entity var22 = (Entity)var25.next();
            var11 = new NBTTagCompound();
            if (var22.writeToNBTOptional(var11)) {
               var1.setHasEntities(true);
               var20.appendTag(var11);
            }
         }
      }

      var3.setTag("Entities", var20);
      NBTTagList var21 = new NBTTagList();
      var25 = var1.getTileEntityMap().values().iterator();

      while(var25.hasNext()) {
         TileEntity var23 = (TileEntity)var25.next();
         var11 = new NBTTagCompound();
         var23.writeToNBT(var11);
         var21.appendTag(var11);
      }

      var3.setTag("TileEntities", var21);
      List var24 = var2.getPendingBlockUpdates(var1, false);
      if (var24 != null) {
         long var26 = var2.getTotalWorldTime();
         NBTTagList var27 = new NBTTagList();
         Iterator var29 = var24.iterator();

         while(var29.hasNext()) {
            NextTickListEntry var28 = (NextTickListEntry)var29.next();
            NBTTagCompound var30 = new NBTTagCompound();
            ResourceLocation var31 = (ResourceLocation)Block.blockRegistry.getNameForObject(var28.getBlock());
            var30.setString("i", var31 == null ? "" : var31.toString());
            var30.setInteger("x", var28.position.getX());
            var30.setInteger("y", var28.position.getY());
            var30.setInteger("z", var28.position.getZ());
            var30.setInteger("t", (int)(var28.scheduledTime - var26));
            var30.setInteger("p", var28.priority);
            var27.appendTag(var30);
         }

         var3.setTag("TileTicks", var27);
      }

   }
}
