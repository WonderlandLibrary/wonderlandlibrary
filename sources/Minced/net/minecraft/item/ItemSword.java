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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSword extends Item
{
    private final float attackDamage;
    private final ToolMaterial material;
    
    public ItemSword(final ToolMaterial material) {
        this.material = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.attackDamage = 3.0f + material.getAttackDamage();
    }
    
    public float getAttackDamage() {
        return this.material.getAttackDamage();
    }
    
    @Override
    public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
        final Block block = state.getBlock();
        if (block == Blocks.WEB) {
            return 15.0f;
        }
        final Material material = state.getMaterial();
        return (material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD) ? 1.0f : 1.5f;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
    
    @Override
    public boolean onBlockDestroyed(final ItemStack stack, final World worldIn, final IBlockState state, final BlockPos pos, final EntityLivingBase entityLiving) {
        if (state.getBlockHardness(worldIn, pos) != 0.0) {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public boolean canHarvestBlock(final IBlockState blockIn) {
        return blockIn.getBlock() == Blocks.WEB;
    }
    
    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }
    
    public String getToolMaterialName() {
        return this.material.toString();
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.material.getRepairItem() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(final EntityEquipmentSlot equipmentSlot) {
        final Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put((Object)SharedMonsterAttributes.ATTACK_DAMAGE.getName(), (Object)new AttributeModifier(ItemSword.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.attackDamage, 0));
            multimap.put((Object)SharedMonsterAttributes.ATTACK_SPEED.getName(), (Object)new AttributeModifier(ItemSword.ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316, 0));
        }
        return multimap;
    }
}
