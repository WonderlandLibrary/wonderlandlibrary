// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.init.Blocks;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.Block;

public class ItemBucket extends Item
{
    private final Block containedBlock;
    
    public ItemBucket(final Block containedBlockIn) {
        this.maxStackSize = 1;
        this.containedBlock = containedBlockIn;
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final boolean flag = this.containedBlock == Blocks.AIR;
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        final BlockPos blockpos = raytraceresult.getBlockPos();
        if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        if (flag) {
            if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            final Material material = iblockstate.getMaterial();
            if (material == Material.WATER && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                playerIn.addStat(StatList.getObjectUseStats(this));
                playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, playerIn, Items.WATER_BUCKET));
            }
            if (material == Material.LAVA && iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0f, 1.0f);
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                playerIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, playerIn, Items.LAVA_BUCKET));
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        else {
            final boolean flag2 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
            final BlockPos blockpos2 = (flag2 && raytraceresult.sideHit == EnumFacing.UP) ? blockpos : blockpos.offset(raytraceresult.sideHit);
            if (!playerIn.canPlayerEdit(blockpos2, raytraceresult.sideHit, itemstack)) {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos2)) {
                if (playerIn instanceof EntityPlayerMP) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos2, itemstack);
                }
                playerIn.addStat(StatList.getObjectUseStats(this));
                return playerIn.capabilities.isCreativeMode ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack) : new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET));
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
    }
    
    private ItemStack fillBucket(final ItemStack emptyBuckets, final EntityPlayer player, final Item fullBucket) {
        if (player.capabilities.isCreativeMode) {
            return emptyBuckets;
        }
        emptyBuckets.shrink(1);
        if (emptyBuckets.isEmpty()) {
            return new ItemStack(fullBucket);
        }
        if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
            player.dropItem(new ItemStack(fullBucket), false);
        }
        return emptyBuckets;
    }
    
    public boolean tryPlaceContainedLiquid(@Nullable final EntityPlayer player, final World worldIn, final BlockPos posIn) {
        if (this.containedBlock == Blocks.AIR) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(posIn);
        final Material material = iblockstate.getMaterial();
        final boolean flag = !material.isSolid();
        final boolean flag2 = iblockstate.getBlock().isReplaceable(worldIn, posIn);
        if (!worldIn.isAirBlock(posIn) && !flag && !flag2) {
            return false;
        }
        if (worldIn.provider.doesWaterVaporize() && this.containedBlock == Blocks.FLOWING_WATER) {
            final int l = posIn.getX();
            final int i = posIn.getY();
            final int j = posIn.getZ();
            worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
            for (int k = 0; k < 8; ++k) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, l + Math.random(), i + Math.random(), j + Math.random(), 0.0, 0.0, 0.0, new int[0]);
            }
        }
        else {
            if (!worldIn.isRemote && (flag || flag2) && !material.isLiquid()) {
                worldIn.destroyBlock(posIn, true);
            }
            final SoundEvent soundevent = (this.containedBlock == Blocks.FLOWING_LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
            worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0f, 1.0f);
            worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
        }
        return true;
    }
}
