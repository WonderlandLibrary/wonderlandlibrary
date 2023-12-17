// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemShears extends Item
{
    public ItemShears() {
        this.setMaxStackSize(1);
        this.setMaxDamage(238);
        this.setCreativeTab(CreativeTabs.TOOLS);
    }
    
    @Override
    public boolean onBlockDestroyed(final ItemStack stack, final World worldIn, final IBlockState state, final BlockPos pos, final EntityLivingBase entityLiving) {
        if (!worldIn.isRemote) {
            stack.damageItem(1, entityLiving);
        }
        final Block block = state.getBlock();
        return state.getMaterial() == Material.LEAVES || block == Blocks.WEB || block == Blocks.TALLGRASS || block == Blocks.VINE || block == Blocks.TRIPWIRE || block == Blocks.WOOL || super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }
    
    @Override
    public boolean canHarvestBlock(final IBlockState blockIn) {
        final Block block = blockIn.getBlock();
        return block == Blocks.WEB || block == Blocks.REDSTONE_WIRE || block == Blocks.TRIPWIRE;
    }
    
    @Override
    public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
        final Block block = state.getBlock();
        if (block != Blocks.WEB && state.getMaterial() != Material.LEAVES) {
            return (block == Blocks.WOOL) ? 5.0f : super.getDestroySpeed(stack, state);
        }
        return 15.0f;
    }
}
