// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class ItemChorusFruit extends ItemFood
{
    public ItemChorusFruit(final int amount, final float saturation) {
        super(amount, saturation, false);
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving) {
        final ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);
        if (!worldIn.isRemote) {
            final double d0 = entityLiving.posX;
            final double d2 = entityLiving.posY;
            final double d3 = entityLiving.posZ;
            for (int i = 0; i < 16; ++i) {
                final double d4 = entityLiving.posX + (entityLiving.getRNG().nextDouble() - 0.5) * 16.0;
                final double d5 = MathHelper.clamp(entityLiving.posY + (entityLiving.getRNG().nextInt(16) - 8), 0.0, worldIn.getActualHeight() - 1);
                final double d6 = entityLiving.posZ + (entityLiving.getRNG().nextDouble() - 0.5) * 16.0;
                if (entityLiving.isRiding()) {
                    entityLiving.dismountRidingEntity();
                }
                if (entityLiving.attemptTeleport(d4, d5, d6)) {
                    worldIn.playSound(null, d0, d2, d3, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    entityLiving.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0f, 1.0f);
                    break;
                }
            }
            if (entityLiving instanceof EntityPlayer) {
                ((EntityPlayer)entityLiving).getCooldownTracker().setCooldown(this, 20);
            }
        }
        return itemstack;
    }
}
