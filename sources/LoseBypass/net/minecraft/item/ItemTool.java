/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import com.google.common.collect.Multimap;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemTool
extends Item {
    private Set<Block> effectiveBlocks;
    protected float efficiencyOnProperMaterial = 4.0f;
    private float damageVsEntity;
    protected Item.ToolMaterial toolMaterial;

    protected ItemTool(float attackDamage, Item.ToolMaterial material, Set<Block> effectiveBlocks) {
        this.toolMaterial = material;
        this.effectiveBlocks = effectiveBlocks;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
        this.damageVsEntity = attackDamage + material.getDamageVsEntity();
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        if (!this.effectiveBlocks.contains(block)) return 1.0f;
        float f = this.efficiencyOnProperMaterial;
        return f;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(2, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
        if ((double)blockIn.getBlockHardness(worldIn, pos) == 0.0) return true;
        stack.damageItem(1, playerIn);
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    public Item.ToolMaterial getToolMaterial() {
        return this.toolMaterial;
    }

    @Override
    public int getItemEnchantability() {
        return this.toolMaterial.getEnchantability();
    }

    public String getToolMaterialName() {
        return this.toolMaterial.toString();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (this.toolMaterial.getRepairItem() == repair.getItem()) {
            return true;
        }
        boolean bl = super.getIsRepairable(toRepair, repair);
        return bl;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", this.damageVsEntity, 0));
        return multimap;
    }
}

