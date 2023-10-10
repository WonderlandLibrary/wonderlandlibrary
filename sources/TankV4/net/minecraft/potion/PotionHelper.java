package net.minecraft.potion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.util.IntegerCache;
import optifine.Config;
import optifine.CustomColors;

public class PotionHelper {
   public static final String sugarEffect = "-0+1-2-3&4-4+13";
   public static final String speckledMelonEffect = "+0-1+2-3&4-4+13";
   public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
   public static final String redstoneEffect = "-5+6-7";
   public static final String gunpowderEffect = "+14&13-13";
   public static final String spiderEyeEffect = "-0-1+2-3&4-4+13";
   private static final String __OBFID = "CL_00000078";
   public static final String fermentedSpiderEyeEffect = "-0+3-4+13";
   public static final Map potionRequirements = Maps.newHashMap();
   public static final String goldenCarrotEffect = "-0+1+2-3+13&4-4";
   public static final Map potionAmplifiers = Maps.newHashMap();
   public static final String rabbitFootEffect = "+0+1-2+3&4-4+13";
   public static final String blazePowderEffect = "+0-1-2+3&4-4+13";
   public static final String pufferfishEffect = "+0-1+2+3+13&4-4";
   private static final Map DATAVALUE_COLORS = Maps.newHashMap();
   public static final String magmaCreamEffect = "+0+1-2-3&4-4+13";
   private static final String[] potionPrefixes = new String[]{"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
   public static final String glowstoneEffect = "+5-6-7";
   public static final String field_77924_a = null;

   public static void clearPotionColorCache() {
      DATAVALUE_COLORS.clear();
   }

   public static int func_77908_a(int var0, int var1, int var2, int var3, int var4, int var5) {
      boolean var10001;
      if (var1 != 0) {
         var10001 = true;
      } else {
         var10001 = false;
      }

      int var10002 = var0 | (var2 != 0 ? 8 : 0);
      int var10003 = var0 | (var3 != 0 ? 4 : 0);
      int var10004 = var0 | (var4 != 0 ? 2 : 0);
      return var0 | (var5 != 0 ? 1 : 0);
   }

   public static int getPotionPrefixIndex(int var0) {
      return func_77908_a(var0, 5, 4, 3, 2, 1);
   }

   private static int isFlagUnset(int var0, int var1) {
      return var1 != 0 ? 0 : 1;
   }

   static {
      potionRequirements.put(Potion.regeneration.getId(), "0 & !1 & !2 & !3 & 0+6");
      potionRequirements.put(Potion.moveSpeed.getId(), "!0 & 1 & !2 & !3 & 1+6");
      potionRequirements.put(Potion.fireResistance.getId(), "0 & 1 & !2 & !3 & 0+6");
      potionRequirements.put(Potion.heal.getId(), "0 & !1 & 2 & !3");
      potionRequirements.put(Potion.poison.getId(), "!0 & !1 & 2 & !3 & 2+6");
      potionRequirements.put(Potion.weakness.getId(), "!0 & !1 & !2 & 3 & 3+6");
      potionRequirements.put(Potion.harm.getId(), "!0 & !1 & 2 & 3");
      potionRequirements.put(Potion.moveSlowdown.getId(), "!0 & 1 & !2 & 3 & 3+6");
      potionRequirements.put(Potion.damageBoost.getId(), "0 & !1 & !2 & 3 & 3+6");
      potionRequirements.put(Potion.nightVision.getId(), "!0 & 1 & 2 & !3 & 2+6");
      potionRequirements.put(Potion.invisibility.getId(), "!0 & 1 & 2 & 3 & 2+6");
      potionRequirements.put(Potion.waterBreathing.getId(), "0 & !1 & 2 & 3 & 2+6");
      potionRequirements.put(Potion.jump.getId(), "0 & 1 & !2 & 3 & 3+6");
      potionAmplifiers.put(Potion.moveSpeed.getId(), "5");
      potionAmplifiers.put(Potion.digSpeed.getId(), "5");
      potionAmplifiers.put(Potion.damageBoost.getId(), "5");
      potionAmplifiers.put(Potion.regeneration.getId(), "5");
      potionAmplifiers.put(Potion.harm.getId(), "5");
      potionAmplifiers.put(Potion.heal.getId(), "5");
      potionAmplifiers.put(Potion.resistance.getId(), "5");
      potionAmplifiers.put(Potion.poison.getId(), "5");
      potionAmplifiers.put(Potion.jump.getId(), "5");
   }

   public static int getLiquidColor(int var0, boolean var1) {
      Integer var2 = IntegerCache.func_181756_a(var0);
      if (!var1) {
         if (DATAVALUE_COLORS.containsKey(var2)) {
            return (Integer)DATAVALUE_COLORS.get(var2);
         } else {
            int var3 = calcPotionLiquidColor(getPotionEffects(var2, false));
            DATAVALUE_COLORS.put(var2, var3);
            return var3;
         }
      } else {
         return calcPotionLiquidColor(getPotionEffects(var2, true));
      }
   }

   private static int countSetFlags(int var0) {
      int var1;
      for(var1 = 0; var0 > 0; ++var1) {
         var0 &= var0 - 1;
      }

      return var1;
   }

   private static int brewBitOperations(int var0, int var1, boolean var2, boolean var3, boolean var4) {
      if (var4) {
         if (var1 != 0) {
            return 0;
         }
      } else if (var2) {
         var0 &= ~(1 << var1);
      } else if (var3) {
         if ((var0 & 1 << var1) == 0) {
            var0 |= 1 << var1;
         } else {
            var0 &= ~(1 << var1);
         }
      } else {
         var0 |= 1 << var1;
      }

      return var0;
   }

   public static int applyIngredient(int var0, String var1) {
      byte var2 = 0;
      int var3 = var1.length();
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      boolean var7 = false;
      int var8 = 0;

      for(int var9 = var2; var9 < var3; ++var9) {
         char var10 = var1.charAt(var9);
         if (var10 >= '0' && var10 <= '9') {
            var8 *= 10;
            var8 += var10 - 48;
            var4 = true;
         } else if (var10 == '!') {
            if (var4) {
               var0 = brewBitOperations(var0, var8, var6, var5, var7);
               var7 = false;
               var5 = false;
               var6 = false;
               var4 = false;
               var8 = 0;
            }

            var5 = true;
         } else if (var10 == '-') {
            if (var4) {
               var0 = brewBitOperations(var0, var8, var6, var5, var7);
               var7 = false;
               var5 = false;
               var6 = false;
               var4 = false;
               var8 = 0;
            }

            var6 = true;
         } else if (var10 == '+') {
            if (var4) {
               var0 = brewBitOperations(var0, var8, var6, var5, var7);
               var7 = false;
               var5 = false;
               var6 = false;
               var4 = false;
               var8 = 0;
            }
         } else if (var10 == '&') {
            if (var4) {
               var0 = brewBitOperations(var0, var8, var6, var5, var7);
               var7 = false;
               var5 = false;
               var6 = false;
               var4 = false;
               var8 = 0;
            }

            var7 = true;
         }
      }

      if (var4) {
         var0 = brewBitOperations(var0, var8, var6, var5, var7);
      }

      return var0 & 32767;
   }

   public static String getPotionPrefix(int var0) {
      int var1 = getPotionPrefixIndex(var0);
      return potionPrefixes[var1];
   }

   private static int parsePotionEffects(String var0, int var1, int var2, int var3) {
      if (var1 < var0.length() && var2 >= 0 && var1 < var2) {
         int var4 = var0.indexOf(124, var1);
         int var5;
         int var17;
         if (var4 >= 0 && var4 < var2) {
            var5 = parsePotionEffects(var0, var1, var4 - 1, var3);
            if (var5 > 0) {
               return var5;
            } else {
               var17 = parsePotionEffects(var0, var4 + 1, var2, var3);
               return var17 > 0 ? var17 : 0;
            }
         } else {
            var5 = var0.indexOf(38, var1);
            if (var5 >= 0 && var5 < var2) {
               var17 = parsePotionEffects(var0, var1, var5 - 1, var3);
               if (var17 <= 0) {
                  return 0;
               } else {
                  int var18 = parsePotionEffects(var0, var5 + 1, var2, var3);
                  return var18 <= 0 ? 0 : (var17 > var18 ? var17 : var18);
               }
            } else {
               boolean var6 = false;
               boolean var7 = false;
               boolean var8 = false;
               boolean var9 = false;
               boolean var10 = false;
               byte var11 = -1;
               int var12 = 0;
               int var13 = 0;
               int var14 = 0;

               for(int var15 = var1; var15 < var2; ++var15) {
                  char var16 = var0.charAt(var15);
                  if (var16 >= '0' && var16 <= '9') {
                     if (var6) {
                        var13 = var16 - 48;
                        var7 = true;
                     } else {
                        var12 *= 10;
                        var12 += var16 - 48;
                        var8 = true;
                     }
                  } else if (var16 == '*') {
                     var6 = true;
                  } else if (var16 == '!') {
                     if (var8) {
                        var14 += func_77904_a(var9, var7, var10, var11, var12, var13, var3);
                        var9 = false;
                        var10 = false;
                        var6 = false;
                        var7 = false;
                        var8 = false;
                        var13 = 0;
                        var12 = 0;
                        var11 = -1;
                     }

                     var9 = true;
                  } else if (var16 == '-') {
                     if (var8) {
                        var14 += func_77904_a(var9, var7, var10, var11, var12, var13, var3);
                        var9 = false;
                        var10 = false;
                        var6 = false;
                        var7 = false;
                        var8 = false;
                        var13 = 0;
                        var12 = 0;
                        var11 = -1;
                     }

                     var10 = true;
                  } else if (var16 != '=' && var16 != '<' && var16 != '>') {
                     if (var16 == '+' && var8) {
                        var14 += func_77904_a(var9, var7, var10, var11, var12, var13, var3);
                        var9 = false;
                        var10 = false;
                        var6 = false;
                        var7 = false;
                        var8 = false;
                        var13 = 0;
                        var12 = 0;
                        var11 = -1;
                     }
                  } else {
                     if (var8) {
                        var14 += func_77904_a(var9, var7, var10, var11, var12, var13, var3);
                        var9 = false;
                        var10 = false;
                        var6 = false;
                        var7 = false;
                        var8 = false;
                        var13 = 0;
                        var12 = 0;
                        var11 = -1;
                     }

                     if (var16 == '=') {
                        var11 = 0;
                     } else if (var16 == '<') {
                        var11 = 2;
                     } else if (var16 == '>') {
                        var11 = 1;
                     }
                  }
               }

               if (var8) {
                  var14 += func_77904_a(var9, var7, var10, var11, var12, var13, var3);
               }

               return var14;
            }
         }
      } else {
         return 0;
      }
   }

   private static int func_77904_a(boolean var0, boolean var1, boolean var2, int var3, int var4, int var5, int var6) {
      int var7 = 0;
      if (var0) {
         var7 = isFlagUnset(var6, var4);
      } else if (var3 != -1) {
         if (var3 == 0 && countSetFlags(var6) == var4) {
            var7 = 1;
         } else if (var3 == 1 && countSetFlags(var6) > var4) {
            var7 = 1;
         } else if (var3 == 2 && countSetFlags(var6) < var4) {
            var7 = 1;
         }
      } else {
         var7 = isFlagSet(var6, var4);
      }

      if (var1) {
         var7 *= var5;
      }

      if (var2) {
         var7 *= -1;
      }

      return var7;
   }

   public static int calcPotionLiquidColor(Collection var0) {
      int var1 = 3694022;
      if (var0 != null && !var0.isEmpty()) {
         float var2 = 0.0F;
         float var3 = 0.0F;
         float var4 = 0.0F;
         float var5 = 0.0F;
         Iterator var7 = var0.iterator();

         while(true) {
            PotionEffect var8;
            do {
               if (!var7.hasNext()) {
                  if (var5 == 0.0F) {
                     return 0;
                  }

                  var2 = var2 / var5 * 255.0F;
                  var3 = var3 / var5 * 255.0F;
                  var4 = var4 / var5 * 255.0F;
                  return (int)var2 << 16 | (int)var3 << 8 | (int)var4;
               }

               Object var6 = var7.next();
               var8 = (PotionEffect)var6;
            } while(!var8.getIsShowParticles());

            int var9 = Potion.potionTypes[var8.getPotionID()].getLiquidColor();
            if (Config.isCustomColors()) {
               var9 = CustomColors.getPotionColor(var8.getPotionID(), var9);
            }

            for(int var10 = 0; var10 <= var8.getAmplifier(); ++var10) {
               var2 += (float)(var9 >> 16 & 255) / 255.0F;
               var3 += (float)(var9 >> 8 & 255) / 255.0F;
               var4 += (float)(var9 >> 0 & 255) / 255.0F;
               ++var5;
            }
         }
      } else {
         if (Config.isCustomColors()) {
            var1 = CustomColors.getPotionColor(0, var1);
         }

         return var1;
      }
   }

   private static int isFlagSet(int var0, int var1) {
      return var1 != 0 ? 1 : 0;
   }

   public static boolean getAreAmbient(Collection var0) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Object var1 = var2.next();
         if (!((PotionEffect)var1).getIsAmbient()) {
            return false;
         }
      }

