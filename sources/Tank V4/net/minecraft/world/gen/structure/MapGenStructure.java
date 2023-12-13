package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.storage.MapStorage;
import optifine.Reflector;

public abstract class MapGenStructure extends MapGenBase {
   private static final String __OBFID = "CL_00000505";
   protected Map structureMap = Maps.newHashMap();
   private MapGenStructureData structureData;
   private LongHashMap structureLongMap = new LongHashMap();

   protected StructureStart func_175797_c(BlockPos var1) {
      Iterator var3 = this.structureMap.values().iterator();

      while(true) {
         StructureStart var4;
         do {
            do {
               if (!var3.hasNext()) {
                  return null;
               }

               Object var2 = var3.next();
               var4 = (StructureStart)var2;
            } while(!var4.isSizeableStructure());
         } while(!var4.getBoundingBox().isVecInside(var1));

         Iterator var5 = var4.getComponents().iterator();

         while(var5.hasNext()) {
            StructureComponent var6 = (StructureComponent)var5.next();
            if (var6.getBoundingBox().isVecInside(var1)) {
               return var4;
            }
         }
      }
   }

   protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

   public BlockPos getClosestStrongholdPos(World var1, BlockPos var2) {
      this.worldObj = var1;
      this.func_143027_a(var1);
      this.rand.setSeed(var1.getSeed());
      long var3 = this.rand.nextLong();
      long var5 = this.rand.nextLong();
      long var7 = (long)(var2.getX() >> 4) * var3;
      long var9 = (long)(var2.getZ() >> 4) * var5;
      this.rand.setSeed(var7 ^ var9 ^ var1.getSeed());
      this.recursiveGenerate(var1, var2.getX() >> 4, var2.getZ() >> 4, 0, 0, (ChunkPrimer)null);
      double var11 = Double.MAX_VALUE;
      BlockPos var13 = null;
      Iterator var15 = this.structureMap.values().iterator();

      while(var15.hasNext()) {
         Object var14 = var15.next();
         StructureStart var16 = (StructureStart)var14;
         if (var16.isSizeableStructure()) {
            StructureComponent var17 = (StructureComponent)var16.getComponents().get(0);
            BlockPos var18 = var17.getBoundingBoxCenter();
            double var19 = var18.distanceSq(var2);
            if (var19 < var11) {
               var11 = var19;
               var13 = var18;
            }
         }
      }

      if (var13 != null) {
         return var13;
      } else {
         List var21 = this.getCoordList();
         if (var21 != null) {
            BlockPos var22 = null;
            Iterator var24 = var21.iterator();

            while(var24.hasNext()) {
               Object var23 = var24.next();
               double var25 = ((BlockPos)var23).distanceSq(var2);
               if (var25 < var11) {
                  var11 = var25;
                  var22 = (BlockPos)var23;
               }
            }

            return var22;
         } else {
            return null;
         }
      }
   }

   protected List getCoordList() {
      return null;
   }

