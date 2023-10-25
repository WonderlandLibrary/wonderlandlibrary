/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCarrotOnAStick
extends Item {
    public ItemCarrotOnAStick() {
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
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
        if (itemStackIn.isRemote) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (worldIn.isRiding() && worldIn.getRidingEntity() instanceof EntityPig) {
            EntityPig entitypig = (EntityPig)worldIn.getRidingEntity();
            if (itemstack.getMaxDamage() - itemstack.getMetadata() >= 7 && entitypig.boost()) {
                itemstack.damageItem(7, worldIn);
                if (itemstack.isEmpty()) {
                    ItemStack itemstack1 = new ItemStack(Items.FISHING_ROD);
                    itemstack1.setTagCompound(itemstack.getTagCompound());
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack1);
                }
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }
        worldIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    }
}

