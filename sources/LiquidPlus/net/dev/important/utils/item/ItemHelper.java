/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.utils.item;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.dev.important.utils.RegexUtils;
import net.dev.important.utils.item.ArmorPart;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0002\"#B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J*\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010J\u0018\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\tH\u0002J\u0010\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0016\u0010\u0019\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\t2\u0006\u0010\u0015\u001a\u00020\u0016J\"\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u00162\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010J\u0016\u0010 \u001a\u00020!2\u0006\u0010\u001f\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u0010R\u0016\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0006\u00a8\u0006$"}, d2={"Lnet/dev/important/utils/item/ItemHelper;", "", "()V", "armorDamageReduceEnchantments", "", "Lnet/dev/important/utils/item/ItemHelper$Enchant;", "[Lnet/dev/important/utils/item/ItemHelper$Enchant;", "otherArmorEnchantments", "compareArmor", "", "o1", "Lnet/dev/important/utils/item/ArmorPart;", "o2", "nbtedPriority", "", "goal", "Lnet/dev/important/utils/item/ItemHelper$EnumNBTPriorityType;", "getArmorDamageReduction", "defensePoints", "toughness", "getArmorEnchantmentThreshold", "itemStack", "Lnet/minecraft/item/ItemStack;", "getArmorThresholdedDamageReduction", "getArmorThresholdedEnchantmentDamageReduction", "getEnchantment", "enchantment", "Lnet/minecraft/enchantment/Enchantment;", "getEnchantmentCount", "getWeaponEnchantFactor", "", "stack", "hasNBTGoal", "", "Enchant", "EnumNBTPriorityType", "LiquidBounce"})
public final class ItemHelper {
    @NotNull
    public static final ItemHelper INSTANCE = new ItemHelper();
    @NotNull
    private static final Enchant[] armorDamageReduceEnchantments;
    @NotNull
    private static final Enchant[] otherArmorEnchantments;

    private ItemHelper() {
    }

    public final int getEnchantment(@NotNull ItemStack itemStack, @NotNull Enchantment enchantment) {
        Intrinsics.checkNotNullParameter(itemStack, "itemStack");
        Intrinsics.checkNotNullParameter(enchantment, "enchantment");
        if (itemStack.func_77986_q() == null || itemStack.func_77986_q().func_82582_d()) {
            return 0;
        }
        int n = 0;
        int n2 = itemStack.func_77986_q().func_74745_c();
        while (n < n2) {
            int i = n++;
            NBTTagCompound tagCompound = itemStack.func_77986_q().func_150305_b(i);
            if ((!tagCompound.func_74764_b("ench") || tagCompound.func_74765_d("ench") != enchantment.field_77352_x) && (!tagCompound.func_74764_b("id") || tagCompound.func_74765_d("id") != enchantment.field_77352_x)) continue;
            return tagCompound.func_74765_d("lvl");
        }
        return 0;
    }

    public final int getEnchantmentCount(@NotNull ItemStack itemStack) {
        Intrinsics.checkNotNullParameter(itemStack, "itemStack");
        if (itemStack.func_77986_q() == null || itemStack.func_77986_q().func_82582_d()) {
            return 0;
        }
        int c = 0;
        int n = 0;
        int n2 = itemStack.func_77986_q().func_74745_c();
        while (n < n2) {
            int i = n++;
            NBTTagCompound tagCompound = itemStack.func_77986_q().func_150305_b(i);
            if (!tagCompound.func_74764_b("ench") && !tagCompound.func_74764_b("id")) continue;
            int n3 = c;
            c = n3 + 1;
        }
        return c;
    }

    public final double getWeaponEnchantFactor(@NotNull ItemStack stack, float nbtedPriority, @NotNull EnumNBTPriorityType goal) {
        Intrinsics.checkNotNullParameter(stack, "stack");
        Intrinsics.checkNotNullParameter((Object)goal, "goal");
        Enchantment enchantment = Enchantment.field_180314_l;
        Intrinsics.checkNotNullExpressionValue(enchantment, "sharpness");
        double d = 1.25 * (double)this.getEnchantment(stack, enchantment);
        enchantment = Enchantment.field_77334_n;
        Intrinsics.checkNotNullExpressionValue(enchantment, "fireAspect");
        return d + 1.0 * (double)this.getEnchantment(stack, enchantment) + (double)(this.hasNBTGoal(stack, goal) ? nbtedPriority : 0.0f);
    }

    public static /* synthetic */ double getWeaponEnchantFactor$default(ItemHelper itemHelper, ItemStack itemStack, float f, EnumNBTPriorityType enumNBTPriorityType, int n, Object object) {
        if ((n & 2) != 0) {
            f = 0.0f;
        }
        if ((n & 4) != 0) {
            enumNBTPriorityType = EnumNBTPriorityType.NONE;
        }
        return itemHelper.getWeaponEnchantFactor(itemStack, f, enumNBTPriorityType);
    }

