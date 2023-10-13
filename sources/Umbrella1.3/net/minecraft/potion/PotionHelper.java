/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.potion;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import optifine.Config;
import optifine.CustomColors;

public class PotionHelper {
    public static final String field_77924_a = null;
    public static final String sugarEffect;
    public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
    public static final String spiderEyeEffect;
    public static final String fermentedSpiderEyeEffect;
    public static final String speckledMelonEffect;
    public static final String blazePowderEffect;
    public static final String magmaCreamEffect;
    public static final String redstoneEffect;
    public static final String glowstoneEffect;
    public static final String gunpowderEffect;
    public static final String goldenCarrotEffect;
    public static final String field_151423_m;
    public static final String field_179538_n;
    private static final Map field_179539_o;
    private static final Map field_179540_p;
    private static final Map field_77925_n;
    private static final String[] potionPrefixes;

    static {
        field_179539_o = Maps.newHashMap();
        field_179540_p = Maps.newHashMap();
        field_179539_o.put(Potion.regeneration.getId(), "0 & !1 & !2 & !3 & 0+6");
        sugarEffect = "-0+1-2-3&4-4+13";
        field_179539_o.put(Potion.moveSpeed.getId(), "!0 & 1 & !2 & !3 & 1+6");
        magmaCreamEffect = "+0+1-2-3&4-4+13";
        field_179539_o.put(Potion.fireResistance.getId(), "0 & 1 & !2 & !3 & 0+6");
        speckledMelonEffect = "+0-1+2-3&4-4+13";
        field_179539_o.put(Potion.heal.getId(), "0 & !1 & 2 & !3");
        spiderEyeEffect = "-0-1+2-3&4-4+13";
        field_179539_o.put(Potion.poison.getId(), "!0 & !1 & 2 & !3 & 2+6");
        fermentedSpiderEyeEffect = "-0+3-4+13";
        field_179539_o.put(Potion.weakness.getId(), "!0 & !1 & !2 & 3 & 3+6");
        field_179539_o.put(Potion.harm.getId(), "!0 & !1 & 2 & 3");
        field_179539_o.put(Potion.moveSlowdown.getId(), "!0 & 1 & !2 & 3 & 3+6");
        blazePowderEffect = "+0-1-2+3&4-4+13";
        field_179539_o.put(Potion.damageBoost.getId(), "0 & !1 & !2 & 3 & 3+6");
        goldenCarrotEffect = "-0+1+2-3+13&4-4";
        field_179539_o.put(Potion.nightVision.getId(), "!0 & 1 & 2 & !3 & 2+6");
        field_179539_o.put(Potion.invisibility.getId(), "!0 & 1 & 2 & 3 & 2+6");
        field_151423_m = "+0-1+2+3+13&4-4";
        field_179539_o.put(Potion.waterBreathing.getId(), "0 & !1 & 2 & 3 & 2+6");
        field_179538_n = "+0+1-2+3&4-4+13";
        field_179539_o.put(Potion.jump.getId(), "0 & 1 & !2 & 3");
        glowstoneEffect = "+5-6-7";
        field_179540_p.put(Potion.moveSpeed.getId(), "5");
        field_179540_p.put(Potion.digSpeed.getId(), "5");
        field_179540_p.put(Potion.damageBoost.getId(), "5");
        field_179540_p.put(Potion.regeneration.getId(), "5");
        field_179540_p.put(Potion.harm.getId(), "5");
        field_179540_p.put(Potion.heal.getId(), "5");
        field_179540_p.put(Potion.resistance.getId(), "5");
        field_179540_p.put(Potion.poison.getId(), "5");
        field_179540_p.put(Potion.jump.getId(), "5");
        redstoneEffect = "-5+6-7";
        gunpowderEffect = "+14&13-13";
        field_77925_n = Maps.newHashMap();
        potionPrefixes = new String[]{"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
    }

    public static boolean checkFlag(int p_77914_0_, int p_77914_1_) {
        return (p_77914_0_ & 1 << p_77914_1_) != 0;
    }

    private static int isFlagSet(int p_77910_0_, int p_77910_1_) {
        return PotionHelper.checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
    }

    private static int isFlagUnset(int p_77916_0_, int p_77916_1_) {
        return PotionHelper.checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
    }

    public static int func_77909_a(int p_77909_0_) {
        return PotionHelper.func_77908_a(p_77909_0_, 5, 4, 3, 2, 1);
    }

    public static int calcPotionLiquidColor(Collection p_77911_0_) {
        int var1 = 3694022;
        if (p_77911_0_ != null && !p_77911_0_.isEmpty()) {
            float var2 = 0.0f;
            float var3 = 0.0f;
            float var4 = 0.0f;
            float var5 = 0.0f;
            for (PotionEffect var7 : p_77911_0_) {
                if (!var7.func_180154_f()) continue;
                int var8 = Potion.potionTypes[var7.getPotionID()].getLiquidColor();
                if (Config.isCustomColors()) {
                    var8 = CustomColors.getPotionColor(var7.getPotionID(), var8);
                }
                for (int var9 = 0; var9 <= var7.getAmplifier(); ++var9) {
                    var2 += (float)(var8 >> 16 & 0xFF) / 255.0f;
                    var3 += (float)(var8 >> 8 & 0xFF) / 255.0f;
                    var4 += (float)(var8 >> 0 & 0xFF) / 255.0f;
                    var5 += 1.0f;
                }
            }
            if (var5 == 0.0f) {
                return 0;
            }
            var2 = var2 / var5 * 255.0f;
            var3 = var3 / var5 * 255.0f;
            var4 = var4 / var5 * 255.0f;
            return (int)var2 << 16 | (int)var3 << 8 | (int)var4;
        }
        if (Config.isCustomColors()) {
            var1 = CustomColors.getPotionColor(0, var1);
        }
        return var1;
    }

    public static boolean func_82817_b(Collection potionEffects) {
        for (PotionEffect var2 : potionEffects) {
            if (var2.getIsAmbient()) continue;
            return false;
        }
        return true;
    }

    public static int func_77915_a(int dataValue, boolean bypassCache) {
        if (!bypassCache) {
            if (field_77925_n.containsKey(dataValue)) {
                return (Integer)field_77925_n.get(dataValue);
            }
            int var2 = PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(dataValue, false));
            field_77925_n.put(dataValue, var2);
            return var2;
        }
        return PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(dataValue, true));
    }

    public static String func_77905_c(int p_77905_0_) {
        int var1 = PotionHelper.func_77909_a(p_77905_0_);
        return potionPrefixes[var1];
    }

    private static int func_77904_a(boolean p_77904_0_, boolean p_77904_1_, boolean p_77904_2_, int p_77904_3_, int p_77904_4_, int p_77904_5_, int p_77904_6_) {
        int var7 = 0;
        if (p_77904_0_) {
            var7 = PotionHelper.isFlagUnset(p_77904_6_, p_77904_4_);
        } else if (p_77904_3_ != -1) {
            if (p_77904_3_ == 0 && PotionHelper.countSetFlags(p_77904_6_) == p_77904_4_) {
                var7 = 1;
            } else if (p_77904_3_ == 1 && PotionHelper.countSetFlags(p_77904_6_) > p_77904_4_) {
                var7 = 1;
            } else if (p_77904_3_ == 2 && PotionHelper.countSetFlags(p_77904_6_) < p_77904_4_) {
                var7 = 1;
            }
        } else {
            var7 = PotionHelper.isFlagSet(p_77904_6_, p_77904_4_);
        }
        if (p_77904_1_) {
            var7 *= p_77904_5_;
        }
        if (p_77904_2_) {
            var7 *= -1;
        }
        return var7;
    }

    private static int countSetFlags(int p_77907_0_) {
        int var1 = 0;
        while (p_77907_0_ > 0) {
            p_77907_0_ &= p_77907_0_ - 1;
            ++var1;
        }
        return var1;
    }

    private static int parsePotionEffects(String p_77912_0_, int p_77912_1_, int p_77912_2_, int p_77912_3_) {
        if (p_77912_1_ < p_77912_0_.length() && p_77912_2_ >= 0 && p_77912_1_ < p_77912_2_) {
            int var4 = p_77912_0_.indexOf(124, p_77912_1_);
            if (var4 >= 0 && var4 < p_77912_2_) {
                int var5 = PotionHelper.parsePotionEffects(p_77912_0_, p_77912_1_, var4 - 1, p_77912_3_);
                if (var5 > 0) {
                    return var5;
                }
                int var17 = PotionHelper.parsePotionEffects(p_77912_0_, var4 + 1, p_77912_2_, p_77912_3_);
                return var17 > 0 ? var17 : 0;
            }
            int var5 = p_77912_0_.indexOf(38, p_77912_1_);
            if (var5 >= 0 && var5 < p_77912_2_) {
                int var17 = PotionHelper.parsePotionEffects(p_77912_0_, p_77912_1_, var5 - 1, p_77912_3_);
                if (var17 <= 0) {
                    return 0;
                }
                int var18 = PotionHelper.parsePotionEffects(p_77912_0_, var5 + 1, p_77912_2_, p_77912_3_);
                return var18 <= 0 ? 0 : (var17 > var18 ? var17 : var18);
            }
            boolean var6 = false;
            boolean var7 = false;
            boolean var8 = false;
            boolean var9 = false;
            boolean var10 = false;
            int var11 = -1;
            int var12 = 0;
            int var13 = 0;
            int var14 = 0;
            for (int var15 = p_77912_1_; var15 < p_77912_2_; ++var15) {
                char var16 = p_77912_0_.charAt(var15);
                if (var16 >= '0' && var16 <= '9') {
                    if (var6) {
                        var13 = var16 - 48;
                        var7 = true;
                        continue;
                    }
                    var12 *= 10;
                    var12 += var16 - 48;
                    var8 = true;
                    continue;
                }
                if (var16 == '*') {
                    var6 = true;
                    continue;
                }
                if (var16 == '!') {
                    if (var8) {
                        var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
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
                    continue;
                }
                if (var16 == '-') {
                    if (var8) {
                        var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
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
                    continue;
                }
                if (var16 != '=' && var16 != '<' && var16 != '>') {
                    if (var16 != '+' || !var8) continue;
                    var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
                    var9 = false;
                    var10 = false;
                    var6 = false;
                    var7 = false;
                    var8 = false;
                    var13 = 0;
                    var12 = 0;
                    var11 = -1;
                    continue;
                }
                if (var8) {
                    var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
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
                    continue;
                }
                if (var16 == '<') {
                    var11 = 2;
                    continue;
                }
                if (var16 != '>') continue;
                var11 = 1;
            }
            if (var8) {
                var14 += PotionHelper.func_77904_a(var9, var7, var10, var11, var12, var13, p_77912_3_);
            }
            return var14;
        }
        return 0;
    }

    public static List getPotionEffects(int p_77917_0_, boolean p_77917_1_) {
        ArrayList var2 = null;
        for (Potion var6 : Potion.potionTypes) {
            int var8;
            String var7;
            if (var6 == null || var6.isUsable() && !p_77917_1_ || (var7 = (String)field_179539_o.get(var6.getId())) == null || (var8 = PotionHelper.parsePotionEffects(var7, 0, var7.length(), p_77917_0_)) <= 0) continue;
            int var9 = 0;
            String var10 = (String)field_179540_p.get(var6.getId());
            if (var10 != null && (var9 = PotionHelper.parsePotionEffects(var10, 0, var10.length(), p_77917_0_)) < 0) {
                var9 = 0;
            }
            if (var6.isInstant()) {
                var8 = 1;
            } else {
                var8 = 1200 * (var8 * 3 + (var8 - 1) * 2);
                var8 >>= var9;
                var8 = (int)Math.round((double)var8 * var6.getEffectiveness());
                if ((p_77917_0_ & 0x4000) != 0) {
                    var8 = (int)Math.round((double)var8 * 0.75 + 0.5);
                }
            }
            if (var2 == null) {
                var2 = Lists.newArrayList();
            }
            PotionEffect var11 = new PotionEffect(var6.getId(), var8, var9);
            if ((p_77917_0_ & 0x4000) != 0) {
                var11.setSplashPotion(true);
            }
            var2.add(var11);
        }
        return var2;
    }

    private static int brewBitOperations(int p_77906_0_, int p_77906_1_, boolean p_77906_2_, boolean p_77906_3_, boolean p_77906_4_) {
        if (p_77906_4_) {
            if (!PotionHelper.checkFlag(p_77906_0_, p_77906_1_)) {
                return 0;
            }
        } else {
            p_77906_0_ = p_77906_2_ ? (p_77906_0_ &= ~(1 << p_77906_1_)) : (p_77906_3_ ? ((p_77906_0_ & 1 << p_77906_1_) == 0 ? (p_77906_0_ |= 1 << p_77906_1_) : (p_77906_0_ &= ~(1 << p_77906_1_))) : (p_77906_0_ |= 1 << p_77906_1_));
        }
        return p_77906_0_;
    }

    public static int applyIngredient(int p_77913_0_, String p_77913_1_) {
        int var2 = 0;
        int var3 = p_77913_1_.length();
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        boolean var7 = false;
        int var8 = 0;
        for (int var9 = var2; var9 < var3; ++var9) {
            char var10 = p_77913_1_.charAt(var9);
            if (var10 >= '0' && var10 <= '9') {
                var8 *= 10;
                var8 += var10 - 48;
                var4 = true;
                continue;
            }
            if (var10 == '!') {
                if (var4) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }
                var5 = true;
                continue;
            }
            if (var10 == '-') {
                if (var4) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }
                var6 = true;
                continue;
            }
            if (var10 == '+') {
                if (!var4) continue;
                p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                var7 = false;
                var5 = false;
                var6 = false;
                var4 = false;
                var8 = 0;
                continue;
            }
            if (var10 != '&') continue;
            if (var4) {
                p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                var7 = false;
                var5 = false;
                var6 = false;
                var4 = false;
                var8 = 0;
            }
            var7 = true;
        }
        if (var4) {
            p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, var8, var6, var5, var7);
        }
        return p_77913_0_ & 0x7FFF;
    }

    public static int func_77908_a(int p_77908_0_, int p_77908_1_, int p_77908_2_, int p_77908_3_, int p_77908_4_, int p_77908_5_) {
        return (PotionHelper.checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
    }

    public static void clearPotionColorCache() {
        field_77925_n.clear();
    }
}

