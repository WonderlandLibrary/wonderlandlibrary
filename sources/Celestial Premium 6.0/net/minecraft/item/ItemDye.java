/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ItemDye
extends Item {
    public static final int[] DYE_COLORS = new int[]{0x1E1B1B, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 0xABABAB, 0x434343, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 0xF0F0F0};

    public ItemDye() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        ItemStack itemstack = stack.getHeldItem(pos);
        if (!stack.canPlayerEdit(worldIn.offset(hand), hand, itemstack)) {
            return EnumActionResult.FAIL;
        }
        EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
        if (enumdyecolor == EnumDyeColor.WHITE) {
            if (ItemDye.applyBonemeal(itemstack, playerIn, worldIn)) {
                if (!playerIn.isRemote) {
                    playerIn.playEvent(2005, worldIn, 0);
                }
                return EnumActionResult.SUCCESS;
            }
        } else if (enumdyecolor == EnumDyeColor.BROWN) {
            IBlockState iblockstate = playerIn.getBlockState(worldIn);
            Block block = iblockstate.getBlock();
            if (block == Blocks.LOG && iblockstate.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                if (hand == EnumFacing.DOWN || hand == EnumFacing.UP) {
                    return EnumActionResult.FAIL;
                }
                if (playerIn.isAirBlock(worldIn = worldIn.offset(hand))) {
                    IBlockState iblockstate1 = Blocks.COCOA.getStateForPlacement(playerIn, worldIn, hand, facing, hitX, hitY, 0, stack);
                    playerIn.setBlockState(worldIn, iblockstate1, 10);
                    if (!stack.capabilities.isCreativeMode) {
                        itemstack.func_190918_g(1);
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
            return EnumActionResult.FAIL;
        }
        return EnumActionResult.PASS;
    }

    public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target) {
        IGrowable igrowable;
        IBlockState iblockstate = worldIn.getBlockState(target);
        if (iblockstate.getBlock() instanceof IGrowable && (igrowable = (IGrowable)((Object)iblockstate.getBlock())).canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
            if (!worldIn.isRemote) {
                if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                    igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                }
                stack.func_190918_g(1);
            }
            return true;
        }
        return false;
    }

    public static void spawnBonemealParticles(World worldIn, BlockPos pos, int amount) {
        IBlockState iblockstate;
        if (amount == 0) {
            amount = 15;
        }
        if ((iblockstate = worldIn.getBlockState(pos)).getMaterial() != Material.AIR) {
            for (int i = 0; i < amount; ++i) {
                double d0 = itemRand.nextGaussian() * 0.02;
                double d1 = itemRand.nextGaussian() * 0.02;
                double d2 = itemRand.nextGaussian() * 0.02;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (float)pos.getX() + itemRand.nextFloat(), (double)pos.getY() + (double)itemRand.nextFloat() * iblockstate.getBoundingBox((IBlockAccess)worldIn, (BlockPos)pos).maxY, (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
            }
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (target instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep)target;
            EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor) {
                entitysheep.setFleeceColor(enumdyecolor);
                stack.func_190918_g(1);
            }
            return true;
        }
        return false;
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

