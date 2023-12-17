// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.SoundType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

public class ItemSlab extends ItemBlock
{
    private final BlockSlab singleSlab;
    private final BlockSlab doubleSlab;
    
    public ItemSlab(final Block block, final BlockSlab singleSlab, final BlockSlab doubleSlab) {
        super(block);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        return this.singleSlab.getTranslationKey(stack.getMetadata());
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!itemstack.isEmpty() && player.canPlayerEdit(pos.offset(facing), facing, itemstack)) {
            final Comparable<?> comparable = this.singleSlab.getTypeForItem(itemstack);
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if (iblockstate.getBlock() == this.singleSlab) {
                final IProperty<?> iproperty = this.singleSlab.getVariantProperty();
                final Comparable<?> comparable2 = iblockstate.getValue(iproperty);
                final BlockSlab.EnumBlockHalf blockslab$enumblockhalf = iblockstate.getValue(BlockSlab.HALF);
                if (((facing == EnumFacing.UP && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM) || (facing == EnumFacing.DOWN && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP)) && comparable2 == comparable) {
                    final IBlockState iblockstate2 = this.makeState(iproperty, comparable2);
                    final AxisAlignedBB axisalignedbb = iblockstate2.getCollisionBoundingBox(worldIn, pos);
                    if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate2, 11)) {
                        final SoundType soundtype = this.doubleSlab.getSoundType();
                        worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                        itemstack.shrink(1);
                        if (player instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
                        }
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
            return this.tryPlace(player, itemstack, worldIn, pos.offset(facing), comparable) ? EnumActionResult.SUCCESS : super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        return EnumActionResult.FAIL;
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos pos, final EnumFacing side, final EntityPlayer player, final ItemStack stack) {
        final BlockPos blockpos = pos;
        final IProperty<?> iproperty = this.singleSlab.getVariantProperty();
        final Comparable<?> comparable = this.singleSlab.getTypeForItem(stack);
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this.singleSlab) {
            final boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
            if (((side == EnumFacing.UP && !flag) || (side == EnumFacing.DOWN && flag)) && comparable == iblockstate.getValue(iproperty)) {
                return true;
            }
        }
        pos = pos.offset(side);
        final IBlockState iblockstate2 = worldIn.getBlockState(pos);
        return (iblockstate2.getBlock() == this.singleSlab && comparable == iblockstate2.getValue(iproperty)) || super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
    }
    
    private boolean tryPlace(final EntityPlayer player, final ItemStack stack, final World worldIn, final BlockPos pos, final Object itemSlabType) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this.singleSlab) {
            final Comparable<?> comparable = iblockstate.getValue(this.singleSlab.getVariantProperty());
            if (comparable == itemSlabType) {
                final IBlockState iblockstate2 = this.makeState(this.singleSlab.getVariantProperty(), comparable);
                final AxisAlignedBB axisalignedbb = iblockstate2.getCollisionBoundingBox(worldIn, pos);
                if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate2, 11)) {
                    final SoundType soundtype = this.doubleSlab.getSoundType();
                    worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                    stack.shrink(1);
                }
                return true;
            }
        }
        return false;
    }
    
    protected <T extends Comparable<T>> IBlockState makeState(final IProperty<T> p_185055_1_, final Comparable<?> p_185055_2_) {
        return this.doubleSlab.getDefaultState().withProperty(p_185055_1_, p_185055_2_);
    }
}
