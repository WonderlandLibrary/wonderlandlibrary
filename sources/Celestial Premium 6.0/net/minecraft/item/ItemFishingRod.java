/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemFishingRod
extends Item {
    public ItemFishingRod() {
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.addPropertyOverride(new ResourceLocation("cast"), new IItemPropertyGetter(){

            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                boolean flag1;
                if (entityIn == null) {
                    return 0.0f;
                }
                boolean flag = entityIn.getHeldItemMainhand() == stack;
                boolean bl = flag1 = entityIn.getHeldItemOffhand() == stack;
                if (entityIn.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
                    flag1 = false;
                }
                return (flag || flag1) && entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).fishEntity != null ? 1.0f : 0.0f;
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
    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        if (worldIn.fishEntity != null) {
            int i = worldIn.fishEntity.handleHookRetraction();
            itemstack.damageItem(i, worldIn);
            worldIn.swingArm(playerIn);
            itemStackIn.playSound(null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.field_193780_J, SoundCategory.NEUTRAL, 1.0f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
        } else {
            itemStackIn.playSound(null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            if (!itemStackIn.isRemote) {
                int k;
                EntityFishHook entityfishhook = new EntityFishHook(itemStackIn, worldIn);
                int j = EnchantmentHelper.func_191528_c(itemstack);
                if (j > 0) {
                    entityfishhook.func_191516_a(j);
                }
                if ((k = EnchantmentHelper.func_191529_b(itemstack)) > 0) {
                    entityfishhook.func_191517_b(k);
                }
                itemStackIn.spawnEntityInWorld(entityfishhook);
            }
            worldIn.swingArm(playerIn);
            worldIn.addStat(StatList.getObjectUseStats(this));
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}