      return true;
   }

   public static List getPotionEffects(int var0, boolean var1) {
      ArrayList var2 = null;
      Potion[] var6;
      int var5 = (var6 = Potion.potionTypes).length;

      for(int var4 = 0; var4 < var5; ++var4) {
         Potion var3 = var6[var4];
         if (var3 != null && (!var3.isUsable() || var1)) {
            String var7 = (String)potionRequirements.get(var3.getId());
            if (var7 != null) {
               int var8 = parsePotionEffects(var7, 0, var7.length(), var0);
               if (var8 > 0) {
                  int var9 = 0;
                  String var10 = (String)potionAmplifiers.get(var3.getId());
                  if (var10 != null) {
                     var9 = parsePotionEffects(var10, 0, var10.length(), var0);
                     if (var9 < 0) {
                        var9 = 0;
                     }
                  }

                  if (var3.isInstant()) {
                     var8 = 1;
                  } else {
                     var8 = 1200 * (var8 * 3 + (var8 - 1) * 2);
                     var8 >>= var9;
                     var8 = (int)Math.round((double)var8 * var3.getEffectiveness());
                     if ((var0 & 16384) != 0) {
                        var8 = (int)Math.round((double)var8 * 0.75D + 0.5D);
                     }
                  }

                  if (var2 == null) {
                     var2 = Lists.newArrayList();
                  }

                  PotionEffect var11 = new PotionEffect(var3.getId(), var8, var9);
                  if ((var0 & 16384) != 0) {
                     var11.setSplashPotion(true);
                  }

                  var2.add(var11);
               }
            }
         }
      }

      return var2;
   }
}
