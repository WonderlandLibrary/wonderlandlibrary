package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.StatCollector;

public class ItemFireworkCharge extends Item {
   public static void addExplosionInfo(NBTTagCompound var0, List var1) {
      byte var2 = var0.getByte("Type");
      if (var2 >= 0 && var2 <= 4) {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.type." + var2).trim());
      } else {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
      }

      int[] var3 = var0.getIntArray("Colors");
      int var7;
      int var8;
      if (var3.length > 0) {
         boolean var4 = true;
         String var5 = "";
         int[] var9 = var3;
         var8 = var3.length;

         for(var7 = 0; var7 < var8; ++var7) {
            int var6 = var9[var7];
            if (!var4) {
               var5 = var5 + ", ";
            }

            var4 = false;
            boolean var10 = false;

            for(int var11 = 0; var11 < ItemDye.dyeColors.length; ++var11) {
               if (var6 == ItemDye.dyeColors[var11]) {
                  var10 = true;
                  var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(var11).getUnlocalizedName());
                  break;
               }
            }

            if (!var10) {
               var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
            }
         }

         var1.add(var5);
      }

      int[] var13 = var0.getIntArray("FadeColors");
      boolean var15;
      if (var13.length > 0) {
         var15 = true;
         String var14 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
         int[] var18 = var13;
         int var17 = var13.length;

         for(var8 = 0; var8 < var17; ++var8) {
            var7 = var18[var8];
            if (!var15) {
               var14 = var14 + ", ";
            }

            var15 = false;
            boolean var19 = false;

            for(int var12 = 0; var12 < 16; ++var12) {
               if (var7 == ItemDye.dyeColors[var12]) {
                  var19 = true;
                  var14 = var14 + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(var12).getUnlocalizedName());
                  break;
               }
            }

            if (!var19) {
               var14 = var14 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
            }
         }

         var1.add(var14);
      }

      var15 = var0.getBoolean("Trail");
      if (var15) {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
      }

      boolean var16 = var0.getBoolean("Flicker");
      if (var16) {
         var1.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
      }

   }

   public static NBTBase getExplosionTag(ItemStack var0, String var1) {
      if (var0.hasTagCompound()) {
         NBTTagCompound var2 = var0.getTagCompound().getCompoundTag("Explosion");
         if (var2 != null) {
            return var2.getTag(var1);
         }
      }

      return null;
   }

   public int getColorFromItemStack(ItemStack var1, int var2) {
      if (var2 != 1) {
         return super.getColorFromItemStack(var1, var2);
      } else {
         NBTBase var3 = getExplosionTag(var1, "Colors");
         if (!(var3 instanceof NBTTagIntArray)) {
            return 9079434;
         } else {
            NBTTagIntArray var4 = (NBTTagIntArray)var3;
            int[] var5 = var4.getIntArray();
            if (var5.length == 1) {
               return var5[0];
            } else {
               int var6 = 0;
               int var7 = 0;
               int var8 = 0;
               int[] var12 = var5;
               int var11 = var5.length;

               for(int var10 = 0; var10 < var11; ++var10) {
                  int var9 = var12[var10];
                  var6 += (var9 & 16711680) >> 16;
                  var7 += (var9 & '\uff00') >> 8;
                  var8 += (var9 & 255) >> 0;
               }

               var6 /= var5.length;
               var7 /= var5.length;
               var8 /= var5.length;
               return var6 << 16 | var7 << 8 | var8;
            }
         }
      }
   }

   public void addInformation(ItemStack var1, EntityPlayer var2, List var3, boolean var4) {
      if (var1.hasTagCompound()) {
         NBTTagCompound var5 = var1.getTagCompound().getCompoundTag("Explosion");
         if (var5 != null) {
            addExplosionInfo(var5, var3);
         }
      }

   }
}
