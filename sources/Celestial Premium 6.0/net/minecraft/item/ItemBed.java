/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemBed
extends Item {
    public ItemBed() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        if (playerIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (hand != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        Block block = iblockstate.getBlock();
        boolean flag = block.isReplaceable(playerIn, worldIn);
        if (!flag) {
            worldIn = worldIn.up();
        }
        int i = MathHelper.floor((double)(stack.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
        EnumFacing enumfacing = EnumFacing.byHorizontalIndex(i);
        BlockPos blockpos = worldIn.offset(enumfacing);
        ItemStack itemstack = stack.getHeldItem(pos);
        if (stack.canPlayerEdit(worldIn, hand, itemstack) && stack.canPlayerEdit(blockpos, hand, itemstack)) {
            boolean flag3;
            IBlockState iblockstate1 = playerIn.getBlockState(blockpos);
            boolean flag1 = iblockstate1.getBlock().isReplaceable(playerIn, blockpos);
            boolean flag2 = flag || playerIn.isAirBlock(worldIn);
            boolean bl = flag3 = flag1 || playerIn.isAirBlock(blockpos);
            if (flag2 && flag3 && playerIn.getBlockState(worldIn.down()).isFullyOpaque() && playerIn.getBlockState(blockpos.down()).isFullyOpaque()) {
                TileEntity tileentity1;
                IBlockState iblockstate2 = Blocks.BED.getDefaultState().withProperty(BlockBed.OCCUPIED, false).withProperty(BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
                playerIn.setBlockState(worldIn, iblockstate2, 10);
                playerIn.setBlockState(blockpos, iblockstate2.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
                SoundType soundtype = iblockstate2.getBlock().getSoundType();
                playerIn.playSound(null, worldIn, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                TileEntity tileentity = playerIn.getTileEntity(blockpos);
                if (tileentity instanceof TileEntityBed) {
                    ((TileEntityBed)tileentity).func_193051_a(itemstack);
                }
                if ((tileentity1 = playerIn.getTileEntity(worldIn)) instanceof TileEntityBed) {
                    ((TileEntityBed)tileentity1).func_193051_a(itemstack);
                }
                playerIn.notifyNeighborsRespectDebug(worldIn, block, false);
                playerIn.notifyNeighborsRespectDebug(blockpos, iblockstate1.getBlock(), false);
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

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
    }

    @Override
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            for (int i = 0; i < 16; ++i) {
                tab.add(new ItemStack(this, 1, i));
            }
        }
    }
}

