/*
 * Decompiled with CFR 0.152.
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
    private float attackDamage;
    private final Item.ToolMaterial material;

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStack2) {
        return this.material.getRepairItem() == itemStack2.getItem() ? true : super.getIsRepairable(itemStack, itemStack2);
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        return block == Blocks.web;
    }

    public float getDamageVsEntity() {
        return this.material.getDamageVsEntity();
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public float getStrVsBlock(ItemStack itemStack, Block block) {
        if (block == Blocks.web) {
            return 15.0f;
        }
        Material material = block.getMaterial();
        return material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd ? 1.0f : 1.5f;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BLOCK;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        itemStack.damageItem(1, entityLivingBase2);
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
        multimap.put((Object)SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(itemModifierUUID, "Weapon modifier", this.attackDamage, 0));
        return multimap;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 72000;
    }

    public ItemSword(Item.ToolMaterial toolMaterial) {
        this.material = toolMaterial;
        this.maxStackSize = 1;
        this.setMaxDamage(toolMaterial.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.attackDamage = 4.0f + toolMaterial.getDamageVsEntity();
    }

    public String getToolMaterialName() {
        return this.material.toString();
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, BlockPos blockPos, EntityLivingBase entityLivingBase) {
        if ((double)block.getBlockHardness(world, blockPos) != 0.0) {
            itemStack.damageItem(2, entityLivingBase);
        }
        return true;
    }
}

