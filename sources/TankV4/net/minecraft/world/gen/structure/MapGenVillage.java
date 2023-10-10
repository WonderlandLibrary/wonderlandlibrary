package net.minecraft.world.gen.structure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenVillage extends MapGenStructure {
   private int field_82666_h;
   public static final List villageSpawnBiomes;
   private int field_82665_g;
   private int terrainType;

   public String getStructureName() {
      return "Village";
   }

   public MapGenVillage(Map var1) {
      this();
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var2 = (Entry)var3.next();
         if (((String)var2.getKey()).equals("size")) {
            this.terrainType = MathHelper.parseIntWithDefaultAndMax((String)var2.getValue(), this.terrainType, 0);
         } else if (((String)var2.getKey()).equals("distance")) {
            this.field_82665_g = MathHelper.parseIntWithDefaultAndMax((String)var2.getValue(), this.field_82665_g, this.field_82666_h + 1);
         }
      }

   }

   static {
      villageSpawnBiomes = Arrays.asList(BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.savanna);
   }

   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      int var3 = var1;
      int var4 = var2;
      if (var1 < 0) {
         var1 -= this.field_82665_g - 1;
      }

      if (var2 < 0) {
         var2 -= this.field_82665_g - 1;
      }

      int var5 = var1 / this.field_82665_g;
      int var6 = var2 / this.field_82665_g;
      Random var7 = this.worldObj.setRandomSeed(var5, var6, 10387312);
      var5 *= this.field_82665_g;
      var6 *= this.field_82665_g;
      var5 += var7.nextInt(this.field_82665_g - this.field_82666_h);
      var6 += var7.nextInt(this.field_82665_g - this.field_82666_h);
      if (var3 == var5 && var4 == var6) {
         boolean var8 = this.worldObj.getWorldChunkManager().areBiomesViable(var3 * 16 + 8, var4 * 16 + 8, 0, villageSpawnBiomes);
         if (var8) {
            return true;
         }
      }

      return false;
   }

   public MapGenVillage() {
      this.field_82665_g = 32;
      this.field_82666_h = 8;
   }

   protected StructureStart getStructureStart(int var1, int var2) {
      return new MapGenVillage.Start(this.worldObj, this.rand, var1, var2, this.terrainType);
   }

   public static class Start extends StructureStart {
      private boolean hasMoreThanTwoComponents;

      public Start(World var1, Random var2, int var3, int var4, int var5) {
         super(var3, var4);
         List var6 = StructureVillagePieces.getStructureVillageWeightedPieceList(var2, var5);
         StructureVillagePieces.Start var7 = new StructureVillagePieces.Start(var1.getWorldChunkManager(), 0, var2, (var3 << 4) + 2, (var4 << 4) + 2, var6, var5);
         this.components.add(var7);
         var7.buildComponent(var7, this.components, var2);
         List var8 = var7.field_74930_j;
         List var9 = var7.field_74932_i;

         int var10;
         StructureComponent var11;
         while(!var8.isEmpty() || !var9.isEmpty()) {
            if (var8.isEmpty()) {
               var10 = var2.nextInt(var9.size());
               var11 = (StructureComponent)var9.remove(var10);
               var11.buildComponent(var7, this.components, var2);
            } else {
               var10 = var2.nextInt(var8.size());
               var11 = (StructureComponent)var8.remove(var10);
               var11.buildComponent(var7, this.components, var2);
            }
         }

         this.updateBoundingBox();
         var10 = 0;
         Iterator var12 = this.components.iterator();

         while(var12.hasNext()) {
            var11 = (StructureComponent)var12.next();
            if (!(var11 instanceof StructureVillagePieces.Road)) {
               ++var10;
            }
         }

         this.hasMoreThanTwoComponents = var10 > 2;
      }

      public Start() {
      }

      public void writeToNBT(NBTTagCompound var1) {
         super.writeToNBT(var1);
         var1.setBoolean("Valid", this.hasMoreThanTwoComponents);
      }

      public void readFromNBT(NBTTagCompound var1) {
         super.readFromNBT(var1);
         this.hasMoreThanTwoComponents = var1.getBoolean("Valid");
      }

      public boolean isSizeableStructure() {
         return this.hasMoreThanTwoComponents;
      }
   }
}
