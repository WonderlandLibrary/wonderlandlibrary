/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLadder
extends Block {
    public static final PropertyDirection field_176382_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final String __OBFID = "CL_00000262";

    protected BlockLadder() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176382_a, (Comparable)((Object)EnumFacing.NORTH)));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        IBlockState var3 = access.getBlockState(pos);
        if (var3.getBlock() == this) {
            float var4 = 0.125f;
            switch (SwitchEnumFacing.field_180190_a[((EnumFacing)((Object)var3.getValue(field_176382_a))).ordinal()]) {
                case 1: {
                    this.setBlockBounds(0.0f, 0.0f, 1.0f - var4, 1.0f, 1.0f, 1.0f);
                    break;
                }
                case 2: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var4);
                    break;
                }
                case 3: {
                    this.setBlockBounds(1.0f - var4, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
                    break;
                }
                default: {
                    this.setBlockBounds(0.0f, 0.0f, 0.0f, var4, 1.0f, 1.0f);
                }
            }
        }
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
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.offsetWest()).getBlock().isNormalCube() ? true : (worldIn.getBlockState(pos.offsetEast()).getBlock().isNormalCube() ? true : (worldIn.getBlockState(pos.offsetNorth()).getBlock().isNormalCube() ? true : worldIn.getBlockState(pos.offsetSouth()).getBlock().isNormalCube()));
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing var10;
        if (facing.getAxis().isHorizontal() && this.func_176381_b(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(field_176382_a, (Comparable)((Object)facing));
        }
        Iterator var9 = EnumFacing.Plane.HORIZONTAL.iterator();
        do {
            if (var9.hasNext()) continue;
            return this.getDefaultState();
        } while (!this.func_176381_b(worldIn, pos, var10 = (EnumFacing)var9.next()));
        return this.getDefaultState().withProperty(field_176382_a, (Comparable)((Object)var10));
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5 = (EnumFacing)((Object)state.getValue(field_176382_a));
        if (!this.func_176381_b(worldIn, pos, var5)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    protected boolean func_176381_b(World worldIn, BlockPos p_176381_2_, EnumFacing p_176381_3_) {
        return worldIn.getBlockState(p_176381_2_.offset(p_176381_3_.getOpposite())).getBlock().isNormalCube();
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(field_176382_a, (Comparable)((Object)var2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)((Object)state.getValue(field_176382_a))).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176382_a);
    }

    static final class SwitchEnumFacing {
        static final int[] field_180190_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002104";

        static {
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

