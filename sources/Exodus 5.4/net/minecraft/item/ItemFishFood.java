/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
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

    @Override
    public float getSaturationModifier(ItemStack itemStack) {
        FishType fishType = FishType.byItemStack(itemStack);
        return this.cooked && fishType.canCook() ? fishType.getCookedSaturationModifier() : fishType.getUncookedSaturationModifier();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        FishType fishType = FishType.byItemStack(itemStack);
        return String.valueOf(this.getUnlocalizedName()) + "." + fishType.getUnlocalizedName() + "." + (this.cooked && fishType.canCook() ? "cooked" : "raw");
    }

    @Override
    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        FishType fishType = FishType.byItemStack(itemStack);
        if (fishType == FishType.PUFFERFISH) {
            entityPlayer.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            entityPlayer.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            entityPlayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }
        super.onFoodEaten(itemStack, world, entityPlayer);
    }

    public ItemFishFood(boolean bl) {
        super(0, 0.0f, false);
        this.cooked = bl;
    }

    @Override
    public String getPotionEffect(ItemStack itemStack) {
        return FishType.byItemStack(itemStack) == FishType.PUFFERFISH ? "+0-1+2+3+13&4-4" : null;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        FishType[] fishTypeArray = FishType.values();
        int n = fishTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            FishType fishType = fishTypeArray[n2];
            if (!this.cooked || fishType.canCook()) {
                list.add(new ItemStack(this, 1, fishType.getMetadata()));
            }
            ++n2;
        }
    }

    @Override
    public int getHealAmount(ItemStack itemStack) {
        FishType fishType = FishType.byItemStack(itemStack);
        return this.cooked && fishType.canCook() ? fishType.getCookedHealAmount() : fishType.getUncookedHealAmount();
    }

    public static enum FishType {
        COD(0, "cod", 2, 0.1f, 5, 0.6f),
        SALMON(1, "salmon", 2, 0.1f, 6, 0.8f),
        CLOWNFISH(2, "clownfish", 1, 0.1f),
        PUFFERFISH(3, "pufferfish", 1, 0.1f);

        private final float cookedSaturationModifier;
        private final int uncookedHealAmount;
        private final String unlocalizedName;
        private final int cookedHealAmount;
        private static final Map<Integer, FishType> META_LOOKUP;
        private final float uncookedSaturationModifier;
        private final int meta;
        private boolean cookable = false;

        public static FishType byMetadata(int n) {
            FishType fishType = META_LOOKUP.get(n);
            return fishType == null ? COD : fishType;
        }

        public int getUncookedHealAmount() {
            return this.uncookedHealAmount;
        }

        public static FishType byItemStack(ItemStack itemStack) {
            return itemStack.getItem() instanceof ItemFishFood ? FishType.byMetadata(itemStack.getMetadata()) : COD;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        private FishType(int n2, String string2, int n3, float f, int n4, float f2) {
            this.meta = n2;
            this.unlocalizedName = string2;
            this.uncookedHealAmount = n3;
            this.uncookedSaturationModifier = f;
            this.cookedHealAmount = n4;
            this.cookedSaturationModifier = f2;
            this.cookable = true;
        }

        static {
            META_LOOKUP = Maps.newHashMap();
            FishType[] fishTypeArray = FishType.values();
            int n = fishTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                FishType fishType = fishTypeArray[n2];
                META_LOOKUP.put(fishType.getMetadata(), fishType);
                ++n2;
            }
        }

        public float getCookedSaturationModifier() {
            return this.cookedSaturationModifier;
        }

        public float getUncookedSaturationModifier() {
            return this.uncookedSaturationModifier;
        }

        public boolean canCook() {
            return this.cookable;
        }

        public int getCookedHealAmount() {
            return this.cookedHealAmount;
        }

        private FishType(int n2, String string2, int n3, float f) {
            this.meta = n2;
            this.unlocalizedName = string2;
            this.uncookedHealAmount = n3;
            this.uncookedSaturationModifier = f;
            this.cookedHealAmount = 0;
            this.cookedSaturationModifier = 0.0f;
            this.cookable = false;
        }
    }
}

