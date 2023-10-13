/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemDye
extends Item {
    public static final int[] dyeColors = new int[]{0x1E1B1B, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 0xABABAB, 0x434343, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 0xF0F0F0};
    private static final String __OBFID = "CL_00000022";

    public ItemDye() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int var2 = stack.getMetadata();
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.func_176766_a(var2).func_176762_d();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState var10;
        Block var11;
        if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
            return false;
        }
        EnumDyeColor var9 = EnumDyeColor.func_176766_a(stack.getMetadata());
        if (var9 == EnumDyeColor.WHITE) {
            if (ItemDye.func_179234_a(stack, worldIn, pos)) {
                if (!worldIn.isRemote) {
                    worldIn.playAuxSFX(2005, pos, 0);
                }
                return true;
            }
        } else if (var9 == EnumDyeColor.BROWN && (var11 = (var10 = worldIn.getBlockState(pos)).getBlock()) == Blocks.log && var10.getValue(BlockPlanks.VARIANT_PROP) == BlockPlanks.EnumType.JUNGLE) {
            if (side == EnumFacing.DOWN) {
                return false;
            }
            if (side == EnumFacing.UP) {
                return false;
            }
            if (worldIn.isAirBlock(pos = pos.offset(side))) {
                IBlockState var12 = Blocks.cocoa.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
                worldIn.setBlockState(pos, var12, 2);
                if (!playerIn.capabilities.isCreativeMode) {
                    --stack.stackSize;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean func_179234_a(ItemStack p_179234_0_, World worldIn, BlockPos p_179234_2_) {
        IGrowable var4;
        IBlockState var3 = worldIn.getBlockState(p_179234_2_);
        if (var3.getBlock() instanceof IGrowable && (var4 = (IGrowable)((Object)var3.getBlock())).isStillGrowing(worldIn, p_179234_2_, var3, worldIn.isRemote)) {
            if (!worldIn.isRemote) {
                if (var4.canUseBonemeal(worldIn, worldIn.rand, p_179234_2_, var3)) {
                    var4.grow(worldIn, worldIn.rand, p_179234_2_, var3);
                }
                --p_179234_0_.stackSize;
            }
            return true;
        }
        return false;
    }

    public static void func_180617_a(World worldIn, BlockPos p_180617_1_, int p_180617_2_) {
        Block var3;
        if (p_180617_2_ == 0) {
            p_180617_2_ = 15;
        }
        if ((var3 = worldIn.getBlockState(p_180617_1_).getBlock()).getMaterial() != Material.air) {
            var3.setBlockBoundsBasedOnState(worldIn, p_180617_1_);
            for (int var4 = 0; var4 < p_180617_2_; ++var4) {
                double var5 = itemRand.nextGaussian() * 0.02;
                double var7 = itemRand.nextGaussian() * 0.02;
                double var9 = itemRand.nextGaussian() * 0.02;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (float)p_180617_1_.getX() + itemRand.nextFloat(), (double)p_180617_1_.getY() + (double)itemRand.nextFloat() * var3.getBlockBoundsMaxY(), (double)((float)p_180617_1_.getZ() + itemRand.nextFloat()), var5, var7, var9, new int[0]);
            }
        }
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
        if (target instanceof EntitySheep) {
            EntitySheep var4 = (EntitySheep)target;
            EnumDyeColor var5 = EnumDyeColor.func_176766_a(stack.getMetadata());
            if (!var4.getSheared() && var4.func_175509_cj() != var5) {
                var4.func_175512_b(var5);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        for (int var4 = 0; var4 < 16; ++var4) {
            subItems.add(new ItemStack(itemIn, 1, var4));
        }
    }
}

