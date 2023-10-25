/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemBucket
extends Item {
    private final Block containedBlock;

    public ItemBucket(Block containedBlockIn) {
        this.maxStackSize = 1;
        this.containedBlock = containedBlockIn;
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
        BlockPos blockpos1;
        boolean flag = this.containedBlock == Blocks.AIR;
        ItemStack itemstack = worldIn.getHeldItem(playerIn);
        RayTraceResult raytraceresult = this.rayTrace(itemStackIn, worldIn, flag);
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        BlockPos blockpos = raytraceresult.getBlockPos();
        if (!itemStackIn.isBlockModifiable(worldIn, blockpos)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        if (flag) {
            if (!worldIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            IBlockState iblockstate = itemStackIn.getBlockState(blockpos);
            Material material = iblockstate.getMaterial();
            if (material == Material.WATER && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
                itemStackIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                worldIn.addStat(StatList.getObjectUseStats(this));
                worldIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, worldIn, Items.WATER_BUCKET));
            }
            if (material == Material.LAVA && iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
                worldIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0f, 1.0f);
                itemStackIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
                worldIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, worldIn, Items.LAVA_BUCKET));
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        boolean flag1 = itemStackIn.getBlockState(blockpos).getBlock().isReplaceable(itemStackIn, blockpos);
        BlockPos blockPos = blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);
        if (!worldIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemstack)) {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        if (this.tryPlaceContainedLiquid(worldIn, itemStackIn, blockpos1)) {
            if (worldIn instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)worldIn, blockpos1, itemstack);
            }
            worldIn.addStat(StatList.getObjectUseStats(this));
            return !worldIn.capabilities.isCreativeMode ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET)) : new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {
        if (player.capabilities.isCreativeMode) {
            return emptyBuckets;
        }
        emptyBuckets.func_190918_g(1);
        if (emptyBuckets.isEmpty()) {
            return new ItemStack(fullBucket);
        }
        if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
            player.dropItem(new ItemStack(fullBucket), false);
        }
        return emptyBuckets;
    }

    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World worldIn, BlockPos posIn) {
        if (this.containedBlock == Blocks.AIR) {
            return false;
        }
        IBlockState iblockstate = worldIn.getBlockState(posIn);
        Material material = iblockstate.getMaterial();
        boolean flag = !material.isSolid();
        boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);
        if (!(worldIn.isAirBlock(posIn) || flag || flag1)) {
            return false;
        }
        if (worldIn.provider.doesWaterVaporize() && this.containedBlock == Blocks.FLOWING_WATER) {
            int l = posIn.getX();
            int i = posIn.getY();
            int j = posIn.getZ();
            worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8f);
            for (int k = 0; k < 8; ++k) {
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0, 0.0, 0.0, new int[0]);
            }
        } else {
            if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid()) {
                worldIn.destroyBlock(posIn, true);
            }
            SoundEvent soundevent = this.containedBlock == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
            worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0f, 1.0f);
            worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
        }
        return true;
    }
}

