// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.block.Block;

public class ItemLilyPad extends ItemColored
{
    public ItemLilyPad(final Block block) {
        super(block, false);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = raytraceresult.getBlockPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            final BlockPos blockpos2 = blockpos.up();
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (iblockstate.getMaterial() == Material.WATER && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0 && worldIn.isAirBlock(blockpos2)) {
                worldIn.setBlockState(blockpos2, Blocks.WATERLILY.getDefaultState(), 11);
                if (playerIn instanceof EntityPlayerMP) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos2, itemstack);
                }
                if (!playerIn.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                playerIn.addStat(StatList.getObjectUseStats(this));
                worldIn.playSound(playerIn, blockpos, SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
}
