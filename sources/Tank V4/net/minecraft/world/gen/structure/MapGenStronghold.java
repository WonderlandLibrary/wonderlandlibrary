package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenStronghold extends MapGenStructure {
   private List field_151546_e;
   private double field_82671_h;
   private ChunkCoordIntPair[] structureCoords;
   private int field_82672_i;
   private boolean ranBiomeCheck;

   public String getStructureName() {
      return "Stronghold";
   }

   protected StructureStart getStructureStart(int var1, int var2) {
      MapGenStronghold.Start var3;
      for(var3 = new MapGenStronghold.Start(this.worldObj, this.rand, var1, var2); var3.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)var3.getComponents().get(0)).strongholdPortalRoom == null; var3 = new MapGenStronghold.Start(this.worldObj, this.rand, var1, var2)) {
      }

      return var3;
   }

   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      if (!this.ranBiomeCheck) {
         Random var3 = new Random();
         var3.setSeed(this.worldObj.getSeed());
         double var4 = var3.nextDouble() * 3.141592653589793D * 2.0D;
         int var6 = 1;

         for(int var7 = 0; var7 < this.structureCoords.length; ++var7) {
            double var8 = (1.25D * (double)var6 + var3.nextDouble()) * this.field_82671_h * (double)var6;
            int var10 = (int)Math.round(Math.cos(var4) * var8);
            int var11 = (int)Math.round(Math.sin(var4) * var8);
            BlockPos var12 = this.worldObj.getWorldChunkManager().findBiomePosition((var10 << 4) + 8, (var11 << 4) + 8, 112, this.field_151546_e, var3);
            if (var12 != null) {
               var10 = var12.getX() >> 4;
               var11 = var12.getZ() >> 4;
            }

            this.structureCoords[var7] = new ChunkCoordIntPair(var10, var11);
            var4 += 6.283185307179586D * (double)var6 / (double)this.field_82672_i;
            if (var7 == this.field_82672_i) {
               var6 += 2 + var3.nextInt(5);
               this.field_82672_i += 1 + var3.nextInt(2);
            }
         }

         this.ranBiomeCheck = true;
      }

      ChunkCoordIntPair[] var15;
      int var5 = (var15 = this.structureCoords).length;

      for(int var14 = 0; var14 < var5; ++var14) {
         ChunkCoordIntPair var13 = var15[var14];
         if (var1 == var13.chunkXPos && var2 == var13.chunkZPos) {
            return true;
         }
      }

      return false;
   }

   protected List getCoordList() {
      ArrayList var1 = Lists.newArrayList();
      ChunkCoordIntPair[] var5;
      int var4 = (var5 = this.structureCoords).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         ChunkCoordIntPair var2 = var5[var3];
         if (var2 != null) {
            var1.add(var2.getCenterBlock(64));
         }
      }

      return var1;
   }

   public MapGenStronghold(Map var1) {
      this();
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (((String)var2.getKey()).equals("distance")) {
            this.field_82671_h = MathHelper.parseDoubleWithDefaultAndMax((String)var2.getValue(), this.field_82671_h, 1.0D);
         } else if (((String)var2.getKey()).equals("count")) {
            this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)var2.getValue(), this.structureCoords.length, 1)];
         } else if (((String)var2.getKey()).equals("spread")) {
            this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String)var2.getValue(), this.field_82672_i, 1);
         }
      }

   }

   public MapGenStronghold() {
      this.structureCoords = new ChunkCoordIntPair[3];
      this.field_82671_h = 32.0D;
      this.field_82672_i = 3;
      this.field_151546_e = Lists.newArrayList();
      BiomeGenBase[] var4;
      int var3 = (var4 = BiomeGenBase.getBiomeGenArray()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         BiomeGenBase var1 = var4[var2];
         if (var1 != null && var1.minHeight > 0.0F) {
            this.field_151546_e.add(var1);
         }
      }

   }

   public static class Start extends StructureStart {
      public Start(World var1, Random var2, int var3, int var4) {
         super(var3, var4);
         StructureStrongholdPieces.prepareStructurePieces();
         StructureStrongholdPieces.Stairs2 var5 = new StructureStrongholdPieces.Stairs2(0, var2, (var3 << 4) + 2, (var4 << 4) + 2);
         this.components.add(var5);
         var5.buildComponent(var5, this.components, var2);
         List var6 = var5.field_75026_c;

         while(!var6.isEmpty()) {
            int var7 = var2.nextInt(var6.size());
            StructureComponent var8 = (StructureComponent)var6.remove(var7);
            var8.buildComponent(var5, this.components, var2);
         }

         this.updateBoundingBox();
         this.markAvailableHeight(var1, var2, 10);
      }

      public Start() {
      }
   }
}
