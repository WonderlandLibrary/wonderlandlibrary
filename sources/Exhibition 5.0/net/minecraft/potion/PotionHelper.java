// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.potion;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;

public class PotionHelper
{
    public static final String field_77924_a;
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
    private static final String __OBFID = "CL_00000078";
    
    public static boolean checkFlag(final int p_77914_0_, final int p_77914_1_) {
        return (p_77914_0_ & 1 << p_77914_1_) != 0x0;
    }
    
    private static int isFlagSet(final int p_77910_0_, final int p_77910_1_) {
        return checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
    }
    
    private static int isFlagUnset(final int p_77916_0_, final int p_77916_1_) {
        return checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
    }
    
    public static int func_77909_a(final int p_77909_0_) {
        return func_77908_a(p_77909_0_, 5, 4, 3, 2, 1);
    }
    
    public static int calcPotionLiquidColor(final Collection p_77911_0_) {
        final int var1 = 3694022;
        if (p_77911_0_ == null || p_77911_0_.isEmpty()) {
            return var1;
        }
        float var2 = 0.0f;
        float var3 = 0.0f;
        float var4 = 0.0f;
        float var5 = 0.0f;
        for (final PotionEffect var7 : p_77911_0_) {
            if (var7.func_180154_f()) {
                final int var8 = Potion.potionTypes[var7.getPotionID()].getLiquidColor();
                for (int var9 = 0; var9 <= var7.getAmplifier(); ++var9) {
                    var2 += (var8 >> 16 & 0xFF) / 255.0f;
                    var3 += (var8 >> 8 & 0xFF) / 255.0f;
                    var4 += (var8 >> 0 & 0xFF) / 255.0f;
                    ++var5;
                }
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
    
    public static boolean func_82817_b(final Collection p_82817_0_) {
        for (final PotionEffect var2 : p_82817_0_) {
            if (!var2.getIsAmbient()) {
                return false;
            }
        }
        return true;
    }
    
    public static int func_77915_a(final int p_77915_0_, final boolean p_77915_1_) {
        if (p_77915_1_) {
            return calcPotionLiquidColor(getPotionEffects(p_77915_0_, true));
        }
        if (PotionHelper.field_77925_n.containsKey(p_77915_0_)) {
            return PotionHelper.field_77925_n.get(p_77915_0_);
        }
        final int var2 = calcPotionLiquidColor(getPotionEffects(p_77915_0_, false));
        PotionHelper.field_77925_n.put(p_77915_0_, var2);
        return var2;
    }
    
    public static String func_77905_c(final int p_77905_0_) {
        final int var1 = func_77909_a(p_77905_0_);
        return PotionHelper.potionPrefixes[var1];
    }
    
    private static int func_77904_a(final boolean p_77904_0_, final boolean p_77904_1_, final boolean p_77904_2_, final int p_77904_3_, final int p_77904_4_, final int p_77904_5_, final int p_77904_6_) {
        int var7 = 0;
        if (p_77904_0_) {
            var7 = isFlagUnset(p_77904_6_, p_77904_4_);
        }
        else if (p_77904_3_ != -1) {
            if (p_77904_3_ == 0 && countSetFlags(p_77904_6_) == p_77904_4_) {
                var7 = 1;
            }
            else if (p_77904_3_ == 1 && countSetFlags(p_77904_6_) > p_77904_4_) {
                var7 = 1;
            }
            else if (p_77904_3_ == 2 && countSetFlags(p_77904_6_) < p_77904_4_) {
                var7 = 1;
            }
        }
        else {
            var7 = isFlagSet(p_77904_6_, p_77904_4_);
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
        int var1;
        for (var1 = 0; p_77907_0_ > 0; p_77907_0_ &= p_77907_0_ - 1, ++var1) {}
        return var1;
    }
    
    private static int parsePotionEffects(final String p_77912_0_, final int p_77912_1_, final int p_77912_2_, final int p_77912_3_) {
        if (p_77912_1_ >= p_77912_0_.length() || p_77912_2_ < 0 || p_77912_1_ >= p_77912_2_) {
            return 0;
        }
        final int var4 = p_77912_0_.indexOf(124, p_77912_1_);
        if (var4 >= 0 && var4 < p_77912_2_) {
            final int var5 = parsePotionEffects(p_77912_0_, p_77912_1_, var4 - 1, p_77912_3_);
            if (var5 > 0) {
                return var5;
            }
            final int var6 = parsePotionEffects(p_77912_0_, var4 + 1, p_77912_2_, p_77912_3_);
            return (var6 > 0) ? var6 : 0;
        }
        else {
            final int var5 = p_77912_0_.indexOf(38, p_77912_1_);
            if (var5 < 0 || var5 >= p_77912_2_) {
                boolean var7 = false;
                boolean var8 = false;
                boolean var9 = false;
                boolean var10 = false;
                boolean var11 = false;
                byte var12 = -1;
                int var13 = 0;
                int var14 = 0;
                int var15 = 0;
                for (int var16 = p_77912_1_; var16 < p_77912_2_; ++var16) {
                    final char var17 = p_77912_0_.charAt(var16);
                    if (var17 >= '0' && var17 <= '9') {
                        if (var7) {
                            var14 = var17 - '0';
                            var8 = true;
                        }
                        else {
                            var13 *= 10;
                            var13 += var17 - '0';
                            var9 = true;
                        }
                    }
                    else if (var17 == '*') {
                        var7 = true;
                    }
                    else if (var17 == '!') {
                        if (var9) {
                            var15 += func_77904_a(var10, var8, var11, var12, var13, var14, p_77912_3_);
                            var10 = false;
                            var11 = false;
                            var7 = false;
                            var8 = false;
                            var9 = false;
                            var14 = 0;
                            var13 = 0;
                            var12 = -1;
                        }
                        var10 = true;
                    }
                    else if (var17 == '-') {
                        if (var9) {
                            var15 += func_77904_a(var10, var8, var11, var12, var13, var14, p_77912_3_);
                            var10 = false;
                            var11 = false;
                            var7 = false;
                            var8 = false;
                            var9 = false;
                            var14 = 0;
                            var13 = 0;
                            var12 = -1;
                        }
                        var11 = true;
                    }
                    else if (var17 != '=' && var17 != '<' && var17 != '>') {
                        if (var17 == '+' && var9) {
                            var15 += func_77904_a(var10, var8, var11, var12, var13, var14, p_77912_3_);
                            var10 = false;
                            var11 = false;
                            var7 = false;
                            var8 = false;
                            var9 = false;
                            var14 = 0;
                            var13 = 0;
                            var12 = -1;
                        }
                    }
                    else {
                        if (var9) {
                            var15 += func_77904_a(var10, var8, var11, var12, var13, var14, p_77912_3_);
                            var10 = false;
                            var11 = false;
                            var7 = false;
                            var8 = false;
                            var9 = false;
                            var14 = 0;
                            var13 = 0;
                            var12 = -1;
                        }
                        if (var17 == '=') {
                            var12 = 0;
                        }
                        else if (var17 == '<') {
                            var12 = 2;
                        }
                        else if (var17 == '>') {
                            var12 = 1;
                        }
                    }
                }
                if (var9) {
                    var15 += func_77904_a(var10, var8, var11, var12, var13, var14, p_77912_3_);
                }
                return var15;
            }
            final int var6 = parsePotionEffects(p_77912_0_, p_77912_1_, var5 - 1, p_77912_3_);
            if (var6 <= 0) {
                return 0;
            }
            final int var18 = parsePotionEffects(p_77912_0_, var5 + 1, p_77912_2_, p_77912_3_);
            return (var18 <= 0) ? 0 : ((var6 > var18) ? var6 : var18);
        }
    }
    
    public static List getPotionEffects(final int p_77917_0_, final boolean p_77917_1_) {
        ArrayList var2 = null;
        for (final Potion var6 : Potion.potionTypes) {
            if (var6 != null && (!var6.isUsable() || p_77917_1_)) {
                final String var7 = PotionHelper.field_179539_o.get(var6.getId());
                if (var7 != null) {
                    int var8 = parsePotionEffects(var7, 0, var7.length(), p_77917_0_);
                    if (var8 > 0) {
                        int var9 = 0;
                        final String var10 = PotionHelper.field_179540_p.get(var6.getId());
                        if (var10 != null) {
                            var9 = parsePotionEffects(var10, 0, var10.length(), p_77917_0_);
                            if (var9 < 0) {
                                var9 = 0;
                            }
                        }
                        if (var6.isInstant()) {
                            var8 = 1;
                        }
                        else {
                            var8 = 1200 * (var8 * 3 + (var8 - 1) * 2);
                            var8 >>= var9;
                            var8 = (int)Math.round(var8 * var6.getEffectiveness());
                            if ((p_77917_0_ & 0x4000) != 0x0) {
                                var8 = (int)Math.round(var8 * 0.75 + 0.5);
                            }
                        }
                        if (var2 == null) {
                            var2 = Lists.newArrayList();
                        }
                        final PotionEffect var11 = new PotionEffect(var6.getId(), var8, var9);
                        if ((p_77917_0_ & 0x4000) != 0x0) {
                            var11.setSplashPotion(true);
                        }
                        var2.add(var11);
                    }
                }
            }
        }
        return var2;
    }
    
    private static int brewBitOperations(int p_77906_0_, final int p_77906_1_, final boolean p_77906_2_, final boolean p_77906_3_, final boolean p_77906_4_) {
        if (p_77906_4_) {
            if (!checkFlag(p_77906_0_, p_77906_1_)) {
                return 0;
            }
        }
        else if (p_77906_2_) {
            p_77906_0_ &= ~(1 << p_77906_1_);
        }
        else if (p_77906_3_) {
            if ((p_77906_0_ & 1 << p_77906_1_) == 0x0) {
                p_77906_0_ |= 1 << p_77906_1_;
            }
            else {
                p_77906_0_ &= ~(1 << p_77906_1_);
            }
        }
        else {
            p_77906_0_ |= 1 << p_77906_1_;
        }
        return p_77906_0_;
    }
    
    public static int applyIngredient(int p_77913_0_, final String p_77913_1_) {
        final byte var2 = 0;
        final int var3 = p_77913_1_.length();
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        boolean var7 = false;
        int var8 = 0;
        for (int var9 = var2; var9 < var3; ++var9) {
            final char var10 = p_77913_1_.charAt(var9);
            if (var10 >= '0' && var10 <= '9') {
                var8 *= 10;
                var8 += var10 - '0';
                var4 = true;
            }
            else if (var10 == '!') {
                if (var4) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }
                var5 = true;
            }
            else if (var10 == '-') {
                if (var4) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }
                var6 = true;
            }
            else if (var10 == '+') {
                if (var4) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, var8, var6, var5, var7);
                    var7 = false;
                    var5 = false;
                    var6 = false;
                    var4 = false;
                    var8 = 0;
                }
            }
            else if (var10 == '&') {
                if (var4) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, var8, var6, var5, var7);
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
            p_77913_0_ = brewBitOperations(p_77913_0_, var8, var6, var5, var7);
        }
        return p_77913_0_ & 0x7FFF;
    }
    
