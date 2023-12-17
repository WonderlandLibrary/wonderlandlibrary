// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import com.google.common.collect.Multimap;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;
import java.util.Set;

public class ItemTool extends Item
{
    private final Set<Block> effectiveBlocks;
    protected float efficiency;
    protected float attackDamage;
    protected float attackSpeed;
    protected ToolMaterial toolMaterial;
    
    protected ItemTool(final float attackDamageIn, final float attackSpeedIn, final ToolMaterial materialIn, final Set<Block> effectiveBlocksIn) {
        this.efficiency = 4.0f;
        this.toolMaterial = materialIn;
        this.effectiveBlocks = effectiveBlocksIn;
        this.maxStackSize = 1;
        this.setMaxDamage(materialIn.getMaxUses());
        this.efficiency = materialIn.getEfficiency();
        this.attackDamage = attackDamageIn + materialIn.getAttackDamage();
        this.attackSpeed = attackSpeedIn;
        this.setCreativeTab(CreativeTabs.TOOLS);
    }
    
    protected ItemTool(final ToolMaterial materialIn, final Set<Block> effectiveBlocksIn) {
        this(0.0f, 0.0f, materialIn, effectiveBlocksIn);
    }
    
    @Override
    public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
        return this.effectiveBlocks.contains(state.getBlock()) ? this.efficiency : 1.0f;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        stack.damageItem(2, attacker);
        return true;
    }
    
    @Override
    public boolean onBlockDestroyed(final ItemStack stack, final World worldIn, final IBlockState state, final BlockPos pos, final EntityLivingBase entityLiving) {
        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0) {
            stack.damageItem(1, entityLiving);
        }
        return true;
    }
    
    @Override
    public boolean isFull3D() {
        return true;
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
        return this.toolMaterial.getRepairItem() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(final EntityEquipmentSlot equipmentSlot) {
        final Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put((Object)SharedMonsterAttributes.ATTACK_DAMAGE.getName(), (Object)new AttributeModifier(ItemTool.ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.attackDamage, 0));
            multimap.put((Object)SharedMonsterAttributes.ATTACK_SPEED.getName(), (Object)new AttributeModifier(ItemTool.ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0));
        }
        return multimap;
    }
}
