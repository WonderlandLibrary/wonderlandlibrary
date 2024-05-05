package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemEgg
extends Item {
    public ItemEgg() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            --itemStackIn.stackSize;
        }
        worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
        if (!worldIn.isRemote) {
            worldIn.spawnEntityInWorld((Entity)new EntityEgg(worldIn, (EntityLivingBase)playerIn));
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem((Item)this)]);
        return itemStackIn;
    }
}
