// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.EnumActionResult;
import net.minecraft.stats.StatList;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFishingRod extends Item
{
    public ItemFishingRod() {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter() {
            @Override
            public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0f;
                }
                final boolean flag = entityIn.getHeldItemMainhand() == stack;
                boolean flag2 = entityIn.getHeldItemOffhand() == stack;
                if (entityIn.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
                    flag2 = false;
                }
                return ((flag || flag2) && entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).fishEntity != null) ? 1.0f : 0.0f;
            }
        });
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (playerIn.fishEntity != null) {
            final int i = playerIn.fishEntity.handleHookRetraction();
            itemstack.damageItem(i, playerIn);
            playerIn.swingArm(handIn);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0f, 0.4f / (ItemFishingRod.itemRand.nextFloat() * 0.4f + 0.8f));
        }
        else {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (ItemFishingRod.itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                final EntityFishHook entityfishhook = new EntityFishHook(worldIn, playerIn);
                final int j = EnchantmentHelper.getFishingSpeedBonus(itemstack);
                if (j > 0) {
                    entityfishhook.setLureSpeed(j);
                }
                final int k = EnchantmentHelper.getFishingLuckBonus(itemstack);
                if (k > 0) {
                    entityfishhook.setLuck(k);
                }
                worldIn.spawnEntity(entityfishhook);
            }
            playerIn.swingArm(handIn);
            playerIn.addStat(StatList.getObjectUseStats(this));
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
