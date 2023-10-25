/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemSign
extends Item {
    public ItemSign() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        boolean flag = iblockstate.getBlock().isReplaceable(playerIn, worldIn);
        if (!(hand == EnumFacing.DOWN || !iblockstate.getMaterial().isSolid() && !flag || flag && hand != EnumFacing.UP)) {
            ItemStack itemstack;
            if (stack.canPlayerEdit(worldIn = worldIn.offset(hand), hand, itemstack = stack.getHeldItem(pos)) && Blocks.STANDING_SIGN.canPlaceBlockAt(playerIn, worldIn)) {
                if (playerIn.isRemote) {
                    return EnumActionResult.SUCCESS;
                }
                BlockPos blockPos = worldIn = flag ? worldIn.down() : worldIn;
                if (hand == EnumFacing.UP) {
                    int i = MathHelper.floor((double)((stack.rotationYaw + 180.0f) * 16.0f / 360.0f) + 0.5) & 0xF;
                    playerIn.setBlockState(worldIn, Blocks.STANDING_SIGN.getDefaultState().withProperty(BlockStandingSign.ROTATION, i), 11);
                } else {
                    playerIn.setBlockState(worldIn, Blocks.WALL_SIGN.getDefaultState().withProperty(BlockWallSign.FACING, hand), 11);
                }
                TileEntity tileentity = playerIn.getTileEntity(worldIn);
                if (tileentity instanceof TileEntitySign && !ItemBlock.setTileEntityNBT(playerIn, stack, worldIn, itemstack)) {
                    stack.openEditSign((TileEntitySign)tileentity);
                }
                if (stack instanceof EntityPlayerMP) {
                    CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                }
                itemstack.func_190918_g(1);
                return EnumActionResult.SUCCESS;
            }
            return EnumActionResult.FAIL;
        }
        return EnumActionResult.FAIL;
    }
}

