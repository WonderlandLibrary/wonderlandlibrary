// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.EnumActionResult;
import net.minecraft.stats.StatList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.potion.PotionUtils;

public class ItemLingeringPotion extends ItemPotion
{
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("lingering_potion.effect."));
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 0.25f);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final ItemStack itemstack2 = playerIn.capabilities.isCreativeMode ? itemstack.copy() : itemstack.splitStack(1);
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_LINGERINGPOTION_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (ItemLingeringPotion.itemRand.nextFloat() * 0.4f + 0.8f));
        if (!worldIn.isRemote) {
            final EntityPotion entitypotion = new EntityPotion(worldIn, playerIn, itemstack2);
            entitypotion.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0f, 0.5f, 1.0f);
            worldIn.spawnEntity(entitypotion);
        }
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
