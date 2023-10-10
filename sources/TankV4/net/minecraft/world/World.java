package net.minecraft.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import my.NewSnake.Tank.module.modules.WORLD.Ceu;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;

public abstract class World implements IBlockAccess {
   protected boolean scheduledUpdatesAreImmediate;
   public final WorldProvider provider;
   protected final int DIST_HASH_MAGIC = 1013904223;
   public final Random rand = new Random();
   protected List worldAccesses = Lists.newArrayList();
   protected boolean findingSpawnPoint;
   protected float prevRainingStrength;
   protected final IntHashMap entitiesById = new IntHashMap();
   public final List loadedEntityList = Lists.newArrayList();
   protected VillageCollection villageCollectionObj;
   protected boolean spawnHostileMobs;
   protected final ISaveHandler saveHandler;
   private final Calendar theCalendar = Calendar.getInstance();
   protected boolean spawnPeacefulMobs;
   public final List tickableTileEntities = Lists.newArrayList();
   protected IChunkProvider chunkProvider;
   private int ambientTickCountdown;
   protected WorldInfo worldInfo;
   protected float prevThunderingStrength;
   int[] lightUpdateBlockList;
   private final WorldBorder worldBorder;
   private boolean processingLoadedTiles;
   public final List playerEntities = Lists.newArrayList();
   protected int updateLCG = (new Random()).nextInt();
   public final List loadedTileEntityList = Lists.newArrayList();
   protected final List unloadedEntityList = Lists.newArrayList();
   private int field_181546_a = 63;
   public final Profiler theProfiler;
   private final List tileEntitiesToBeRemoved = Lists.newArrayList();
   private int lastLightningBolt;
   public final List weatherEffects = Lists.newArrayList();
   private int skylightSubtracted;
   protected float thunderingStrength;
   private long cloudColour = 16777215L;
   public final boolean isRemote;
   protected float rainingStrength;
   private final List addedTileEntityList = Lists.newArrayList();
   protected Set activeChunkSet = Sets.newHashSet();
   protected MapStorage mapStorage;
   protected Scoreboard worldScoreboard = new Scoreboard();

   private int getRawLight(BlockPos var1, EnumSkyBlock var2) {
      if (var2 == EnumSkyBlock.SKY && this.canSeeSky(var1)) {
         return 15;
      } else {
         Block var3 = this.getBlockState(var1).getBlock();
         int var4 = var2 == EnumSkyBlock.SKY ? 0 : var3.getLightValue();
         int var5 = var3.getLightOpacity();
         if (var5 >= 15 && var3.getLightValue() > 0) {
            var5 = 1;
         }

         if (var5 < 1) {
            var5 = 1;
         }

         if (var5 >= 15) {
            return 0;
         } else if (var4 >= 14) {
            return var4;
         } else {
            EnumFacing[] var9;
            int var8 = (var9 = EnumFacing.values()).length;

            for(int var7 = 0; var7 < var8; ++var7) {
               EnumFacing var6 = var9[var7];
               BlockPos var10 = var1.offset(var6);
               int var11 = this.getLightFor(var2, var10) - var5;
               if (var11 > var4) {
                  var4 = var11;
               }

               if (var4 >= 14) {
                  return var4;
               }
            }

            return var4;
         }
      }
   }

   public boolean canSeeSky(BlockPos var1) {
      return this.getChunkFromBlockCoords(var1).canSeeSky(var1);
   }

   public boolean isBlockFullCube(BlockPos var1) {
      IBlockState var2 = this.getBlockState(var1);
      AxisAlignedBB var3 = var2.getBlock().getCollisionBoundingBox(this, var1, var2);
      return var3 != null && var3.getAverageEdgeLength() >= 1.0D;
   }

   public boolean checkLight(BlockPos var1) {
      boolean var2 = false;
      if (!this.provider.getHasNoSky()) {
         var2 |= this.checkLightFor(EnumSkyBlock.SKY, var1);
      }

      var2 |= this.checkLightFor(EnumSkyBlock.BLOCK, var1);
      return var2;
   }

   public boolean canBlockFreezeWater(BlockPos var1) {
      return this.canBlockFreeze(var1, false);
   }

   public void calculateInitialSkylight() {
      int var1 = this.calculateSkylightSubtracted(1.0F);
      if (var1 != this.skylightSubtracted) {
         this.skylightSubtracted = var1;
      }

   }

   public List getEntitiesWithinAABB(Class var1, AxisAlignedBB var2) {
      return this.getEntitiesWithinAABB(var1, var2, EntitySelectors.NOT_SPECTATING);
   }

   public IChunkProvider getChunkProvider() {
      return this.chunkProvider;
   }

   public boolean isBlockinHighHumidity(BlockPos var1) {
      BiomeGenBase var2 = this.getBiomeGenForCoords(var1);
      return var2.isHighHumidity();
   }

   public List getPendingBlockUpdates(Chunk var1, boolean var2) {
      return null;
   }

   public void removeWorldAccess(IWorldAccess var1) {
      this.worldAccesses.remove(var1);
   }

   public List getPlayers(Class var1, Predicate var2) {
      ArrayList var3 = Lists.newArrayList();
      Iterator var5 = this.playerEntities.iterator();

      while(var5.hasNext()) {
         Entity var4 = (Entity)var5.next();
         if (var1.isAssignableFrom(var4.getClass()) && var2.apply(var4)) {
            var3.add(var4);
         }
      }

      return var3;
   }

   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      if (var2.getY() < 0) {
         var2 = new BlockPos(var2.getX(), 0, var2.getZ());
      }

