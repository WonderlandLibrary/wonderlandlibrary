package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public class ItemFishFood
extends ItemFood {
    private final boolean cooked;

    public ItemFishFood(boolean cooked) {
        super(0, 0.0f, false);
        this.cooked = cooked;
    }

    public int getHealAmount(ItemStack stack) {
        FishType itemfishfood$fishtype = FishType.byItemStack((ItemStack)stack);
        return this.cooked && itemfishfood$fishtype.canCook() ? itemfishfood$fishtype.getCookedHealAmount() : itemfishfood$fishtype.getUncookedHealAmount();
    }

    public float getSaturationModifier(ItemStack stack) {
        FishType itemfishfood$fishtype = FishType.byItemStack((ItemStack)stack);
        return this.cooked && itemfishfood$fishtype.canCook() ? itemfishfood$fishtype.getCookedSaturationModifier() : itemfishfood$fishtype.getUncookedSaturationModifier();
    }

    public String getPotionEffect(ItemStack stack) {
        return FishType.byItemStack((ItemStack)stack) == FishType.PUFFERFISH ? "+0-1+2+3+13&4-4" : null;
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        FishType itemfishfood$fishtype = FishType.byItemStack((ItemStack)stack);
        if (itemfishfood$fishtype == FishType.PUFFERFISH) {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, 1200, 3));
            player.addPotionEffect(new PotionEffect(Potion.hunger.id, 300, 2));
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
        }
        super.onFoodEaten(stack, worldIn, player);
    }

    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (FishType itemfishfood$fishtype : FishType.values()) {
            if (this.cooked && !itemfishfood$fishtype.canCook()) continue;
            subItems.add((Object)new ItemStack((Item)this, 1, itemfishfood$fishtype.getMetadata()));
        }
    }

    public String getUnlocalizedName(ItemStack stack) {
        FishType itemfishfood$fishtype = FishType.byItemStack((ItemStack)stack);
        return this.getUnlocalizedName() + "." + itemfishfood$fishtype.getUnlocalizedName() + "." + (this.cooked && itemfishfood$fishtype.canCook() ? "cooked" : "raw");
    }
}
