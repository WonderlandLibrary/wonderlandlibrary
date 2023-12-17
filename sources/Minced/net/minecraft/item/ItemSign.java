// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSign extends Item
{
    public ItemSign() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final boolean flag = iblockstate.getBlock().isReplaceable(worldIn, pos);
        if (facing == EnumFacing.DOWN || (!iblockstate.getMaterial().isSolid() && !flag) || (flag && facing != EnumFacing.UP)) {
            return EnumActionResult.FAIL;
        }
        pos = pos.offset(facing);
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, itemstack) || !Blocks.STANDING_SIGN.canPlaceBlockAt(worldIn, pos)) {
            return EnumActionResult.FAIL;
        }
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        pos = (flag ? pos.down() : pos);
        if (facing == EnumFacing.UP) {
            final int i = MathHelper.floor((player.rotationYaw + 180.0f) * 16.0f / 360.0f + 0.5) & 0xF;
            worldIn.setBlockState(pos, Blocks.STANDING_SIGN.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, i), 11);
        }
        else {
            worldIn.setBlockState(pos, Blocks.WALL_SIGN.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, facing), 11);
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(worldIn, player, pos, itemstack)) {
            player.openEditSign((TileEntitySign)tileentity);
        }
        if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
        }
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
