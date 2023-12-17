// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemAppleGold extends ItemFood
{
    public ItemAppleGold(final int amount, final float saturation, final boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.setHasSubtypes(true);
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return super.hasEffect(stack) || stack.getMetadata() > 0;
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return (stack.getMetadata() == 0) ? EnumRarity.RARE : EnumRarity.EPIC;
    }
    
    @Override
    protected void onFoodEaten(final ItemStack stack, final World worldIn, final EntityPlayer player) {
        if (!worldIn.isRemote) {
            if (stack.getMetadata() > 0) {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 400, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 6000, 0));
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 6000, 0));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 3));
            }
            else {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 100, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2400, 0));
            }
        }
    }
    
    @Override
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this));
            items.add(new ItemStack(this, 1, 1));
        }
    }
}
