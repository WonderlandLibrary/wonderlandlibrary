package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;

public class FlatGeneratorInfo {
   private int biomeToUse;
   private final List flatLayers = Lists.newArrayList();
   private final Map worldFeatures = Maps.newHashMap();

   public List getFlatLayers() {
      return this.flatLayers;
   }

   private static List func_180716_a(int var0, String var1) {
      if (var1 != null && var1.length() >= 1) {
         ArrayList var2 = Lists.newArrayList();
         String[] var3 = var1.split(",");
         int var4 = 0;
         String[] var8 = var3;
         int var7 = var3.length;

         for(int var6 = 0; var6 < var7; ++var6) {
            String var5 = var8[var6];
            FlatLayerInfo var9 = func_180715_a(var0, var5, var4);
            if (var9 == null) {
               return null;
            }

            var2.add(var9);
            var4 += var9.getLayerCount();
         }

         return var2;
      } else {
         return null;
      }
   }

   public void setBiome(int var1) {
      this.biomeToUse = var1;
   }

   public static FlatGeneratorInfo createFlatGeneratorFromString(String var0) {
      if (var0 == null) {
         return getDefaultFlatGenerator();
      } else {
         String[] var1 = var0.split(";", -1);
         int var2 = var1.length == 1 ? 0 : MathHelper.parseIntWithDefault(var1[0], 0);
         if (var2 >= 0 && var2 <= 3) {
            FlatGeneratorInfo var3 = new FlatGeneratorInfo();
            int var4 = var1.length == 1 ? 0 : 1;
            List var5 = func_180716_a(var2, var1[var4++]);
            if (var5 != null && !var5.isEmpty()) {
               var3.getFlatLayers().addAll(var5);
               var3.func_82645_d();
               int var6 = BiomeGenBase.plains.biomeID;
               if (var2 > 0 && var1.length > var4) {
                  var6 = MathHelper.parseIntWithDefault(var1[var4++], var6);
               }

               var3.setBiome(var6);
               if (var2 > 0 && var1.length > var4) {
                  String[] var7 = var1[var4++].toLowerCase().split(",");
                  String[] var11 = var7;
                  int var10 = var7.length;

                  for(int var9 = 0; var9 < var10; ++var9) {
                     String var8 = var11[var9];
                     String[] var12 = var8.split("\\(", 2);
                     HashMap var13 = Maps.newHashMap();
                     if (var12[0].length() > 0) {
                        var3.getWorldFeatures().put(var12[0], var13);
                        if (var12.length > 1 && var12[1].endsWith(")") && var12[1].length() > 1) {
                           String[] var14 = var12[1].substring(0, var12[1].length() - 1).split(" ");

                           for(int var15 = 0; var15 < var14.length; ++var15) {
                              String[] var16 = var14[var15].split("=", 2);
                              if (var16.length == 2) {
                                 var13.put(var16[0], var16[1]);
                              }
                           }
                        }
                     }
                  }
               } else {
                  var3.getWorldFeatures().put("village", Maps.newHashMap());
               }

               return var3;
            } else {
               return getDefaultFlatGenerator();
            }
         } else {
            return getDefaultFlatGenerator();
         }
      }
   }

   public static FlatGeneratorInfo getDefaultFlatGenerator() {
      FlatGeneratorInfo var0 = new FlatGeneratorInfo();
      var0.setBiome(BiomeGenBase.plains.biomeID);
      var0.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
      var0.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
      var0.getFlatLayers().add(new FlatLayerInfo(1, Blocks.grass));
      var0.func_82645_d();
      var0.getWorldFeatures().put("village", Maps.newHashMap());
      return var0;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(3);
      var1.append(";");

      int var2;
      for(var2 = 0; var2 < this.flatLayers.size(); ++var2) {
         if (var2 > 0) {
            var1.append(",");
         }

         var1.append(((FlatLayerInfo)this.flatLayers.get(var2)).toString());
      }

      var1.append(";");
      var1.append(this.biomeToUse);
      if (!this.worldFeatures.isEmpty()) {
         var1.append(";");
         var2 = 0;
         Iterator var4 = this.worldFeatures.entrySet().iterator();

         while(true) {
            Map var5;
            do {
               if (!var4.hasNext()) {
                  return String.valueOf(var1);
               }

               Entry var3 = (Entry)var4.next();
               if (var2++ > 0) {
                  var1.append(",");
               }

               var1.append(((String)var3.getKey()).toLowerCase());
               var5 = (Map)var3.getValue();
            } while(var5.isEmpty());

            var1.append("(");
            int var6 = 0;
            Iterator var8 = var5.entrySet().iterator();

            while(var8.hasNext()) {
               Entry var7 = (Entry)var8.next();
               if (var6++ > 0) {
                  var1.append(" ");
               }

               var1.append((String)var7.getKey());
               var1.append("=");
               var1.append((String)var7.getValue());
            }

            var1.append(")");
         }
      } else {
         var1.append(";");
         return String.valueOf(var1);
      }
   }

   public Map getWorldFeatures() {
      return this.worldFeatures;
   }

   public int getBiome() {
      return this.biomeToUse;
   }

   public void func_82645_d() {
      int var1 = 0;

      FlatLayerInfo var2;
      for(Iterator var3 = this.flatLayers.iterator(); var3.hasNext(); var1 += var2.getLayerCount()) {
         var2 = (FlatLayerInfo)var3.next();
         var2.setMinY(var1);
      }

   }

   private static FlatLayerInfo func_180715_a(int var0, String var1, int var2) {
      String[] var3 = var0 >= 3 ? var1.split("\\*", 2) : var1.split("x", 2);
      int var4 = 1;
      int var5 = 0;
      if (var3.length == 2) {
         try {
            var4 = Integer.parseInt(var3[0]);
            if (var2 + var4 >= 256) {
               var4 = 256 - var2;
            }

            if (var4 < 0) {
               var4 = 0;
            }
         } catch (Throwable var8) {
            return null;
         }
      }

      Block var6 = null;

      try {
         String var7 = var3[var3.length - 1];
         if (var0 < 3) {
            var3 = var7.split(":", 2);
            if (var3.length > 1) {
               var5 = Integer.parseInt(var3[1]);
            }

            var6 = Block.getBlockById(Integer.parseInt(var3[0]));
         } else {
            var3 = var7.split(":", 3);
            var6 = var3.length > 1 ? Block.getBlockFromName(var3[0] + ":" + var3[1]) : null;
            if (var6 != null) {
               var5 = var3.length > 2 ? Integer.parseInt(var3[2]) : 0;
            } else {
               var6 = Block.getBlockFromName(var3[0]);
               if (var6 != null) {
                  var5 = var3.length > 1 ? Integer.parseInt(var3[1]) : 0;
               }
            }

            if (var6 == null) {
               return null;
            }
         }

         if (var6 == Blocks.air) {
            var5 = 0;
         }

         if (var5 < 0 || var5 > 15) {
            var5 = 0;
         }
      } catch (Throwable var9) {
         return null;
      }

      FlatLayerInfo var10 = new FlatLayerInfo(var0, var4, var6, var5);
      var10.setMinY(var2);
      return var10;
   }
}
