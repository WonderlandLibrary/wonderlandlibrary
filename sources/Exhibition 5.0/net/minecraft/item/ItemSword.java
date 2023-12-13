// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSword extends Item
{
    private float field_150934_a;
    private final ToolMaterial repairMaterial;
    private static final String __OBFID = "CL_00000072";
    
    public ItemSword(final ToolMaterial p_i45356_1_) {
        this.repairMaterial = p_i45356_1_;
        this.maxStackSize = 1;
        this.setMaxDamage(p_i45356_1_.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.field_150934_a = 4.0f + p_i45356_1_.getDamageVsEntity();
    }
    
    public float getDamageGiven() {
        return this.repairMaterial.getDamageVsEntity();
    }
    
    @Override
    public float getStrVsBlock(final ItemStack stack, final Block p_150893_2_) {
        if (p_150893_2_ == Blocks.web) {
            return 15.0f;
        }
        final Material var3 = p_150893_2_.getMaterial();
        return (var3 != Material.plants && var3 != Material.vine && var3 != Material.coral && var3 != Material.leaves && var3 != Material.gourd) ? 1.0f : 1.5f;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
    
    @Override
    public boolean onBlockDestroyed(final ItemStack stack, final World worldIn, final Block blockIn, final BlockPos pos, final EntityLivingBase playerIn) {
        if (blockIn.getBlockHardness(worldIn, pos) != 0.0) {
            stack.damageItem(2, playerIn);
        }
        return true;
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.BLOCK;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 72000;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }
    
    @Override
    public boolean canHarvestBlock(final Block blockIn) {
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
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.repairMaterial.getBaseItemForRepair() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public Multimap getItemAttributeModifiers() {
        final Multimap var1 = super.getItemAttributeModifiers();
        var1.put((Object)SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(Item.itemModifierUUID, "Weapon modifier", this.field_150934_a, 0));
        return var1;
    }
}
