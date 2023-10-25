/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.util.IntegerCache;

public class PotionHelper {
    public static final String pufferfishEffect = "+0-1+2+3+13&4-4";
    private static final Map<Integer, Integer> DATAVALUE_COLORS;
    private static final String[] potionPrefixes;
    public static final String field_77924_a;
    private static final Map<Integer, String> potionAmplifiers;
    public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
    public static final String sugarEffect = "-0+1-2-3&4-4+13";
    public static final String blazePowderEffect = "+0-1-2+3&4-4+13";
    public static final String redstoneEffect = "-5+6-7";
    public static final String goldenCarrotEffect = "-0+1+2-3+13&4-4";
    public static final String glowstoneEffect = "+5-6-7";
    public static final String speckledMelonEffect = "+0-1+2-3&4-4+13";
    public static final String spiderEyeEffect = "-0-1+2-3&4-4+13";
    public static final String rabbitFootEffect = "+0+1-2+3&4-4+13";
    public static final String gunpowderEffect = "+14&13-13";
    public static final String fermentedSpiderEyeEffect = "-0+3-4+13";
    private static final Map<Integer, String> potionRequirements;
    public static final String magmaCreamEffect = "+0+1-2-3&4-4+13";

    private static int isFlagSet(int n, int n2) {
        return PotionHelper.checkFlag(n, n2) ? 1 : 0;
    }

    private static int isFlagUnset(int n, int n2) {
        return PotionHelper.checkFlag(n, n2) ? 0 : 1;
    }

    public static boolean getAreAmbient(Collection<PotionEffect> collection) {
        for (PotionEffect potionEffect : collection) {
            if (potionEffect.getIsAmbient()) continue;
            return false;
        }
        return true;
    }

    public static int calcPotionLiquidColor(Collection<PotionEffect> collection) {
        int n = 3694022;
        if (collection != null && !collection.isEmpty()) {
            float f = 0.0f;
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            for (PotionEffect potionEffect : collection) {
                if (!potionEffect.getIsShowParticles()) continue;
                int n2 = Potion.potionTypes[potionEffect.getPotionID()].getLiquidColor();
                int n3 = 0;
                while (n3 <= potionEffect.getAmplifier()) {
                    f += (float)(n2 >> 16 & 0xFF) / 255.0f;
                    f2 += (float)(n2 >> 8 & 0xFF) / 255.0f;
                    f3 += (float)(n2 >> 0 & 0xFF) / 255.0f;
                    f4 += 1.0f;
                    ++n3;
                }
            }
            if (f4 == 0.0f) {
                return 0;
            }
            f = f / f4 * 255.0f;
            f2 = f2 / f4 * 255.0f;
            f3 = f3 / f4 * 255.0f;
            return (int)f << 16 | (int)f2 << 8 | (int)f3;
        }
        return n;
    }

    private static int brewBitOperations(int n, int n2, boolean bl, boolean bl2, boolean bl3) {
        if (bl3) {
            if (!PotionHelper.checkFlag(n, n2)) {
                return 0;
            }
        } else {
            n = bl ? (n &= ~(1 << n2)) : (bl2 ? ((n & 1 << n2) == 0 ? (n |= 1 << n2) : (n &= ~(1 << n2))) : (n |= 1 << n2));
        }
        return n;
    }

