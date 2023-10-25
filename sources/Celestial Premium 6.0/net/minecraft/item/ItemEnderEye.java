/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemEnderEye
extends Item {
    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        ItemStack itemstack = stack.getHeldItem(pos);
        if (stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack) && iblockstate.getBlock() == Blocks.END_PORTAL_FRAME && !iblockstate.getValue(BlockEndPortalFrame.EYE).booleanValue()) {
            if (playerIn.isRemote) {
                return EnumActionResult.SUCCESS;
            }
            playerIn.setBlockState(worldIn, iblockstate.withProperty(BlockEndPortalFrame.EYE, true), 2);
            playerIn.updateComparatorOutputLevel(worldIn, Blocks.END_PORTAL_FRAME);
            itemstack.func_190918_g(1);
            for (int i = 0; i < 16; ++i) {
                double d0 = (float)worldIn.getX() + (5.0f + itemRand.nextFloat() * 6.0f) / 16.0f;
                double d1 = (float)worldIn.getY() + 0.8125f;
                double d2 = (float)worldIn.getZ() + (5.0f + itemRand.nextFloat() * 6.0f) / 16.0f;
                double d3 = 0.0;
                double d4 = 0.0;
                double d5 = 0.0;
                playerIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
            }
            playerIn.playSound(null, worldIn, SoundEvents.field_193781_bp, SoundCategory.BLOCKS, 1.0f, 1.0f);
            BlockPattern.PatternHelper blockpattern$patternhelper = BlockEndPortalFrame.getOrCreatePortalShape().match(playerIn, worldIn);
            if (blockpattern$patternhelper != null) {
                BlockPos blockpos = blockpattern$patternhelper.getFrontTopLeft().add(-3, 0, -3);
                for (int j = 0; j < 3; ++j) {
                    for (int k = 0; k < 3; ++k) {
                        playerIn.setBlockState(blockpos.add(j, 0, k), Blocks.END_PORTAL.getDefaultState(), 2);
                    }
                }
                playerIn.playBroadcastSound(1038, blockpos.add(1, 0, 1), 0);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
        BlockPos blockpos;
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        RayTraceResult raytraceresult = this.rayTrace(itemStackIn, worldIn, false);
        if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && itemStackIn.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.END_PORTAL_FRAME) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        worldIn.setActiveHand(playerIn);
        if (!itemStackIn.isRemote && (blockpos = ((WorldServer)itemStackIn).getChunkProvider().getStrongholdGen(itemStackIn, "Stronghold", new BlockPos(worldIn), false)) != null) {
            EntityEnderEye entityendereye = new EntityEnderEye(itemStackIn, worldIn.posX, worldIn.posY + (double)(worldIn.height / 2.0f), worldIn.posZ);
            entityendereye.moveTowards(blockpos);
            itemStackIn.spawnEntityInWorld(entityendereye);
            if (worldIn instanceof EntityPlayerMP) {
                CriteriaTriggers.field_192132_l.func_192239_a((EntityPlayerMP)worldIn, blockpos);
            }
            itemStackIn.playSound(null, worldIn.posX, worldIn.posY, worldIn.posZ, SoundEvents.ENTITY_ENDEREYE_LAUNCH, SoundCategory.NEUTRAL, 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            itemStackIn.playEvent(null, 1003, new BlockPos(worldIn), 0);
            if (!worldIn.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            worldIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}

