package optifine;

import java.util.ArrayList;
import java.util.Properties;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

public class RandomMobsProperties {
   public String name = null;
   public String basePath = null;
   public ResourceLocation[] resourceLocations = null;
   public RandomMobsRule[] rules = null;

   public RandomMobsProperties(String var1, ResourceLocation[] var2) {
      ConnectedParser var3 = new ConnectedParser("RandomMobs");
      this.name = var3.parseName(var1);
      this.basePath = var3.parseBasePath(var1);
      this.resourceLocations = var2;
   }

   public RandomMobsProperties(Properties var1, String var2, ResourceLocation var3) {
      ConnectedParser var4 = new ConnectedParser("RandomMobs");
      this.name = var4.parseName(var2);
      this.basePath = var4.parseBasePath(var2);
      this.rules = this.parseRules(var1, var3, var4);
   }

   public ResourceLocation getTextureLocation(ResourceLocation var1, EntityLiving var2) {
      int var3;
      if (this.rules != null) {
         for(var3 = 0; var3 < this.rules.length; ++var3) {
            RandomMobsRule var4 = this.rules[var3];
            if (var4.matches(var2)) {
               return var4.getTextureLocation(var1, var2.randomMobsId);
            }
         }
      }

      if (this.resourceLocations != null) {
         var3 = var2.randomMobsId;
         int var5 = var3 % this.resourceLocations.length;
         return this.resourceLocations[var5];
      } else {
         return var1;
      }
   }

   private RandomMobsRule[] parseRules(Properties var1, ResourceLocation var2, ConnectedParser var3) {
      ArrayList var4 = new ArrayList();
      int var5 = var1.size();

      for(int var6 = 0; var6 < var5; ++var6) {
         int var7 = var6 + 1;
         String var8 = var1.getProperty("skins." + var7);
         if (var8 != null) {
            int[] var9 = var3.parseIntList(var8);
            int[] var10 = var3.parseIntList(var1.getProperty("weights." + var7));
            BiomeGenBase[] var11 = var3.parseBiomes(var1.getProperty("biomes." + var7));
            RangeListInt var12 = var3.parseRangeListInt(var1.getProperty("heights." + var7));
            if (var12 == null) {
               var12 = this.parseMinMaxHeight(var1, var7);
            }

            RandomMobsRule var13 = new RandomMobsRule(var2, var9, var10, var11, var12);
            var4.add(var13);
         }
      }

      RandomMobsRule[] var14 = (RandomMobsRule[])var4.toArray(new RandomMobsRule[var4.size()]);
      return var14;
   }

   private RangeListInt parseMinMaxHeight(Properties var1, int var2) {
      String var3 = var1.getProperty("minHeight." + var2);
      String var4 = var1.getProperty("maxHeight." + var2);
      if (var3 == null && var4 == null) {
         return null;
      } else {
         int var5 = 0;
         if (var3 != null) {
            var5 = Config.parseInt(var3, -1);
            if (var5 < 0) {
               Config.warn("Invalid minHeight: " + var3);
               return null;
            }
         }

         int var6 = 256;
         if (var4 != null) {
            var6 = Config.parseInt(var4, -1);
            if (var6 < 0) {
               Config.warn("Invalid maxHeight: " + var4);
               return null;
            }
         }

         if (var6 < 0) {
            Config.warn("Invalid minHeight, maxHeight: " + var3 + ", " + var4);
            return null;
         } else {
            RangeListInt var7 = new RangeListInt();
            var7.addRange(new RangeInt(var5, var6));
            return var7;
         }
      }
   }

   public boolean isValid(String var1) {
      if (this.resourceLocations == null && this.rules == null) {
         Config.warn("No skins specified: " + var1);
         return false;
      } else {
         int var2;
         if (this.rules != null) {
            for(var2 = 0; var2 < this.rules.length; ++var2) {
               RandomMobsRule var3 = this.rules[var2];
               if (!var3.isValid(var1)) {
                  return false;
               }
            }
         }

         if (this.resourceLocations != null) {
            for(var2 = 0; var2 < this.resourceLocations.length; ++var2) {
               ResourceLocation var4 = this.resourceLocations[var2];
               if (!Config.hasResource(var4)) {
                  Config.warn("Texture not found: " + var4.getResourcePath());
                  return false;
               }
            }
         }

         return true;
      }
   }
}
