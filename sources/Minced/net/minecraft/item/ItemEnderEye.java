// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.ActionResult;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEnderEye extends Item
{
    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack) || iblockstate.getBlock() != Blocks.END_PORTAL_FRAME || iblockstate.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
            return EnumActionResult.FAIL;
        }
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        worldIn.setBlockState(pos, iblockstate.withProperty((IProperty<Comparable>)BlockEndPortalFrame.EYE, true), 2);
        worldIn.updateComparatorOutputLevel(pos, Blocks.END_PORTAL_FRAME);
        itemstack.shrink(1);
        for (int i = 0; i < 16; ++i) {
            final double d0 = pos.getX() + (5.0f + ItemEnderEye.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double d2 = pos.getY() + 0.8125f;
            final double d3 = pos.getZ() + (5.0f + ItemEnderEye.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double d4 = 0.0;
            final double d5 = 0.0;
            final double d6 = 0.0;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
        worldIn.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
        final BlockPattern.PatternHelper blockpattern$patternhelper = BlockEndPortalFrame.getOrCreatePortalShape().match(worldIn, pos);
        if (blockpattern$patternhelper != null) {
            final BlockPos blockpos = blockpattern$patternhelper.getFrontTopLeft().add(-3, 0, -3);
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    worldIn.setBlockState(blockpos.add(j, 0, k), Blocks.END_PORTAL.getDefaultState(), 2);
                }
            }
            worldIn.playBroadcastSound(1038, blockpos.add(1, 0, 1), 0);
        }
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, false);
        if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        playerIn.setActiveHand(handIn);
        if (!worldIn.isRemote) {
            final BlockPos blockpos = ((WorldServer)worldIn).getChunkProvider().getNearestStructurePos(worldIn, "Stronghold", new BlockPos(playerIn), false);
            if (blockpos != null) {
                final EntityEnderEye entityendereye = new EntityEnderEye(worldIn, playerIn.posX, playerIn.posY + playerIn.height / 2.0f, playerIn.posZ);
                entityendereye.moveTowards(blockpos);
                worldIn.spawnEntity(entityendereye);
                if (playerIn instanceof EntityPlayerMP) {
                    CriteriaTriggers.USED_ENDER_EYE.trigger((EntityPlayerMP)playerIn, blockpos);
                }
                worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 0.5f, 0.4f / (ItemEnderEye.itemRand.nextFloat() * 0.4f + 0.8f));
                worldIn.playEvent(null, 1003, new BlockPos(playerIn), 0);
                if (!playerIn.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
                playerIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
