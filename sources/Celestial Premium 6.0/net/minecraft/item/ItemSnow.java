/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSnow
extends ItemBlock {
    public ItemSnow(Block block) {
        super(block);
        this.setMaxDamage(0);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        ItemStack itemstack = stack.getHeldItem(pos);
        if (!itemstack.isEmpty() && stack.canPlayerEdit(worldIn, hand, itemstack)) {
            IBlockState iblockstate1;
            AxisAlignedBB axisalignedbb;
            int i;
            IBlockState iblockstate = playerIn.getBlockState(worldIn);
            Block block = iblockstate.getBlock();
            BlockPos blockpos = worldIn;
            if (!(hand == EnumFacing.UP && block == this.block || block.isReplaceable(playerIn, worldIn))) {
                blockpos = worldIn.offset(hand);
                iblockstate = playerIn.getBlockState(blockpos);
                block = iblockstate.getBlock();
            }
            if (block == this.block && (i = iblockstate.getValue(BlockSnow.LAYERS).intValue()) < 8 && (axisalignedbb = (iblockstate1 = iblockstate.withProperty(BlockSnow.LAYERS, i + 1)).getCollisionBoundingBox(playerIn, blockpos)) != Block.NULL_AABB && playerIn.checkNoEntityCollision(axisalignedbb.offset(blockpos)) && playerIn.setBlockState(blockpos, iblockstate1, 10)) {
                SoundType soundtype = this.block.getSoundType();
                playerIn.playSound(stack, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0f) / 2.0f, soundtype.getPitch() * 0.8f);
                if (stack instanceof EntityPlayerMP) {
                    CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                }
                itemstack.func_190918_g(1);
                return EnumActionResult.SUCCESS;
            }
            return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY);
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}

