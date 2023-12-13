/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.World;

public class ItemFishFood
extends ItemFood {
    private final boolean cooked;
    private static final String __OBFID = "CL_00000032";

    public ItemFishFood(boolean p_i45338_1_) {
        super(0, 0.0f, false);
        this.cooked = p_i45338_1_;
    }

    @Override
    public int getHealAmount(ItemStack itemStackIn) {
        FishType var2 = FishType.getFishTypeForItemStack(itemStackIn);
        return this.cooked && var2.getCookable() ? var2.getCookedHealAmount() : var2.getUncookedHealAmount();
    }

    @Override
    public float getSaturationModifier(ItemStack itemStackIn) {
        FishType var2 = FishType.getFishTypeForItemStack(itemStackIn);
        return this.cooked && var2.getCookable() ? var2.getCookedSaturationModifier() : var2.getUncookedSaturationModifier();
    }

    @Override
    public String getPotionEffect(ItemStack stack) {
        return FishType.getFishTypeForItemStack(stack) == FishType.PUFFERFISH ? PotionHelper.field_151423_m : null;
    }

    @Override
    protected void onFoodEaten(ItemStack p_77849_1_, World worldIn, EntityPlayer p_77849_3_) {
        FishType var4 = FishType.getFishTypeForItemStack(p_77849_1_);
        if (var4 == FishType.PUFFERFISH) {
            p_77849_3_.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            p_77849_3_.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            p_77849_3_.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }
        super.onFoodEaten(p_77849_1_, worldIn, p_77849_3_);
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (FishType var7 : FishType.values()) {
            if (this.cooked && !var7.getCookable()) continue;
            subItems.add(new ItemStack(this, 1, var7.getItemDamage()));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        FishType var2 = FishType.getFishTypeForItemStack(stack);
        return String.valueOf(this.getUnlocalizedName()) + "." + var2.getUnlocalizedNamePart() + "." + (this.cooked && var2.getCookable() ? "cooked" : "raw");
    }

    public static enum FishType {
        COD("COD", 0, 0, "cod", 2, 0.1f, 5, 0.6f),
        SALMON("SALMON", 1, 1, "salmon", 2, 0.1f, 6, 0.8f),
        CLOWNFISH("CLOWNFISH", 2, 2, "clownfish", 1, 0.1f),
        PUFFERFISH("PUFFERFISH", 3, 3, "pufferfish", 1, 0.1f);

        private static final Map itemDamageToFishTypeMap;
        private final int itemDamage;
        private final String unlocalizedNamePart;
        private final int uncookedHealAmount;
        private final float uncookedSaturationModifier;
        private final int cookedHealAmount;
        private final float cookedSaturationModifier;
        private boolean cookable = false;
        private static final FishType[] $VALUES;
        private static final String __OBFID = "CL_00000033";

        static {
            itemDamageToFishTypeMap = Maps.newHashMap();
            $VALUES = new FishType[]{COD, SALMON, CLOWNFISH, PUFFERFISH};
            for (FishType var3 : FishType.values()) {
                itemDamageToFishTypeMap.put(var3.getItemDamage(), var3);
            }
        }

        private FishType(String p_i45336_1_, int p_i45336_2_, int p_i45336_3_, String p_i45336_4_, int p_i45336_5_, float p_i45336_6_, int p_i45336_7_, float p_i45336_8_) {
            this.itemDamage = p_i45336_3_;
            this.unlocalizedNamePart = p_i45336_4_;
            this.uncookedHealAmount = p_i45336_5_;
            this.uncookedSaturationModifier = p_i45336_6_;
            this.cookedHealAmount = p_i45336_7_;
            this.cookedSaturationModifier = p_i45336_8_;
            this.cookable = true;
        }

        private FishType(String p_i45337_1_, int p_i45337_2_, int p_i45337_3_, String p_i45337_4_, int p_i45337_5_, float p_i45337_6_) {
            this.itemDamage = p_i45337_3_;
            this.unlocalizedNamePart = p_i45337_4_;
            this.uncookedHealAmount = p_i45337_5_;
            this.uncookedSaturationModifier = p_i45337_6_;
            this.cookedHealAmount = 0;
            this.cookedSaturationModifier = 0.0f;
            this.cookable = false;
        }

        public int getItemDamage() {
            return this.itemDamage;
        }

        public String getUnlocalizedNamePart() {
            return this.unlocalizedNamePart;
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

        public boolean getCookable() {
            return this.cookable;
        }

        public static FishType getFishTypeForItemDamage(int p_150974_0_) {
            FishType var1 = (FishType)((Object)itemDamageToFishTypeMap.get(p_150974_0_));
            return var1 == null ? COD : var1;
        }

        public static FishType getFishTypeForItemStack(ItemStack p_150978_0_) {
            return p_150978_0_.getItem() instanceof ItemFishFood ? FishType.getFishTypeForItemDamage(p_150978_0_.getMetadata()) : COD;
        }
    }
}

