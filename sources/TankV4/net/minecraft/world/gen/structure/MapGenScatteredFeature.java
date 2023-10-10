package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenScatteredFeature extends MapGenStructure {
   private int minDistanceBetweenScatteredFeatures;
   private List scatteredFeatureSpawnList;
   private static final List biomelist;
   private int maxDistanceBetweenScatteredFeatures;

   static {
      biomelist = Arrays.asList(BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland);
   }

   protected StructureStart getStructureStart(int var1, int var2) {
      return new MapGenScatteredFeature.Start(this.worldObj, this.rand, var1, var2);
   }

   public boolean func_175798_a(BlockPos var1) {
      StructureStart var2 = this.func_175797_c(var1);
      if (var2 != null && var2 instanceof MapGenScatteredFeature.Start && !var2.components.isEmpty()) {
         StructureComponent var3 = (StructureComponent)var2.components.getFirst();
         return var3 instanceof ComponentScatteredFeaturePieces.SwampHut;
      } else {
         return false;
      }
   }

   public String getStructureName() {
      return "Temple";
   }

   public List getScatteredFeatureSpawnList() {
      return this.scatteredFeatureSpawnList;
   }

   public MapGenScatteredFeature() {
      this.scatteredFeatureSpawnList = Lists.newArrayList();
      this.maxDistanceBetweenScatteredFeatures = 32;
      this.minDistanceBetweenScatteredFeatures = 8;
      this.scatteredFeatureSpawnList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 1, 1, 1));
   }

   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int var3 = var1;
      int var4 = var2;
      if (var1 < 0) {
         var1 -= this.maxDistanceBetweenScatteredFeatures - 1;
      }

      if (var2 < 0) {
         var2 -= this.maxDistanceBetweenScatteredFeatures - 1;
      }

      int var5 = var1 / this.maxDistanceBetweenScatteredFeatures;
      int var6 = var2 / this.maxDistanceBetweenScatteredFeatures;
      Random var7 = this.worldObj.setRandomSeed(var5, var6, 14357617);
      var5 *= this.maxDistanceBetweenScatteredFeatures;
      var6 *= this.maxDistanceBetweenScatteredFeatures;
      var5 += var7.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
      var6 += var7.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
      if (var3 == var5 && var4 == var6) {
         BiomeGenBase var8 = this.worldObj.getWorldChunkManager().getBiomeGenerator(new BlockPos(var3 * 16 + 8, 0, var4 * 16 + 8));
         if (var8 == null) {
            return false;
         }

         Iterator var10 = biomelist.iterator();

         while(var10.hasNext()) {
            BiomeGenBase var9 = (BiomeGenBase)var10.next();
            if (var8 == var9) {
               return true;
            }
         }
      }

      return false;
   }

   public MapGenScatteredFeature(Map var1) {
      this();
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (((String)var2.getKey()).equals("distance")) {
            this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax((String)var2.getValue(), this.maxDistanceBetweenScatteredFeatures, this.minDistanceBetweenScatteredFeatures + 1);
         }
      }

   }

   public static class Start extends StructureStart {
      public Start(World var1, Random var2, int var3, int var4) {
         super(var3, var4);
         BiomeGenBase var5 = var1.getBiomeGenForCoords(new BlockPos(var3 * 16 + 8, 0, var4 * 16 + 8));
         if (var5 != BiomeGenBase.jungle && var5 != BiomeGenBase.jungleHills) {
            if (var5 == BiomeGenBase.swampland) {
               ComponentScatteredFeaturePieces.SwampHut var7 = new ComponentScatteredFeaturePieces.SwampHut(var2, var3 * 16, var4 * 16);
               this.components.add(var7);
            } else if (var5 == BiomeGenBase.desert || var5 == BiomeGenBase.desertHills) {
               ComponentScatteredFeaturePieces.DesertPyramid var8 = new ComponentScatteredFeaturePieces.DesertPyramid(var2, var3 * 16, var4 * 16);
               this.components.add(var8);
            }
         } else {
            ComponentScatteredFeaturePieces.JunglePyramid var6 = new ComponentScatteredFeaturePieces.JunglePyramid(var2, var3 * 16, var4 * 16);
            this.components.add(var6);
         }

         this.updateBoundingBox();
      }

      public Start() {
      }
   }
}
