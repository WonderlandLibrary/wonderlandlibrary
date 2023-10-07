// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import java.util.Arrays;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.material.MapColor;
import net.minecraft.world.Explosion;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Random;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyDirection;

public class BlockStairs extends Block
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum HALF;
    public static final PropertyEnum SHAPE;
    private static final int[][] field_150150_a;
    private final Block modelBlock;
    private final IBlockState modelState;
    private boolean field_150152_N;
    private int field_150153_O;
    private static final String __OBFID = "CL_00000314";
    
    protected BlockStairs(final IBlockState modelState) {
        super(modelState.getBlock().blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, EnumHalf.BOTTOM).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT));
        this.modelBlock = modelState.getBlock();
        this.modelState = modelState;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0f);
        this.setStepSound(this.modelBlock.stepSound);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        if (this.field_150152_N) {
            this.setBlockBounds(0.5f * (this.field_150153_O % 2), 0.5f * (this.field_150153_O / 4 % 2), 0.5f * (this.field_150153_O / 2 % 2), 0.5f + 0.5f * (this.field_150153_O % 2), 0.5f + 0.5f * (this.field_150153_O / 4 % 2), 0.5f + 0.5f * (this.field_150153_O / 2 % 2));
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
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
    
    public void setBaseCollisionBounds(final IBlockAccess worldIn, final BlockPos pos) {
        if (worldIn.getBlockState(pos).getValue(BlockStairs.HALF) == EnumHalf.TOP) {
            this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
    }
    
    public static boolean isBlockStairs(final Block p_150148_0_) {
        return p_150148_0_ instanceof BlockStairs;
    }
    
    public static boolean isSameStair(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state) {
        final IBlockState var3 = worldIn.getBlockState(pos);
        final Block var4 = var3.getBlock();
        return isBlockStairs(var4) && var3.getValue(BlockStairs.HALF) == state.getValue(BlockStairs.HALF) && var3.getValue(BlockStairs.FACING) == state.getValue(BlockStairs.FACING);
    }
    
    public int func_176307_f(final IBlockAccess p_176307_1_, final BlockPos p_176307_2_) {
        final IBlockState var3 = p_176307_1_.getBlockState(p_176307_2_);
        final EnumFacing var4 = (EnumFacing)var3.getValue(BlockStairs.FACING);
        final EnumHalf var5 = (EnumHalf)var3.getValue(BlockStairs.HALF);
        final boolean var6 = var5 == EnumHalf.TOP;
        if (var4 == EnumFacing.EAST) {
            final IBlockState var7 = p_176307_1_.getBlockState(p_176307_2_.offsetEast());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.NORTH && !isSameStair(p_176307_1_, p_176307_2_.offsetSouth(), var3)) {
                    return var6 ? 1 : 2;
                }
                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176307_1_, p_176307_2_.offsetNorth(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        }
        else if (var4 == EnumFacing.WEST) {
            final IBlockState var7 = p_176307_1_.getBlockState(p_176307_2_.offsetWest());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.NORTH && !isSameStair(p_176307_1_, p_176307_2_.offsetSouth(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176307_1_, p_176307_2_.offsetNorth(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        }
        else if (var4 == EnumFacing.SOUTH) {
            final IBlockState var7 = p_176307_1_.getBlockState(p_176307_2_.offsetSouth());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.WEST && !isSameStair(p_176307_1_, p_176307_2_.offsetEast(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.EAST && !isSameStair(p_176307_1_, p_176307_2_.offsetWest(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        }
        else if (var4 == EnumFacing.NORTH) {
            final IBlockState var7 = p_176307_1_.getBlockState(p_176307_2_.offsetNorth());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.WEST && !isSameStair(p_176307_1_, p_176307_2_.offsetEast(), var3)) {
                    return var6 ? 1 : 2;
                }
                if (var9 == EnumFacing.EAST && !isSameStair(p_176307_1_, p_176307_2_.offsetWest(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        }
        return 0;
    }
    
    public int func_176305_g(final IBlockAccess p_176305_1_, final BlockPos p_176305_2_) {
        final IBlockState var3 = p_176305_1_.getBlockState(p_176305_2_);
        final EnumFacing var4 = (EnumFacing)var3.getValue(BlockStairs.FACING);
        final EnumHalf var5 = (EnumHalf)var3.getValue(BlockStairs.HALF);
        final boolean var6 = var5 == EnumHalf.TOP;
        if (var4 == EnumFacing.EAST) {
            final IBlockState var7 = p_176305_1_.getBlockState(p_176305_2_.offsetWest());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.NORTH && !isSameStair(p_176305_1_, p_176305_2_.offsetNorth(), var3)) {
                    return var6 ? 1 : 2;
                }
                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176305_1_, p_176305_2_.offsetSouth(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        }
        else if (var4 == EnumFacing.WEST) {
            final IBlockState var7 = p_176305_1_.getBlockState(p_176305_2_.offsetEast());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.NORTH && !isSameStair(p_176305_1_, p_176305_2_.offsetNorth(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.SOUTH && !isSameStair(p_176305_1_, p_176305_2_.offsetSouth(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        }
        else if (var4 == EnumFacing.SOUTH) {
            final IBlockState var7 = p_176305_1_.getBlockState(p_176305_2_.offsetNorth());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.WEST && !isSameStair(p_176305_1_, p_176305_2_.offsetWest(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.EAST && !isSameStair(p_176305_1_, p_176305_2_.offsetEast(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        }
        else if (var4 == EnumFacing.NORTH) {
            final IBlockState var7 = p_176305_1_.getBlockState(p_176305_2_.offsetSouth());
            final Block var8 = var7.getBlock();
            if (isBlockStairs(var8) && var5 == var7.getValue(BlockStairs.HALF)) {
                final EnumFacing var9 = (EnumFacing)var7.getValue(BlockStairs.FACING);
                if (var9 == EnumFacing.WEST && !isSameStair(p_176305_1_, p_176305_2_.offsetWest(), var3)) {
                    return var6 ? 1 : 2;
                }
                if (var9 == EnumFacing.EAST && !isSameStair(p_176305_1_, p_176305_2_.offsetEast(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        }
        return 0;
    }
    
    public boolean func_176306_h(final IBlockAccess p_176306_1_, final BlockPos p_176306_2_) {
        final IBlockState var3 = p_176306_1_.getBlockState(p_176306_2_);
        final EnumFacing var4 = (EnumFacing)var3.getValue(BlockStairs.FACING);
        final EnumHalf var5 = (EnumHalf)var3.getValue(BlockStairs.HALF);
        final boolean var6 = var5 == EnumHalf.TOP;
        float var7 = 0.5f;
        float var8 = 1.0f;
        if (var6) {
            var7 = 0.0f;
            var8 = 0.5f;
        }
        float var9 = 0.0f;
        float var10 = 1.0f;
        float var11 = 0.0f;
        float var12 = 0.5f;
        boolean var13 = true;
        if (var4 == EnumFacing.EAST) {
            var9 = 0.5f;
            var12 = 1.0f;
            final IBlockState var14 = p_176306_1_.getBlockState(p_176306_2_.offsetEast());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.NORTH && !isSameStair(p_176306_1_, p_176306_2_.offsetSouth(), var3)) {
                    var12 = 0.5f;
                    var13 = false;
                }
                else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176306_1_, p_176306_2_.offsetNorth(), var3)) {
                    var11 = 0.5f;
                    var13 = false;
                }
            }
        }
        else if (var4 == EnumFacing.WEST) {
            var10 = 0.5f;
            var12 = 1.0f;
            final IBlockState var14 = p_176306_1_.getBlockState(p_176306_2_.offsetWest());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.NORTH && !isSameStair(p_176306_1_, p_176306_2_.offsetSouth(), var3)) {
                    var12 = 0.5f;
                    var13 = false;
                }
                else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176306_1_, p_176306_2_.offsetNorth(), var3)) {
                    var11 = 0.5f;
                    var13 = false;
                }
            }
        }
        else if (var4 == EnumFacing.SOUTH) {
            var11 = 0.5f;
            var12 = 1.0f;
            final IBlockState var14 = p_176306_1_.getBlockState(p_176306_2_.offsetSouth());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.WEST && !isSameStair(p_176306_1_, p_176306_2_.offsetEast(), var3)) {
                    var10 = 0.5f;
                    var13 = false;
                }
                else if (var16 == EnumFacing.EAST && !isSameStair(p_176306_1_, p_176306_2_.offsetWest(), var3)) {
                    var9 = 0.5f;
                    var13 = false;
                }
            }
        }
        else if (var4 == EnumFacing.NORTH) {
            final IBlockState var14 = p_176306_1_.getBlockState(p_176306_2_.offsetNorth());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.WEST && !isSameStair(p_176306_1_, p_176306_2_.offsetEast(), var3)) {
                    var10 = 0.5f;
                    var13 = false;
                }
                else if (var16 == EnumFacing.EAST && !isSameStair(p_176306_1_, p_176306_2_.offsetWest(), var3)) {
                    var9 = 0.5f;
                    var13 = false;
                }
            }
        }
        this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        return var13;
    }
    
    public boolean func_176304_i(final IBlockAccess p_176304_1_, final BlockPos p_176304_2_) {
        final IBlockState var3 = p_176304_1_.getBlockState(p_176304_2_);
        final EnumFacing var4 = (EnumFacing)var3.getValue(BlockStairs.FACING);
        final EnumHalf var5 = (EnumHalf)var3.getValue(BlockStairs.HALF);
        final boolean var6 = var5 == EnumHalf.TOP;
        float var7 = 0.5f;
        float var8 = 1.0f;
        if (var6) {
            var7 = 0.0f;
            var8 = 0.5f;
        }
        float var9 = 0.0f;
        float var10 = 0.5f;
        float var11 = 0.5f;
        float var12 = 1.0f;
        boolean var13 = false;
        if (var4 == EnumFacing.EAST) {
            final IBlockState var14 = p_176304_1_.getBlockState(p_176304_2_.offsetWest());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.NORTH && !isSameStair(p_176304_1_, p_176304_2_.offsetNorth(), var3)) {
                    var11 = 0.0f;
                    var12 = 0.5f;
                    var13 = true;
                }
                else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176304_1_, p_176304_2_.offsetSouth(), var3)) {
                    var11 = 0.5f;
                    var12 = 1.0f;
                    var13 = true;
                }
            }
        }
        else if (var4 == EnumFacing.WEST) {
            final IBlockState var14 = p_176304_1_.getBlockState(p_176304_2_.offsetEast());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                var9 = 0.5f;
                var10 = 1.0f;
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.NORTH && !isSameStair(p_176304_1_, p_176304_2_.offsetNorth(), var3)) {
                    var11 = 0.0f;
                    var12 = 0.5f;
                    var13 = true;
                }
                else if (var16 == EnumFacing.SOUTH && !isSameStair(p_176304_1_, p_176304_2_.offsetSouth(), var3)) {
                    var11 = 0.5f;
                    var12 = 1.0f;
                    var13 = true;
                }
            }
        }
        else if (var4 == EnumFacing.SOUTH) {
            final IBlockState var14 = p_176304_1_.getBlockState(p_176304_2_.offsetNorth());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                var11 = 0.0f;
                var12 = 0.5f;
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.WEST && !isSameStair(p_176304_1_, p_176304_2_.offsetWest(), var3)) {
                    var13 = true;
                }
                else if (var16 == EnumFacing.EAST && !isSameStair(p_176304_1_, p_176304_2_.offsetEast(), var3)) {
                    var9 = 0.5f;
                    var10 = 1.0f;
                    var13 = true;
                }
            }
        }
        else if (var4 == EnumFacing.NORTH) {
            final IBlockState var14 = p_176304_1_.getBlockState(p_176304_2_.offsetSouth());
            final Block var15 = var14.getBlock();
            if (isBlockStairs(var15) && var5 == var14.getValue(BlockStairs.HALF)) {
                final EnumFacing var16 = (EnumFacing)var14.getValue(BlockStairs.FACING);
                if (var16 == EnumFacing.WEST && !isSameStair(p_176304_1_, p_176304_2_.offsetWest(), var3)) {
                    var13 = true;
                }
                else if (var16 == EnumFacing.EAST && !isSameStair(p_176304_1_, p_176304_2_.offsetEast(), var3)) {
                    var9 = 0.5f;
                    var10 = 1.0f;
                    var13 = true;
                }
            }
        }
        if (var13) {
            this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        }
        return var13;
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        this.setBaseCollisionBounds(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        final boolean var7 = this.func_176306_h(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        if (var7 && this.func_176304_i(worldIn, pos)) {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.modelBlock.randomDisplayTick(worldIn, pos, state, rand);
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
    }
    
    @Override
    public void onBlockDestroyedByPlayer(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
    }
    
    @Override
    public int getMixedBrightnessForBlock(final IBlockAccess worldIn, final BlockPos pos) {
        return this.modelBlock.getMixedBrightnessForBlock(worldIn, pos);
    }
    
    @Override
    public float getExplosionResistance(final Entity exploder) {
        return this.modelBlock.getExplosionResistance(exploder);
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.modelBlock.getBlockLayer();
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return this.modelBlock.tickRate(worldIn);
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
        return this.modelBlock.getSelectedBoundingBox(worldIn, pos);
    }
    
    @Override
    public Vec3 modifyAcceleration(final World worldIn, final BlockPos pos, final Entity entityIn, final Vec3 motion) {
        return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
    }
    
    @Override
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }
    
    @Override
    public boolean canCollideCheck(final IBlockState state, final boolean p_176209_2_) {
        return this.modelBlock.canCollideCheck(state, p_176209_2_);
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return this.modelBlock.canPlaceBlockAt(worldIn, pos);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.onNeighborBlockChange(worldIn, pos, this.modelState, Blocks.air);
        this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        this.modelBlock.breakBlock(worldIn, pos, this.modelState);
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final Entity entityIn) {
        this.modelBlock.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.modelBlock.updateTick(worldIn, pos, state, rand);
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public void onBlockDestroyedByExplosion(final World worldIn, final BlockPos pos, final Explosion explosionIn) {
        this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return this.modelBlock.getMapColor(this.modelState);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        IBlockState var9 = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        var9 = var9.withProperty(BlockStairs.FACING, placer.func_174811_aO()).withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT);
        return (facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5)) ? var9.withProperty(BlockStairs.HALF, EnumHalf.BOTTOM) : var9.withProperty(BlockStairs.HALF, EnumHalf.TOP);
    }
    
    @Override
    public MovingObjectPosition collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3 start, final Vec3 end) {
        final MovingObjectPosition[] var5 = new MovingObjectPosition[8];
        final IBlockState var6 = worldIn.getBlockState(pos);
        final int var7 = ((EnumFacing)var6.getValue(BlockStairs.FACING)).getHorizontalIndex();
        final boolean var8 = var6.getValue(BlockStairs.HALF) == EnumHalf.TOP;
        final int[] var9 = BlockStairs.field_150150_a[var7 + (var8 ? 4 : 0)];
        this.field_150152_N = true;
        for (int var10 = 0; var10 < 8; ++var10) {
            this.field_150153_O = var10;
            if (Arrays.binarySearch(var9, var10) < 0) {
                var5[var10] = super.collisionRayTrace(worldIn, pos, start, end);
            }
        }
        final int[] var11 = var9;
        for (int var12 = var9.length, var13 = 0; var13 < var12; ++var13) {
            final int var14 = var11[var13];
            var5[var14] = null;
        }
        MovingObjectPosition var15 = null;
        double var16 = 0.0;
        final MovingObjectPosition[] var17 = var5;
        for (int var18 = var5.length, var19 = 0; var19 < var18; ++var19) {
            final MovingObjectPosition var20 = var17[var19];
            if (var20 != null) {
                final double var21 = var20.hitVec.squareDistanceTo(end);
                if (var21 > var16) {
                    var15 = var20;
                    var16 = var21;
                }
            }
        }
        return var15;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(BlockStairs.HALF, ((meta & 0x4) > 0) ? EnumHalf.TOP : EnumHalf.BOTTOM);
        var2 = var2.withProperty(BlockStairs.FACING, EnumFacing.getFront(5 - (meta & 0x3)));
        return var2;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int var2 = 0;
        if (state.getValue(BlockStairs.HALF) == EnumHalf.TOP) {
            var2 |= 0x4;
        }
        var2 |= 5 - ((EnumFacing)state.getValue(BlockStairs.FACING)).getIndex();
        return var2;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (this.func_176306_h(worldIn, pos)) {
            switch (this.func_176305_g(worldIn, pos)) {
                case 0: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT);
                    break;
                }
                case 1: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.INNER_RIGHT);
                    break;
                }
                case 2: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.INNER_LEFT);
                    break;
                }
            }
        }
        else {
            switch (this.func_176307_f(worldIn, pos)) {
                case 0: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.STRAIGHT);
                    break;
                }
                case 1: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.OUTER_RIGHT);
                    break;
                }
                case 2: {
                    state = state.withProperty(BlockStairs.SHAPE, EnumShape.OUTER_LEFT);
                    break;
                }
            }
        }
        return state;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockStairs.FACING, BlockStairs.HALF, BlockStairs.SHAPE });
    }
    
    static {
        FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
        HALF = PropertyEnum.create("half", EnumHalf.class);
        SHAPE = PropertyEnum.create("shape", EnumShape.class);
        field_150150_a = new int[][] { { 4, 5 }, { 5, 7 }, { 6, 7 }, { 4, 6 }, { 0, 1 }, { 1, 3 }, { 2, 3 }, { 0, 2 } };
    }
    
    public enum EnumHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"), 
        BOTTOM("BOTTOM", 1, "bottom");
        
        private final String field_176709_c;
        private static final EnumHalf[] $VALUES;
        private static final String __OBFID = "CL_00002062";
        
        private EnumHalf(final String p_i45683_1_, final int p_i45683_2_, final String p_i45683_3_) {
            this.field_176709_c = p_i45683_3_;
        }
        
        @Override
        public String toString() {
            return this.field_176709_c;
        }
        
        @Override
        public String getName() {
            return this.field_176709_c;
        }
        
        static {
            $VALUES = new EnumHalf[] { EnumHalf.TOP, EnumHalf.BOTTOM };
        }
    }
    
    public enum EnumShape implements IStringSerializable
    {
        STRAIGHT("STRAIGHT", 0, "straight"), 
        INNER_LEFT("INNER_LEFT", 1, "inner_left"), 
        INNER_RIGHT("INNER_RIGHT", 2, "inner_right"), 
        OUTER_LEFT("OUTER_LEFT", 3, "outer_left"), 
        OUTER_RIGHT("OUTER_RIGHT", 4, "outer_right");
        
        private final String field_176699_f;
        private static final EnumShape[] $VALUES;
        private static final String __OBFID = "CL_00002061";
        
        private EnumShape(final String p_i45682_1_, final int p_i45682_2_, final String p_i45682_3_) {
            this.field_176699_f = p_i45682_3_;
        }
        
        @Override
        public String toString() {
            return this.field_176699_f;
        }
        
        @Override
        public String getName() {
            return this.field_176699_f;
        }
        
        static {
            $VALUES = new EnumShape[] { EnumShape.STRAIGHT, EnumShape.INNER_LEFT, EnumShape.INNER_RIGHT, EnumShape.OUTER_LEFT, EnumShape.OUTER_RIGHT };
        }
    }
}
