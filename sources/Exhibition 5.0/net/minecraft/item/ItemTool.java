// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import java.util.Set;

public class ItemTool extends Item
{
    private Set effectiveBlocksTool;
    protected float efficiencyOnProperMaterial;
    private float damageVsEntity;
    protected ToolMaterial toolMaterial;
    private static final String __OBFID = "CL_00000019";
    
    protected ItemTool(final float p_i45333_1_, final ToolMaterial p_i45333_2_, final Set p_i45333_3_) {
        this.efficiencyOnProperMaterial = 4.0f;
        this.toolMaterial = p_i45333_2_;
        this.effectiveBlocksTool = p_i45333_3_;
        this.maxStackSize = 1;
        this.setMaxDamage(p_i45333_2_.getMaxUses());
        this.efficiencyOnProperMaterial = p_i45333_2_.getEfficiencyOnProperMaterial();
        this.damageVsEntity = p_i45333_1_ + p_i45333_2_.getDamageVsEntity();
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public float getStrVsBlock(final ItemStack stack, final Block p_150893_2_) {
        return this.effectiveBlocksTool.contains(p_150893_2_) ? this.efficiencyOnProperMaterial : 1.0f;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        stack.damageItem(2, attacker);
        return true;
    }
    
    @Override
    public boolean onBlockDestroyed(final ItemStack stack, final World worldIn, final Block blockIn, final BlockPos pos, final EntityLivingBase playerIn) {
        if (blockIn.getBlockHardness(worldIn, pos) != 0.0) {
            stack.damageItem(1, playerIn);
        }
        return true;
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    public ToolMaterial getToolMaterial() {
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
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.toolMaterial.getBaseItemForRepair() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public Multimap getItemAttributeModifiers() {
        final Multimap var1 = super.getItemAttributeModifiers();
        var1.put((Object)SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(Item.itemModifierUUID, "Tool modifier", this.damageVsEntity, 0));
        return var1;
    }
}
