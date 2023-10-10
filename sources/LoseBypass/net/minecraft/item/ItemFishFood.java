/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFishFood
extends ItemFood {
    private final boolean cooked;

    public ItemFishFood(boolean cooked) {
        super(0, 0.0f, false);
        this.cooked = cooked;
    }

    @Override
    public int getHealAmount(ItemStack stack) {
        int n;
        FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        if (this.cooked && itemfishfood$fishtype.canCook()) {
            n = itemfishfood$fishtype.getCookedHealAmount();
            return n;
        }
        n = itemfishfood$fishtype.getUncookedHealAmount();
        return n;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        float f;
        FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        if (this.cooked && itemfishfood$fishtype.canCook()) {
            f = itemfishfood$fishtype.getCookedSaturationModifier();
            return f;
        }
        f = itemfishfood$fishtype.getUncookedSaturationModifier();
        return f;
    }

    @Override
    public String getPotionEffect(ItemStack stack) {
        if (FishType.byItemStack(stack) != FishType.PUFFERFISH) return null;
        return "+0-1+2+3+13&4-4";
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        if (itemfishfood$fishtype == FishType.PUFFERFISH) {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            player.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }
        super.onFoodEaten(stack, worldIn, player);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        FishType[] fishTypeArray = FishType.values();
        int n = fishTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            FishType itemfishfood$fishtype = fishTypeArray[n2];
            if (!this.cooked || itemfishfood$fishtype.canCook()) {
                subItems.add(new ItemStack(this, 1, itemfishfood$fishtype.getMetadata()));
            }
            ++n2;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        String string;
        FishType itemfishfood$fishtype = FishType.byItemStack(stack);
        StringBuilder stringBuilder = new StringBuilder().append(this.getUnlocalizedName()).append(".").append(itemfishfood$fishtype.getUnlocalizedName()).append(".");
        if (this.cooked && itemfishfood$fishtype.canCook()) {
            string = "cooked";
            return stringBuilder.append(string).toString();
        }
        string = "raw";
        return stringBuilder.append(string).toString();
    }

    public static enum FishType {
        COD(0, "cod", 2, 0.1f, 5, 0.6f),
        SALMON(1, "salmon", 2, 0.1f, 6, 0.8f),
        CLOWNFISH(2, "clownfish", 1, 0.1f),
        PUFFERFISH(3, "pufferfish", 1, 0.1f);

        private static final Map<Integer, FishType> META_LOOKUP;
        private final int meta;
        private final String unlocalizedName;
        private final int uncookedHealAmount;
        private final float uncookedSaturationModifier;
        private final int cookedHealAmount;
        private final float cookedSaturationModifier;
        private boolean cookable = false;

        private FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation, int cookedHeal, float cookedSaturation) {
            this.meta = meta;
            this.unlocalizedName = unlocalizedName;
            this.uncookedHealAmount = uncookedHeal;
            this.uncookedSaturationModifier = uncookedSaturation;
            this.cookedHealAmount = cookedHeal;
            this.cookedSaturationModifier = cookedSaturation;
            this.cookable = true;
        }

        private FishType(int meta, String unlocalizedName, int uncookedHeal, float uncookedSaturation) {
            this.meta = meta;
            this.unlocalizedName = unlocalizedName;
            this.uncookedHealAmount = uncookedHeal;
            this.uncookedSaturationModifier = uncookedSaturation;
            this.cookedHealAmount = 0;
            this.cookedSaturationModifier = 0.0f;
            this.cookable = false;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public int getUncookedHealAmount() {
            return this.uncookedHealAmount;
        }

        public float getUncookedSaturationModifier() {
            return this.uncookedSaturationModifier;
        }

        public int getCookedHealAmount() {
            return this.cookedHealAmount;
        }

        public float getCookedSaturationModifier() {
            return this.cookedSaturationModifier;
        }

        public boolean canCook() {
            return this.cookable;
        }

        public static FishType byMetadata(int meta) {
            FishType fishType;
            FishType itemfishfood$fishtype = META_LOOKUP.get(meta);
            if (itemfishfood$fishtype == null) {
                fishType = COD;
                return fishType;
            }
            fishType = itemfishfood$fishtype;
            return fishType;
        }

        public static FishType byItemStack(ItemStack stack) {
            FishType fishType;
            if (stack.getItem() instanceof ItemFishFood) {
                fishType = FishType.byMetadata(stack.getMetadata());
                return fishType;
            }
            fishType = COD;
            return fishType;
        }

        static {
            META_LOOKUP = Maps.newHashMap();
            FishType[] fishTypeArray = FishType.values();
            int n = fishTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                FishType itemfishfood$fishtype = fishTypeArray[n2];
                META_LOOKUP.put(itemfishfood$fishtype.getMetadata(), itemfishfood$fishtype);
                ++n2;
            }
        }
    }
}

