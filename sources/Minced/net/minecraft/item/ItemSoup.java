// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class ItemSoup extends ItemFood
{
    public ItemSoup(final int healAmount) {
        super(healAmount, false);
        this.setMaxStackSize(1);
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(Items.BOWL);
    }
}
