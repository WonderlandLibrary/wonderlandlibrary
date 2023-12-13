// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemRedstone extends Item
{
    private static final String __OBFID = "CL_00000058";
    
    public ItemRedstone() {
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final boolean var9 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        final BlockPos var10 = var9 ? pos : pos.offset(side);
        if (!playerIn.func_175151_a(var10, side, stack)) {
            return false;
        }
        final Block var11 = worldIn.getBlockState(var10).getBlock();
        if (!worldIn.canBlockBePlaced(var11, var10, false, side, null, stack)) {
            return false;
        }
        if (Blocks.redstone_wire.canPlaceBlockAt(worldIn, var10)) {
            --stack.stackSize;
            worldIn.setBlockState(var10, Blocks.redstone_wire.getDefaultState());
            return true;
        }
        return false;
    }
}
