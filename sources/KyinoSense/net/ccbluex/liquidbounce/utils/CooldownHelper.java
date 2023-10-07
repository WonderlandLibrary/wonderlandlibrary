/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemHoe
 *  net.minecraft.item.ItemPickaxe
 *  net.minecraft.item.ItemSpade
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.MathHelper
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils;

import kotlin.Metadata;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.utils.CooldownHelper$WhenMappings;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\u0004J\u0006\u0010\b\u001a\u00020\u0004J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\nJ\u0010\u0010\f\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/utils/CooldownHelper;", "", "()V", "genericAttackSpeed", "", "lastAttackedTicks", "", "getAttackCooldownProgress", "getAttackCooldownProgressPerTick", "incrementLastAttackedTicks", "", "resetLastAttackedTicks", "updateGenericAttackSpeed", "itemStack", "Lnet/minecraft/item/ItemStack;", "KyinoClient"})
public final class CooldownHelper {
    private static int lastAttackedTicks;
    private static double genericAttackSpeed;
    public static final CooldownHelper INSTANCE;

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public final void updateGenericAttackSpeed(@Nullable ItemStack itemStack) {
        block14: {
            block18: {
                block19: {
                    block20: {
                        block17: {
                            block16: {
                                block15: {
                                    block13: {
                                        v0 = itemStack;
                                        v1 /* !! */  = var2_2 = v0 != null ? v0.func_77973_b() : null;
                                        if (!(var2_2 instanceof ItemSword)) break block13;
                                        v2 = 1.6;
                                        break block14;
                                    }
                                    if (!(var2_2 instanceof ItemAxe)) break block15;
                                    v3 = itemStack.func_77973_b();
                                    if (v3 == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemAxe");
                                    }
                                    axe = (ItemAxe)v3;
                                    v4 = axe.func_150913_i();
                                    if (v4 == null) ** GOTO lbl-1000
                                    switch (CooldownHelper$WhenMappings.$EnumSwitchMapping$0[v4.ordinal()]) {
                                        case 1: {
                                            v2 = 0.9;
                                            break;
                                        }
                                        case 2: 
                                        case 3: {
                                            v2 = 0.8;
                                            break;
                                        }
                                        default: lbl-1000:
                                        // 2 sources

                                        {
                                            v2 = 1.0;
                                            break;
                                        }
                                    }
                                    break block14;
                                }
                                if (!(var2_2 instanceof ItemPickaxe)) break block16;
                                v2 = 1.2;
                                break block14;
                            }
                            if (!(var2_2 instanceof ItemSpade)) break block17;
                            v2 = 1.0;
                            break block14;
                        }
                        if (!(var2_2 instanceof ItemHoe)) break block18;
                        v5 = itemStack.func_77973_b();
                        if (v5 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.ItemHoe");
                        }
                        hoe = (ItemHoe)v5;
                        v6 = hoe.func_77842_f();
                        if (v6 == null) break block19;
                        var4_6 = v6;
                        switch (var4_6.hashCode()) {
                            case -1921929932: {
                                if (!var4_6.equals("DIAMOND")) ** break;
                                break block20;
                            }
                            case 2256072: {
                                if (!var4_6.equals("IRON")) ** break;
                                break;
                            }
                            case 79233093: {
                                if (!var4_6.equals("STONE")) ** break;
                                v2 = 2.0;
                                break block14;
                            }
                        }
                        v2 = 3.0;
                        break block14;
                    }
                    v2 = 4.0;
                    break block14;
                }
                v2 = 1.0;
                break block14;
            }
            v2 = CooldownHelper.genericAttackSpeed = 4.0;
        }
        if (MinecraftInstance.mc.field_71439_g.func_70644_a(Potion.field_76419_f)) {
            var2_3 = 1.0;
            var4_7 = 0.1 * (double)MinecraftInstance.mc.field_71439_g.func_70660_b(Potion.field_76419_f).func_76458_c() + (double)true;
            var9_8 = 1.0;
            var7_9 = CooldownHelper.genericAttackSpeed;
            var6_10 = false;
            var11_11 = Math.min(var2_3, var4_7);
            CooldownHelper.genericAttackSpeed = var7_9 * (var9_8 - var11_11);
        }
        if (MinecraftInstance.mc.field_71439_g.func_70644_a(Potion.field_76422_e)) {
            CooldownHelper.genericAttackSpeed *= 1.0 + (0.1 * (double)MinecraftInstance.mc.field_71439_g.func_70660_b(Potion.field_76422_e).func_76458_c() + (double)true);
        }
    }

    public final double getAttackCooldownProgressPerTick() {
        return 1.0 / genericAttackSpeed * 20.0;
    }

    public final double getAttackCooldownProgress() {
        return MathHelper.func_151237_a((double)((double)lastAttackedTicks / this.getAttackCooldownProgressPerTick()), (double)0.0, (double)1.0);
    }

    public final void resetLastAttackedTicks() {
        lastAttackedTicks = 0;
    }

    public final void incrementLastAttackedTicks() {
        int n = lastAttackedTicks;
        lastAttackedTicks = n + 1;
    }

    private CooldownHelper() {
    }

    static {
        CooldownHelper cooldownHelper;
        INSTANCE = cooldownHelper = new CooldownHelper();
    }
}

