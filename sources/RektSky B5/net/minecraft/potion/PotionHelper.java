/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.util.IntegerCache;

public class PotionHelper {
    public static final String field_77924_a = null;
    public static final String sugarEffect = "-0+1-2-3&4-4+13";
    public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
    public static final String spiderEyeEffect = "-0-1+2-3&4-4+13";
    public static final String fermentedSpiderEyeEffect = "-0+3-4+13";
    public static final String speckledMelonEffect = "+0-1+2-3&4-4+13";
    public static final String blazePowderEffect = "+0-1-2+3&4-4+13";
    public static final String magmaCreamEffect = "+0+1-2-3&4-4+13";
    public static final String redstoneEffect = "-5+6-7";
    public static final String glowstoneEffect = "+5-6-7";
    public static final String gunpowderEffect = "+14&13-13";
    public static final String goldenCarrotEffect = "-0+1+2-3+13&4-4";
    public static final String pufferfishEffect = "+0-1+2+3+13&4-4";
    public static final String rabbitFootEffect = "+0+1-2+3&4-4+13";
    private static final Map<Integer, String> potionRequirements = Maps.newHashMap();
    private static final Map<Integer, String> potionAmplifiers = Maps.newHashMap();
    private static final Map<Integer, Integer> DATAVALUE_COLORS = Maps.newHashMap();
    private static final String[] potionPrefixes = new String[]{"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};

    public static boolean checkFlag(int p_77914_0_, int p_77914_1_) {
        return (p_77914_0_ & 1 << p_77914_1_) != 0;
    }

    private static int isFlagSet(int p_77910_0_, int p_77910_1_) {
        return PotionHelper.checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
    }

    private static int isFlagUnset(int p_77916_0_, int p_77916_1_) {
        return PotionHelper.checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
    }

    public static int getPotionPrefixIndex(int dataValue) {
        return PotionHelper.func_77908_a(dataValue, 5, 4, 3, 2, 1);
    }

    public static int calcPotionLiquidColor(Collection<PotionEffect> p_77911_0_) {
        int i2 = 3694022;
        if (p_77911_0_ != null && !p_77911_0_.isEmpty()) {
            float f2 = 0.0f;
            float f1 = 0.0f;
            float f22 = 0.0f;
            float f3 = 0.0f;
            for (PotionEffect potioneffect : p_77911_0_) {
                if (!potioneffect.getIsShowParticles()) continue;
                int j2 = Potion.potionTypes[potioneffect.getPotionID()].getLiquidColor();
                for (int k2 = 0; k2 <= potioneffect.getAmplifier(); ++k2) {
                    f2 += (float)(j2 >> 16 & 0xFF) / 255.0f;
                    f1 += (float)(j2 >> 8 & 0xFF) / 255.0f;
                    f22 += (float)(j2 >> 0 & 0xFF) / 255.0f;
                    f3 += 1.0f;
                }
            }
            if (f3 == 0.0f) {
                return 0;
            }
            f2 = f2 / f3 * 255.0f;
            f1 = f1 / f3 * 255.0f;
            f22 = f22 / f3 * 255.0f;
            return (int)f2 << 16 | (int)f1 << 8 | (int)f22;
        }
        return i2;
    }

    public static boolean getAreAmbient(Collection<PotionEffect> potionEffects) {
        for (PotionEffect potioneffect : potionEffects) {
            if (potioneffect.getIsAmbient()) continue;
            return false;
        }
        return true;
    }

    public static int getLiquidColor(int dataValue, boolean bypassCache) {
        Integer integer = IntegerCache.func_181756_a(dataValue);
        if (!bypassCache) {
            if (DATAVALUE_COLORS.containsKey(integer)) {
                return DATAVALUE_COLORS.get(integer);
            }
            int i2 = PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(integer, false));
            DATAVALUE_COLORS.put(integer, i2);
            return i2;
        }
        return PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(integer, true));
    }

    public static String getPotionPrefix(int dataValue) {
        int i2 = PotionHelper.getPotionPrefixIndex(dataValue);
        return potionPrefixes[i2];
    }

    private static int func_77904_a(boolean p_77904_0_, boolean p_77904_1_, boolean p_77904_2_, int p_77904_3_, int p_77904_4_, int p_77904_5_, int p_77904_6_) {
        int i2 = 0;
        if (p_77904_0_) {
            i2 = PotionHelper.isFlagUnset(p_77904_6_, p_77904_4_);
        } else if (p_77904_3_ != -1) {
            if (p_77904_3_ == 0 && PotionHelper.countSetFlags(p_77904_6_) == p_77904_4_) {
                i2 = 1;
            } else if (p_77904_3_ == 1 && PotionHelper.countSetFlags(p_77904_6_) > p_77904_4_) {
                i2 = 1;
            } else if (p_77904_3_ == 2 && PotionHelper.countSetFlags(p_77904_6_) < p_77904_4_) {
                i2 = 1;
            }
        } else {
            i2 = PotionHelper.isFlagSet(p_77904_6_, p_77904_4_);
        }
        if (p_77904_1_) {
            i2 *= p_77904_5_;
        }
        if (p_77904_2_) {
            i2 *= -1;
        }
        return i2;
    }

    private static int countSetFlags(int p_77907_0_) {
        int i2 = 0;
        while (p_77907_0_ > 0) {
            p_77907_0_ &= p_77907_0_ - 1;
            ++i2;
        }
        return i2;
    }

    private static int parsePotionEffects(String p_77912_0_, int p_77912_1_, int p_77912_2_, int p_77912_3_) {
        if (p_77912_1_ < p_77912_0_.length() && p_77912_2_ >= 0 && p_77912_1_ < p_77912_2_) {
            int i2 = p_77912_0_.indexOf(124, p_77912_1_);
            if (i2 >= 0 && i2 < p_77912_2_) {
                int l1 = PotionHelper.parsePotionEffects(p_77912_0_, p_77912_1_, i2 - 1, p_77912_3_);
                if (l1 > 0) {
                    return l1;
                }
                int j2 = PotionHelper.parsePotionEffects(p_77912_0_, i2 + 1, p_77912_2_, p_77912_3_);
                return j2 > 0 ? j2 : 0;
            }
            int j2 = p_77912_0_.indexOf(38, p_77912_1_);
            if (j2 >= 0 && j2 < p_77912_2_) {
                int i22 = PotionHelper.parsePotionEffects(p_77912_0_, p_77912_1_, j2 - 1, p_77912_3_);
                if (i22 <= 0) {
                    return 0;
                }
                int k2 = PotionHelper.parsePotionEffects(p_77912_0_, j2 + 1, p_77912_2_, p_77912_3_);
                return k2 <= 0 ? 0 : (i22 > k2 ? i22 : k2);
            }
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            boolean flag4 = false;
            int k2 = -1;
            int l2 = 0;
            int i1 = 0;
            int j1 = 0;
            for (int k1 = p_77912_1_; k1 < p_77912_2_; ++k1) {
                char c0 = p_77912_0_.charAt(k1);
                if (c0 >= '0' && c0 <= '9') {
                    if (flag) {
                        i1 = c0 - 48;
                        flag1 = true;
                        continue;
                    }
                    l2 *= 10;
                    l2 += c0 - 48;
                    flag2 = true;
                    continue;
                }
                if (c0 == '*') {
                    flag = true;
                    continue;
                }
                if (c0 == '!') {
                    if (flag2) {
                        j1 += PotionHelper.func_77904_a(flag3, flag1, flag4, k2, l2, i1, p_77912_3_);
                        flag3 = false;
                        flag4 = false;
                        flag = false;
                        flag1 = false;
                        flag2 = false;
                        i1 = 0;
                        l2 = 0;
                        k2 = -1;
                    }
                    flag3 = true;
                    continue;
                }
                if (c0 == '-') {
                    if (flag2) {
                        j1 += PotionHelper.func_77904_a(flag3, flag1, flag4, k2, l2, i1, p_77912_3_);
                        flag3 = false;
                        flag4 = false;
                        flag = false;
                        flag1 = false;
                        flag2 = false;
                        i1 = 0;
                        l2 = 0;
                        k2 = -1;
                    }
                    flag4 = true;
                    continue;
                }
                if (c0 != '=' && c0 != '<' && c0 != '>') {
                    if (c0 != '+' || !flag2) continue;
                    j1 += PotionHelper.func_77904_a(flag3, flag1, flag4, k2, l2, i1, p_77912_3_);
                    flag3 = false;
                    flag4 = false;
                    flag = false;
                    flag1 = false;
                    flag2 = false;
                    i1 = 0;
                    l2 = 0;
                    k2 = -1;
                    continue;
                }
                if (flag2) {
                    j1 += PotionHelper.func_77904_a(flag3, flag1, flag4, k2, l2, i1, p_77912_3_);
                    flag3 = false;
                    flag4 = false;
                    flag = false;
                    flag1 = false;
                    flag2 = false;
                    i1 = 0;
                    l2 = 0;
                    k2 = -1;
                }
                if (c0 == '=') {
                    k2 = 0;
                    continue;
                }
                if (c0 == '<') {
                    k2 = 2;
                    continue;
                }
                if (c0 != '>') continue;
                k2 = 1;
            }
            if (flag2) {
                j1 += PotionHelper.func_77904_a(flag3, flag1, flag4, k2, l2, i1, p_77912_3_);
            }
            return j1;
        }
        return 0;
    }

    public static List<PotionEffect> getPotionEffects(int p_77917_0_, boolean p_77917_1_) {
        ArrayList<PotionEffect> list = null;
        for (Potion potion : Potion.potionTypes) {
            int i2;
            String s2;
            if (potion == null || potion.isUsable() && !p_77917_1_ || (s2 = potionRequirements.get(potion.getId())) == null || (i2 = PotionHelper.parsePotionEffects(s2, 0, s2.length(), p_77917_0_)) <= 0) continue;
            int j2 = 0;
            String s1 = potionAmplifiers.get(potion.getId());
            if (s1 != null && (j2 = PotionHelper.parsePotionEffects(s1, 0, s1.length(), p_77917_0_)) < 0) {
                j2 = 0;
            }
            if (potion.isInstant()) {
                i2 = 1;
            } else {
                i2 = 1200 * (i2 * 3 + (i2 - 1) * 2);
                i2 >>= j2;
                i2 = (int)Math.round((double)i2 * potion.getEffectiveness());
                if ((p_77917_0_ & 0x4000) != 0) {
                    i2 = (int)Math.round((double)i2 * 0.75 + 0.5);
                }
            }
            if (list == null) {
                list = Lists.newArrayList();
            }
            PotionEffect potioneffect = new PotionEffect(potion.getId(), i2, j2);
            if ((p_77917_0_ & 0x4000) != 0) {
                potioneffect.setSplashPotion(true);
            }
            list.add(potioneffect);
        }
        return list;
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
        int i2 = 0;
        int j2 = p_77913_1_.length();
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        int k2 = 0;
        for (int l2 = i2; l2 < j2; ++l2) {
            char c0 = p_77913_1_.charAt(l2);
            if (c0 >= '0' && c0 <= '9') {
                k2 *= 10;
                k2 += c0 - 48;
                flag = true;
                continue;
            }
            if (c0 == '!') {
                if (flag) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, k2, flag2, flag1, flag3);
                    flag3 = false;
                    flag1 = false;
                    flag2 = false;
                    flag = false;
                    k2 = 0;
                }
                flag1 = true;
                continue;
            }
            if (c0 == '-') {
                if (flag) {
                    p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, k2, flag2, flag1, flag3);
                    flag3 = false;
                    flag1 = false;
                    flag2 = false;
                    flag = false;
                    k2 = 0;
                }
                flag2 = true;
                continue;
            }
            if (c0 == '+') {
                if (!flag) continue;
                p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, k2, flag2, flag1, flag3);
                flag3 = false;
                flag1 = false;
                flag2 = false;
                flag = false;
                k2 = 0;
                continue;
            }
            if (c0 != '&') continue;
            if (flag) {
                p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, k2, flag2, flag1, flag3);
                flag3 = false;
                flag1 = false;
                flag2 = false;
                flag = false;
                k2 = 0;
            }
            flag3 = true;
        }
        if (flag) {
            p_77913_0_ = PotionHelper.brewBitOperations(p_77913_0_, k2, flag2, flag1, flag3);
        }
        return p_77913_0_ & Short.MAX_VALUE;
    }

    public static int func_77908_a(int p_77908_0_, int p_77908_1_, int p_77908_2_, int p_77908_3_, int p_77908_4_, int p_77908_5_) {
        return (PotionHelper.checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (PotionHelper.checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
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
}

