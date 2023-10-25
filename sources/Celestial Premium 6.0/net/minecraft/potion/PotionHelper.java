/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.potion;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class PotionHelper {
    private static final List<MixPredicate<PotionType>> POTION_TYPE_CONVERSIONS = Lists.newArrayList();
    private static final List<MixPredicate<Item>> POTION_ITEM_CONVERSIONS = Lists.newArrayList();
    private static final List<Ingredient> POTION_ITEMS = Lists.newArrayList();
    private static final Predicate<ItemStack> IS_POTION_ITEM = new Predicate<ItemStack>(){

        @Override
        public boolean apply(ItemStack p_apply_1_) {
            for (Ingredient ingredient : POTION_ITEMS) {
                if (!ingredient.apply(p_apply_1_)) continue;
                return true;
            }
            return false;
        }
    };

    public static boolean isReagent(ItemStack stack) {
        return PotionHelper.isItemConversionReagent(stack) || PotionHelper.isTypeConversionReagent(stack);
    }

    protected static boolean isItemConversionReagent(ItemStack stack) {
        int j = POTION_ITEM_CONVERSIONS.size();
        for (int i = 0; i < j; ++i) {
            if (!PotionHelper.POTION_ITEM_CONVERSIONS.get((int)i).reagent.apply(stack)) continue;
            return true;
        }
        return false;
    }

    protected static boolean isTypeConversionReagent(ItemStack stack) {
        int j = POTION_TYPE_CONVERSIONS.size();
        for (int i = 0; i < j; ++i) {
            if (!PotionHelper.POTION_TYPE_CONVERSIONS.get((int)i).reagent.apply(stack)) continue;
            return true;
        }
        return false;
    }

    public static boolean hasConversions(ItemStack input, ItemStack reagent) {
        if (!IS_POTION_ITEM.apply(input)) {
            return false;
        }
        return PotionHelper.hasItemConversions(input, reagent) || PotionHelper.hasTypeConversions(input, reagent);
    }

    protected static boolean hasItemConversions(ItemStack p_185206_0_, ItemStack p_185206_1_) {
        Item item = p_185206_0_.getItem();
        int j = POTION_ITEM_CONVERSIONS.size();
        for (int i = 0; i < j; ++i) {
            MixPredicate<Item> mixpredicate = POTION_ITEM_CONVERSIONS.get(i);
            if (mixpredicate.input != item || !mixpredicate.reagent.apply(p_185206_1_)) continue;
            return true;
        }
        return false;
    }

    protected static boolean hasTypeConversions(ItemStack p_185209_0_, ItemStack p_185209_1_) {
        PotionType potiontype = PotionUtils.getPotionFromItem(p_185209_0_);
        int j = POTION_TYPE_CONVERSIONS.size();
        for (int i = 0; i < j; ++i) {
            MixPredicate<PotionType> mixpredicate = POTION_TYPE_CONVERSIONS.get(i);
            if (mixpredicate.input != potiontype || !mixpredicate.reagent.apply(p_185209_1_)) continue;
            return true;
        }
        return false;
    }

    public static ItemStack doReaction(ItemStack reagent, ItemStack potionIn) {
        if (!potionIn.isEmpty()) {
            int i;
            PotionType potiontype = PotionUtils.getPotionFromItem(potionIn);
            Item item = potionIn.getItem();
            int j = POTION_ITEM_CONVERSIONS.size();
            for (i = 0; i < j; ++i) {
                MixPredicate<Item> mixpredicate = POTION_ITEM_CONVERSIONS.get(i);
                if (mixpredicate.input != item || !mixpredicate.reagent.apply(reagent)) continue;
                return PotionUtils.addPotionToItemStack(new ItemStack((Item)mixpredicate.output), potiontype);
            }
            int k = POTION_TYPE_CONVERSIONS.size();
            for (i = 0; i < k; ++i) {
                MixPredicate<PotionType> mixpredicate1 = POTION_TYPE_CONVERSIONS.get(i);
                if (mixpredicate1.input != potiontype || !mixpredicate1.reagent.apply(reagent)) continue;
                return PotionUtils.addPotionToItemStack(new ItemStack(item), (PotionType)mixpredicate1.output);
            }
        }
        return potionIn;
    }

    public static void init() {
        PotionHelper.func_193354_a(Items.POTIONITEM);
        PotionHelper.func_193354_a(Items.SPLASH_POTION);
        PotionHelper.func_193354_a(Items.LINGERING_POTION);
        PotionHelper.func_193355_a(Items.POTIONITEM, Items.GUNPOWDER, Items.SPLASH_POTION);
        PotionHelper.func_193355_a(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.SPECKLED_MELON, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.GHAST_TEAR, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.RABBIT_FOOT, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.BLAZE_POWDER, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.SPIDER_EYE, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.SUGAR, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.MAGMA_CREAM, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.GLOWSTONE_DUST, PotionTypes.THICK);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.REDSTONE, PotionTypes.MUNDANE);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.NETHER_WART, PotionTypes.AWKWARD);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.GOLDEN_CARROT, PotionTypes.NIGHT_VISION);
        PotionHelper.func_193357_a(PotionTypes.NIGHT_VISION, Items.REDSTONE, PotionTypes.LONG_NIGHT_VISION);
        PotionHelper.func_193357_a(PotionTypes.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, PotionTypes.INVISIBILITY);
        PotionHelper.func_193357_a(PotionTypes.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_INVISIBILITY);
        PotionHelper.func_193357_a(PotionTypes.INVISIBILITY, Items.REDSTONE, PotionTypes.LONG_INVISIBILITY);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.MAGMA_CREAM, PotionTypes.FIRE_RESISTANCE);
        PotionHelper.func_193357_a(PotionTypes.FIRE_RESISTANCE, Items.REDSTONE, PotionTypes.LONG_FIRE_RESISTANCE);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.RABBIT_FOOT, PotionTypes.LEAPING);
        PotionHelper.func_193357_a(PotionTypes.LEAPING, Items.REDSTONE, PotionTypes.LONG_LEAPING);
        PotionHelper.func_193357_a(PotionTypes.LEAPING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_LEAPING);
        PotionHelper.func_193357_a(PotionTypes.LEAPING, Items.FERMENTED_SPIDER_EYE, PotionTypes.SLOWNESS);
        PotionHelper.func_193357_a(PotionTypes.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_SLOWNESS);
        PotionHelper.func_193357_a(PotionTypes.SLOWNESS, Items.REDSTONE, PotionTypes.LONG_SLOWNESS);
        PotionHelper.func_193357_a(PotionTypes.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, PotionTypes.SLOWNESS);
        PotionHelper.func_193357_a(PotionTypes.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, PotionTypes.LONG_SLOWNESS);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.SUGAR, PotionTypes.SWIFTNESS);
        PotionHelper.func_193357_a(PotionTypes.SWIFTNESS, Items.REDSTONE, PotionTypes.LONG_SWIFTNESS);
        PotionHelper.func_193357_a(PotionTypes.SWIFTNESS, Items.GLOWSTONE_DUST, PotionTypes.STRONG_SWIFTNESS);
        PotionHelper.func_193356_a(PotionTypes.AWKWARD, Ingredient.func_193369_a(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())), PotionTypes.WATER_BREATHING);
        PotionHelper.func_193357_a(PotionTypes.WATER_BREATHING, Items.REDSTONE, PotionTypes.LONG_WATER_BREATHING);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.SPECKLED_MELON, PotionTypes.HEALING);
        PotionHelper.func_193357_a(PotionTypes.HEALING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_HEALING);
        PotionHelper.func_193357_a(PotionTypes.HEALING, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
        PotionHelper.func_193357_a(PotionTypes.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, PotionTypes.STRONG_HARMING);
        PotionHelper.func_193357_a(PotionTypes.HARMING, Items.GLOWSTONE_DUST, PotionTypes.STRONG_HARMING);
        PotionHelper.func_193357_a(PotionTypes.POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
        PotionHelper.func_193357_a(PotionTypes.LONG_POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.HARMING);
        PotionHelper.func_193357_a(PotionTypes.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, PotionTypes.STRONG_HARMING);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.SPIDER_EYE, PotionTypes.POISON);
        PotionHelper.func_193357_a(PotionTypes.POISON, Items.REDSTONE, PotionTypes.LONG_POISON);
        PotionHelper.func_193357_a(PotionTypes.POISON, Items.GLOWSTONE_DUST, PotionTypes.STRONG_POISON);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.GHAST_TEAR, PotionTypes.REGENERATION);
        PotionHelper.func_193357_a(PotionTypes.REGENERATION, Items.REDSTONE, PotionTypes.LONG_REGENERATION);
        PotionHelper.func_193357_a(PotionTypes.REGENERATION, Items.GLOWSTONE_DUST, PotionTypes.STRONG_REGENERATION);
        PotionHelper.func_193357_a(PotionTypes.AWKWARD, Items.BLAZE_POWDER, PotionTypes.STRENGTH);
        PotionHelper.func_193357_a(PotionTypes.STRENGTH, Items.REDSTONE, PotionTypes.LONG_STRENGTH);
        PotionHelper.func_193357_a(PotionTypes.STRENGTH, Items.GLOWSTONE_DUST, PotionTypes.STRONG_STRENGTH);
        PotionHelper.func_193357_a(PotionTypes.WATER, Items.FERMENTED_SPIDER_EYE, PotionTypes.WEAKNESS);
        PotionHelper.func_193357_a(PotionTypes.WEAKNESS, Items.REDSTONE, PotionTypes.LONG_WEAKNESS);
    }

    private static void func_193355_a(ItemPotion p_193355_0_, Item p_193355_1_, ItemPotion p_193355_2_) {
        POTION_ITEM_CONVERSIONS.add(new MixPredicate<ItemPotion>(p_193355_0_, Ingredient.func_193368_a(p_193355_1_), p_193355_2_));
    }

    private static void func_193354_a(ItemPotion p_193354_0_) {
        POTION_ITEMS.add(Ingredient.func_193368_a(p_193354_0_));
    }

    private static void func_193357_a(PotionType p_193357_0_, Item p_193357_1_, PotionType p_193357_2_) {
        PotionHelper.func_193356_a(p_193357_0_, Ingredient.func_193368_a(p_193357_1_), p_193357_2_);
    }

    private static void func_193356_a(PotionType p_193356_0_, Ingredient p_193356_1_, PotionType p_193356_2_) {
        POTION_TYPE_CONVERSIONS.add(new MixPredicate<PotionType>(p_193356_0_, p_193356_1_, p_193356_2_));
    }

    static class MixPredicate<T> {
        final T input;
        final Ingredient reagent;
        final T output;

        public MixPredicate(T p_i47570_1_, Ingredient p_i47570_2_, T p_i47570_3_) {
            this.input = p_i47570_1_;
            this.reagent = p_i47570_2_;
            this.output = p_i47570_3_;
        }
    }
}

