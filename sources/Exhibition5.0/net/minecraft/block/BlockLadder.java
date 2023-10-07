// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;

public class BlockLadder extends Block
{
    public static final PropertyDirection field_176382_a;
    private static final String __OBFID = "CL_00000262";
    
    protected BlockLadder() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLadder.field_176382_a, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final IBlockState var3 = access.getBlockState(pos);
        if (var3.getBlock() == this) {
            final float var4 = 0.125f;
            switch (SwitchEnumFacing.field_180190_a[((EnumFacing)var3.getValue(BlockLadder.field_176382_a)).ordinal()]) {
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
                    break;
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
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.offsetWest()).getBlock().isNormalCube() || worldIn.getBlockState(pos.offsetEast()).getBlock().isNormalCube() || worldIn.getBlockState(pos.offsetNorth()).getBlock().isNormalCube() || worldIn.getBlockState(pos.offsetSouth()).getBlock().isNormalCube();
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (facing.getAxis().isHorizontal() && this.func_176381_b(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(BlockLadder.field_176382_a, facing);
        }
        for (final EnumFacing var10 : EnumFacing.Plane.HORIZONTAL) {
            if (this.func_176381_b(worldIn, pos, var10)) {
                return this.getDefaultState().withProperty(BlockLadder.field_176382_a, var10);
            }
        }
        return this.getDefaultState();
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing var5 = (EnumFacing)state.getValue(BlockLadder.field_176382_a);
        if (!this.func_176381_b(worldIn, pos, var5)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }
    
    protected boolean func_176381_b(final World worldIn, final BlockPos p_176381_2_, final EnumFacing p_176381_3_) {
        return worldIn.getBlockState(p_176381_2_.offset(p_176381_3_.getOpposite())).getBlock().isNormalCube();
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(BlockLadder.field_176382_a, var2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumFacing)state.getValue(BlockLadder.field_176382_a)).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockLadder.field_176382_a });
    }
    
    static {
        field_176382_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_180190_a;
        private static final String __OBFID = "CL_00002104";
        
        static {
            field_180190_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_180190_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
