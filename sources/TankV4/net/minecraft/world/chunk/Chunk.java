package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderDebug;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk {
   private static final Logger logger = LogManager.getLogger();
   private boolean hasEntities;
   private boolean field_150815_m;
   private final byte[] blockBiomeArray;
   private final int[] precipitationHeightMap;
   private final ClassInheritanceMultiMap[] entityLists;
   private final int[] heightMap;
   private boolean isModified;
   private ConcurrentLinkedQueue tileEntityPosQueue;
   private final World worldObj;
   private final boolean[] updateSkylightColumns;
   private boolean isGapLightingUpdated;
   private boolean isChunkLoaded;
   private int heightMapMinimum;
   private long lastSaveTime;
   private boolean isLightPopulated;
   private int queuedLightChecks;
   private boolean isTerrainPopulated;
   private final ExtendedBlockStorage[] storageArrays;
   public final int xPosition;
   private final Map chunkTileEntityMap;
   public final int zPosition;
   private long inhabitedTime;

   protected void generateHeightMap() {
      int var1 = this.getTopFilledSegment();
      this.heightMapMinimum = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < 16; ++var2) {
         for(int var3 = 0; var3 < 16; ++var3) {
            this.precipitationHeightMap[var2 + (var3 << 4)] = -999;

            for(int var4 = var1 + 16; var4 > 0; --var4) {
               Block var5 = this.getBlock0(var2, var4 - 1, var3);
               if (var5.getLightOpacity() != 0) {
                  this.heightMap[var3 << 4 | var2] = var4;
                  if (var4 < this.heightMapMinimum) {
                     this.heightMapMinimum = var4;
                  }
                  break;
               }
            }
         }
      }

      this.isModified = true;
   }

   public int getLightSubtracted(BlockPos var1, int var2) {
      int var3 = var1.getX() & 15;
      int var4 = var1.getY();
      int var5 = var1.getZ() & 15;
      ExtendedBlockStorage var6 = this.storageArrays[var4 >> 4];
      if (var6 != null) {
         int var7 = this.worldObj.provider.getHasNoSky() ? 0 : var6.getExtSkylightValue(var3, var4 & 15, var5);
         var7 -= var2;
         int var8 = var6.getExtBlocklightValue(var3, var4 & 15, var5);
         if (var8 > var7) {
            var7 = var8;
         }

         return var7;
      } else {
         return !this.worldObj.provider.getHasNoSky() && var2 < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - var2 : 0;
      }
   }

   public void setHeightMap(int[] var1) {
      if (this.heightMap.length != var1.length) {
         logger.warn("Could not set level chunk heightmap, array length is " + var1.length + " instead of " + this.heightMap.length);
      } else {
         for(int var2 = 0; var2 < this.heightMap.length; ++var2) {
            this.heightMap[var2] = var1[var2];
         }
      }

   }

   public Map getTileEntityMap() {
      return this.chunkTileEntityMap;
   }

   public void setModified(boolean var1) {
      this.isModified = var1;
   }

   public Random getRandomWithSeed(long var1) {
      return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ var1);
   }

   public void onChunkLoad() {
      this.isChunkLoaded = true;
      this.worldObj.addTileEntities(this.chunkTileEntityMap.values());

      for(int var1 = 0; var1 < this.entityLists.length; ++var1) {
         Iterator var3 = this.entityLists[var1].iterator();

         while(var3.hasNext()) {
            Entity var2 = (Entity)var3.next();
            var2.onChunkLoad();
         }

         this.worldObj.loadEntities(this.entityLists[var1]);
      }

   }

   public ExtendedBlockStorage[] getBlockStorageArray() {
      return this.storageArrays;
   }

   public World getWorld() {
      return this.worldObj;
   }

   public void getEntitiesWithinAABBForEntity(Entity var1, AxisAlignedBB var2, List var3, Predicate var4) {
      int var5 = MathHelper.floor_double((var2.minY - 2.0D) / 16.0D);
      int var6 = MathHelper.floor_double((var2.maxY + 2.0D) / 16.0D);
      var5 = MathHelper.clamp_int(var5, 0, this.entityLists.length - 1);
      var6 = MathHelper.clamp_int(var6, 0, this.entityLists.length - 1);

      label66:
      for(int var7 = var5; var7 <= var6; ++var7) {
         if (!this.entityLists[var7].isEmpty()) {
            Iterator var9 = this.entityLists[var7].iterator();

            while(true) {
               Entity var8;
               Entity[] var10;
               do {
                  do {
                     do {
                        if (!var9.hasNext()) {
                           continue label66;
                        }

                        var8 = (Entity)var9.next();
                     } while(!var8.getEntityBoundingBox().intersectsWith(var2));
                  } while(var8 == var1);

                  if (var4 == null || var4.apply(var8)) {
                     var3.add(var8);
                  }

                  var10 = var8.getParts();
               } while(var10 == null);

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  var8 = var10[var11];
                  if (var8 != var1 && var8.getEntityBoundingBox().intersectsWith(var2) && (var4 == null || var4.apply(var8))) {
                     var3.add(var8);
                  }
               }
            }
         }
      }

   }

   public Chunk(World var1, int var2, int var3) {
      this.storageArrays = new ExtendedBlockStorage[16];
      this.blockBiomeArray = new byte[256];
      this.precipitationHeightMap = new int[256];
      this.updateSkylightColumns = new boolean[256];
      this.chunkTileEntityMap = Maps.newHashMap();
      this.queuedLightChecks = 4096;
      this.tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
      this.entityLists = new ClassInheritanceMultiMap[16];
      this.worldObj = var1;
      this.xPosition = var2;
      this.zPosition = var3;
      this.heightMap = new int[256];

      for(int var4 = 0; var4 < this.entityLists.length; ++var4) {
         this.entityLists[var4] = new ClassInheritanceMultiMap(Entity.class);
      }

      Arrays.fill(this.precipitationHeightMap, -999);
      Arrays.fill(this.blockBiomeArray, (byte)-1);
   }

   private TileEntity createNewTileEntity(BlockPos var1) {
      Block var2 = this.getBlock(var1);
      return !var2.hasTileEntity() ? null : ((ITileEntityProvider)var2).createNewTileEntity(this.worldObj, this.getBlockMetadata(var1));
   }

   public boolean isLoaded() {
      return this.isChunkLoaded;
   }

   private void func_177441_y() {
      for(int var1 = 0; var1 < this.updateSkylightColumns.length; ++var1) {
         this.updateSkylightColumns[var1] = true;
      }

      this.recheckGaps(false);
   }

   public void generateSkylightMap() {
      int var1 = this.getTopFilledSegment();
      this.heightMapMinimum = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < 16; ++var2) {
         for(int var3 = 0; var3 < 16; ++var3) {
            this.precipitationHeightMap[var2 + (var3 << 4)] = -999;

            int var4;
            for(var4 = var1 + 16; var4 > 0; --var4) {
               if (this.getBlockLightOpacity(var2, var4 - 1, var3) != 0) {
                  this.heightMap[var3 << 4 | var2] = var4;
                  if (var4 < this.heightMapMinimum) {
                     this.heightMapMinimum = var4;
                  }
                  break;
               }
            }

            if (!this.worldObj.provider.getHasNoSky()) {
               var4 = 15;
               int var5 = var1 + 16 - 1;

               do {
                  int var6 = this.getBlockLightOpacity(var2, var5, var3);
                  if (var6 == 0 && var4 != 15) {
                     var6 = 1;
                  }

                  var4 -= var6;
                  if (var4 > 0) {
                     ExtendedBlockStorage var7 = this.storageArrays[var5 >> 4];
                     if (var7 != null) {
                        var7.setExtSkylightValue(var2, var5 & 15, var3, var4);
                        this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + var2, var5, (this.zPosition << 4) + var3));
                     }
                  }

                  --var5;
               } while(var5 > 0 && var4 > 0);
            }
         }
      }

      this.isModified = true;
   }

   public void addEntity(Entity var1) {
      this.hasEntities = true;
      int var2 = MathHelper.floor_double(var1.posX / 16.0D);
      int var3 = MathHelper.floor_double(var1.posZ / 16.0D);
      if (var2 != this.xPosition || var3 != this.zPosition) {
         logger.warn("Wrong location! (" + var2 + ", " + var3 + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + var1, var1);
         var1.setDead();
      }

      int var4 = MathHelper.floor_double(var1.posY / 16.0D);
      if (var4 < 0) {
         var4 = 0;
      }

      if (var4 >= this.entityLists.length) {
         var4 = this.entityLists.length - 1;
      }

      var1.addedToChunk = true;
      var1.chunkCoordX = this.xPosition;
      var1.chunkCoordY = var4;
      var1.chunkCoordZ = this.zPosition;
      this.entityLists[var4].add(var1);
   }

   public int getHeight(BlockPos var1) {
      return this.getHeightValue(var1.getX() & 15, var1.getZ() & 15);
   }

   public void setBiomeArray(byte[] var1) {
      if (this.blockBiomeArray.length != var1.length) {
         logger.warn("Could not set level chunk biomes, array length is " + var1.length + " instead of " + this.blockBiomeArray.length);
      } else {
         for(int var2 = 0; var2 < this.blockBiomeArray.length; ++var2) {
            this.blockBiomeArray[var2] = var1[var2];
         }
      }

   }

   public BlockPos getPrecipitationHeight(BlockPos var1) {
      int var2 = var1.getX() & 15;
      int var3 = var1.getZ() & 15;
      int var4 = var2 | var3 << 4;
      BlockPos var5 = new BlockPos(var1.getX(), this.precipitationHeightMap[var4], var1.getZ());
      if (var5.getY() == -999) {
         int var6 = this.getTopFilledSegment() + 15;
         var5 = new BlockPos(var1.getX(), var6, var1.getZ());
         int var7 = -1;

         while(true) {
            while(var5.getY() > 0 && var7 == -1) {
               Block var8 = this.getBlock(var5);
               Material var9 = var8.getMaterial();
               if (!var9.blocksMovement() && !var9.isLiquid()) {
                  var5 = var5.down();
               } else {
                  var7 = var5.getY() + 1;
               }
            }

            this.precipitationHeightMap[var4] = var7;
            break;
         }
      }

      return new BlockPos(var1.getX(), this.precipitationHeightMap[var4], var1.getZ());
   }

   public byte[] getBiomeArray() {
      return this.blockBiomeArray;
   }

   public void removeEntityAtIndex(Entity var1, int var2) {
      if (var2 < 0) {
         var2 = 0;
      }

      if (var2 >= this.entityLists.length) {
         var2 = this.entityLists.length - 1;
      }

      this.entityLists[var2].remove(var1);
   }

   public void setInhabitedTime(long var1) {
      this.inhabitedTime = var1;
   }

   public void setTerrainPopulated(boolean var1) {
      this.isTerrainPopulated = var1;
   }

   public int getHeightValue(int var1, int var2) {
      return this.heightMap[var2 << 4 | var1];
   }

   public void getEntitiesOfTypeWithinAAAB(Class var1, AxisAlignedBB var2, List var3, Predicate var4) {
      int var5 = MathHelper.floor_double((var2.minY - 2.0D) / 16.0D);
      int var6 = MathHelper.floor_double((var2.maxY + 2.0D) / 16.0D);
      var5 = MathHelper.clamp_int(var5, 0, this.entityLists.length - 1);
      var6 = MathHelper.clamp_int(var6, 0, this.entityLists.length - 1);

      label32:
      for(int var7 = var5; var7 <= var6; ++var7) {
         Iterator var9 = this.entityLists[var7].getByClass(var1).iterator();

         while(true) {
            Entity var8;
            do {
               do {
                  if (!var9.hasNext()) {
                     continue label32;
                  }

                  var8 = (Entity)var9.next();
               } while(!var8.getEntityBoundingBox().intersectsWith(var2));
            } while(var4 != null && !var4.apply(var8));

            var3.add(var8);
         }
      }

   }

   public boolean isAtLocation(int var1, int var2) {
      return var1 == this.xPosition && var2 == this.zPosition;
   }

   public void func_150804_b(boolean var1) {
      if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !var1) {
         this.recheckGaps(this.worldObj.isRemote);
      }

      this.field_150815_m = true;
      if (!this.isLightPopulated && this.isTerrainPopulated) {
         this.func_150809_p();
      }

      while(!this.tileEntityPosQueue.isEmpty()) {
         BlockPos var2 = (BlockPos)this.tileEntityPosQueue.poll();
         if (this.getTileEntity(var2, Chunk.EnumCreateEntityType.CHECK) == null && this.getBlock(var2).hasTileEntity()) {
            TileEntity var3 = this.createNewTileEntity(var2);
            this.worldObj.setTileEntity(var2, var3);
            this.worldObj.markBlockRangeForRenderUpdate(var2, var2);
         }
      }

   }

   public boolean isEmpty() {
      return false;
   }

   public boolean isPopulated() {
      return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
   }

   public void setChunkModified() {
      this.isModified = true;
   }

   public TileEntity getTileEntity(BlockPos var1, Chunk.EnumCreateEntityType var2) {
      TileEntity var3 = (TileEntity)this.chunkTileEntityMap.get(var1);
      if (var3 == null) {
         if (var2 == Chunk.EnumCreateEntityType.IMMEDIATE) {
            var3 = this.createNewTileEntity(var1);
            this.worldObj.setTileEntity(var1, var3);
         } else if (var2 == Chunk.EnumCreateEntityType.QUEUED) {
            this.tileEntityPosQueue.add(var1);
         }
      } else if (var3.isInvalid()) {
         this.chunkTileEntityMap.remove(var1);
         return null;
      }

      return var3;
   }

   public boolean isLightPopulated() {
      return this.isLightPopulated;
   }

   public Chunk(World var1, ChunkPrimer var2, int var3, int var4) {
      this(var1, var3, var4);
      short var5 = 256;
      boolean var6 = !var1.provider.getHasNoSky();

      for(int var7 = 0; var7 < 16; ++var7) {
         for(int var8 = 0; var8 < 16; ++var8) {
            for(int var9 = 0; var9 < var5; ++var9) {
               int var10 = var7 * var5 * 16 | var8 * var5 | var9;
               IBlockState var11 = var2.getBlockState(var10);
               if (var11.getBlock().getMaterial() != Material.air) {
                  int var12 = var9 >> 4;
                  if (this.storageArrays[var12] == null) {
                     this.storageArrays[var12] = new ExtendedBlockStorage(var12 << 4, var6);
                  }

                  this.storageArrays[var12].set(var7, var9 & 15, var8, var11);
               }
            }
         }
      }

   }

   public void enqueueRelightChecks() {
      BlockPos var1 = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);

      for(int var2 = 0; var2 < 8; ++var2) {
         if (this.queuedLightChecks >= 4096) {
            return;
         }

         int var3 = this.queuedLightChecks % 16;
         int var4 = this.queuedLightChecks / 16 % 16;
         int var5 = this.queuedLightChecks / 256;
         ++this.queuedLightChecks;

         for(int var6 = 0; var6 < 16; ++var6) {
            BlockPos var7 = var1.add(var4, (var3 << 4) + var6, var5);
            boolean var8 = var6 == 0 || var6 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15;
            if (this.storageArrays[var3] == null && var8 || this.storageArrays[var3] != null && this.storageArrays[var3].getBlockByExtId(var4, var6, var5).getMaterial() == Material.air) {
               EnumFacing[] var12;
               int var11 = (var12 = EnumFacing.values()).length;

               for(int var10 = 0; var10 < var11; ++var10) {
                  EnumFacing var9 = var12[var10];
                  BlockPos var13 = var7.offset(var9);
                  if (this.worldObj.getBlockState(var13).getBlock().getLightValue() > 0) {
                     this.worldObj.checkLight(var13);
                  }
               }

               this.worldObj.checkLight(var7);
            }
         }
      }

   }

   public int getBlockMetadata(BlockPos var1) {
      return this.getBlockMetadata(var1.getX() & 15, var1.getY(), var1.getZ() & 15);
   }

   public ChunkCoordIntPair getChunkCoordIntPair() {
      return new ChunkCoordIntPair(this.xPosition, this.zPosition);
   }

   public void removeTileEntity(BlockPos var1) {
      if (this.isChunkLoaded) {
         TileEntity var2 = (TileEntity)this.chunkTileEntityMap.remove(var1);
         if (var2 != null) {
            var2.invalidate();
         }
      }

   }

   public void setLastSaveTime(long var1) {
      this.lastSaveTime = var1;
   }

   public int getBlockLightOpacity(BlockPos var1) {
      return this.getBlock(var1).getLightOpacity();
   }

   private void propagateSkylightOcclusion(int var1, int var2) {
      this.updateSkylightColumns[var1 + var2 * 16] = true;
      this.isGapLightingUpdated = true;
   }

   public Block getBlock(int var1, int var2, int var3) {
      try {
         return this.getBlock0(var1 & 15, var2, var3 & 15);
      } catch (ReportedException var6) {
         CrashReportCategory var5 = var6.getCrashReport().makeCategory("Block being got");
         var5.addCrashSectionCallable("Location", new Callable(this, var1, var2, var3) {
            private final int val$y;
            final Chunk this$0;
            private final int val$x;
            private final int val$z;

            public Object call() throws Exception {
               return this.call();
            }

            {
               this.this$0 = var1;
               this.val$x = var2;
               this.val$y = var3;
               this.val$z = var4;
            }

            public String call() throws Exception {
               return CrashReportCategory.getCoordinateInfo(new BlockPos(this.this$0.xPosition * 16 + this.val$x, this.val$y, this.this$0.zPosition * 16 + this.val$z));
            }
         });
         throw var6;
      }
   }

   public int getTopFilledSegment() {
      for(int var1 = this.storageArrays.length - 1; var1 >= 0; --var1) {
         if (this.storageArrays[var1] != null) {
            return this.storageArrays[var1].getYLocation();
         }
      }

      return 0;
   }

   public void resetRelightChecks() {
      this.queuedLightChecks = 0;
   }

   private void relightBlock(int var1, int var2, int var3) {
      int var4 = this.heightMap[var3 << 4 | var1] & 255;
      int var5 = var4;
      if (var2 > var4) {
         var5 = var2;
      }

      while(var5 > 0 && this.getBlockLightOpacity(var1, var5 - 1, var3) == 0) {
         --var5;
      }

      if (var5 != var4) {
         this.worldObj.markBlocksDirtyVertical(var1 + this.xPosition * 16, var3 + this.zPosition * 16, var5, var4);
         this.heightMap[var3 << 4 | var1] = var5;
         int var6 = this.xPosition * 16 + var1;
         int var7 = this.zPosition * 16 + var3;
         int var8;
         int var13;
         if (!this.worldObj.provider.getHasNoSky()) {
            ExtendedBlockStorage var9;
            if (var5 < var4) {
               for(var8 = var5; var8 < var4; ++var8) {
                  var9 = this.storageArrays[var8 >> 4];
                  if (var9 != null) {
                     var9.setExtSkylightValue(var1, var8 & 15, var3, 15);
                     this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + var1, var8, (this.zPosition << 4) + var3));
                  }
               }
            } else {
               for(var8 = var4; var8 < var5; ++var8) {
                  var9 = this.storageArrays[var8 >> 4];
                  if (var9 != null) {
                     var9.setExtSkylightValue(var1, var8 & 15, var3, 0);
                     this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + var1, var8, (this.zPosition << 4) + var3));
                  }
               }
            }

            var8 = 15;

            while(var5 > 0 && var8 > 0) {
               --var5;
               var13 = this.getBlockLightOpacity(var1, var5, var3);
               if (var13 == 0) {
                  var13 = 1;
               }

               var8 -= var13;
               if (var8 < 0) {
                  var8 = 0;
               }

               ExtendedBlockStorage var10 = this.storageArrays[var5 >> 4];
               if (var10 != null) {
                  var10.setExtSkylightValue(var1, var5 & 15, var3, var8);
               }
            }
         }

         var8 = this.heightMap[var3 << 4 | var1];
         var13 = var4;
         int var14 = var8;
         if (var8 < var4) {
            var13 = var8;
            var14 = var4;
         }

         if (var8 < this.heightMapMinimum) {
            this.heightMapMinimum = var8;
         }

         if (!this.worldObj.provider.getHasNoSky()) {
            Iterator var12 = EnumFacing.Plane.HORIZONTAL.iterator();

            while(var12.hasNext()) {
               Object var11 = var12.next();
               this.updateSkylightNeighborHeight(var6 + ((EnumFacing)var11).getFrontOffsetX(), var7 + ((EnumFacing)var11).getFrontOffsetZ(), var13, var14);
            }

            this.updateSkylightNeighborHeight(var6, var7, var13, var14);
         }

         this.isModified = true;
      }

   }

   public void removeEntity(Entity var1) {
      this.removeEntityAtIndex(var1, var1.chunkCoordY);
   }

   public void setStorageArrays(ExtendedBlockStorage[] var1) {
      if (this.storageArrays.length != var1.length) {
         logger.warn("Could not set level chunk sections, array length is " + var1.length + " instead of " + this.storageArrays.length);
      } else {
         for(int var2 = 0; var2 < this.storageArrays.length; ++var2) {
            this.storageArrays[var2] = var1[var2];
         }
      }

   }

   public int[] getHeightMap() {
      return this.heightMap;
   }

   public int getLowestHeight() {
      return this.heightMapMinimum;
   }

   private void func_180700_a(EnumFacing var1) {
      if (this.isTerrainPopulated) {
         int var2;
         if (var1 == EnumFacing.EAST) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.func_150811_f(15, var2);
            }
         } else if (var1 == EnumFacing.WEST) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.func_150811_f(0, var2);
            }
         } else if (var1 == EnumFacing.SOUTH) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.func_150811_f(var2, 15);
            }
         } else if (var1 == EnumFacing.NORTH) {
            for(var2 = 0; var2 < 16; ++var2) {
               this.func_150811_f(var2, 0);
            }
         }
      }

   }

   public IBlockState getBlockState(BlockPos var1) {
      if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
         IBlockState var7 = null;
         if (var1.getY() == 60) {
            var7 = Blocks.barrier.getDefaultState();
         }

         if (var1.getY() == 70) {
            var7 = ChunkProviderDebug.func_177461_b(var1.getX(), var1.getZ());
         }

         return var7 == null ? Blocks.air.getDefaultState() : var7;
      } else {
         try {
            if (var1.getY() >= 0 && var1.getY() >> 4 < this.storageArrays.length) {
               ExtendedBlockStorage var2 = this.storageArrays[var1.getY() >> 4];
               if (var2 != null) {
                  int var8 = var1.getX() & 15;
                  int var9 = var1.getY() & 15;
                  int var5 = var1.getZ() & 15;
                  return var2.get(var8, var9, var5);
               }
            }

            return Blocks.air.getDefaultState();
         } catch (Throwable var6) {
            CrashReport var3 = CrashReport.makeCrashReport(var6, "Getting block state");
            CrashReportCategory var4 = var3.makeCategory("Block being got");
            var4.addCrashSectionCallable("Location", new Callable(this, var1) {
               private final BlockPos val$pos;
               final Chunk this$0;

               public String call() throws Exception {
                  return CrashReportCategory.getCoordinateInfo(this.val$pos);
               }

               public Object call() throws Exception {
                  return this.call();
               }

               {
                  this.this$0 = var1;
                  this.val$pos = var2;
               }
            });
            throw new ReportedException(var3);
         }
      }
   }

   private void checkSkylightNeighborHeight(int var1, int var2, int var3) {
      int var4 = this.worldObj.getHeight(new BlockPos(var1, 0, var2)).getY();
      if (var4 > var3) {
         this.updateSkylightNeighborHeight(var1, var2, var3, var4 + 1);
      } else if (var4 < var3) {
         this.updateSkylightNeighborHeight(var1, var2, var4, var3 + 1);
      }

   }

   private int getBlockLightOpacity(int var1, int var2, int var3) {
      return this.getBlock0(var1, var2, var3).getLightOpacity();
   }

   public void populateChunk(IChunkProvider var1, IChunkProvider var2, int var3, int var4) {
      boolean var5 = var1.chunkExists(var3, var4 - 1);
      boolean var6 = var1.chunkExists(var3 + 1, var4);
      boolean var7 = var1.chunkExists(var3, var4 + 1);
      boolean var8 = var1.chunkExists(var3 - 1, var4);
      boolean var9 = var1.chunkExists(var3 - 1, var4 - 1);
      boolean var10 = var1.chunkExists(var3 + 1, var4 + 1);
      boolean var11 = var1.chunkExists(var3 - 1, var4 + 1);
      boolean var12 = var1.chunkExists(var3 + 1, var4 - 1);
      if (var6 && var7 && var10) {
         if (!this.isTerrainPopulated) {
            var1.populate(var2, var3, var4);
         } else {
            var1.func_177460_a(var2, this, var3, var4);
         }
      }

      Chunk var13;
      if (var8 && var7 && var11) {
         var13 = var1.provideChunk(var3 - 1, var4);
         if (!var13.isTerrainPopulated) {
            var1.populate(var2, var3 - 1, var4);
         } else {
            var1.func_177460_a(var2, var13, var3 - 1, var4);
         }
      }

      if (var5 && var6 && var12) {
         var13 = var1.provideChunk(var3, var4 - 1);
         if (!var13.isTerrainPopulated) {
            var1.populate(var2, var3, var4 - 1);
         } else {
            var1.func_177460_a(var2, var13, var3, var4 - 1);
         }
      }

      if (var9 && var5 && var8) {
         var13 = var1.provideChunk(var3 - 1, var4 - 1);
         if (!var13.isTerrainPopulated) {
            var1.populate(var2, var3 - 1, var4 - 1);
         } else {
            var1.func_177460_a(var2, var13, var3 - 1, var4 - 1);
         }
      }

   }

   public boolean needsSaving(boolean var1) {
      if (var1) {
         if (this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified) {
            return true;
         }
      } else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
         return true;
      }

      return this.isModified;
   }

   public void fillChunk(byte[] var1, int var2, boolean var3) {
      int var4 = 0;
      boolean var5 = !this.worldObj.provider.getHasNoSky();

      int var6;
      for(var6 = 0; var6 < this.storageArrays.length; ++var6) {
         if ((var2 & 1 << var6) != 0) {
            if (this.storageArrays[var6] == null) {
               this.storageArrays[var6] = new ExtendedBlockStorage(var6 << 4, var5);
            }

            char[] var7 = this.storageArrays[var6].getData();

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var7[var8] = (char)((var1[var4 + 1] & 255) << 8 | var1[var4] & 255);
               var4 += 2;
            }
         } else if (var3 && this.storageArrays[var6] != null) {
            this.storageArrays[var6] = null;
         }
      }

      NibbleArray var9;
      for(var6 = 0; var6 < this.storageArrays.length; ++var6) {
         if ((var2 & 1 << var6) != 0 && this.storageArrays[var6] != null) {
            var9 = this.storageArrays[var6].getBlocklightArray();
            System.arraycopy(var1, var4, var9.getData(), 0, var9.getData().length);
            var4 += var9.getData().length;
         }
      }

      if (var5) {
         for(var6 = 0; var6 < this.storageArrays.length; ++var6) {
            if ((var2 & 1 << var6) != 0 && this.storageArrays[var6] != null) {
               var9 = this.storageArrays[var6].getSkylightArray();
               System.arraycopy(var1, var4, var9.getData(), 0, var9.getData().length);
               var4 += var9.getData().length;
            }
         }
      }

      if (var3) {
         System.arraycopy(var1, var4, this.blockBiomeArray, 0, this.blockBiomeArray.length);
         int var10000 = var4 + this.blockBiomeArray.length;
      }

      for(var6 = 0; var6 < this.storageArrays.length; ++var6) {
         if (this.storageArrays[var6] != null && (var2 & 1 << var6) != 0) {
            this.storageArrays[var6].removeInvalidBlocks();
         }
      }

      this.isLightPopulated = true;
      this.isTerrainPopulated = true;
      this.generateHeightMap();
      Iterator var10 = this.chunkTileEntityMap.values().iterator();

      while(var10.hasNext()) {
         TileEntity var11 = (TileEntity)var10.next();
         var11.updateContainingBlockInfo();
      }

   }

   public BiomeGenBase getBiome(BlockPos var1, WorldChunkManager var2) {
      int var3 = var1.getX() & 15;
      int var4 = var1.getZ() & 15;
      int var5 = this.blockBiomeArray[var4 << 4 | var3] & 255;
      BiomeGenBase var6;
      if (var5 == 255) {
         var6 = var2.getBiomeGenerator(var1, BiomeGenBase.plains);
         var5 = var6.biomeID;
         this.blockBiomeArray[var4 << 4 | var3] = (byte)(var5 & 255);
      }

      var6 = BiomeGenBase.getBiome(var5);
      return var6 == null ? BiomeGenBase.plains : var6;
   }

   public void onChunkUnload() {
      this.isChunkLoaded = false;
      Iterator var2 = this.chunkTileEntityMap.values().iterator();

      while(var2.hasNext()) {
         TileEntity var1 = (TileEntity)var2.next();
         this.worldObj.markTileEntityForRemoval(var1);
      }

      for(int var3 = 0; var3 < this.entityLists.length; ++var3) {
         this.worldObj.unloadEntities(this.entityLists[var3]);
      }

   }

   public boolean isTerrainPopulated() {
      return this.isTerrainPopulated;
   }

   public long getInhabitedTime() {
      return this.inhabitedTime;
   }

   public void addTileEntity(TileEntity var1) {
      this.addTileEntity(var1.getPos(), var1);
      if (this.isChunkLoaded) {
         this.worldObj.addTileEntity(var1);
      }

   }

   public ClassInheritanceMultiMap[] getEntityLists() {
      return this.entityLists;
   }

   public IBlockState setBlockState(BlockPos var1, IBlockState var2) {
      int var3 = var1.getX() & 15;
      int var4 = var1.getY();
      int var5 = var1.getZ() & 15;
      int var6 = var5 << 4 | var3;
      if (var4 >= this.precipitationHeightMap[var6] - 1) {
         this.precipitationHeightMap[var6] = -999;
      }

      int var7 = this.heightMap[var6];
      IBlockState var8 = this.getBlockState(var1);
      if (var8 == var2) {
         return null;
      } else {
         Block var9 = var2.getBlock();
         Block var10 = var8.getBlock();
         ExtendedBlockStorage var11 = this.storageArrays[var4 >> 4];
         boolean var12 = false;
         if (var11 == null) {
            if (var9 == Blocks.air) {
               return null;
            }

            var11 = this.storageArrays[var4 >> 4] = new ExtendedBlockStorage(var4 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            var12 = var4 >= var7;
         }

         var11.set(var3, var4 & 15, var5, var2);
         if (var10 != var9) {
            if (!this.worldObj.isRemote) {
               var10.breakBlock(this.worldObj, var1, var8);
            } else if (var10 instanceof ITileEntityProvider) {
               this.worldObj.removeTileEntity(var1);
            }
         }

         if (var11.getBlockByExtId(var3, var4 & 15, var5) != var9) {
            return null;
         } else {
            if (var12) {
               this.generateSkylightMap();
            } else {
               int var13 = var9.getLightOpacity();
               int var14 = var10.getLightOpacity();
               if (var13 > 0) {
                  if (var4 >= var7) {
                     this.relightBlock(var3, var4 + 1, var5);
                  }
               } else if (var4 == var7 - 1) {
                  this.relightBlock(var3, var4, var5);
               }

               if (var13 != var14 && (var13 < var14 || this.getLightFor(EnumSkyBlock.SKY, var1) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, var1) > 0)) {
                  this.propagateSkylightOcclusion(var3, var5);
               }
            }

            TileEntity var15;
            if (var10 instanceof ITileEntityProvider) {
               var15 = this.getTileEntity(var1, Chunk.EnumCreateEntityType.CHECK);
               if (var15 != null) {
                  var15.updateContainingBlockInfo();
               }
            }

            if (!this.worldObj.isRemote && var10 != var9) {
               var9.onBlockAdded(this.worldObj, var1, var2);
            }

            if (var9 instanceof ITileEntityProvider) {
               var15 = this.getTileEntity(var1, Chunk.EnumCreateEntityType.CHECK);
               if (var15 == null) {
                  var15 = ((ITileEntityProvider)var9).createNewTileEntity(this.worldObj, var9.getMetaFromState(var2));
                  this.worldObj.setTileEntity(var1, var15);
               }

               if (var15 != null) {
                  var15.updateContainingBlockInfo();
               }
            }

            this.isModified = true;
            return var8;
         }
      }
   }

   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      int var3 = var2.getX() & 15;
      int var4 = var2.getY();
      int var5 = var2.getZ() & 15;
      ExtendedBlockStorage var6 = this.storageArrays[var4 >> 4];
      return var6 == null ? (this >= var2 ? var1.defaultLightValue : 0) : (var1 == EnumSkyBlock.SKY ? (this.worldObj.provider.getHasNoSky() ? 0 : var6.getExtSkylightValue(var3, var4 & 15, var5)) : (var1 == EnumSkyBlock.BLOCK ? var6.getExtBlocklightValue(var3, var4 & 15, var5) : var1.defaultLightValue));
   }

   private Block getBlock0(int var1, int var2, int var3) {
      Block var4 = Blocks.air;
      if (var2 >= 0 && var2 >> 4 < this.storageArrays.length) {
         ExtendedBlockStorage var5 = this.storageArrays[var2 >> 4];
         if (var5 != null) {
            try {
               var4 = var5.getBlockByExtId(var1, var2 & 15, var3);
            } catch (Throwable var8) {
               CrashReport var7 = CrashReport.makeCrashReport(var8, "Getting block");
               throw new ReportedException(var7);
            }
         }
      }

      return var4;
   }

   public void setLightPopulated(boolean var1) {
      this.isLightPopulated = var1;
   }

   private void recheckGaps(boolean var1) {
      this.worldObj.theProfiler.startSection("recheckGaps");
      if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
         for(int var2 = 0; var2 < 16; ++var2) {
            for(int var3 = 0; var3 < 16; ++var3) {
               if (this.updateSkylightColumns[var2 + var3 * 16]) {
                  this.updateSkylightColumns[var2 + var3 * 16] = false;
                  int var4 = this.getHeightValue(var2, var3);
                  int var5 = this.xPosition * 16 + var2;
                  int var6 = this.zPosition * 16 + var3;
                  int var7 = Integer.MAX_VALUE;

                  Object var8;
                  Iterator var9;
                  for(var9 = EnumFacing.Plane.HORIZONTAL.iterator(); var9.hasNext(); var7 = Math.min(var7, this.worldObj.getChunksLowestHorizon(var5 + ((EnumFacing)var8).getFrontOffsetX(), var6 + ((EnumFacing)var8).getFrontOffsetZ()))) {
                     var8 = var9.next();
                  }

                  this.checkSkylightNeighborHeight(var5, var6, var7);
                  var9 = EnumFacing.Plane.HORIZONTAL.iterator();

                  while(var9.hasNext()) {
                     var8 = var9.next();
                     this.checkSkylightNeighborHeight(var5 + ((EnumFacing)var8).getFrontOffsetX(), var6 + ((EnumFacing)var8).getFrontOffsetZ(), var4);
                  }

                  if (var1) {
                     this.worldObj.theProfiler.endSection();
                     return;
                  }
               }
            }
         }

         this.isGapLightingUpdated = false;
      }

      this.worldObj.theProfiler.endSection();
   }

   public void setChunkLoaded(boolean var1) {
      this.isChunkLoaded = var1;
   }

   public void setHasEntities(boolean var1) {
      this.hasEntities = var1;
   }

   public void addTileEntity(BlockPos var1, TileEntity var2) {
      var2.setWorldObj(this.worldObj);
      var2.setPos(var1);
      if (this.getBlock(var1) instanceof ITileEntityProvider) {
         if (this.chunkTileEntityMap.containsKey(var1)) {
            ((TileEntity)this.chunkTileEntityMap.get(var1)).invalidate();
         }

         var2.validate();
         this.chunkTileEntityMap.put(var1, var2);
      }

   }

   private void updateSkylightNeighborHeight(int var1, int var2, int var3, int var4) {
      if (var4 > var3 && this.worldObj.isAreaLoaded(new BlockPos(var1, 0, var2), 16)) {
         for(int var5 = var3; var5 < var4; ++var5) {
            this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(var1, var5, var2));
         }

         this.isModified = true;
      }

   }

   public Block getBlock(BlockPos var1) {
      try {
         return this.getBlock0(var1.getX() & 15, var1.getY(), var1.getZ() & 15);
      } catch (ReportedException var5) {
         CrashReportCategory var3 = var5.getCrashReport().makeCategory("Block being got");
         var3.addCrashSectionCallable("Location", new Callable(this, var1) {
            private final BlockPos val$pos;
            final Chunk this$0;

            public Object call() throws Exception {
               return this.call();
            }

            public String call() throws Exception {
               return CrashReportCategory.getCoordinateInfo(this.val$pos);
            }

            {
               this.this$0 = var1;
               this.val$pos = var2;
            }
         });
         throw var5;
      }
   }

   public void setLightFor(EnumSkyBlock var1, BlockPos var2, int var3) {
      int var4 = var2.getX() & 15;
      int var5 = var2.getY();
      int var6 = var2.getZ() & 15;
      ExtendedBlockStorage var7 = this.storageArrays[var5 >> 4];
      if (var7 == null) {
         var7 = this.storageArrays[var5 >> 4] = new ExtendedBlockStorage(var5 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
         this.generateSkylightMap();
      }

      this.isModified = true;
      if (var1 == EnumSkyBlock.SKY) {
         if (!this.worldObj.provider.getHasNoSky()) {
            var7.setExtSkylightValue(var4, var5 & 15, var6, var3);
         }
      } else if (var1 == EnumSkyBlock.BLOCK) {
         var7.setExtBlocklightValue(var4, var5 & 15, var6, var3);
      }

   }

   public boolean getAreLevelsEmpty(int var1, int var2) {
      if (var1 < 0) {
         var1 = 0;
      }

      if (var2 >= 256) {
         var2 = 255;
      }

      for(int var3 = var1; var3 <= var2; var3 += 16) {
         ExtendedBlockStorage var4 = this.storageArrays[var3 >> 4];
         if (var4 != null && !var4.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public void func_150809_p() {
      this.isTerrainPopulated = true;
      this.isLightPopulated = true;
      BlockPos var1 = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
      if (!this.worldObj.provider.getHasNoSky()) {
         if (this.worldObj.isAreaLoaded(var1.add(-1, 0, -1), var1.add(16, this.worldObj.func_181545_F(), 16))) {
            label44:
            for(int var2 = 0; var2 < 16; ++var2) {
               for(int var3 = 0; var3 < 16; ++var3) {
                  if (var2 == var3) {
                     this.isLightPopulated = false;
                     break label44;
                  }
               }
            }

            if (this.isLightPopulated) {
               Iterator var6 = EnumFacing.Plane.HORIZONTAL.iterator();

               while(var6.hasNext()) {
                  Object var5 = var6.next();
                  int var4 = ((EnumFacing)var5).getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                  this.worldObj.getChunkFromBlockCoords(var1.offset((EnumFacing)var5, var4)).func_180700_a(((EnumFacing)var5).getOpposite());
               }

               this.func_177441_y();
            }
         } else {
            this.isLightPopulated = false;
         }
      }

   }

   private int getBlockMetadata(int var1, int var2, int var3) {
      if (var2 >> 4 >= this.storageArrays.length) {
         return 0;
      } else {
         ExtendedBlockStorage var4 = this.storageArrays[var2 >> 4];
         return var4 != null ? var4.getExtBlockMetadata(var1, var2 & 15, var3) : 0;
      }
   }

   public static enum EnumCreateEntityType {
      CHECK,
      QUEUED,
      IMMEDIATE;

      private static final Chunk.EnumCreateEntityType[] ENUM$VALUES = new Chunk.EnumCreateEntityType[]{IMMEDIATE, QUEUED, CHECK};
   }
}
