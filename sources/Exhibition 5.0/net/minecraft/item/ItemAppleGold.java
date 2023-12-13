// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemAppleGold extends ItemFood
{
    private static final String __OBFID = "CL_00000037";
    
    public ItemAppleGold(final int p_i45341_1_, final float p_i45341_2_, final boolean p_i45341_3_) {
        super(p_i45341_1_, p_i45341_2_, p_i45341_3_);
        this.setHasSubtypes(true);
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return stack.getMetadata() > 0;
    }
    
    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return (stack.getMetadata() == 0) ? EnumRarity.RARE : EnumRarity.EPIC;
    }
    
    @Override
    protected void onFoodEaten(final ItemStack p_77849_1_, final World worldIn, final EntityPlayer p_77849_3_) {
        if (!worldIn.isRemote) {
            p_77849_3_.addPotionEffect(new PotionEffect(Potion.absorption.id, 2400, 0));
        }
        if (p_77849_1_.getMetadata() > 0) {
            if (!worldIn.isRemote) {
                p_77849_3_.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 4));
                p_77849_3_.addPotionEffect(new PotionEffect(Potion.resistance.id, 6000, 0));
                p_77849_3_.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 6000, 0));
            }
        }
        else {
            super.onFoodEaten(p_77849_1_, worldIn, p_77849_3_);
        }
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List subItems) {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 1));
    }
}