    public static int func_77908_a(final int p_77908_0_, final int p_77908_1_, final int p_77908_2_, final int p_77908_3_, final int p_77908_4_, final int p_77908_5_) {
        return (checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
    }
    
    static {
        field_77924_a = null;
        field_179539_o = Maps.newHashMap();
        field_179540_p = Maps.newHashMap();
        PotionHelper.field_179539_o.put(Potion.regeneration.getId(), "0 & !1 & !2 & !3 & 0+6");
        sugarEffect = "-0+1-2-3&4-4+13";
        PotionHelper.field_179539_o.put(Potion.moveSpeed.getId(), "!0 & 1 & !2 & !3 & 1+6");
        magmaCreamEffect = "+0+1-2-3&4-4+13";
        PotionHelper.field_179539_o.put(Potion.fireResistance.getId(), "0 & 1 & !2 & !3 & 0+6");
        speckledMelonEffect = "+0-1+2-3&4-4+13";
        PotionHelper.field_179539_o.put(Potion.heal.getId(), "0 & !1 & 2 & !3");
        spiderEyeEffect = "-0-1+2-3&4-4+13";
        PotionHelper.field_179539_o.put(Potion.poison.getId(), "!0 & !1 & 2 & !3 & 2+6");
        fermentedSpiderEyeEffect = "-0+3-4+13";
        PotionHelper.field_179539_o.put(Potion.weakness.getId(), "!0 & !1 & !2 & 3 & 3+6");
        PotionHelper.field_179539_o.put(Potion.harm.getId(), "!0 & !1 & 2 & 3");
        PotionHelper.field_179539_o.put(Potion.moveSlowdown.getId(), "!0 & 1 & !2 & 3 & 3+6");
        blazePowderEffect = "+0-1-2+3&4-4+13";
        PotionHelper.field_179539_o.put(Potion.damageBoost.getId(), "0 & !1 & !2 & 3 & 3+6");
        goldenCarrotEffect = "-0+1+2-3+13&4-4";
        PotionHelper.field_179539_o.put(Potion.nightVision.getId(), "!0 & 1 & 2 & !3 & 2+6");
        PotionHelper.field_179539_o.put(Potion.invisibility.getId(), "!0 & 1 & 2 & 3 & 2+6");
        field_151423_m = "+0-1+2+3+13&4-4";
        PotionHelper.field_179539_o.put(Potion.waterBreathing.getId(), "0 & !1 & 2 & 3 & 2+6");
        field_179538_n = "+0+1-2+3&4-4+13";
        PotionHelper.field_179539_o.put(Potion.jump.getId(), "0 & 1 & !2 & 3");
        glowstoneEffect = "+5-6-7";
        PotionHelper.field_179540_p.put(Potion.moveSpeed.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.digSpeed.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.damageBoost.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.regeneration.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.harm.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.heal.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.resistance.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.poison.getId(), "5");
        PotionHelper.field_179540_p.put(Potion.jump.getId(), "5");
        redstoneEffect = "-5+6-7";
        gunpowderEffect = "+14&13-13";
        field_77925_n = Maps.newHashMap();
        potionPrefixes = new String[] { "potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky" };
    }
}
