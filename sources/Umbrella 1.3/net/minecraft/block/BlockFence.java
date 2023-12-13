/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemLead;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFence
extends Block {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    private static final String __OBFID = "CL_00000242";

    public BlockFence(Material p_i45721_1_) {
        super(p_i45721_1_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        boolean var7 = this.func_176524_e(worldIn, pos.offsetNorth());
        boolean var8 = this.func_176524_e(worldIn, pos.offsetSouth());
        boolean var9 = this.func_176524_e(worldIn, pos.offsetWest());
        boolean var10 = this.func_176524_e(worldIn, pos.offsetEast());
        float var11 = 0.375f;
        float var12 = 0.625f;
        float var13 = 0.375f;
        float var14 = 0.625f;
        if (var7) {
            var13 = 0.0f;
        }
        if (var8) {
            var14 = 1.0f;
        }
        if (var7 || var8) {
            this.setBlockBounds(var11, 0.0f, var13, var12, 1.5f, var14);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        var13 = 0.375f;
        var14 = 0.625f;
        if (var9) {
            var11 = 0.0f;
        }
        if (var10) {
            var12 = 1.0f;
        }
        if (var9 || var10 || !var7 && !var8) {
            this.setBlockBounds(var11, 0.0f, var13, var12, 1.5f, var14);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        if (var7) {
            var13 = 0.0f;
        }
        if (var8) {
            var14 = 1.0f;
        }
        this.setBlockBounds(var11, 0.0f, var13, var12, 1.0f, var14);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        boolean var3 = this.func_176524_e(access, pos.offsetNorth());
        boolean var4 = this.func_176524_e(access, pos.offsetSouth());
        boolean var5 = this.func_176524_e(access, pos.offsetWest());
        boolean var6 = this.func_176524_e(access, pos.offsetEast());
        float var7 = 0.375f;
        float var8 = 0.625f;
        float var9 = 0.375f;
        float var10 = 0.625f;
        if (var3) {
            var9 = 0.0f;
        }
        if (var4) {
            var10 = 1.0f;
        }
        if (var5) {
            var7 = 0.0f;
        }
        if (var6) {
            var8 = 1.0f;
        }
        this.setBlockBounds(var7, 0.0f, var9, var8, 1.0f, var10);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isPassable(IBlockAccess blockAccess, BlockPos pos) {
        return false;
    }

    public boolean func_176524_e(IBlockAccess p_176524_1_, BlockPos p_176524_2_) {
        Block var3 = p_176524_1_.getBlockState(p_176524_2_).getBlock();
        return var3 == Blocks.barrier ? false : (!(var3 instanceof BlockFence && var3.blockMaterial == this.blockMaterial || var3 instanceof BlockFenceGate) ? (var3.blockMaterial.isOpaque() && var3.isFullCube() ? var3.blockMaterial != Material.gourd : false) : true);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return worldIn.isRemote ? true : ItemLead.func_180618_a(playerIn, worldIn, pos);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH, Boolean.valueOf(this.func_176524_e(worldIn, pos.offsetNorth()))).withProperty(EAST, Boolean.valueOf(this.func_176524_e(worldIn, pos.offsetEast()))).withProperty(SOUTH, Boolean.valueOf(this.func_176524_e(worldIn, pos.offsetSouth()))).withProperty(WEST, Boolean.valueOf(this.func_176524_e(worldIn, pos.offsetWest())));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, NORTH, EAST, WEST, SOUTH);
    }
}

