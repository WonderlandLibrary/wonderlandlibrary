// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.EnumParticleTypes;
import java.util.Random;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.init.Blocks;
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

public class BlockTorch extends Block
{
    public static final PropertyDirection FACING_PROP;
    private static final String __OBFID = "CL_00000325";
    
    protected BlockTorch() {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTorch.FACING_PROP, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    private boolean func_176594_d(final World worldIn, final BlockPos p_176594_2_) {
        if (World.doesBlockHaveSolidTopSurface(worldIn, p_176594_2_)) {
            return true;
        }
        final Block var3 = worldIn.getBlockState(p_176594_2_).getBlock();
        return var3 instanceof BlockFence || var3 == Blocks.glass || var3 == Blocks.cobblestone_wall || var3 == Blocks.stained_glass;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        for (final EnumFacing var4 : BlockTorch.FACING_PROP.getAllowedValues()) {
            if (this.func_176595_b(worldIn, pos, var4)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean func_176595_b(final World worldIn, final BlockPos p_176595_2_, final EnumFacing p_176595_3_) {
        final BlockPos var4 = p_176595_2_.offset(p_176595_3_.getOpposite());
        final boolean var5 = p_176595_3_.getAxis().isHorizontal();
        return (var5 && worldIn.func_175677_d(var4, true)) || (p_176595_3_.equals(EnumFacing.UP) && this.func_176594_d(worldIn, var4));
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        if (this.func_176595_b(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(BlockTorch.FACING_PROP, facing);
        }
        for (final EnumFacing var10 : EnumFacing.Plane.HORIZONTAL) {
            if (worldIn.func_175677_d(pos.offset(var10.getOpposite()), true)) {
                return this.getDefaultState().withProperty(BlockTorch.FACING_PROP, var10);
            }
        }
        return this.getDefaultState();
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.func_176593_f(worldIn, pos, state);
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        this.func_176592_e(worldIn, pos, state);
    }
    
    protected boolean func_176592_e(final World worldIn, final BlockPos p_176592_2_, final IBlockState p_176592_3_) {
        if (!this.func_176593_f(worldIn, p_176592_2_, p_176592_3_)) {
            return true;
        }
        final EnumFacing var4 = (EnumFacing)p_176592_3_.getValue(BlockTorch.FACING_PROP);
        final EnumFacing.Axis var5 = var4.getAxis();
        final EnumFacing var6 = var4.getOpposite();
        boolean var7 = false;
        if (var5.isHorizontal() && !worldIn.func_175677_d(p_176592_2_.offset(var6), true)) {
            var7 = true;
        }
        else if (var5.isVertical() && !this.func_176594_d(worldIn, p_176592_2_.offset(var6))) {
            var7 = true;
        }
        if (var7) {
            this.dropBlockAsItem(worldIn, p_176592_2_, p_176592_3_, 0);
            worldIn.setBlockToAir(p_176592_2_);
            return true;
        }
        return false;
    }
    
    protected boolean func_176593_f(final World worldIn, final BlockPos p_176593_2_, final IBlockState p_176593_3_) {
        if (p_176593_3_.getBlock() == this && this.func_176595_b(worldIn, p_176593_2_, (EnumFacing)p_176593_3_.getValue(BlockTorch.FACING_PROP))) {
            return true;
        }
        if (worldIn.getBlockState(p_176593_2_).getBlock() == this) {
            this.dropBlockAsItem(worldIn, p_176593_2_, p_176593_3_, 0);
            worldIn.setBlockToAir(p_176593_2_);
        }
        return false;
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        final EnumFacing var5 = (EnumFacing)worldIn.getBlockState(pos).getValue(BlockTorch.FACING_PROP);
        float var6 = 0.15f;
        if (var5 == EnumFacing.EAST) {
            this.setBlockBounds(0.0f, 0.2f, 0.5f - var6, var6 * 2.0f, 0.8f, 0.5f + var6);
        }
        else if (var5 == EnumFacing.WEST) {
            this.setBlockBounds(1.0f - var6 * 2.0f, 0.2f, 0.5f - var6, 1.0f, 0.8f, 0.5f + var6);
        }
        else if (var5 == EnumFacing.SOUTH) {
            this.setBlockBounds(0.5f - var6, 0.2f, 0.0f, 0.5f + var6, 0.8f, var6 * 2.0f);
        }
        else if (var5 == EnumFacing.NORTH) {
            this.setBlockBounds(0.5f - var6, 0.2f, 1.0f - var6 * 2.0f, 0.5f + var6, 0.8f, 1.0f);
        }
        else {
            var6 = 0.1f;
            this.setBlockBounds(0.5f - var6, 0.0f, 0.5f - var6, 0.5f + var6, 0.6f, 0.5f + var6);
        }
        return super.collisionRayTrace(worldIn, pos, start, end);
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final EnumFacing var5 = (EnumFacing)state.getValue(BlockTorch.FACING_PROP);
        final double var6 = pos.getX() + 0.5;
        final double var7 = pos.getY() + 0.7;
        final double var8 = pos.getZ() + 0.5;
        final double var9 = 0.22;
        final double var10 = 0.27;
        if (var5.getAxis().isHorizontal()) {
            final EnumFacing var11 = var5.getOpposite();
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6 + var10 * var11.getFrontOffsetX(), var7 + var9, var8 + var10 * var11.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, var6 + var10 * var11.getFrontOffsetX(), var7 + var9, var8 + var10 * var11.getFrontOffsetZ(), 0.0, 0.0, 0.0, new int[0]);
        }
        else {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, var6, var7, var8, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState var2 = this.getDefaultState();
        switch (meta) {
            case 1: {
                var2 = var2.withProperty(BlockTorch.FACING_PROP, EnumFacing.EAST);
                break;
            }
            case 2: {
                var2 = var2.withProperty(BlockTorch.FACING_PROP, EnumFacing.WEST);
                break;
            }
            case 3: {
                var2 = var2.withProperty(BlockTorch.FACING_PROP, EnumFacing.SOUTH);
                break;
            }
            case 4: {
                var2 = var2.withProperty(BlockTorch.FACING_PROP, EnumFacing.NORTH);
                break;
            }
            default: {
                var2 = var2.withProperty(BlockTorch.FACING_PROP, EnumFacing.UP);
                break;
            }
        }
        return var2;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        final byte var2 = 0;
        int var3 = 0;
        switch (SwitchEnumFacing.field_176609_a[((EnumFacing)state.getValue(BlockTorch.FACING_PROP)).ordinal()]) {
            case 1: {
                var3 = (var2 | 0x1);
                break;
            }
            case 2: {
                var3 = (var2 | 0x2);
                break;
            }
            case 3: {
                var3 = (var2 | 0x3);
                break;
            }
            case 4: {
                var3 = (var2 | 0x4);
                break;
            }
            default: {
                var3 = (var2 | 0x5);
                break;
            }
        }
        return var3;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockTorch.FACING_PROP });
    }
    
    static {
        FACING_PROP = PropertyDirection.create("facing", (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00002054";
            
            public boolean func_176601_a(final EnumFacing p_176601_1_) {
                return p_176601_1_ != EnumFacing.DOWN;
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_176601_a((EnumFacing)p_apply_1_);
            }
        });
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_176609_a;
        private static final String __OBFID = "CL_00002053";
        
        static {
            field_176609_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.SOUTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.NORTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.DOWN.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumFacing.field_176609_a[EnumFacing.UP.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
