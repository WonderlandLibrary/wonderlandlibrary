// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;

public class ItemDoor extends Item
{
    private final Block block;
    
    public ItemDoor(final Block block) {
        this.block = block;
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos, facing, itemstack) && this.block.canPlaceBlockAt(worldIn, pos)) {
            final EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
            final int i = enumfacing.getXOffset();
            final int j = enumfacing.getZOffset();
            final boolean flag = (i < 0 && hitZ < 0.5f) || (i > 0 && hitZ > 0.5f) || (j < 0 && hitX > 0.5f) || (j > 0 && hitX < 0.5f);
            placeDoor(worldIn, pos, enumfacing, this.block, flag);
            final SoundType soundtype = this.block.getSoundType();
            worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
    
    public static void placeDoor(final World worldIn, final BlockPos pos, final EnumFacing facing, final Block door, boolean isRightHinge) {
        final BlockPos blockpos = pos.offset(facing.rotateY());
        final BlockPos blockpos2 = pos.offset(facing.rotateYCCW());
        final int i = (worldIn.getBlockState(blockpos2).isNormalCube() + worldIn.getBlockState(blockpos2.up()).isNormalCube()) ? 1 : 0;
        final int j = (worldIn.getBlockState(blockpos).isNormalCube() + worldIn.getBlockState(blockpos.up()).isNormalCube()) ? 1 : 0;
        final boolean flag = worldIn.getBlockState(blockpos2).getBlock() == door || worldIn.getBlockState(blockpos2.up()).getBlock() == door;
        final boolean flag2 = worldIn.getBlockState(blockpos).getBlock() == door || worldIn.getBlockState(blockpos.up()).getBlock() == door;
        if ((!flag || flag2) && j <= i) {
            if ((flag2 && !flag) || j < i) {
                isRightHinge = false;
            }
        }
        else {
            isRightHinge = true;
        }
        final BlockPos blockpos3 = pos.up();
        final boolean flag3 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos3);
        final IBlockState iblockstate = door.getDefaultState().withProperty((IProperty<Comparable>)BlockDoor.FACING, facing).withProperty(BlockDoor.HINGE, isRightHinge ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT).withProperty((IProperty<Comparable>)BlockDoor.POWERED, flag3).withProperty((IProperty<Comparable>)BlockDoor.OPEN, flag3);
        worldIn.setBlockState(pos, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
        worldIn.setBlockState(blockpos3, iblockstate.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
        worldIn.notifyNeighborsOfStateChange(pos, door, false);
        worldIn.notifyNeighborsOfStateChange(blockpos3, door, false);
    }
}
