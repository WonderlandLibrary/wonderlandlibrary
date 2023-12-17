// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSnow;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemBlockSpecial extends Item
{
    private final Block block;
    
    public ItemBlockSpecial(final Block block) {
        this.block = block;
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (block == Blocks.SNOW_LAYER && iblockstate.getValue((IProperty<Integer>)BlockSnow.LAYERS) < 1) {
            facing = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.isEmpty() || !player.canPlayerEdit(pos, facing, itemstack) || !worldIn.mayPlace(this.block, pos, false, facing, null)) {
            return EnumActionResult.FAIL;
        }
        IBlockState iblockstate2 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 0, player);
        if (!worldIn.setBlockState(pos, iblockstate2, 11)) {
            return EnumActionResult.FAIL;
        }
        iblockstate2 = worldIn.getBlockState(pos);
        if (iblockstate2.getBlock() == this.block) {
            ItemBlock.setTileEntityNBT(worldIn, player, pos, itemstack);
            iblockstate2.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate2, player, itemstack);
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
            }
        }
        final SoundType soundtype = this.block.getSoundType();
        worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