    public static int getLiquidColor(int n, boolean bl) {
        Integer n2 = IntegerCache.func_181756_a(n);
        if (!bl) {
            if (DATAVALUE_COLORS.containsKey(n2)) {
                return DATAVALUE_COLORS.get(n2);
            }
            int n3 = PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(n2, false));
            DATAVALUE_COLORS.put(n2, n3);
            return n3;
        }
        return PotionHelper.calcPotionLiquidColor(PotionHelper.getPotionEffects(n2, true));
    }

    public static int applyIngredient(int n, String string) {
        int n2 = 0;
        int n3 = string.length();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        int n4 = 0;
        int n5 = n2;
        while (n5 < n3) {
            char c = string.charAt(n5);
            if (c >= '0' && c <= '9') {
                n4 *= 10;
                n4 += c - 48;
                bl = true;
            } else if (c == '!') {
                if (bl) {
                    n = PotionHelper.brewBitOperations(n, n4, bl3, bl2, bl4);
                    bl4 = false;
                    bl2 = false;
                    bl3 = false;
                    bl = false;
                    n4 = 0;
                }
                bl2 = true;
            } else if (c == '-') {
                if (bl) {
                    n = PotionHelper.brewBitOperations(n, n4, bl3, bl2, bl4);
                    bl4 = false;
                    bl2 = false;
                    bl3 = false;
                    bl = false;
                    n4 = 0;
                }
                bl3 = true;
            } else if (c == '+') {
                if (bl) {
                    n = PotionHelper.brewBitOperations(n, n4, bl3, bl2, bl4);
                    bl4 = false;
                    bl2 = false;
                    bl3 = false;
                    bl = false;
                    n4 = 0;
                }
            } else if (c == '&') {
                if (bl) {
                    n = PotionHelper.brewBitOperations(n, n4, bl3, bl2, bl4);
                    bl4 = false;
                    bl2 = false;
                    bl3 = false;
                    bl = false;
                    n4 = 0;
                }
                bl4 = true;
            }
            ++n5;
        }
        if (bl) {
            n = PotionHelper.brewBitOperations(n, n4, bl3, bl2, bl4);
        }
        return n & Short.MAX_VALUE;
    }

    public static boolean checkFlag(int n, int n2) {
        return (n & 1 << n2) != 0;
    }

    private static int countSetFlags(int n) {
        int n2 = 0;
        while (n > 0) {
            n &= n - 1;
            ++n2;
        }
        return n2;
    }

    private static int parsePotionEffects(String string, int n, int n2, int n3) {
        if (n < string.length() && n2 >= 0 && n < n2) {
            int n4 = string.indexOf(124, n);
            if (n4 >= 0 && n4 < n2) {
                int n5 = PotionHelper.parsePotionEffects(string, n, n4 - 1, n3);
                if (n5 > 0) {
                    return n5;
                }
                int n6 = PotionHelper.parsePotionEffects(string, n4 + 1, n2, n3);
                return n6 > 0 ? n6 : 0;
            }
            int n7 = string.indexOf(38, n);
            if (n7 >= 0 && n7 < n2) {
                int n8 = PotionHelper.parsePotionEffects(string, n, n7 - 1, n3);
                if (n8 <= 0) {
                    return 0;
                }
                int n9 = PotionHelper.parsePotionEffects(string, n7 + 1, n2, n3);
                return n9 <= 0 ? 0 : (n8 > n9 ? n8 : n9);
            }
            boolean bl = false;
            boolean bl2 = false;
            boolean bl3 = false;
            boolean bl4 = false;
            boolean bl5 = false;
            int n10 = -1;
            int n11 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = n;
            while (n14 < n2) {
                char c = string.charAt(n14);
                if (c >= '0' && c <= '9') {
                    if (bl) {
                        n12 = c - 48;
                        bl2 = true;
                    } else {
                        n11 *= 10;
                        n11 += c - 48;
                        bl3 = true;
                    }
                } else if (c == '*') {
                    bl = true;
                } else if (c == '!') {
                    if (bl3) {
                        n13 += PotionHelper.func_77904_a(bl4, bl2, bl5, n10, n11, n12, n3);
                        bl4 = false;
                        bl5 = false;
                        bl = false;
                        bl2 = false;
                        bl3 = false;
                        n12 = 0;
                        n11 = 0;
                        n10 = -1;
                    }
                    bl4 = true;
                } else if (c == '-') {
                    if (bl3) {
                        n13 += PotionHelper.func_77904_a(bl4, bl2, bl5, n10, n11, n12, n3);
                        bl4 = false;
                        bl5 = false;
                        bl = false;
                        bl2 = false;
                        bl3 = false;
                        n12 = 0;
                        n11 = 0;
                        n10 = -1;
                    }
                    bl5 = true;
                } else if (c != '=' && c != '<' && c != '>') {
                    if (c == '+' && bl3) {
                        n13 += PotionHelper.func_77904_a(bl4, bl2, bl5, n10, n11, n12, n3);
                        bl4 = false;
                        bl5 = false;
                        bl = false;
                        bl2 = false;
                        bl3 = false;
                        n12 = 0;
                        n11 = 0;
                        n10 = -1;
                    }
                } else {
                    if (bl3) {
                        n13 += PotionHelper.func_77904_a(bl4, bl2, bl5, n10, n11, n12, n3);
                        bl4 = false;
                        bl5 = false;
                        bl = false;
                        bl2 = false;
                        bl3 = false;
                        n12 = 0;
                        n11 = 0;
                        n10 = -1;
                    }
                    if (c == '=') {
                        n10 = 0;
                    } else if (c == '<') {
                        n10 = 2;
                    } else if (c == '>') {
                        n10 = 1;
                    }
                }
                ++n14;
            }
            if (bl3) {
                n13 += PotionHelper.func_77904_a(bl4, bl2, bl5, n10, n11, n12, n3);
            }
            return n13;
        }
        return 0;
    }

    static {
        field_77924_a = null;
        potionRequirements = Maps.newHashMap();
        potionAmplifiers = Maps.newHashMap();
        DATAVALUE_COLORS = Maps.newHashMap();
        potionPrefixes = new String[]{"potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky"};
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

    private static int func_77904_a(boolean bl, boolean bl2, boolean bl3, int n, int n2, int n3, int n4) {
        int n5 = 0;
        if (bl) {
            n5 = PotionHelper.isFlagUnset(n4, n2);
        } else if (n != -1) {
            if (n == 0 && PotionHelper.countSetFlags(n4) == n2) {
                n5 = 1;
            } else if (n == 1 && PotionHelper.countSetFlags(n4) > n2) {
                n5 = 1;
            } else if (n == 2 && PotionHelper.countSetFlags(n4) < n2) {
                n5 = 1;
            }
        } else {
            n5 = PotionHelper.isFlagSet(n4, n2);
        }
        if (bl2) {
            n5 *= n3;
        }
        if (bl3) {
            n5 *= -1;
        }
        return n5;
    }

    public static int func_77908_a(int n, int n2, int n3, int n4, int n5, int n6) {
        return (PotionHelper.checkFlag(n, n2) ? 16 : 0) | (PotionHelper.checkFlag(n, n3) ? 8 : 0) | (PotionHelper.checkFlag(n, n4) ? 4 : 0) | (PotionHelper.checkFlag(n, n5) ? 2 : 0) | (PotionHelper.checkFlag(n, n6) ? 1 : 0);
    }

    public static List<PotionEffect> getPotionEffects(int n, boolean bl) {
        ArrayList arrayList = null;
        Potion[] potionArray = Potion.potionTypes;
        int n2 = Potion.potionTypes.length;
        int n3 = 0;
        while (n3 < n2) {
            int n4;
            String string;
            Potion potion = potionArray[n3];
            if (potion != null && (!potion.isUsable() || bl) && (string = potionRequirements.get(potion.getId())) != null && (n4 = PotionHelper.parsePotionEffects(string, 0, string.length(), n)) > 0) {
                int n5 = 0;
                String string2 = potionAmplifiers.get(potion.getId());
                if (string2 != null && (n5 = PotionHelper.parsePotionEffects(string2, 0, string2.length(), n)) < 0) {
                    n5 = 0;
                }
                if (potion.isInstant()) {
                    n4 = 1;
                } else {
                    n4 = 1200 * (n4 * 3 + (n4 - 1) * 2);
                    n4 >>= n5;
                    n4 = (int)Math.round((double)n4 * potion.getEffectiveness());
                    if ((n & 0x4000) != 0) {
                        n4 = (int)Math.round((double)n4 * 0.75 + 0.5);
                    }
                }
                if (arrayList == null) {
                    arrayList = Lists.newArrayList();
                }
                PotionEffect potionEffect = new PotionEffect(potion.getId(), n4, n5);
                if ((n & 0x4000) != 0) {
                    potionEffect.setSplashPotion(true);
                }
                arrayList.add(potionEffect);
            }
            ++n3;
        }
        return arrayList;
    }

    public static String getPotionPrefix(int n) {
        int n2 = PotionHelper.getPotionPrefixIndex(n);
        return potionPrefixes[n2];
    }

    public static int getPotionPrefixIndex(int n) {
        return PotionHelper.func_77908_a(n, 5, 4, 3, 2, 1);
    }
}

