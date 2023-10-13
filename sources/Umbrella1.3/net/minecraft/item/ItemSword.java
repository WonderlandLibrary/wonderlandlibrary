/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 */
package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemSword
extends Item {
    private float field_150934_a;
    private final Item.ToolMaterial repairMaterial;
    private static final String __OBFID = "CL_00000072";

    public ItemSword(Item.ToolMaterial p_i45356_1_) {
        this.repairMaterial = p_i45356_1_;
        this.maxStackSize = 1;
        this.setMaxDamage(p_i45356_1_.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.field_150934_a = 4.0f + p_i45356_1_.getDamageVsEntity();
    }

    public float func_150931_i() {
        return this.repairMaterial.getDamageVsEntity();
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block p_150893_2_) {
        if (p_150893_2_ == Blocks.web) {
            return 15.0f;
        }
        Material var3 = p_150893_2_.getMaterial();
        return var3 != Material.plants && var3 != Material.vine && var3 != Material.coral && var3 != Material.leaves && var3 != Material.gourd ? 1.0f : 1.5f;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0) {
            stack.damageItem(2, playerIn);
        }
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }

    @Override
    public boolean canHarvestBlock(Block blockIn) {
        return blockIn == Blocks.web;
    }

    @Override
    public int getItemEnchantability() {
        return this.repairMaterial.getEnchantability();
    }

    public String getToolMaterialName() {
        return this.repairMaterial.toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this.repairMaterial.getBaseItemForRepair() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap var1 = super.getItemAttributeModifiers();
        var1.put((Object)SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(itemModifierUUID, "Weapon modifier", this.field_150934_a, 0));
        return var1;
    }
}

