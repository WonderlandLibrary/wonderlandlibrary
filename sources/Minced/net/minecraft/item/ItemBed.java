// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.NonNullList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.SoundCategory;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBed extends Item
{
    public ItemBed() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        final boolean flag = block.isReplaceable(worldIn, pos);
        if (!flag) {
            pos = pos.up();
        }
        final int i = MathHelper.floor(player.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3;
        final EnumFacing enumfacing = EnumFacing.byHorizontalIndex(i);
        final BlockPos blockpos = pos.offset(enumfacing);
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, itemstack) || !player.canPlayerEdit(blockpos, facing, itemstack)) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate2 = worldIn.getBlockState(blockpos);
        final boolean flag2 = iblockstate2.getBlock().isReplaceable(worldIn, blockpos);
        final boolean flag3 = flag || worldIn.isAirBlock(pos);
        final boolean flag4 = flag2 || worldIn.isAirBlock(blockpos);
        if (flag3 && flag4 && worldIn.getBlockState(pos.down()).isTopSolid() && worldIn.getBlockState(blockpos.down()).isTopSolid()) {
            final IBlockState iblockstate3 = Blocks.BED.getDefaultState().withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false).withProperty((IProperty<Comparable>)BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);
            worldIn.setBlockState(pos, iblockstate3, 10);
            worldIn.setBlockState(blockpos, iblockstate3.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 10);
            final SoundType soundtype = iblockstate3.getBlock().getSoundType();
            worldIn.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
            final TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof TileEntityBed) {
                ((TileEntityBed)tileentity).setItemValues(itemstack);
            }
            final TileEntity tileentity2 = worldIn.getTileEntity(pos);
            if (tileentity2 instanceof TileEntityBed) {
                ((TileEntityBed)tileentity2).setItemValues(itemstack);
            }
            worldIn.notifyNeighborsRespectDebug(pos, block, false);
            worldIn.notifyNeighborsRespectDebug(blockpos, iblockstate2.getBlock(), false);
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
            }
            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        return super.getTranslationKey() + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getTranslationKey();
    }
    
    @Override
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < 16; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}