    public final int compareArmor(@NotNull ArmorPart o1, @NotNull ArmorPart o2, float nbtedPriority, @NotNull EnumNBTPriorityType goal) {
        Intrinsics.checkNotNullParameter(o1, "o1");
        Intrinsics.checkNotNullParameter(o2, "o2");
        Intrinsics.checkNotNullParameter((Object)goal, "goal");
        int compare = Double.compare(RegexUtils.INSTANCE.round((double)this.getArmorThresholdedDamageReduction(o2.getItemStack()) - (double)(this.hasNBTGoal(o2.getItemStack(), goal) ? nbtedPriority / 5.0f : 0.0f), 3), RegexUtils.INSTANCE.round((double)this.getArmorThresholdedDamageReduction(o1.getItemStack()) - (double)(this.hasNBTGoal(o1.getItemStack(), goal) ? nbtedPriority / 5.0f : 0.0f), 3));
        if (compare == 0) {
            int otherEnchantmentCmp = Double.compare(RegexUtils.INSTANCE.round(this.getArmorEnchantmentThreshold(o1.getItemStack()), 3), RegexUtils.INSTANCE.round(this.getArmorEnchantmentThreshold(o2.getItemStack()), 3));
            if (otherEnchantmentCmp == 0) {
                int enchantmentCountCmp = Intrinsics.compare(this.getEnchantmentCount(o1.getItemStack()), this.getEnchantmentCount(o2.getItemStack()));
                if (enchantmentCountCmp != 0) {
                    return enchantmentCountCmp;
                }
                Item item = o1.getItemStack().func_77973_b();
                if (item == null) {
                    throw new NullPointerException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
                }
                ItemArmor o1a = (ItemArmor)item;
                Item item2 = o2.getItemStack().func_77973_b();
                if (item2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
                }
                ItemArmor o2a = (ItemArmor)item2;
                int durabilityCmp = Intrinsics.compare(o1a.func_82812_d().func_78046_a(o1a.field_77881_a), o2a.func_82812_d().func_78046_a(o2a.field_77881_a));
                return durabilityCmp != 0 ? durabilityCmp : Intrinsics.compare(o1a.func_82812_d().func_78045_a(), o2a.func_82812_d().func_78045_a());
            }
            return otherEnchantmentCmp;
        }
        return compare;
    }

    public static /* synthetic */ int compareArmor$default(ItemHelper itemHelper, ArmorPart armorPart, ArmorPart armorPart2, float f, EnumNBTPriorityType enumNBTPriorityType, int n, Object object) {
        if ((n & 4) != 0) {
            f = 0.0f;
        }
        if ((n & 8) != 0) {
            enumNBTPriorityType = EnumNBTPriorityType.NONE;
        }
        return itemHelper.compareArmor(armorPart, armorPart2, f, enumNBTPriorityType);
    }

    private final float getArmorThresholdedDamageReduction(ItemStack itemStack) {
        Item item = itemStack.func_77973_b();
        if (item == null) {
            throw new NullPointerException("null cannot be cast to non-null type net.minecraft.item.ItemArmor");
        }
        ItemArmor item2 = (ItemArmor)item;
        return this.getArmorDamageReduction(item2.func_82812_d().func_78044_b(item2.field_77881_a), 0) * (1.0f - this.getArmorThresholdedEnchantmentDamageReduction(itemStack));
    }

    private final float getArmorDamageReduction(int defensePoints, int toughness) {
        return 1.0f - RangesKt.coerceAtMost(20.0f, RangesKt.coerceAtLeast((float)defensePoints / 5.0f, (float)defensePoints - 1.0f / ((float)2 + (float)toughness / 4.0f))) / 25.0f;
    }

    private final float getArmorThresholdedEnchantmentDamageReduction(ItemStack itemStack) {
        float sum = 0.0f;
        int n = 0;
        int n2 = armorDamageReduceEnchantments.length;
        while (n < n2) {
            int i = n++;
            sum += (float)this.getEnchantment(itemStack, armorDamageReduceEnchantments[i].getEnchantment()) * armorDamageReduceEnchantments[i].getFactor();
        }
        return sum;
    }

    private final float getArmorEnchantmentThreshold(ItemStack itemStack) {
        float sum = 0.0f;
        int n = 0;
        int n2 = otherArmorEnchantments.length;
        while (n < n2) {
            int i = n++;
            sum += (float)this.getEnchantment(itemStack, otherArmorEnchantments[i].getEnchantment()) * otherArmorEnchantments[i].getFactor();
        }
        return sum;
    }

