/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemBed
extends Item {
    private static final String __OBFID = "CL_00001771";

    public ItemBed() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        boolean var17;
        if (worldIn.isRemote) {
            return true;
        }
        if (side != EnumFacing.UP) {
            return false;
        }
        IBlockState var9 = worldIn.getBlockState(pos);
        Block var10 = var9.getBlock();
        boolean var11 = var10.isReplaceable(worldIn, pos);
        if (!var11) {
            pos = pos.offsetUp();
        }
        int var12 = MathHelper.floor_double((double)(playerIn.rotationYaw * 4.0f / 360.0f) + 0.5) & 3;
        EnumFacing var13 = EnumFacing.getHorizontal(var12);
        BlockPos var14 = pos.offset(var13);
        boolean var15 = var10.isReplaceable(worldIn, var14);
        boolean var16 = worldIn.isAirBlock(pos) || var11;
        boolean bl = var17 = worldIn.isAirBlock(var14) || var15;
        if (playerIn.func_175151_a(pos, side, stack) && playerIn.func_175151_a(var14, side, stack)) {
            if (var16 && var17 && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown()) && World.doesBlockHaveSolidTopSurface(worldIn, var14.offsetDown())) {
                int var18 = var13.getHorizontalIndex();
                IBlockState var19 = Blocks.bed.getDefaultState().withProperty(BlockBed.OCCUPIED_PROP, Boolean.valueOf(false)).withProperty(BlockBed.AGE, (Comparable)((Object)var13)).withProperty(BlockBed.PART_PROP, (Comparable)((Object)BlockBed.EnumPartType.FOOT));
                if (worldIn.setBlockState(pos, var19, 3)) {
                    IBlockState var20 = var19.withProperty(BlockBed.PART_PROP, (Comparable)((Object)BlockBed.EnumPartType.HEAD));
                    worldIn.setBlockState(var14, var20, 3);
                }
                --stack.stackSize;
                return true;
            }
            return false;
        }
        return false;
    }
}