      if (var2 != false) {
         return var1.defaultLightValue;
      } else if (!this.isBlockLoaded(var2)) {
         return var1.defaultLightValue;
      } else {
         Chunk var3 = this.getChunkFromBlockCoords(var2);
         return var3.getLightFor(var1, var2);
      }
   }

   public void notifyLightSet(BlockPos var1) {
      for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
         ((IWorldAccess)this.worldAccesses.get(var2)).notifyLightSet(var1);
      }

   }

   public IBlockState getBlockState(BlockPos var1) {
      if (var1 != false) {
         return Blocks.air.getDefaultState();
      } else {
         Chunk var2 = this.getChunkFromBlockCoords(var1);
         return var2.getBlockState(var1);
      }
   }

   public int getLightFromNeighbors(BlockPos var1) {
      return this.getLight(var1, true);
   }

   public int getStrongPower(BlockPos var1, EnumFacing var2) {
      IBlockState var3 = this.getBlockState(var1);
      return var3.getBlock().getStrongPower(this, var1, var3, var2);
   }

   public List getCollidingBoundingBoxes(Entity var1, AxisAlignedBB var2) {
      ArrayList var3 = Lists.newArrayList();
      int var4 = MathHelper.floor_double(var2.minX);
      int var5 = MathHelper.floor_double(var2.maxX + 1.0D);
      int var6 = MathHelper.floor_double(var2.minY);
      int var7 = MathHelper.floor_double(var2.maxY + 1.0D);
      int var8 = MathHelper.floor_double(var2.minZ);
      int var9 = MathHelper.floor_double(var2.maxZ + 1.0D);
      WorldBorder var10 = this.getWorldBorder();
      boolean var11 = var1.isOutsideBorder();
      boolean var12 = this.isInsideBorder(var10, var1);
      IBlockState var13 = Blocks.stone.getDefaultState();
      BlockPos.MutableBlockPos var14 = new BlockPos.MutableBlockPos();

      for(int var15 = var4; var15 < var5; ++var15) {
         for(int var16 = var8; var16 < var9; ++var16) {
            if (this.isBlockLoaded(var14.func_181079_c(var15, 64, var16))) {
               for(int var17 = var6 - 1; var17 < var7; ++var17) {
                  var14.func_181079_c(var15, var17, var16);
                  if (var11 && var12) {
                     var1.setOutsideBorder(false);
                  } else if (!var11 && !var12) {
                     var1.setOutsideBorder(true);
                  }

                  IBlockState var18 = var13;
                  if (var10.contains((BlockPos)var14) || !var12) {
                     var18 = this.getBlockState(var14);
                  }

                  var18.getBlock().addCollisionBoxesToList(this, var14, var18, var2, var3, var1);
               }
            }
         }
      }

      double var20 = 0.25D;
      List var21 = this.getEntitiesWithinAABBExcludingEntity(var1, var2.expand(var20, var20, var20));

      for(int var22 = 0; var22 < var21.size(); ++var22) {
         if (var1.riddenByEntity != var21 && var1.ridingEntity != var21) {
            AxisAlignedBB var19 = ((Entity)var21.get(var22)).getCollisionBoundingBox();
            if (var19 != null && var19.intersectsWith(var2)) {
               var3.add(var19);
            }

            var19 = var1.getCollisionBox((Entity)var21.get(var22));
            if (var19 != null && var19.intersectsWith(var2)) {
               var3.add(var19);
            }
         }
      }

      return var3;
   }

   public boolean setBlockToAir(BlockPos var1) {
      return this.setBlockState(var1, Blocks.air.getDefaultState(), 3);
   }

   public boolean isAreaLoaded(BlockPos var1, int var2, boolean var3) {
      return this.isAreaLoaded(var1.getX() - var2, var1.getY() - var2, var1.getZ() - var2, var1.getX() + var2, var1.getY() + var2, var1.getZ() + var2, var3);
   }

   public void addTileEntities(Collection var1) {
      if (this.processingLoadedTiles) {
         this.addedTileEntityList.addAll(var1);
      } else {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            TileEntity var2 = (TileEntity)var3.next();
            this.loadedTileEntityList.add(var2);
            if (var2 instanceof ITickable) {
               this.tickableTileEntities.add(var2);
            }
         }
      }

   }

   public void playAuxSFX(int var1, BlockPos var2, int var3) {
      this.playAuxSFXAtEntity((EntityPlayer)null, var1, var2, var3);
   }

   public void unloadEntities(Collection var1) {
      this.unloadedEntityList.addAll(var1);
   }

   public void updateAllPlayersSleepingFlag() {
   }

   public boolean checkBlockCollision(AxisAlignedBB var1) {
      int var2 = MathHelper.floor_double(var1.minX);
      int var3 = MathHelper.floor_double(var1.maxX);
      int var4 = MathHelper.floor_double(var1.minY);
      int var5 = MathHelper.floor_double(var1.maxY);
      int var6 = MathHelper.floor_double(var1.minZ);
      int var7 = MathHelper.floor_double(var1.maxZ);
      BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();

      for(int var9 = var2; var9 <= var3; ++var9) {
         for(int var10 = var4; var10 <= var5; ++var10) {
            for(int var11 = var6; var11 <= var7; ++var11) {
               Block var12 = this.getBlockState(var8.func_181079_c(var9, var10, var11)).getBlock();
               if (var12.getMaterial() != Material.air) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean isFindingSpawnPoint() {
      return this.findingSpawnPoint;
   }

   public WorldBorder getWorldBorder() {
      return this.worldBorder;
   }

   protected void playMoodSoundAndCheckLight(int var1, int var2, Chunk var3) {
      this.theProfiler.endStartSection("moodSound");
      if (this.ambientTickCountdown == 0 && !this.isRemote) {
         this.updateLCG = this.updateLCG * 3 + 1013904223;
         int var4 = this.updateLCG >> 2;
         int var5 = var4 & 15;
         int var6 = var4 >> 8 & 15;
         int var7 = var4 >> 16 & 255;
         BlockPos var8 = new BlockPos(var5, var7, var6);
         Block var9 = var3.getBlock(var8);
         var5 += var1;
         var6 += var2;
         if (var9.getMaterial() == Material.air && this.getLight(var8) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, var8) <= 0) {
            EntityPlayer var10 = this.getClosestPlayer((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D, 8.0D);
            if (var10 != null && var10.getDistanceSq((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D) > 4.0D) {
               this.playSoundEffect((double)var5 + 0.5D, (double)var7 + 0.5D, (double)var6 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
               this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
            }
         }
      }

      this.theProfiler.endStartSection("checkLight");
      var3.enqueueRelightChecks();
   }

   public long getSeed() {
      return this.worldInfo.getSeed();
   }

   public void scheduleBlockUpdate(BlockPos var1, Block var2, int var3, int var4) {
   }

   public void notifyNeighborsRespectDebug(BlockPos var1, Block var2) {
      if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
         this.notifyNeighborsOfStateChange(var1, var2);
      }

   }

   public boolean canBlockFreeze(BlockPos var1, boolean var2) {
      BiomeGenBase var3 = this.getBiomeGenForCoords(var1);
      float var4 = var3.getFloatTemperature(var1);
      if (var4 > 0.15F) {
         return false;
      } else {
         if (var1.getY() >= 0 && var1.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, var1) < 10) {
            IBlockState var5 = this.getBlockState(var1);
            Block var6 = var5.getBlock();
            if ((var6 == Blocks.water || var6 == Blocks.flowing_water) && (Integer)var5.getValue(BlockLiquid.LEVEL) == 0) {
               if (!var2) {
                  return true;
               }

               World var10001;
               label31: {
                  if (var1.west() != false) {
                     var10001 = this;
                     if (var1.east() != false && var1.north() != false && var1.south() != false) {
                        boolean var10004 = true;
                        break label31;
                     }
                  }

                  var10001 = false;
               }

               World var7 = var10001;
               if (var7 == false) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public void setSpawnPoint(BlockPos var1) {
      this.worldInfo.setSpawn(var1);
   }

   public ISaveHandler getSaveHandler() {
      return this.saveHandler;
   }

   public boolean setBlockState(BlockPos var1, IBlockState var2) {
      return this.setBlockState(var1, var2, 3);
   }

   public boolean isBlockLoaded(BlockPos var1) {
      return this.isBlockLoaded(var1, true);
   }

   protected void onEntityRemoved(Entity var1) {
      for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
         ((IWorldAccess)this.worldAccesses.get(var2)).onEntityRemoved(var1);
      }

   }

   public List func_175712_a(StructureBoundingBox var1, boolean var2) {
      return null;
   }

   public boolean isBlockPowered(BlockPos var1) {
      return this.getRedstonePower(var1.down(), EnumFacing.DOWN) > 0 ? true : (this.getRedstonePower(var1.up(), EnumFacing.UP) > 0 ? true : (this.getRedstonePower(var1.north(), EnumFacing.NORTH) > 0 ? true : (this.getRedstonePower(var1.south(), EnumFacing.SOUTH) > 0 ? true : (this.getRedstonePower(var1.west(), EnumFacing.WEST) > 0 ? true : this.getRedstonePower(var1.east(), EnumFacing.EAST) > 0))));
   }

   public void updateComparatorOutputLevel(BlockPos var1, Block var2) {
      Iterator var4 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         BlockPos var5 = var1.offset((EnumFacing)var3);
         if (this.isBlockLoaded(var5)) {
            IBlockState var6 = this.getBlockState(var5);
            if (Blocks.unpowered_comparator.isAssociated(var6.getBlock())) {
               var6.getBlock().onNeighborBlockChange(this, var5, var6, var2);
            } else if (var6.getBlock().isNormalCube()) {
               var5 = var5.offset((EnumFacing)var3);
               var6 = this.getBlockState(var5);
               if (Blocks.unpowered_comparator.isAssociated(var6.getBlock())) {
                  var6.getBlock().onNeighborBlockChange(this, var5, var6, var2);
               }
            }
         }
      }

   }

   public void setRainStrength(float var1) {
      this.prevRainingStrength = var1;
      this.rainingStrength = var1;
   }

   public void setTileEntity(BlockPos var1, TileEntity var2) {
      if (var2 != null && !var2.isInvalid()) {
         if (this.processingLoadedTiles) {
            var2.setPos(var1);
            Iterator var3 = this.addedTileEntityList.iterator();

            while(var3.hasNext()) {
               TileEntity var4 = (TileEntity)var3.next();
               if (var4.getPos().equals(var1)) {
                  var4.invalidate();
                  var3.remove();
               }
            }

            this.addedTileEntityList.add(var2);
         } else {
            this.addTileEntity(var2);
            this.getChunkFromBlockCoords(var1).addTileEntity(var1, var2);
         }
      }

   }

   public CrashReportCategory addWorldInfoToCrashReport(CrashReport var1) {
      CrashReportCategory var2 = var1.makeCategoryDepth("Affected level", 1);
      var2.addCrashSection("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
      var2.addCrashSectionCallable("All players", new Callable(this) {
         final World this$0;

         public Object call() throws Exception {
            return this.call();
         }

         public String call() {
            return this.this$0.playerEntities.size() + " total; " + this.this$0.playerEntities.toString();
         }

         {
            this.this$0 = var1;
         }
      });
      var2.addCrashSectionCallable("Chunk stats", new Callable(this) {
         final World this$0;

         public Object call() throws Exception {
            return this.call();
         }

         {
            this.this$0 = var1;
         }

         public String call() {
            return this.this$0.chunkProvider.makeString();
         }
      });

      try {
         this.worldInfo.addToCrashReport(var2);
      } catch (Throwable var4) {
         var2.addCrashSectionThrowable("Level Data Unobtainable", var4);
      }

      return var2;
   }

   public World init() {
      return this;
   }

   public boolean isAnyLiquid(AxisAlignedBB var1) {
      int var2 = MathHelper.floor_double(var1.minX);
      int var3 = MathHelper.floor_double(var1.maxX);
      int var4 = MathHelper.floor_double(var1.minY);
      int var5 = MathHelper.floor_double(var1.maxY);
      int var6 = MathHelper.floor_double(var1.minZ);
      int var7 = MathHelper.floor_double(var1.maxZ);
      BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();

      for(int var9 = var2; var9 <= var3; ++var9) {
         for(int var10 = var4; var10 <= var5; ++var10) {
            for(int var11 = var6; var11 <= var7; ++var11) {
               Block var12 = this.getBlockState(var8.func_181079_c(var9, var10, var11)).getBlock();
               if (var12.getMaterial().isLiquid()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public int getLastLightningBolt() {
      return this.lastLightningBolt;
   }

   public int countEntities(Class var1) {
      int var2 = 0;
      Iterator var4 = this.loadedEntityList.iterator();

      while(true) {
         Entity var3;
         do {
            if (!var4.hasNext()) {
               return var2;
            }

            var3 = (Entity)var4.next();
         } while(var3 instanceof EntityLiving && ((EntityLiving)var3).isNoDespawnRequired());

         if (var1.isAssignableFrom(var3.getClass())) {
            ++var2;
         }
      }
   }

   public boolean isAreaLoaded(BlockPos var1, BlockPos var2) {
      return this.isAreaLoaded(var1, var2, true);
   }

   public boolean isSidePowered(BlockPos var1, EnumFacing var2) {
      return this.getRedstonePower(var1, var2) > 0;
   }

   public VillageCollection getVillageCollection() {
      return this.villageCollectionObj;
   }

   public void markChunkDirty(BlockPos var1, TileEntity var2) {
      if (this.isBlockLoaded(var1)) {
         this.getChunkFromBlockCoords(var1).setChunkModified();
      }

   }

   public boolean handleMaterialAcceleration(AxisAlignedBB var1, Material var2, Entity var3) {
      int var4 = MathHelper.floor_double(var1.minX);
      int var5 = MathHelper.floor_double(var1.maxX + 1.0D);
      int var6 = MathHelper.floor_double(var1.minY);
      int var7 = MathHelper.floor_double(var1.maxY + 1.0D);
      int var8 = MathHelper.floor_double(var1.minZ);
      int var9 = MathHelper.floor_double(var1.maxZ + 1.0D);
      return false;
   }

   public BlockPos getTopSolidOrLiquidBlock(BlockPos var1) {
      Chunk var2 = this.getChunkFromBlockCoords(var1);

      BlockPos var3;
      BlockPos var4;
      for(var3 = new BlockPos(var1.getX(), var2.getTopFilledSegment() + 16, var1.getZ()); var3.getY() >= 0; var3 = var4) {
         var4 = var3.down();
         Material var5 = var2.getBlock(var4).getMaterial();
         if (var5.blocksMovement() && var5 != Material.leaves) {
            break;
         }
      }

      return var3;
   }

   public BlockPos getHeight(BlockPos var1) {
      int var2;
      if (var1.getX() >= -30000000 && var1.getZ() >= -30000000 && var1.getX() < 30000000 && var1.getZ() < 30000000) {
         int var10001 = var1.getX() >> 4;
         int var10002 = var1.getZ() >> 4;
         var2 = this.getChunkFromChunkCoords(var1.getX() >> 4, var1.getZ() >> 4).getHeightValue(var1.getX() & 15, var1.getZ() & 15);
      } else {
         var2 = this.func_181545_F() + 1;
      }

      return new BlockPos(var1.getX(), var2, var1.getZ());
   }

   public boolean addTileEntity(TileEntity var1) {
      boolean var2 = this.loadedTileEntityList.add(var1);
      if (var2 && var1 instanceof ITickable) {
         this.tickableTileEntities.add(var1);
      }

      return var2;
   }

   public boolean extendedLevelsInChunkCache() {
      return false;
   }

   public void setThunderStrength(float var1) {
      this.prevThunderingStrength = var1;
      this.thunderingStrength = var1;
   }

   public EnumDifficulty getDifficulty() {
      return this.getWorldInfo().getDifficulty();
   }

   public double getHorizon() {
      return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
   }

   public void setTotalWorldTime(long var1) {
      this.worldInfo.setWorldTotalTime(var1);
   }

   public float getCelestialAngle(float var1) {
      return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), var1);
   }

   public void markBlockRangeForRenderUpdate(BlockPos var1, BlockPos var2) {
      this.markBlockRangeForRenderUpdate(var1.getX(), var1.getY(), var1.getZ(), var2.getX(), var2.getY(), var2.getZ());
   }

   public float getCurrentMoonPhaseFactor() {
      return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
   }

   public void updateBlockTick(BlockPos var1, Block var2, int var3, int var4) {
   }

   public void setInitialSpawnLocation() {
      this.setSpawnPoint(new BlockPos(8, 64, 8));
   }

   public void tick() {
      this.updateWeather();
   }

   public WorldChunkManager getWorldChunkManager() {
      return this.provider.getWorldChunkManager();
   }

   public Vec3 getCloudColour(float var1) {
      float var2 = this.getCelestialAngle(var1);
      float var3 = MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      var3 = MathHelper.clamp_float(var3, 0.0F, 1.0F);
      float var4 = (float)(this.cloudColour >> 16 & 255L) / 255.0F;
      float var5 = (float)(this.cloudColour >> 8 & 255L) / 255.0F;
      float var6 = (float)(this.cloudColour & 255L) / 255.0F;
      float var7 = this.getRainStrength(var1);
      float var8;
      float var9;
      if (var7 > 0.0F) {
         var8 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.6F;
         var9 = 1.0F - var7 * 0.95F;
         var4 = var4 * var9 + var8 * (1.0F - var9);
         var5 = var5 * var9 + var8 * (1.0F - var9);
         var6 = var6 * var9 + var8 * (1.0F - var9);
      }

      var4 *= var3 * 0.9F + 0.1F;
      var5 *= var3 * 0.9F + 0.1F;
      var6 *= var3 * 0.85F + 0.15F;
      var8 = this.getThunderStrength(var1);
      if (var8 > 0.0F) {
         var9 = (var4 * 0.3F + var5 * 0.59F + var6 * 0.11F) * 0.2F;
         float var10 = 1.0F - var8 * 0.95F;
         var4 = var4 * var10 + var9 * (1.0F - var10);
         var5 = var5 * var10 + var9 * (1.0F - var10);
         var6 = var6 * var10 + var9 * (1.0F - var10);
      }

      return new Vec3((double)var4, (double)var5, (double)var6);
   }

   public int getHeight() {
      return 256;
   }

   public void removePlayerEntityDangerously(Entity var1) {
      var1.setDead();
      if (var1 instanceof EntityPlayer) {
         this.playerEntities.remove(var1);
         this.updateAllPlayersSleepingFlag();
      }

      int var2 = var1.chunkCoordX;
      int var3 = var1.chunkCoordZ;
      if (var1.addedToChunk) {
         this.getChunkFromChunkCoords(var2, var3).removeEntity(var1);
      }

      this.loadedEntityList.remove(var1);
      this.onEntityRemoved(var1);
   }

   public boolean isAnyPlayerWithinRangeAt(double var1, double var3, double var5, double var7) {
      for(int var9 = 0; var9 < this.playerEntities.size(); ++var9) {
         EntityPlayer var10 = (EntityPlayer)this.playerEntities.get(var9);
         if (EntitySelectors.NOT_SPECTATING.apply(var10)) {
            double var11 = var10.getDistanceSq(var1, var3, var5);
            if (var7 < 0.0D || var11 < var7 * var7) {
               return true;
            }
         }
      }

      return false;
   }

   public void setLightFor(EnumSkyBlock var1, BlockPos var2, int var3) {
      if (this >= var2 && this.isBlockLoaded(var2)) {
         Chunk var4 = this.getChunkFromBlockCoords(var2);
         var4.setLightFor(var1, var2, var3);
         this.notifyLightSet(var2);
      }

   }

   public boolean canLightningStrike(BlockPos var1) {
      if (this > 0) {
         return false;
      } else if (!this.canSeeSky(var1)) {
         return false;
      } else if (this.getPrecipitationHeight(var1).getY() > var1.getY()) {
         return false;
      } else {
         BiomeGenBase var2 = this.getBiomeGenForCoords(var1);
         if (var2.getEnableSnow()) {
            boolean var10000 = false;
         }

         return var2.canSpawnLightningBolt();
      }
   }

   public void notifyBlockOfStateChange(BlockPos var1, Block var2) {
      if (!this.isRemote) {
         IBlockState var3 = this.getBlockState(var1);

         try {
            var3.getBlock().onNeighborBlockChange(this, var1, var3, var2);
         } catch (Throwable var7) {
            CrashReport var5 = CrashReport.makeCrashReport(var7, "Exception while updating neighbours");
            CrashReportCategory var6 = var5.makeCategory("Block being updated");
            var6.addCrashSectionCallable("Source block type", new Callable(this, var2) {
               final World this$0;
               private final Block val$blockIn;

               public Object call() throws Exception {
                  return this.call();
               }

               public String call() throws Exception {
                  try {
                     return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(this.val$blockIn), this.val$blockIn.getUnlocalizedName(), this.val$blockIn.getClass().getCanonicalName());
                  } catch (Throwable var2) {
                     return "ID #" + Block.getIdFromBlock(this.val$blockIn);
                  }
               }

               {
                  this.this$0 = var1;
                  this.val$blockIn = var2;
               }
            });
            CrashReportCategory.addBlockInfo(var6, var1, var3);
            throw new ReportedException(var5);
         }
      }

   }

   public boolean isAABBInMaterial(AxisAlignedBB var1, Material var2) {
      int var3 = MathHelper.floor_double(var1.minX);
      int var4 = MathHelper.floor_double(var1.maxX + 1.0D);
      int var5 = MathHelper.floor_double(var1.minY);
      int var6 = MathHelper.floor_double(var1.maxY + 1.0D);
      int var7 = MathHelper.floor_double(var1.minZ);
      int var8 = MathHelper.floor_double(var1.maxZ + 1.0D);
      BlockPos.MutableBlockPos var9 = new BlockPos.MutableBlockPos();

      for(int var10 = var3; var10 < var4; ++var10) {
         for(int var11 = var5; var11 < var6; ++var11) {
            for(int var12 = var7; var12 < var8; ++var12) {
               IBlockState var13 = this.getBlockState(var9.func_181079_c(var10, var11, var12));
               Block var14 = var13.getBlock();
               if (var14.getMaterial() == var2) {
                  int var15 = (Integer)var13.getValue(BlockLiquid.LEVEL);
                  double var16 = (double)(var11 + 1);
                  if (var15 < 8) {
                     var16 = (double)(var11 + 1) - (double)var15 / 8.0D;
                  }

                  if (var16 >= var1.minY) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public static boolean doesBlockHaveSolidTopSurface(IBlockAccess var0, BlockPos var1) {
      IBlockState var2 = var0.getBlockState(var1);
      Block var3 = var2.getBlock();
      return var3.getMaterial().isOpaque() && var3.isFullCube() ? true : (var3 instanceof BlockStairs ? var2.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP : (var3 instanceof BlockSlab ? var2.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP : (var3 instanceof BlockHopper ? true : (var3 instanceof BlockSnow ? (Integer)var2.getValue(BlockSnow.LAYERS) == 7 : false))));
   }

   private void spawnParticle(int var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      for(int var16 = 0; var16 < this.worldAccesses.size(); ++var16) {
         ((IWorldAccess)this.worldAccesses.get(var16)).spawnParticle(var1, var2, var3, var5, var7, var9, var11, var13, var15);
      }

   }

   public WorldType getWorldType() {
      return this.worldInfo.getTerrainType();
   }

   public boolean spawnEntityInWorld(Entity var1) {
      int var2 = MathHelper.floor_double(var1.posX / 16.0D);
      int var3 = MathHelper.floor_double(var1.posZ / 16.0D);
      boolean var4 = var1.forceSpawn;
      if (var1 instanceof EntityPlayer) {
         var4 = true;
      }

      if (!var4) {
         return false;
      } else {
         if (var1 instanceof EntityPlayer) {
            EntityPlayer var5 = (EntityPlayer)var1;
            this.playerEntities.add(var5);
            this.updateAllPlayersSleepingFlag();
         }

         this.getChunkFromChunkCoords(var2, var3).addEntity(var1);
         this.loadedEntityList.add(var1);
         this.onEntityAdded(var1);
         return true;
      }
   }

   public boolean checkLightFor(EnumSkyBlock var1, BlockPos var2) {
      if (!this.isAreaLoaded(var2, 17, false)) {
         return false;
      } else {
         int var3 = 0;
         int var4 = 0;
         this.theProfiler.startSection("getBrightness");
         int var5 = this.getLightFor(var1, var2);
         int var6 = this.getRawLight(var2, var1);
         int var7 = var2.getX();
         int var8 = var2.getY();
         int var9 = var2.getZ();
         int var10;
         int var11;
         int var12;
         int var13;
         int var16;
         int var17;
         int var18;
         int var19;
         if (var6 > var5) {
            this.lightUpdateBlockList[var4++] = 133152;
         } else if (var6 < var5) {
            this.lightUpdateBlockList[var4++] = 133152 | var5 << 18;

            label88:
            while(true) {
               int var14;
               do {
                  do {
                     BlockPos var15;
                     do {
                        if (var3 >= var4) {
                           var3 = 0;
                           break label88;
                        }

                        var10 = this.lightUpdateBlockList[var3++];
                        var11 = (var10 & 63) - 32 + var7;
                        var12 = (var10 >> 6 & 63) - 32 + var8;
                        var13 = (var10 >> 12 & 63) - 32 + var9;
                        var14 = var10 >> 18 & 15;
                        var15 = new BlockPos(var11, var12, var13);
                        var16 = this.getLightFor(var1, var15);
                     } while(var16 != var14);

                     this.setLightFor(var1, var15, 0);
                  } while(var14 <= 0);

                  var17 = MathHelper.abs_int(var11 - var7);
                  var18 = MathHelper.abs_int(var12 - var8);
                  var19 = MathHelper.abs_int(var13 - var9);
               } while(var17 + var18 + var19 >= 17);

               BlockPos.MutableBlockPos var20 = new BlockPos.MutableBlockPos();
               EnumFacing[] var24;
               int var23 = (var24 = EnumFacing.values()).length;

               for(int var22 = 0; var22 < var23; ++var22) {
                  EnumFacing var21 = var24[var22];
                  int var25 = var11 + var21.getFrontOffsetX();
                  int var26 = var12 + var21.getFrontOffsetY();
                  int var27 = var13 + var21.getFrontOffsetZ();
                  var20.func_181079_c(var25, var26, var27);
                  int var28 = Math.max(1, this.getBlockState(var20).getBlock().getLightOpacity());
                  var16 = this.getLightFor(var1, var20);
                  if (var16 == var14 - var28 && var4 < this.lightUpdateBlockList.length) {
                     this.lightUpdateBlockList[var4++] = var25 - var7 + 32 | var26 - var8 + 32 << 6 | var27 - var9 + 32 << 12 | var14 - var28 << 18;
                  }
               }
            }
         }

         this.theProfiler.endSection();
         this.theProfiler.startSection("checkedPosition < toCheckCount");

         while(var3 < var4) {
            var10 = this.lightUpdateBlockList[var3++];
            var11 = (var10 & 63) - 32 + var7;
            var12 = (var10 >> 6 & 63) - 32 + var8;
            var13 = (var10 >> 12 & 63) - 32 + var9;
            BlockPos var29 = new BlockPos(var11, var12, var13);
            int var30 = this.getLightFor(var1, var29);
            var16 = this.getRawLight(var29, var1);
            if (var16 != var30) {
               this.setLightFor(var1, var29, var16);
               if (var16 > var30) {
                  var17 = Math.abs(var11 - var7);
                  var18 = Math.abs(var12 - var8);
                  var19 = Math.abs(var13 - var9);
                  boolean var31 = var4 < this.lightUpdateBlockList.length - 6;
                  if (var17 + var18 + var19 < 17 && var31) {
                     if (this.getLightFor(var1, var29.west()) < var16) {
                        this.lightUpdateBlockList[var4++] = var11 - 1 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if (this.getLightFor(var1, var29.east()) < var16) {
                        this.lightUpdateBlockList[var4++] = var11 + 1 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if (this.getLightFor(var1, var29.down()) < var16) {
                        this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 - 1 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if (this.getLightFor(var1, var29.up()) < var16) {
                        this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 + 1 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                     }

                     if (this.getLightFor(var1, var29.north()) < var16) {
                        this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - 1 - var9 + 32 << 12);
                     }

                     if (this.getLightFor(var1, var29.south()) < var16) {
                        this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 + 1 - var9 + 32 << 12);
                     }
                  }
               }
            }
         }

         this.theProfiler.endSection();
         return true;
      }
   }

   public void notifyNeighborsOfStateChange(BlockPos var1, Block var2) {
      this.notifyBlockOfStateChange(var1.west(), var2);
      this.notifyBlockOfStateChange(var1.east(), var2);
      this.notifyBlockOfStateChange(var1.down(), var2);
      this.notifyBlockOfStateChange(var1.up(), var2);
      this.notifyBlockOfStateChange(var1.north(), var2);
      this.notifyBlockOfStateChange(var1.south(), var2);
   }

   protected abstract IChunkProvider createChunkProvider();

   public float getThunderStrength(float var1) {
      return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * var1) * this.getRainStrength(var1);
   }

   public void playSoundEffect(double var1, double var3, double var5, String var7, float var8, float var9) {
      for(int var10 = 0; var10 < this.worldAccesses.size(); ++var10) {
         ((IWorldAccess)this.worldAccesses.get(var10)).playSound(var7, var1, var3, var5, var8, var9);
      }

   }

   public List getEntities(Class var1, Predicate var2) {
      ArrayList var3 = Lists.newArrayList();
      Iterator var5 = this.loadedEntityList.iterator();

      while(var5.hasNext()) {
         Entity var4 = (Entity)var5.next();
         if (var1.isAssignableFrom(var4.getClass()) && var2.apply(var4)) {
            var3.add(var4);
         }
      }

      return var3;
   }

   public boolean checkNoEntityCollision(AxisAlignedBB var1) {
      return this.checkNoEntityCollision(var1, (Entity)null);
   }

   public int getUniqueDataId(String var1) {
      return this.mapStorage.getUniqueDataId(var1);
   }

   public DifficultyInstance getDifficultyForLocation(BlockPos var1) {
      long var2 = 0L;
      float var4 = 0.0F;
      if (this.isBlockLoaded(var1)) {
         var4 = this.getCurrentMoonPhaseFactor();
         var2 = this.getChunkFromBlockCoords(var1).getInhabitedTime();
      }

      return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), var2, var4);
   }

   public List getEntitiesWithinAABBExcludingEntity(Entity var1, AxisAlignedBB var2) {
      return this.getEntitiesInAABBexcluding(var1, var2, EntitySelectors.NOT_SPECTATING);
   }

   public boolean isAreaLoaded(BlockPos var1, BlockPos var2, boolean var3) {
      return this.isAreaLoaded(var1.getX(), var1.getY(), var1.getZ(), var2.getX(), var2.getY(), var2.getZ(), var3);
   }

   public boolean isBlockLoaded(BlockPos var1, boolean var2) {
      return this >= var1 ? false : this.isChunkLoaded(var1.getX() >> 4, var1.getZ() >> 4, var2);
   }

   public void setItemData(String var1, WorldSavedData var2) {
      this.mapStorage.setData(var1, var2);
   }

   public boolean tickUpdates(boolean var1) {
      return false;
   }

   public void loadEntities(Collection var1) {
      this.loadedEntityList.addAll(var1);
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Entity var2 = (Entity)var3.next();
         this.onEntityAdded(var2);
      }

   }

   public BiomeGenBase getBiomeGenForCoords(BlockPos var1) {
      if (this.isBlockLoaded(var1)) {
         Chunk var2 = this.getChunkFromBlockCoords(var1);

         try {
            return var2.getBiome(var1, this.provider.getWorldChunkManager());
         } catch (Throwable var7) {
            CrashReport var4 = CrashReport.makeCrashReport(var7, "Getting biome");
            CrashReportCategory var5 = var4.makeCategory("Coordinates of biome request");
            var5.addCrashSectionCallable("Location", new Callable(this, var1) {
               final World this$0;
               private final BlockPos val$pos;

               {
                  this.this$0 = var1;
                  this.val$pos = var2;
               }

               public String call() throws Exception {
                  return CrashReportCategory.getCoordinateInfo(this.val$pos);
               }

               public Object call() throws Exception {
                  return this.call();
               }
            });
            throw new ReportedException(var4);
         }
      } else {
         return this.provider.getWorldChunkManager().getBiomeGenerator(var1, BiomeGenBase.plains);
      }
   }

   public void setEntityState(Entity var1, byte var2) {
   }

   public boolean isInsideBorder(WorldBorder var1, Entity var2) {
      double var3 = var1.minX();
      double var5 = var1.minZ();
      double var7 = var1.maxX();
      double var9 = var1.maxZ();
      if (var2.isOutsideBorder()) {
         ++var3;
         ++var5;
         --var7;
         --var9;
      } else {
         --var3;
         --var5;
         ++var7;
         ++var9;
      }

      return var2.posX > var3 && var2.posX < var7 && var2.posZ > var5 && var2.posZ < var9;
   }

   public Entity getEntityByID(int var1) {
      return (Entity)this.entitiesById.lookup(var1);
   }

   public boolean extinguishFire(EntityPlayer var1, BlockPos var2, EnumFacing var3) {
      var2 = var2.offset(var3);
      if (this.getBlockState(var2).getBlock() == Blocks.fire) {
         this.playAuxSFXAtEntity(var1, 1004, var2, 0);
         this.setBlockToAir(var2);
         return true;
      } else {
         return false;
      }
   }

   public MovingObjectPosition rayTraceBlocks(Vec3 var1, Vec3 var2, boolean var3, boolean var4, boolean var5) {
      if (!Double.isNaN(var1.xCoord) && !Double.isNaN(var1.yCoord) && !Double.isNaN(var1.zCoord)) {
         if (!Double.isNaN(var2.xCoord) && !Double.isNaN(var2.yCoord) && !Double.isNaN(var2.zCoord)) {
            int var6 = MathHelper.floor_double(var2.xCoord);
            int var7 = MathHelper.floor_double(var2.yCoord);
            int var8 = MathHelper.floor_double(var2.zCoord);
            int var9 = MathHelper.floor_double(var1.xCoord);
            int var10 = MathHelper.floor_double(var1.yCoord);
            int var11 = MathHelper.floor_double(var1.zCoord);
            BlockPos var12 = new BlockPos(var9, var10, var11);
            IBlockState var13 = this.getBlockState(var12);
            Block var14 = var13.getBlock();
            MovingObjectPosition var15;
            if ((!var4 || var14.getCollisionBoundingBox(this, var12, var13) != null) && var14.canCollideCheck(var13, var3)) {
               var15 = var14.collisionRayTrace(this, var12, var1, var2);
               if (var15 != null) {
                  return var15;
               }
            }

            var15 = null;
            int var16 = 200;

            while(var16-- >= 0) {
               if (Double.isNaN(var1.xCoord) || Double.isNaN(var1.yCoord) || Double.isNaN(var1.zCoord)) {
                  return null;
               }

               if (var9 == var6 && var10 == var7 && var11 == var8) {
                  return var5 ? var15 : null;
               }

               boolean var17 = true;
               boolean var18 = true;
               boolean var19 = true;
               double var20 = 999.0D;
               double var22 = 999.0D;
               double var24 = 999.0D;
               if (var6 > var9) {
                  var20 = (double)var9 + 1.0D;
               } else if (var6 < var9) {
                  var20 = (double)var9 + 0.0D;
               } else {
                  var17 = false;
               }

               if (var7 > var10) {
                  var22 = (double)var10 + 1.0D;
               } else if (var7 < var10) {
                  var22 = (double)var10 + 0.0D;
               } else {
                  var18 = false;
               }

               if (var8 > var11) {
                  var24 = (double)var11 + 1.0D;
               } else if (var8 < var11) {
                  var24 = (double)var11 + 0.0D;
               } else {
                  var19 = false;
               }

               double var26 = 999.0D;
               double var28 = 999.0D;
               double var30 = 999.0D;
               double var32 = var2.xCoord - var1.xCoord;
               double var34 = var2.yCoord - var1.yCoord;
               double var36 = var2.zCoord - var1.zCoord;
               if (var17) {
                  var26 = (var20 - var1.xCoord) / var32;
               }

               if (var18) {
                  var28 = (var22 - var1.yCoord) / var34;
               }

               if (var19) {
                  var30 = (var24 - var1.zCoord) / var36;
               }

               if (var26 == -0.0D) {
                  var26 = -1.0E-4D;
               }

               if (var28 == -0.0D) {
                  var28 = -1.0E-4D;
               }

               if (var30 == -0.0D) {
                  var30 = -1.0E-4D;
               }

               EnumFacing var38;
               if (var26 < var28 && var26 < var30) {
                  var38 = var6 > var9 ? EnumFacing.WEST : EnumFacing.EAST;
                  var1 = new Vec3(var20, var1.yCoord + var34 * var26, var1.zCoord + var36 * var26);
               } else if (var28 < var30) {
                  var38 = var7 > var10 ? EnumFacing.DOWN : EnumFacing.UP;
                  var1 = new Vec3(var1.xCoord + var32 * var28, var22, var1.zCoord + var36 * var28);
               } else {
                  var38 = var8 > var11 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                  var1 = new Vec3(var1.xCoord + var32 * var30, var1.yCoord + var34 * var30, var24);
               }

               var9 = MathHelper.floor_double(var1.xCoord) - (var38 == EnumFacing.EAST ? 1 : 0);
               var10 = MathHelper.floor_double(var1.yCoord) - (var38 == EnumFacing.UP ? 1 : 0);
               var11 = MathHelper.floor_double(var1.zCoord) - (var38 == EnumFacing.SOUTH ? 1 : 0);
               var12 = new BlockPos(var9, var10, var11);
               IBlockState var39 = this.getBlockState(var12);
               Block var40 = var39.getBlock();
               if (!var4 || var40.getCollisionBoundingBox(this, var12, var39) != null) {
                  if (var40.canCollideCheck(var39, var3)) {
                     MovingObjectPosition var41 = var40.collisionRayTrace(this, var12, var1, var2);
                     if (var41 != null) {
                        return var41;
                     }
                  } else {
                     var15 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, var1, var38, var12);
                  }
               }
            }

            return var5 ? var15 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public void addBlockEvent(BlockPos var1, Block var2, int var3, int var4) {
      var2.onBlockEventReceived(this, var1, this.getBlockState(var1), var3, var4);
   }

   public float getLightBrightness(BlockPos var1) {
      return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(var1)];
   }

   public EntityPlayer getClosestPlayerToEntity(Entity var1, double var2) {
      return this.getClosestPlayer(var1.posX, var1.posY, var1.posZ, var2);
   }

   public void markBlockForUpdate(BlockPos var1) {
      for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
         ((IWorldAccess)this.worldAccesses.get(var2)).markBlockForUpdate(var1);
      }

   }

   public void playRecord(BlockPos var1, String var2) {
      for(int var3 = 0; var3 < this.worldAccesses.size(); ++var3) {
         ((IWorldAccess)this.worldAccesses.get(var3)).playRecord(var2, var1);
      }

   }

   public long getWorldTime() {
      return this.worldInfo.getWorldTime();
   }

   public boolean canBlockFreezeNoWater(BlockPos var1) {
      return this.canBlockFreeze(var1, true);
   }

   public void sendBlockBreakProgress(int var1, BlockPos var2, int var3) {
      for(int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
         IWorldAccess var5 = (IWorldAccess)this.worldAccesses.get(var4);
         var5.sendBlockBreakProgress(var1, var2, var3);
      }

   }

   public void setLastLightningBolt(int var1) {
      this.lastLightningBolt = var1;
   }

   public void func_181544_b(int var1) {
      this.field_181546_a = var1;
   }

   public void notifyNeighborsOfStateExcept(BlockPos var1, Block var2, EnumFacing var3) {
      if (var3 != EnumFacing.WEST) {
         this.notifyBlockOfStateChange(var1.west(), var2);
      }

      if (var3 != EnumFacing.EAST) {
         this.notifyBlockOfStateChange(var1.east(), var2);
      }

      if (var3 != EnumFacing.DOWN) {
         this.notifyBlockOfStateChange(var1.down(), var2);
      }

      if (var3 != EnumFacing.UP) {
         this.notifyBlockOfStateChange(var1.up(), var2);
      }

      if (var3 != EnumFacing.NORTH) {
         this.notifyBlockOfStateChange(var1.north(), var2);
      }

      if (var3 != EnumFacing.SOUTH) {
         this.notifyBlockOfStateChange(var1.south(), var2);
      }

   }

   public float getStarBrightness(float var1) {
      float var2 = this.getCelestialAngle(var1);
      float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.25F);
      var3 = MathHelper.clamp_float(var3, 0.0F, 1.0F);
      return var3 * var3 * 0.5F;
   }

   public int getActualHeight() {
      return this.provider.getHasNoSky() ? 128 : 256;
   }

   public WorldInfo getWorldInfo() {
      return this.worldInfo;
   }

   public int getLight(BlockPos var1, boolean var2) {
      if (var1.getX() >= -30000000 && var1.getZ() >= -30000000 && var1.getX() < 30000000 && var1.getZ() < 30000000) {
         if (var2 && this.getBlockState(var1).getBlock().getUseNeighborBrightness()) {
            int var8 = this.getLight(var1.up(), false);
            int var4 = this.getLight(var1.east(), false);
            int var5 = this.getLight(var1.west(), false);
            int var6 = this.getLight(var1.south(), false);
            int var7 = this.getLight(var1.north(), false);
            if (var4 > var8) {
               var8 = var4;
            }

            if (var5 > var8) {
               var8 = var5;
            }

            if (var6 > var8) {
               var8 = var6;
            }

            if (var7 > var8) {
               var8 = var7;
            }

            return var8;
         } else if (var1.getY() < 0) {
            return 0;
         } else {
            if (var1.getY() >= 256) {
               var1 = new BlockPos(var1.getX(), 255, var1.getZ());
            }

            Chunk var3 = this.getChunkFromBlockCoords(var1);
            return var3.getLightSubtracted(var1, this.skylightSubtracted);
         }
      } else {
         return 15;
      }
   }

   public void removeEntity(Entity var1) {
      if (var1.riddenByEntity != null) {
         var1.riddenByEntity.mountEntity((Entity)null);
      }

      if (var1.ridingEntity != null) {
         var1.mountEntity((Entity)null);
      }

      var1.setDead();
      if (var1 instanceof EntityPlayer) {
         this.playerEntities.remove(var1);
         this.updateAllPlayersSleepingFlag();
         this.onEntityRemoved(var1);
      }

   }

   public long getTotalWorldTime() {
      return this.worldInfo.getWorldTotalTime();
   }

   public void playSoundAtEntity(Entity var1, String var2, float var3, float var4) {
      for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
         ((IWorldAccess)this.worldAccesses.get(var5)).playSound(var2, var1.posX, var1.posY, var1.posZ, var3, var4);
      }

   }

   public boolean isAreaLoaded(BlockPos var1, int var2) {
      return this.isAreaLoaded(var1, var2, true);
   }

   public void setSkylightSubtracted(int var1) {
      this.skylightSubtracted = var1;
   }

   public void forceBlockUpdateTick(Block var1, BlockPos var2, Random var3) {
      this.scheduledUpdatesAreImmediate = true;
      var1.updateTick(this, var2, this.getBlockState(var2), var3);
      this.scheduledUpdatesAreImmediate = false;
   }

   public void updateEntity(Entity var1) {
      this.updateEntityWithOptionalForce(var1, true);
   }

   public List func_147461_a(AxisAlignedBB var1) {
      ArrayList var2 = Lists.newArrayList();
      int var3 = MathHelper.floor_double(var1.minX);
      int var4 = MathHelper.floor_double(var1.maxX + 1.0D);
      int var5 = MathHelper.floor_double(var1.minY);
      int var6 = MathHelper.floor_double(var1.maxY + 1.0D);
      int var7 = MathHelper.floor_double(var1.minZ);
      int var8 = MathHelper.floor_double(var1.maxZ + 1.0D);
      BlockPos.MutableBlockPos var9 = new BlockPos.MutableBlockPos();

      for(int var10 = var3; var10 < var4; ++var10) {
         for(int var11 = var7; var11 < var8; ++var11) {
            if (this.isBlockLoaded(var9.func_181079_c(var10, 64, var11))) {
               for(int var12 = var5 - 1; var12 < var6; ++var12) {
                  var9.func_181079_c(var10, var12, var11);
                  IBlockState var13;
                  if (var10 >= -30000000 && var10 < 30000000 && var11 >= -30000000 && var11 < 30000000) {
                     var13 = this.getBlockState(var9);
                  } else {
                     var13 = Blocks.bedrock.getDefaultState();
                  }

                  var13.getBlock().addCollisionBoxesToList(this, var9, var13, var1, var2, (Entity)null);
               }
            }
         }
      }

      return var2;
   }

   protected void calculateInitialWeather() {
      if (this.worldInfo.isRaining()) {
         this.rainingStrength = 1.0F;
         if (this.worldInfo.isThundering()) {
            this.thunderingStrength = 1.0F;
         }
      }

   }

   public String getDebugLoadedEntities() {
      return "All: " + this.loadedEntityList.size();
   }

   public void removeTileEntity(BlockPos var1) {
      TileEntity var2 = this.getTileEntity(var1);
      if (var2 != null && this.processingLoadedTiles) {
         var2.invalidate();
         this.addedTileEntityList.remove(var2);
      } else {
         if (var2 != null) {
            this.addedTileEntityList.remove(var2);
            this.loadedTileEntityList.remove(var2);
            this.tickableTileEntities.remove(var2);
         }

         this.getChunkFromBlockCoords(var1).removeTileEntity(var1);
      }

   }

   public WorldSavedData loadItemData(Class var1, String var2) {
      return this.mapStorage.loadData(var1, var2);
   }

   public EntityPlayer getPlayerEntityByName(String var1) {
      for(int var2 = 0; var2 < this.playerEntities.size(); ++var2) {
         EntityPlayer var3 = (EntityPlayer)this.playerEntities.get(var2);
         if (var1.equals(var3.getName())) {
            return var3;
         }
      }

      return null;
   }

   protected abstract int getRenderDistanceChunks();

   public MovingObjectPosition rayTraceBlocks(Vec3 var1, Vec3 var2, boolean var3) {
      return this.rayTraceBlocks(var1, var2, var3, false, false);
   }

   public Chunk getChunkFromBlockCoords(BlockPos var1) {
      return this.getChunkFromChunkCoords(var1.getX() >> 4, var1.getZ() >> 4);
   }

   public TileEntity getTileEntity(BlockPos var1) {
      if (this >= var1) {
         return null;
      } else {
         TileEntity var2 = null;
         int var3;
         TileEntity var4;
         if (this.processingLoadedTiles) {
            for(var3 = 0; var3 < this.addedTileEntityList.size(); ++var3) {
               var4 = (TileEntity)this.addedTileEntityList.get(var3);
               if (!var4.isInvalid() && var4.getPos().equals(var1)) {
                  var2 = var4;
                  break;
               }
            }
         }

         if (var2 == null) {
            var2 = this.getChunkFromBlockCoords(var1).getTileEntity(var1, Chunk.EnumCreateEntityType.IMMEDIATE);
         }

         if (var2 == null) {
            for(var3 = 0; var3 < this.addedTileEntityList.size(); ++var3) {
               var4 = (TileEntity)this.addedTileEntityList.get(var3);
               if (!var4.isInvalid() && var4.getPos().equals(var1)) {
                  var2 = var4;
                  break;
               }
            }
         }

         return var2;
      }
   }

   public EntityPlayer getClosestPlayer(double var1, double var3, double var5, double var7) {
      double var9 = -1.0D;
      EntityPlayer var11 = null;

      for(int var12 = 0; var12 < this.playerEntities.size(); ++var12) {
         EntityPlayer var13 = (EntityPlayer)this.playerEntities.get(var12);
         if (EntitySelectors.NOT_SPECTATING.apply(var13)) {
            double var14 = var13.getDistanceSq(var1, var3, var5);
            if ((var7 < 0.0D || var14 < var7 * var7) && (var9 == -1.0D || var14 < var9)) {
               var9 = var14;
               var11 = var13;
            }
         }
      }

      return var11;
   }

   public boolean isBlockTickPending(BlockPos var1, Block var2) {
      return false;
   }

   public void spawnParticle(EnumParticleTypes var1, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, int... var15) {
      this.spawnParticle(var1.getParticleID(), var1.getShouldIgnoreRange() | var2, var3, var5, var7, var9, var11, var13, var15);
   }

   public int getChunksLowestHorizon(int var1, int var2) {
      if (var1 >= -30000000 && var2 >= -30000000 && var1 < 30000000 && var2 < 30000000) {
         int var10001 = var1 >> 4;
         int var10002 = var2 >> 4;
         return 0;
      } else {
         return this.func_181545_F() + 1;
      }
   }

   public void spawnParticle(EnumParticleTypes var1, double var2, double var4, double var6, double var8, double var10, double var12, int... var14) {
      this.spawnParticle(var1.getParticleID(), var1.getShouldIgnoreRange(), var2, var4, var6, var8, var10, var12, var14);
   }

   public void joinEntityInSurroundings(Entity var1) {
      int var2 = MathHelper.floor_double(var1.posX / 16.0D);
      int var3 = MathHelper.floor_double(var1.posZ / 16.0D);
      byte var4 = 2;

      for(int var5 = var2 - var4; var5 <= var2 + var4; ++var5) {
         for(int var6 = var3 - var4; var6 <= var3 + var4; ++var6) {
            this.getChunkFromChunkCoords(var5, var6);
         }
      }

      if (!this.loadedEntityList.contains(var1)) {
         this.loadedEntityList.add(var1);
      }

   }

   protected void updateWeather() {
      if (!this.provider.getHasNoSky() && !this.isRemote) {
         int var1 = this.worldInfo.getCleanWeatherTime();
         if (var1 > 0) {
            --var1;
            this.worldInfo.setCleanWeatherTime(var1);
            this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
            this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
         }

         int var2 = this.worldInfo.getThunderTime();
         if (var2 <= 0) {
            if (this.worldInfo.isThundering()) {
               this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
            } else {
               this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
            }
         } else {
            --var2;
            this.worldInfo.setThunderTime(var2);
            if (var2 <= 0) {
               this.worldInfo.setThundering(!this.worldInfo.isThundering());
            }
         }

         this.prevThunderingStrength = this.thunderingStrength;
         if (this.worldInfo.isThundering()) {
            this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
         } else {
            this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
         }

         this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0F, 1.0F);
         int var3 = this.worldInfo.getRainTime();
         if (var3 <= 0) {
            if (this.worldInfo.isRaining()) {
               this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
            } else {
               this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
            }
         } else {
            --var3;
            this.worldInfo.setRainTime(var3);
            if (var3 <= 0) {
               this.worldInfo.setRaining(!this.worldInfo.isRaining());
            }
         }

         this.prevRainingStrength = this.rainingStrength;
         if (this.worldInfo.isRaining()) {
            this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
         } else {
            this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
         }

         this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0F, 1.0F);
      }

   }

   public Explosion newExplosion(Entity var1, double var2, double var4, double var6, float var8, boolean var9, boolean var10) {
      Explosion var11 = new Explosion(this, var1, var2, var4, var6, var8, var9, var10);
      var11.doExplosionA();
      var11.doExplosionB(true);
      return var11;
   }

   public boolean destroyBlock(BlockPos var1, boolean var2) {
      IBlockState var3 = this.getBlockState(var1);
      Block var4 = var3.getBlock();
      if (var4.getMaterial() == Material.air) {
         return false;
      } else {
         this.playAuxSFX(2001, var1, Block.getStateId(var3));
         if (var2) {
            var4.dropBlockAsItem(this, var1, var3, 0);
         }

         return this.setBlockState(var1, Blocks.air.getDefaultState(), 3);
      }
   }

   public int getLightFromNeighborsFor(EnumSkyBlock var1, BlockPos var2) {
      if (this.provider.getHasNoSky() && var1 == EnumSkyBlock.SKY) {
         return 0;
      } else {
         if (var2.getY() < 0) {
            var2 = new BlockPos(var2.getX(), 0, var2.getZ());
         }

         if (this >= var2) {
            return var1.defaultLightValue;
         } else if (!this.isBlockLoaded(var2)) {
            return var1.defaultLightValue;
         } else if (this.getBlockState(var2).getBlock().getUseNeighborBrightness()) {
            int var8 = this.getLightFor(var1, var2.up());
            int var4 = this.getLightFor(var1, var2.east());
            int var5 = this.getLightFor(var1, var2.west());
            int var6 = this.getLightFor(var1, var2.south());
            int var7 = this.getLightFor(var1, var2.north());
            if (var4 > var8) {
               var8 = var4;
            }

            if (var5 > var8) {
               var8 = var5;
            }

            if (var6 > var8) {
               var8 = var6;
            }

            if (var7 > var8) {
               var8 = var7;
            }

            return var8;
         } else {
            Chunk var3 = this.getChunkFromBlockCoords(var2);
            return var3.getLightFor(var1, var2);
         }
      }
   }

   public boolean canBlockBePlaced(Block var1, BlockPos var2, boolean var3, EnumFacing var4, Entity var5, ItemStack var6) {
      Block var7 = this.getBlockState(var2).getBlock();
      AxisAlignedBB var10000;
      Object var10002;
      if (var3) {
         var10000 = null;
      } else {
         var10002 = var2;
         var10000 = var1.getCollisionBoundingBox(this, var2, var1.getDefaultState());
      }

      AxisAlignedBB var8 = var10000;
      if (var8 != null) {
         var10002 = var5;
         if (var5 == false) {
            var10002 = false;
            return (boolean)var10002;
         }
      }

      boolean var9;
      if (var7.getMaterial() == Material.circuits && var1 == Blocks.anvil) {
         var9 = true;
      } else {
         if (var7.getMaterial().isReplaceable()) {
            var10002 = var2;
            if (var1.canReplace(this, var2, var4, var6)) {
               var9 = true;
               return (boolean)var10002;
            }
         }

         var9 = false;
      }

      return (boolean)var10002;
   }

   public float getSunBrightness(float var1) {
      float var2 = this.getCelestialAngle(var1);
      float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.2F);
      var3 = MathHelper.clamp_float(var3, 0.0F, 1.0F);
      var3 = 1.0F - var3;
      var3 = (float)((double)var3 * (1.0D - (double)(this.getRainStrength(var1) * 5.0F) / 16.0D));
      var3 = (float)((double)var3 * (1.0D - (double)(this.getThunderStrength(var1) * 5.0F) / 16.0D));
      return var3 * 0.8F + 0.2F;
   }

   public List getLoadedEntityList() {
      return this.loadedEntityList;
   }

   public boolean isThundering() {
      return (double)this.getThunderStrength(1.0F) > 0.9D;
   }

   public void setAllowedSpawnTypes(boolean var1, boolean var2) {
      this.spawnHostileMobs = var1;
      this.spawnPeacefulMobs = var2;
   }

   public Explosion createExplosion(Entity var1, double var2, double var4, double var6, float var8, boolean var9) {
      return this.newExplosion(var1, var2, var4, var6, var8, false, var9);
   }

   public int getMoonPhase() {
      return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
   }

   public int func_181545_F() {
      return this.field_181546_a;
   }

   public int getLight(BlockPos var1) {
      if (var1.getY() < 0) {
         return 0;
      } else {
         if (var1.getY() >= 256) {
            var1 = new BlockPos(var1.getX(), 255, var1.getZ());
         }

         return this.getChunkFromBlockCoords(var1).getLightSubtracted(var1, 0);
      }
   }

   public boolean isAreaLoaded(StructureBoundingBox var1, boolean var2) {
      return this.isAreaLoaded(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ, var2);
   }

   public int getRedstonePower(BlockPos var1, EnumFacing var2) {
      IBlockState var3 = this.getBlockState(var1);
      Block var4 = var3.getBlock();
      return var4.isNormalCube() ? this.getStrongPower(var1) : var4.getWeakPower(this, var1, var3, var2);
   }

   public float getBlockDensity(Vec3 var1, AxisAlignedBB var2) {
      double var3 = 1.0D / ((var2.maxX - var2.minX) * 2.0D + 1.0D);
      double var5 = 1.0D / ((var2.maxY - var2.minY) * 2.0D + 1.0D);
      double var7 = 1.0D / ((var2.maxZ - var2.minZ) * 2.0D + 1.0D);
      double var9 = (1.0D - Math.floor(1.0D / var3) * var3) / 2.0D;
      double var11 = (1.0D - Math.floor(1.0D / var7) * var7) / 2.0D;
      if (var3 >= 0.0D && var5 >= 0.0D && var7 >= 0.0D) {
         int var13 = 0;
         int var14 = 0;

         for(float var15 = 0.0F; var15 <= 1.0F; var15 = (float)((double)var15 + var3)) {
            for(float var16 = 0.0F; var16 <= 1.0F; var16 = (float)((double)var16 + var5)) {
               for(float var17 = 0.0F; var17 <= 1.0F; var17 = (float)((double)var17 + var7)) {
                  double var18 = var2.minX + (var2.maxX - var2.minX) * (double)var15;
                  double var20 = var2.minY + (var2.maxY - var2.minY) * (double)var16;
                  double var22 = var2.minZ + (var2.maxZ - var2.minZ) * (double)var17;
                  if (this.rayTraceBlocks(new Vec3(var18 + var9, var20, var22 + var11), var1) == null) {
                     ++var13;
                  }

                  ++var14;
               }
            }
         }

         return (float)var13 / (float)var14;
      } else {
         return 0.0F;
      }
   }

   public EntityPlayer getPlayerEntityByUUID(UUID var1) {
      for(int var2 = 0; var2 < this.playerEntities.size(); ++var2) {
         EntityPlayer var3 = (EntityPlayer)this.playerEntities.get(var2);
         if (var1.equals(var3.getUniqueID())) {
            return var3;
         }
      }

      return null;
   }

   public BlockPos getStrongholdPos(String var1, BlockPos var2) {
      return this.getChunkProvider().getStrongholdGen(this, var1, var2);
   }

   public void markBlocksDirtyVertical(int var1, int var2, int var3, int var4) {
      int var5;
      if (var3 > var4) {
         var5 = var4;
         var4 = var3;
         var3 = var5;
      }

      if (!this.provider.getHasNoSky()) {
         for(var5 = var3; var5 <= var4; ++var5) {
            this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(var1, var5, var2));
         }
      }

      this.markBlockRangeForRenderUpdate(var1, var3, var2, var1, var4, var2);
   }

   public boolean isFlammableWithin(AxisAlignedBB var1) {
      int var2 = MathHelper.floor_double(var1.minX);
      int var3 = MathHelper.floor_double(var1.maxX + 1.0D);
      int var4 = MathHelper.floor_double(var1.minY);
      int var5 = MathHelper.floor_double(var1.maxY + 1.0D);
      int var6 = MathHelper.floor_double(var1.minZ);
      int var7 = MathHelper.floor_double(var1.maxZ + 1.0D);
      BlockPos.MutableBlockPos var8 = new BlockPos.MutableBlockPos();

      for(int var9 = var2; var9 < var3; ++var9) {
         for(int var10 = var4; var10 < var5; ++var10) {
            for(int var11 = var6; var11 < var7; ++var11) {
               Block var12 = this.getBlockState(var8.func_181079_c(var9, var10, var11)).getBlock();
               if (var12 == Blocks.fire || var12 == Blocks.flowing_lava || var12 == Blocks.lava) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void playBroadcastSound(int var1, BlockPos var2, int var3) {
      for(int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
         ((IWorldAccess)this.worldAccesses.get(var4)).broadcastSound(var1, var2, var3);
      }

   }

   public List getEntitiesWithinAABB(Class var1, AxisAlignedBB var2, Predicate var3) {
      int var4 = MathHelper.floor_double((var2.minX - 2.0D) / 16.0D);
      int var5 = MathHelper.floor_double((var2.maxX + 2.0D) / 16.0D);
      int var6 = MathHelper.floor_double((var2.minZ - 2.0D) / 16.0D);
      int var7 = MathHelper.floor_double((var2.maxZ + 2.0D) / 16.0D);
      ArrayList var8 = Lists.newArrayList();

      for(int var9 = var4; var9 <= var5; ++var9) {
         for(int var10 = var6; var10 <= var7; ++var10) {
            this.getChunkFromChunkCoords(var9, var10).getEntitiesOfTypeWithinAAAB(var1, var2, var8, var3);
         }
      }

      return var8;
   }

   public int calculateSkylightSubtracted(float var1) {
      float var2 = this.getCelestialAngle(var1);
      float var3 = 1.0F - (MathHelper.cos(var2 * 3.1415927F * 2.0F) * 2.0F + 0.5F);
      var3 = MathHelper.clamp_float(var3, 0.0F, 1.0F);
      var3 = 1.0F - var3;
      var3 = (float)((double)var3 * (1.0D - (double)(this.getRainStrength(var1) * 5.0F) / 16.0D));
      var3 = (float)((double)var3 * (1.0D - (double)(this.getThunderStrength(var1) * 5.0F) / 16.0D));
      var3 = 1.0F - var3;
      return (int)(var3 * 11.0F);
   }

   public boolean addWeatherEffect(Entity var1) {
      this.weatherEffects.add(var1);
      return true;
   }

   public void markBlockRangeForRenderUpdate(int var1, int var2, int var3, int var4, int var5, int var6) {
      for(int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
         ((IWorldAccess)this.worldAccesses.get(var7)).markBlockRangeForRenderUpdate(var1, var2, var3, var4, var5, var6);
      }

   }

   public void playSound(double var1, double var3, double var5, String var7, float var8, float var9, boolean var10) {
   }

   public boolean isAreaLoaded(StructureBoundingBox var1) {
      return this.isAreaLoaded(var1, true);
   }

   public void scheduleUpdate(BlockPos var1, Block var2, int var3) {
   }

   public boolean isMaterialInBB(AxisAlignedBB var1, Material var2) {
      int var3 = MathHelper.floor_double(var1.minX);
      int var4 = MathHelper.floor_double(var1.maxX + 1.0D);
      int var5 = MathHelper.floor_double(var1.minY);
      int var6 = MathHelper.floor_double(var1.maxY + 1.0D);
      int var7 = MathHelper.floor_double(var1.minZ);
      int var8 = MathHelper.floor_double(var1.maxZ + 1.0D);
      BlockPos.MutableBlockPos var9 = new BlockPos.MutableBlockPos();

      for(int var10 = var3; var10 < var4; ++var10) {
         for(int var11 = var5; var11 < var6; ++var11) {
            for(int var12 = var7; var12 < var8; ++var12) {
               if (this.getBlockState(var9.func_181079_c(var10, var11, var12)).getBlock().getMaterial() == var2) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public Random setRandomSeed(int var1, int var2, int var3) {
      long var4 = (long)var1 * 341873128712L + (long)var2 * 132897987541L + this.getWorldInfo().getSeed() + (long)var3;
      this.rand.setSeed(var4);
      return this.rand;
   }

   public void sendQuittingDisconnectingPacket() {
   }

   public boolean isDaytime() {
      return this.skylightSubtracted < 4;
   }

   public List getEntitiesInAABBexcluding(Entity var1, AxisAlignedBB var2, Predicate var3) {
      ArrayList var4 = Lists.newArrayList();
      int var5 = MathHelper.floor_double((var2.minX - 2.0D) / 16.0D);
      int var6 = MathHelper.floor_double((var2.maxX + 2.0D) / 16.0D);
      int var7 = MathHelper.floor_double((var2.minZ - 2.0D) / 16.0D);
      int var8 = MathHelper.floor_double((var2.maxZ + 2.0D) / 16.0D);

      for(int var9 = var5; var9 <= var6; ++var9) {
         for(int var10 = var7; var10 <= var8; ++var10) {
            this.getChunkFromChunkCoords(var9, var10).getEntitiesWithinAABBForEntity(var1, var2, var4, var3);
         }
      }

      return var4;
   }

   public Vec3 getSkyColor(Entity var1, float var2) {
      float var3 = this.getCelestialAngle(var2);
      float var4 = MathHelper.cos(var3 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      var4 = MathHelper.clamp_float(var4, 0.0F, 1.0F);
      int var5 = MathHelper.floor_double(var1.posX);
      int var6 = MathHelper.floor_double(var1.posY);
      int var7 = MathHelper.floor_double(var1.posZ);
      BlockPos var8 = new BlockPos(var5, var6, var7);
      BiomeGenBase var9 = this.getBiomeGenForCoords(var8);
      float var10 = var9.getFloatTemperature(var8);
      int var11 = var9.getSkyColorByTemp(var10);
      float var12 = (float)(var11 >> 16 & 255) / 255.0F;
      float var13 = (float)(var11 >> 8 & 255) / 255.0F;
      float var14 = (float)(var11 & 255) / 255.0F;
      var12 *= var4;
      var13 *= var4;
      var14 *= var4;
      float var15 = this.getRainStrength(var2);
      float var16;
      float var17;
      if (var15 > 0.0F) {
         var16 = (var12 * 0.3F + var13 * 0.59F + var14 * 0.11F) * 0.6F;
         var17 = 1.0F - var15 * 0.75F;
         var12 = var12 * var17 + var16 * (1.0F - var17);
         var13 = var13 * var17 + var16 * (1.0F - var17);
         var14 = var14 * var17 + var16 * (1.0F - var17);
      }

      var16 = this.getThunderStrength(var2);
      if (var16 > 0.0F) {
         var17 = (var12 * 0.3F + var13 * 0.59F + var14 * 0.11F) * 0.2F;
         float var18 = 1.0F - var16 * 0.75F;
         var12 = var12 * var18 + var17 * (1.0F - var18);
         var13 = var13 * var18 + var17 * (1.0F - var18);
         var14 = var14 * var18 + var17 * (1.0F - var18);
      }

      if (this.lastLightningBolt > 0) {
         var17 = (float)this.lastLightningBolt - var2;
         if (var17 > 1.0F) {
            var17 = 1.0F;
         }

         var17 *= 0.45F;
         var12 = var12 * (1.0F - var17) + 0.8F * var17;
         var13 = var13 * (1.0F - var17) + 0.8F * var17;
         var14 = var14 * (1.0F - var17) + 1.0F * var17;
      }

      return Ceu.Ativo ? new Vec3(Ceu.Vermelho, Ceu.Verde, Ceu.Azul) : new Vec3((double)var12, (double)var13, (double)var14);
   }

   protected World(ISaveHandler var1, WorldInfo var2, WorldProvider var3, Profiler var4, boolean var5) {
      this.ambientTickCountdown = this.rand.nextInt(12000);
      this.spawnHostileMobs = true;
      this.spawnPeacefulMobs = true;
      this.lightUpdateBlockList = new int['耀'];
      this.saveHandler = var1;
      this.theProfiler = var4;
      this.worldInfo = var2;
      this.provider = var3;
      this.isRemote = var5;
      this.worldBorder = var3.getWorldBorder();
   }

   public int getCombinedLight(BlockPos var1, int var2) {
      int var3 = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, var1);
      int var4 = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, var1);
      if (var4 < var2) {
         var4 = var2;
      }

      return var3 << 20 | var4 << 4;
   }

   public int getSkylightSubtracted() {
      return this.skylightSubtracted;
   }

   public Entity findNearestEntityWithinAABB(Class var1, AxisAlignedBB var2, Entity var3) {
      List var4 = this.getEntitiesWithinAABB(var1, var2);
      Entity var5 = null;
      double var6 = Double.MAX_VALUE;

      for(int var8 = 0; var8 < var4.size(); ++var8) {
         Entity var9 = (Entity)var4.get(var8);
         if (var9 != var3 && EntitySelectors.NOT_SPECTATING.apply(var9)) {
            double var10 = var3.getDistanceSqToEntity(var9);
            if (var10 <= var6) {
               var5 = var9;
               var6 = var10;
            }
         }
      }

      return var5;
   }

   public MovingObjectPosition rayTraceBlocks(Vec3 var1, Vec3 var2) {
      return this.rayTraceBlocks(var1, var2, false, false, false);
   }

   public float getRainStrength(float var1) {
      return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * var1;
   }

   public void updateEntityWithOptionalForce(Entity var1, boolean var2) {
      int var3 = MathHelper.floor_double(var1.posX);
      int var4 = MathHelper.floor_double(var1.posZ);
      byte var5 = 32;
      int var10001;
      if (var2) {
         var10001 = var3 - var5;
         boolean var10002 = false;
         int var10003 = var4 - var5;
         int var10004 = var3 + var5;
         boolean var10005 = false;
         int var10006 = var4 + var5;
      }

      var1.lastTickPosX = var1.posX;
      var1.lastTickPosY = var1.posY;
      var1.lastTickPosZ = var1.posZ;
      var1.prevRotationYaw = var1.rotationYaw;
      var1.prevRotationPitch = var1.rotationPitch;
      if (var2 && var1.addedToChunk) {
         ++var1.ticksExisted;
         if (var1.ridingEntity != null) {
            var1.updateRidden();
         } else {
            var1.onUpdate();
         }
      }

      this.theProfiler.startSection("chunkCheck");
      if (Double.isNaN(var1.posX) || Double.isInfinite(var1.posX)) {
         var1.posX = var1.lastTickPosX;
      }

      if (Double.isNaN(var1.posY) || Double.isInfinite(var1.posY)) {
         var1.posY = var1.lastTickPosY;
      }

      if (Double.isNaN(var1.posZ) || Double.isInfinite(var1.posZ)) {
         var1.posZ = var1.lastTickPosZ;
      }

      if (Double.isNaN((double)var1.rotationPitch) || Double.isInfinite((double)var1.rotationPitch)) {
         var1.rotationPitch = var1.prevRotationPitch;
      }

      if (Double.isNaN((double)var1.rotationYaw) || Double.isInfinite((double)var1.rotationYaw)) {
         var1.rotationYaw = var1.prevRotationYaw;
      }

      int var6 = MathHelper.floor_double(var1.posX / 16.0D);
      int var7 = MathHelper.floor_double(var1.posY / 16.0D);
      int var8 = MathHelper.floor_double(var1.posZ / 16.0D);
      if (!var1.addedToChunk || var1.chunkCoordX != var6 || var1.chunkCoordY != var7 || var1.chunkCoordZ != var8) {
         if (var1.addedToChunk) {
            var10001 = var1.chunkCoordX;
            int var9 = var1.chunkCoordZ;
            this.getChunkFromChunkCoords(var1.chunkCoordX, var1.chunkCoordZ).removeEntityAtIndex(var1, var1.chunkCoordY);
         }

         var1.addedToChunk = true;
         this.getChunkFromChunkCoords(var6, var8).addEntity(var1);
      }

      this.theProfiler.endSection();
      if (var2 && var1.addedToChunk && var1.riddenByEntity != null) {
         if (!var1.riddenByEntity.isDead && var1.riddenByEntity.ridingEntity == var1) {
            this.updateEntity(var1.riddenByEntity);
         } else {
            var1.riddenByEntity.ridingEntity = null;
            var1.riddenByEntity = null;
         }
      }

   }

   public boolean setBlockState(BlockPos var1, IBlockState var2, int var3) {
      if (this >= var1) {
         return false;
      } else if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
         return false;
      } else {
         Chunk var4 = this.getChunkFromBlockCoords(var1);
         Block var5 = var2.getBlock();
         IBlockState var6 = var4.setBlockState(var1, var2);
         if (var6 == null) {
            return false;
         } else {
            Block var7 = var6.getBlock();
            if (var5.getLightOpacity() != var7.getLightOpacity() || var5.getLightValue() != var7.getLightValue()) {
               this.theProfiler.startSection("checkLight");
               this.checkLight(var1);
               this.theProfiler.endSection();
            }

            if ((var3 & 2) != 0 && (!this.isRemote || (var3 & 4) == 0) && var4.isPopulated()) {
               this.markBlockForUpdate(var1);
            }

            if (!this.isRemote && (var3 & 1) != 0) {
               this.notifyNeighborsRespectDebug(var1, var6.getBlock());
               if (var5.hasComparatorInputOverride()) {
                  this.updateComparatorOutputLevel(var1, var5);
               }
            }

            return true;
         }
      }
   }

   public BlockPos getPrecipitationHeight(BlockPos var1) {
      return this.getChunkFromBlockCoords(var1).getPrecipitationHeight(var1);
   }

   public float getCelestialAngleRadians(float var1) {
      float var2 = this.getCelestialAngle(var1);
      return var2 * 3.1415927F * 2.0F;
   }

   public int getStrongPower(BlockPos var1) {
      byte var2 = 0;
      int var3 = Math.max(var2, this.getStrongPower(var1.down(), EnumFacing.DOWN));
      if (var3 >= 15) {
         return var3;
      } else {
         var3 = Math.max(var3, this.getStrongPower(var1.up(), EnumFacing.UP));
         if (var3 >= 15) {
            return var3;
         } else {
            var3 = Math.max(var3, this.getStrongPower(var1.north(), EnumFacing.NORTH));
            if (var3 >= 15) {
               return var3;
            } else {
               var3 = Math.max(var3, this.getStrongPower(var1.south(), EnumFacing.SOUTH));
               if (var3 >= 15) {
                  return var3;
               } else {
                  var3 = Math.max(var3, this.getStrongPower(var1.west(), EnumFacing.WEST));
                  if (var3 >= 15) {
                     return var3;
                  } else {
                     var3 = Math.max(var3, this.getStrongPower(var1.east(), EnumFacing.EAST));
                     return var3 >= 15 ? var3 : var3;
                  }
               }
            }
         }
      }
   }

   protected void setActivePlayerChunksAndCheckLight() {
      this.activeChunkSet.clear();
      this.theProfiler.startSection("buildList");

      int var1;
      EntityPlayer var2;
      int var3;
      int var4;
      int var5;
      for(var1 = 0; var1 < this.playerEntities.size(); ++var1) {
         var2 = (EntityPlayer)this.playerEntities.get(var1);
         var3 = MathHelper.floor_double(var2.posX / 16.0D);
         var4 = MathHelper.floor_double(var2.posZ / 16.0D);
         var5 = this.getRenderDistanceChunks();

         for(int var6 = -var5; var6 <= var5; ++var6) {
            for(int var7 = -var5; var7 <= var5; ++var7) {
               this.activeChunkSet.add(new ChunkCoordIntPair(var6 + var3, var7 + var4));
            }
         }
      }

      this.theProfiler.endSection();
      if (this.ambientTickCountdown > 0) {
         --this.ambientTickCountdown;
      }

      this.theProfiler.startSection("playerCheckLight");
      if (!this.playerEntities.isEmpty()) {
         var1 = this.rand.nextInt(this.playerEntities.size());
         var2 = (EntityPlayer)this.playerEntities.get(var1);
         var3 = MathHelper.floor_double(var2.posX) + this.rand.nextInt(11) - 5;
         var4 = MathHelper.floor_double(var2.posY) + this.rand.nextInt(11) - 5;
         var5 = MathHelper.floor_double(var2.posZ) + this.rand.nextInt(11) - 5;
         this.checkLight(new BlockPos(var3, var4, var5));
      }

      this.theProfiler.endSection();
   }

   public void playAuxSFXAtEntity(EntityPlayer var1, int var2, BlockPos var3, int var4) {
      try {
         for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
            ((IWorldAccess)this.worldAccesses.get(var5)).playAuxSFX(var1, var2, var3, var4);
         }

      } catch (Throwable var9) {
         CrashReport var6 = CrashReport.makeCrashReport(var9, "Playing level event");
         CrashReportCategory var7 = var6.makeCategory("Level event being played");
         var7.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(var3));
         var7.addCrashSection("Event source", var1);
         var7.addCrashSection("Event type", var2);
         var7.addCrashSection("Event data", var4);
         throw new ReportedException(var6);
      }
   }

   public Block getGroundAboveSeaLevel(BlockPos var1) {
      BlockPos var2;
      for(var2 = new BlockPos(var1.getX(), this.func_181545_F(), var1.getZ()); this != var2.up(); var2 = var2.up()) {
      }

      return this.getBlockState(var2).getBlock();
   }

   public void addWorldAccess(IWorldAccess var1) {
      this.worldAccesses.add(var1);
   }

   public void updateEntities() {
      this.theProfiler.startSection("entities");
      this.theProfiler.startSection("global");

      int var1;
      Entity var2;
      CrashReport var4;
      CrashReportCategory var5;
      for(var1 = 0; var1 < this.weatherEffects.size(); ++var1) {
         var2 = (Entity)this.weatherEffects.get(var1);

         try {
            ++var2.ticksExisted;
            var2.onUpdate();
         } catch (Throwable var9) {
            var4 = CrashReport.makeCrashReport(var9, "Ticking entity");
            var5 = var4.makeCategory("Entity being ticked");
            if (var2 == null) {
               var5.addCrashSection("Entity", "~~NULL~~");
            } else {
               var2.addEntityCrashInfo(var5);
            }

            throw new ReportedException(var4);
         }

         if (var2.isDead) {
            this.weatherEffects.remove(var1--);
         }
      }

      this.theProfiler.endStartSection("remove");
      this.loadedEntityList.removeAll(this.unloadedEntityList);

      int var3;
      int var14;
      for(var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
         var2 = (Entity)this.unloadedEntityList.get(var1);
         var3 = var2.chunkCoordX;
         var14 = var2.chunkCoordZ;
         if (var2.addedToChunk && true) {
            this.getChunkFromChunkCoords(var3, var14).removeEntity(var2);
         }
      }

      for(var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
         this.onEntityRemoved((Entity)this.unloadedEntityList.get(var1));
      }

      this.unloadedEntityList.clear();
      this.theProfiler.endStartSection("regular");

      for(var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
         var2 = (Entity)this.loadedEntityList.get(var1);
         if (var2.ridingEntity != null) {
            if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
               continue;
            }

            var2.ridingEntity.riddenByEntity = null;
            var2.ridingEntity = null;
         }

         this.theProfiler.startSection("tick");
         if (!var2.isDead) {
            try {
               this.updateEntity(var2);
            } catch (Throwable var8) {
               var4 = CrashReport.makeCrashReport(var8, "Ticking entity");
               var5 = var4.makeCategory("Entity being ticked");
               var2.addEntityCrashInfo(var5);
               throw new ReportedException(var4);
            }
         }

         this.theProfiler.endSection();
         this.theProfiler.startSection("remove");
         if (var2.isDead) {
            var3 = var2.chunkCoordX;
            var14 = var2.chunkCoordZ;
            if (var2.addedToChunk && true) {
               this.getChunkFromChunkCoords(var3, var14).removeEntity(var2);
            }

            this.loadedEntityList.remove(var1--);
            this.onEntityRemoved(var2);
         }

         this.theProfiler.endSection();
      }

      this.theProfiler.endStartSection("blockEntities");
      this.processingLoadedTiles = true;
      Iterator var15 = this.tickableTileEntities.iterator();

      while(var15.hasNext()) {
         TileEntity var10 = (TileEntity)var15.next();
         if (!var10.isInvalid() && var10.hasWorldObj()) {
            BlockPos var12 = var10.getPos();
            if (this.isBlockLoaded(var12) && this.worldBorder.contains(var12)) {
               try {
                  ((ITickable)var10).update();
               } catch (Throwable var7) {
                  CrashReport var16 = CrashReport.makeCrashReport(var7, "Ticking block entity");
                  CrashReportCategory var6 = var16.makeCategory("Block entity being ticked");
                  var10.addInfoToCrashReport(var6);
                  throw new ReportedException(var16);
               }
            }
         }

         if (var10.isInvalid()) {
            var15.remove();
            this.loadedTileEntityList.remove(var10);
            if (this.isBlockLoaded(var10.getPos())) {
               this.getChunkFromBlockCoords(var10.getPos()).removeTileEntity(var10.getPos());
            }
         }
      }

      this.processingLoadedTiles = false;
      if (!this.tileEntitiesToBeRemoved.isEmpty()) {
         this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
         this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
         this.tileEntitiesToBeRemoved.clear();
      }

      this.theProfiler.endStartSection("pendingBlockEntities");
      if (!this.addedTileEntityList.isEmpty()) {
         for(int var11 = 0; var11 < this.addedTileEntityList.size(); ++var11) {
            TileEntity var13 = (TileEntity)this.addedTileEntityList.get(var11);
            if (!var13.isInvalid()) {
               if (!this.loadedTileEntityList.contains(var13)) {
                  this.addTileEntity(var13);
               }

               if (this.isBlockLoaded(var13.getPos())) {
                  this.getChunkFromBlockCoords(var13.getPos()).addTileEntity(var13.getPos(), var13);
               }

               this.markBlockForUpdate(var13.getPos());
            }
         }

         this.addedTileEntityList.clear();
      }

      this.theProfiler.endSection();
      this.theProfiler.endSection();
   }

   protected void updateBlocks() {
      this.setActivePlayerChunksAndCheckLight();
   }

   public BlockPos getSpawnPoint() {
      BlockPos var1 = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
      if (!this.getWorldBorder().contains(var1)) {
         var1 = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
      }

      return var1;
   }

   public MapStorage getMapStorage() {
      return this.mapStorage;
   }

   protected void onEntityAdded(Entity var1) {
      for(int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
         ((IWorldAccess)this.worldAccesses.get(var2)).onEntityAdded(var1);
      }

   }

   public void initialize(WorldSettings var1) {
      this.worldInfo.setServerInitialized(true);
   }

   public void markTileEntityForRemoval(TileEntity var1) {
      this.tileEntitiesToBeRemoved.add(var1);
   }

   public Chunk getChunkFromChunkCoords(int var1, int var2) {
      return this.chunkProvider.provideChunk(var1, var2);
   }

   public boolean isSpawnChunk(int var1, int var2) {
      BlockPos var3 = this.getSpawnPoint();
      int var4 = var1 * 16 + 8 - var3.getX();
      int var5 = var2 * 16 + 8 - var3.getZ();
      short var6 = 128;
      return var4 >= -var6 && var4 <= var6 && var5 >= -var6 && var5 <= var6;
   }

   public Scoreboard getScoreboard() {
      return this.worldScoreboard;
   }

   public boolean isBlockNormalCube(BlockPos var1, boolean var2) {
      if (this >= var1) {
         return var2;
      } else {
         Chunk var3 = this.chunkProvider.provideChunk(var1);
         if (var3.isEmpty()) {
            return var2;
         } else {
            Block var4 = this.getBlockState(var1).getBlock();
            return var4.getMaterial().isOpaque() && var4.isFullCube();
         }
      }
   }

   public boolean isBlockModifiable(EntityPlayer var1, BlockPos var2) {
      return true;
   }

   public void setWorldTime(long var1) {
      this.worldInfo.setWorldTime(var1);
   }

   public String getProviderName() {
      return this.chunkProvider.makeString();
   }

   public int isBlockIndirectlyGettingPowered(BlockPos var1) {
      int var2 = 0;
      EnumFacing[] var6;
      int var5 = (var6 = EnumFacing.values()).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         EnumFacing var3 = var6[var4];
         int var7 = this.getRedstonePower(var1.offset(var3), var3);
         if (var7 >= 15) {
            return 15;
         }

         if (var7 > var2) {
            var2 = var7;
         }
      }

      return var2;
   }

   public Calendar getCurrentDate() {
      if (this.getTotalWorldTime() % 600L == 0L) {
         this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
      }

      return this.theCalendar;
   }

   public void playSoundToNearExcept(EntityPlayer var1, String var2, float var3, float var4) {
      for(int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
         ((IWorldAccess)this.worldAccesses.get(var5)).playSoundToNearExcept(var1, var2, var1.posX, var1.posY, var1.posZ, var3, var4);
      }

   }

   public Vec3 getFogColor(float var1) {
      float var2 = this.getCelestialAngle(var1);
      return this.provider.getFogColor(var2, var1);
   }

   public void makeFireworks(double var1, double var3, double var5, double var7, double var9, double var11, NBTTagCompound var13) {
   }

   public GameRules getGameRules() {
      return this.worldInfo.getGameRulesInstance();
   }

   public void checkSessionLock() throws MinecraftException {
      this.saveHandler.checkSessionLock();
   }

   public boolean canBlockSeeSky(BlockPos var1) {
      if (var1.getY() >= this.func_181545_F()) {
         return this.canSeeSky(var1);
      } else {
         BlockPos var2 = new BlockPos(var1.getX(), this.func_181545_F(), var1.getZ());
         if (!this.canSeeSky(var2)) {
            return false;
         } else {
            for(var2 = var2.down(); var2.getY() > var1.getY(); var2 = var2.down()) {
               Block var3 = this.getBlockState(var2).getBlock();
               if (var3.getLightOpacity() > 0 && !var3.getMaterial().isLiquid()) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