    public final boolean hasNBTGoal(@NotNull ItemStack stack, @NotNull EnumNBTPriorityType goal) {
        Intrinsics.checkNotNullParameter(stack, "stack");
        Intrinsics.checkNotNullParameter((Object)goal, "goal");
        if (stack.func_77942_o() && stack.func_77978_p().func_150297_b("display", 10)) {
            NBTTagCompound display = stack.func_77978_p().func_74775_l("display");
            if (goal == EnumNBTPriorityType.HAS_DISPLAY_TAG) {
                return true;
            }
            if (goal == EnumNBTPriorityType.HAS_NAME) {
                return display.func_74764_b("Name");
            }
            if (goal == EnumNBTPriorityType.HAS_LORE) {
                return display.func_74764_b("Lore") && display.func_150295_c("Lore", 8).func_74745_c() > 0;
            }
        }
        return false;
    }

    static {
        Enchant[] enchantArray = new Enchant[4];
        Enchantment enchantment = Enchantment.field_180310_c;
        Intrinsics.checkNotNullExpressionValue(enchantment, "protection");
        enchantArray[0] = new Enchant(enchantment, 0.06f);
        enchantment = Enchantment.field_180308_g;
        Intrinsics.checkNotNullExpressionValue(enchantment, "projectileProtection");
        enchantArray[1] = new Enchant(enchantment, 0.032f);
        enchantment = Enchantment.field_77329_d;
        Intrinsics.checkNotNullExpressionValue(enchantment, "fireProtection");
        enchantArray[2] = new Enchant(enchantment, 0.0585f);
        enchantment = Enchantment.field_77327_f;
        Intrinsics.checkNotNullExpressionValue(enchantment, "blastProtection");
        enchantArray[3] = new Enchant(enchantment, 0.0304f);
        armorDamageReduceEnchantments = enchantArray;
        enchantArray = new Enchant[5];
        enchantment = Enchantment.field_180309_e;
        Intrinsics.checkNotNullExpressionValue(enchantment, "featherFalling");
        enchantArray[0] = new Enchant(enchantment, 3.0f);
        enchantment = Enchantment.field_92091_k;
        Intrinsics.checkNotNullExpressionValue(enchantment, "thorns");
        enchantArray[1] = new Enchant(enchantment, 1.0f);
        enchantment = Enchantment.field_180317_h;
        Intrinsics.checkNotNullExpressionValue(enchantment, "respiration");
        enchantArray[2] = new Enchant(enchantment, 0.1f);
        enchantment = Enchantment.field_77341_i;
        Intrinsics.checkNotNullExpressionValue(enchantment, "aquaAffinity");
        enchantArray[3] = new Enchant(enchantment, 0.05f);
        enchantment = Enchantment.field_77347_r;
        Intrinsics.checkNotNullExpressionValue(enchantment, "unbreaking");
        enchantArray[4] = new Enchant(enchantment, 0.01f);
        otherArmorEnchantments = enchantArray;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2={"Lnet/dev/important/utils/item/ItemHelper$Enchant;", "", "enchantment", "Lnet/minecraft/enchantment/Enchantment;", "factor", "", "(Lnet/minecraft/enchantment/Enchantment;F)V", "getEnchantment", "()Lnet/minecraft/enchantment/Enchantment;", "getFactor", "()F", "LiquidBounce"})
    public static final class Enchant {
        @NotNull
        private final Enchantment enchantment;
        private final float factor;

        public Enchant(@NotNull Enchantment enchantment, float factor) {
            Intrinsics.checkNotNullParameter(enchantment, "enchantment");
            this.enchantment = enchantment;
            this.factor = factor;
        }

        @NotNull
        public final Enchantment getEnchantment() {
            return this.enchantment;
        }

        public final float getFactor() {
            return this.factor;
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/utils/item/ItemHelper$EnumNBTPriorityType;", "", "(Ljava/lang/String;I)V", "HAS_NAME", "HAS_LORE", "HAS_DISPLAY_TAG", "NONE", "LiquidBounce"})
    public static final class EnumNBTPriorityType
    extends Enum<EnumNBTPriorityType> {
        public static final /* enum */ EnumNBTPriorityType HAS_NAME = new EnumNBTPriorityType();
        public static final /* enum */ EnumNBTPriorityType HAS_LORE = new EnumNBTPriorityType();
        public static final /* enum */ EnumNBTPriorityType HAS_DISPLAY_TAG = new EnumNBTPriorityType();
        public static final /* enum */ EnumNBTPriorityType NONE = new EnumNBTPriorityType();
        private static final /* synthetic */ EnumNBTPriorityType[] $VALUES;

        public static EnumNBTPriorityType[] values() {
            return (EnumNBTPriorityType[])$VALUES.clone();
        }

        public static EnumNBTPriorityType valueOf(String value) {
            return Enum.valueOf(EnumNBTPriorityType.class, value);
        }

        static {
            $VALUES = enumNBTPriorityTypeArray = new EnumNBTPriorityType[]{EnumNBTPriorityType.HAS_NAME, EnumNBTPriorityType.HAS_LORE, EnumNBTPriorityType.HAS_DISPLAY_TAG, EnumNBTPriorityType.NONE};
        }
    }
}

