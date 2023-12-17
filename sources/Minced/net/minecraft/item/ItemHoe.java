// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import com.google.common.collect.Multimap;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirt;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemHoe extends Item
{
    private final float speed;
    protected ToolMaterial toolMaterial;
    
    public ItemHoe(final ToolMaterial material) {
        this.toolMaterial = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.speed = material.getAttackDamage() + 1.0f;
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR) {
            if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                return EnumActionResult.SUCCESS;
            }
            if (block == Blocks.DIRT) {
                switch (iblockstate.getValue(BlockDirt.VARIANT)) {
                    case DIRT: {
                        this.setBlock(itemstack, player, worldIn, pos, Blocks.FARMLAND.getDefaultState());
                        return EnumActionResult.SUCCESS;
                    }
                    case COARSE_DIRT: {
                        this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
        return EnumActionResult.PASS;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
    
    protected void setBlock(final ItemStack stack, final EntityPlayer player, final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    public String getMaterialName() {
        return this.toolMaterial.toString();
    }
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(final EntityEquipmentSlot equipmentSlot) {
        final Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put((Object)SharedMonsterAttributes.ATTACK_DAMAGE.getName(), (Object)new AttributeModifier(ItemHoe.ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0, 0));
            multimap.put((Object)SharedMonsterAttributes.ATTACK_SPEED.getName(), (Object)new AttributeModifier(ItemHoe.ATTACK_SPEED_MODIFIER, "Weapon modifier", this.speed - 4.0f, 0));
        }
        return multimap;
    }
}