   protected final void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, ChunkPrimer var6) {
      this.func_143027_a(var1);
      if (!this.structureLongMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(var2, var3))) {
         this.rand.nextInt();

         try {
            if (this.canSpawnStructureAtCoords(var2, var3)) {
               StructureStart var7 = this.getStructureStart(var2, var3);
               this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(var2, var3), var7);
               this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(var2, var3), var7);
               this.func_143026_a(var2, var3, var7);
            }
         } catch (Throwable var11) {
            CrashReport var8 = CrashReport.makeCrashReport(var11, "Exception preparing structure feature");
            CrashReportCategory var9 = var8.makeCategory("Feature being prepared");
            var9.addCrashSectionCallable("Is feature chunk", new Callable(this, var2, var3) {
               private final int val$chunkZ;
               final MapGenStructure this$0;
               private static final String __OBFID = "CL_00000506";
               private final int val$chunkX;

               public Object call() throws Exception {
                  return this.call();
               }

               public String call() throws Exception {
                  return this.this$0.canSpawnStructureAtCoords(this.val$chunkX, this.val$chunkZ) ? "True" : "False";
               }

               {
                  this.this$0 = var1;
                  this.val$chunkX = var2;
                  this.val$chunkZ = var3;
               }
            });
            var9.addCrashSection("Chunk location", String.format("%d,%d", var2, var3));
            var9.addCrashSectionCallable("Chunk pos hash", new Callable(this, var2, var3) {
               private final int val$chunkZ;
               final MapGenStructure this$0;
               private static final String __OBFID = "CL_00000507";
               private final int val$chunkX;

               public Object call() throws Exception {
                  return this.call();
               }

               public String call() throws Exception {
                  return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(this.val$chunkX, this.val$chunkZ));
               }

               {
                  this.this$0 = var1;
                  this.val$chunkX = var2;
                  this.val$chunkZ = var3;
               }
            });
            var9.addCrashSectionCallable("Structure type", new Callable(this) {
               final MapGenStructure this$0;
               private static final String __OBFID = "CL_00000508";

               public String call() throws Exception {
                  return this.this$0.getClass().getCanonicalName();
               }

               {
                  this.this$0 = var1;
               }

               public Object call() throws Exception {
                  return this.call();
               }
            });
            throw new ReportedException(var8);
         }
      }

   }

   public boolean func_175795_b(BlockPos var1) {
      this.func_143027_a(this.worldObj);
      return this.func_175797_c(var1) != null;
   }

   protected abstract StructureStart getStructureStart(int var1, int var2);

   public boolean generateStructure(World var1, Random var2, ChunkCoordIntPair var3) {
      this.func_143027_a(var1);
      int var4 = (var3.chunkXPos << 4) + 8;
      int var5 = (var3.chunkZPos << 4) + 8;
      boolean var6 = false;
      Iterator var8 = this.structureMap.values().iterator();

      while(var8.hasNext()) {
         Object var7 = var8.next();
         StructureStart var9 = (StructureStart)var7;
         if (var9.isSizeableStructure() && var9.func_175788_a(var3) && var9.getBoundingBox().intersectsWith(var4, var5, var4 + 15, var5 + 15)) {
            var9.generateStructure(var1, var2, new StructureBoundingBox(var4, var5, var4 + 15, var5 + 15));
            var9.func_175787_b(var3);
            var6 = true;
            this.func_143026_a(var9.getChunkPosX(), var9.getChunkPosZ(), var9);
         }
      }

      return var6;
   }

   public abstract String getStructureName();

   public boolean func_175796_a(World var1, BlockPos var2) {
      this.func_143027_a(var1);
      Iterator var4 = this.structureMap.values().iterator();

      StructureStart var5;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         Object var3 = var4.next();
         var5 = (StructureStart)var3;
      } while(!var5.isSizeableStructure() || !var5.getBoundingBox().isVecInside(var2));

      return true;
   }

   private void func_143027_a(World var1) {
      if (this.structureData == null) {
         MapStorage var2;
         if (Reflector.ForgeWorld_getPerWorldStorage.exists()) {
            var2 = (MapStorage)Reflector.call(var1, Reflector.ForgeWorld_getPerWorldStorage);
            this.structureData = (MapGenStructureData)var2.loadData(MapGenStructureData.class, this.getStructureName());
         } else {
            this.structureData = (MapGenStructureData)var1.loadItemData(MapGenStructureData.class, this.getStructureName());
         }

         if (this.structureData == null) {
            this.structureData = new MapGenStructureData(this.getStructureName());
            if (Reflector.ForgeWorld_getPerWorldStorage.exists()) {
               var2 = (MapStorage)Reflector.call(var1, Reflector.ForgeWorld_getPerWorldStorage);
               var2.setData(this.getStructureName(), this.structureData);
            } else {
               var1.setItemData(this.getStructureName(), this.structureData);
            }
         } else {
            NBTTagCompound var10 = this.structureData.getTagCompound();
            Iterator var4 = var10.getKeySet().iterator();

            while(var4.hasNext()) {
               String var3 = (String)var4.next();
               NBTBase var5 = var10.getTag(var3);
               if (var5.getId() == 10) {
                  NBTTagCompound var6 = (NBTTagCompound)var5;
                  if (var6.hasKey("ChunkX") && var6.hasKey("ChunkZ")) {
                     int var7 = var6.getInteger("ChunkX");
                     int var8 = var6.getInteger("ChunkZ");
                     StructureStart var9 = MapGenStructureIO.getStructureStart(var6, var1);
                     if (var9 != null) {
                        this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(var7, var8), var9);
                        this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(var7, var8), var9);
                     }
                  }
               }
            }
         }
      }

   }

   private void func_143026_a(int var1, int var2, StructureStart var3) {
      this.structureData.writeInstance(var3.writeStructureComponentsToNBT(var1, var2), var1, var2);
      this.structureData.markDirty();
   }
}
