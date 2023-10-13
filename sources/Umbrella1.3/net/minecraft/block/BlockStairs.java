/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStairs
extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum HALF = PropertyEnum.create("half", EnumHalf.class);
    public static final PropertyEnum SHAPE = PropertyEnum.create("shape", EnumShape.class);
    private static final int[][] field_150150_a;
    private final Block modelBlock;
    private final IBlockState modelState;
    private boolean field_150152_N;
    private int field_150153_O;
    private static final String __OBFID = "CL_00000314";

    static {
        int[][] arrarrn = new int[8][];
        arrarrn[0] = new int[]{4, 5};
        arrarrn[1] = new int[]{5, 7};
        arrarrn[2] = new int[]{6, 7};
        arrarrn[3] = new int[]{4, 6};
        int[] arrn = new int[2];
        arrn[1] = 1;
        arrarrn[4] = arrn;
        arrarrn[5] = new int[]{1, 3};
        arrarrn[6] = new int[]{2, 3};
        int[] arrn2 = new int[2];
        arrn2[1] = 2;
        arrarrn[7] = arrn2;
        field_150150_a = arrarrn;
    }

    protected BlockStairs(IBlockState modelState) {
        super(modelState.getBlock().blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, (Comparable)((Object)EnumFacing.NORTH)).withProperty(HALF, (Comparable)((Object)EnumHalf.BOTTOM)).withProperty(SHAPE, (Comparable)((Object)EnumShape.STRAIGHT)));
        this.modelBlock = modelState.getBlock();
        this.modelState = modelState;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0f);
        this.setStepSound(this.modelBlock.stepSound);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        if (this.field_150152_N) {
            this.setBlockBounds(0.5f * (float)(this.field_150153_O % 2), 0.5f * (float)(this.field_150153_O / 4 % 2), 0.5f * (float)(this.field_150153_O / 2 % 2), 0.5f + 0.5f * (float)(this.field_150153_O % 2), 0.5f + 0.5f * (float)(this.field_150153_O / 4 % 2), 0.5f + 0.5f * (float)(this.field_150153_O / 2 % 2));
        } else {
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

    public void setBaseCollisionBounds(IBlockAccess worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getValue(HALF) == EnumHalf.TOP) {
            this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
    }

    public static boolean isBlockStairs(Block p_150148_0_) {
        return p_150148_0_ instanceof BlockStairs;
    }

    public static boolean isSameStair(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
        IBlockState var3 = worldIn.getBlockState(pos);
        Block var4 = var3.getBlock();
        return BlockStairs.isBlockStairs(var4) && var3.getValue(HALF) == state.getValue(HALF) && var3.getValue(FACING) == state.getValue(FACING);
    }

    public int func_176307_f(IBlockAccess p_176307_1_, BlockPos p_176307_2_) {
        IBlockState var7;
        Block var8;
        boolean var6;
        IBlockState var3 = p_176307_1_.getBlockState(p_176307_2_);
        EnumFacing var4 = (EnumFacing)((Object)var3.getValue(FACING));
        EnumHalf var5 = (EnumHalf)((Object)var3.getValue(HALF));
        boolean bl = var6 = var5 == EnumHalf.TOP;
        if (var4 == EnumFacing.EAST) {
            IBlockState var72 = p_176307_1_.getBlockState(p_176307_2_.offsetEast());
            Block var82 = var72.getBlock();
            if (BlockStairs.isBlockStairs(var82) && var5 == var72.getValue(HALF)) {
                EnumFacing var9 = (EnumFacing)((Object)var72.getValue(FACING));
                if (var9 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetSouth(), var3)) {
                    return var6 ? 1 : 2;
                }
                if (var9 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetNorth(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            IBlockState var73 = p_176307_1_.getBlockState(p_176307_2_.offsetWest());
            Block var83 = var73.getBlock();
            if (BlockStairs.isBlockStairs(var83) && var5 == var73.getValue(HALF)) {
                EnumFacing var9 = (EnumFacing)((Object)var73.getValue(FACING));
                if (var9 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetSouth(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetNorth(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            IBlockState var74 = p_176307_1_.getBlockState(p_176307_2_.offsetSouth());
            Block var84 = var74.getBlock();
            if (BlockStairs.isBlockStairs(var84) && var5 == var74.getValue(HALF)) {
                EnumFacing var9 = (EnumFacing)((Object)var74.getValue(FACING));
                if (var9 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetEast(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetWest(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.NORTH && BlockStairs.isBlockStairs(var8 = (var7 = p_176307_1_.getBlockState(p_176307_2_.offsetNorth())).getBlock()) && var5 == var7.getValue(HALF)) {
            EnumFacing var9 = (EnumFacing)((Object)var7.getValue(FACING));
            if (var9 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetEast(), var3)) {
                return var6 ? 1 : 2;
            }
            if (var9 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176307_1_, p_176307_2_.offsetWest(), var3)) {
                return var6 ? 2 : 1;
            }
        }
        return 0;
    }

    public int func_176305_g(IBlockAccess p_176305_1_, BlockPos p_176305_2_) {
        IBlockState var7;
        Block var8;
        boolean var6;
        IBlockState var3 = p_176305_1_.getBlockState(p_176305_2_);
        EnumFacing var4 = (EnumFacing)((Object)var3.getValue(FACING));
        EnumHalf var5 = (EnumHalf)((Object)var3.getValue(HALF));
        boolean bl = var6 = var5 == EnumHalf.TOP;
        if (var4 == EnumFacing.EAST) {
            IBlockState var72 = p_176305_1_.getBlockState(p_176305_2_.offsetWest());
            Block var82 = var72.getBlock();
            if (BlockStairs.isBlockStairs(var82) && var5 == var72.getValue(HALF)) {
                EnumFacing var9 = (EnumFacing)((Object)var72.getValue(FACING));
                if (var9 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetNorth(), var3)) {
                    return var6 ? 1 : 2;
                }
                if (var9 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetSouth(), var3)) {
                    return var6 ? 2 : 1;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            IBlockState var73 = p_176305_1_.getBlockState(p_176305_2_.offsetEast());
            Block var83 = var73.getBlock();
            if (BlockStairs.isBlockStairs(var83) && var5 == var73.getValue(HALF)) {
                EnumFacing var9 = (EnumFacing)((Object)var73.getValue(FACING));
                if (var9 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetNorth(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetSouth(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            IBlockState var74 = p_176305_1_.getBlockState(p_176305_2_.offsetNorth());
            Block var84 = var74.getBlock();
            if (BlockStairs.isBlockStairs(var84) && var5 == var74.getValue(HALF)) {
                EnumFacing var9 = (EnumFacing)((Object)var74.getValue(FACING));
                if (var9 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetWest(), var3)) {
                    return var6 ? 2 : 1;
                }
                if (var9 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetEast(), var3)) {
                    return var6 ? 1 : 2;
                }
            }
        } else if (var4 == EnumFacing.NORTH && BlockStairs.isBlockStairs(var8 = (var7 = p_176305_1_.getBlockState(p_176305_2_.offsetSouth())).getBlock()) && var5 == var7.getValue(HALF)) {
            EnumFacing var9 = (EnumFacing)((Object)var7.getValue(FACING));
            if (var9 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetWest(), var3)) {
                return var6 ? 1 : 2;
            }
            if (var9 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176305_1_, p_176305_2_.offsetEast(), var3)) {
                return var6 ? 2 : 1;
            }
        }
        return 0;
    }

    public boolean func_176306_h(IBlockAccess p_176306_1_, BlockPos p_176306_2_) {
        IBlockState var14;
        Block var15;
        IBlockState var3 = p_176306_1_.getBlockState(p_176306_2_);
        EnumFacing var4 = (EnumFacing)((Object)var3.getValue(FACING));
        EnumHalf var5 = (EnumHalf)((Object)var3.getValue(HALF));
        boolean var6 = var5 == EnumHalf.TOP;
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
            IBlockState var142 = p_176306_1_.getBlockState(p_176306_2_.offsetEast());
            Block var152 = var142.getBlock();
            if (BlockStairs.isBlockStairs(var152) && var5 == var142.getValue(HALF)) {
                EnumFacing var16 = (EnumFacing)((Object)var142.getValue(FACING));
                if (var16 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetSouth(), var3)) {
                    var12 = 0.5f;
                    var13 = false;
                } else if (var16 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetNorth(), var3)) {
                    var11 = 0.5f;
                    var13 = false;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            var10 = 0.5f;
            var12 = 1.0f;
            IBlockState var143 = p_176306_1_.getBlockState(p_176306_2_.offsetWest());
            Block var153 = var143.getBlock();
            if (BlockStairs.isBlockStairs(var153) && var5 == var143.getValue(HALF)) {
                EnumFacing var16 = (EnumFacing)((Object)var143.getValue(FACING));
                if (var16 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetSouth(), var3)) {
                    var12 = 0.5f;
                    var13 = false;
                } else if (var16 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetNorth(), var3)) {
                    var11 = 0.5f;
                    var13 = false;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            var11 = 0.5f;
            var12 = 1.0f;
            IBlockState var144 = p_176306_1_.getBlockState(p_176306_2_.offsetSouth());
            Block var154 = var144.getBlock();
            if (BlockStairs.isBlockStairs(var154) && var5 == var144.getValue(HALF)) {
                EnumFacing var16 = (EnumFacing)((Object)var144.getValue(FACING));
                if (var16 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetEast(), var3)) {
                    var10 = 0.5f;
                    var13 = false;
                } else if (var16 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetWest(), var3)) {
                    var9 = 0.5f;
                    var13 = false;
                }
            }
        } else if (var4 == EnumFacing.NORTH && BlockStairs.isBlockStairs(var15 = (var14 = p_176306_1_.getBlockState(p_176306_2_.offsetNorth())).getBlock()) && var5 == var14.getValue(HALF)) {
            EnumFacing var16 = (EnumFacing)((Object)var14.getValue(FACING));
            if (var16 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetEast(), var3)) {
                var10 = 0.5f;
                var13 = false;
            } else if (var16 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176306_1_, p_176306_2_.offsetWest(), var3)) {
                var9 = 0.5f;
                var13 = false;
            }
        }
        this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        return var13;
    }

    public boolean func_176304_i(IBlockAccess p_176304_1_, BlockPos p_176304_2_) {
        IBlockState var14;
        Block var15;
        IBlockState var3 = p_176304_1_.getBlockState(p_176304_2_);
        EnumFacing var4 = (EnumFacing)((Object)var3.getValue(FACING));
        EnumHalf var5 = (EnumHalf)((Object)var3.getValue(HALF));
        boolean var6 = var5 == EnumHalf.TOP;
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
            IBlockState var142 = p_176304_1_.getBlockState(p_176304_2_.offsetWest());
            Block var152 = var142.getBlock();
            if (BlockStairs.isBlockStairs(var152) && var5 == var142.getValue(HALF)) {
                EnumFacing var16 = (EnumFacing)((Object)var142.getValue(FACING));
                if (var16 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetNorth(), var3)) {
                    var11 = 0.0f;
                    var12 = 0.5f;
                    var13 = true;
                } else if (var16 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetSouth(), var3)) {
                    var11 = 0.5f;
                    var12 = 1.0f;
                    var13 = true;
                }
            }
        } else if (var4 == EnumFacing.WEST) {
            IBlockState var143 = p_176304_1_.getBlockState(p_176304_2_.offsetEast());
            Block var153 = var143.getBlock();
            if (BlockStairs.isBlockStairs(var153) && var5 == var143.getValue(HALF)) {
                var9 = 0.5f;
                var10 = 1.0f;
                EnumFacing var16 = (EnumFacing)((Object)var143.getValue(FACING));
                if (var16 == EnumFacing.NORTH && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetNorth(), var3)) {
                    var11 = 0.0f;
                    var12 = 0.5f;
                    var13 = true;
                } else if (var16 == EnumFacing.SOUTH && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetSouth(), var3)) {
                    var11 = 0.5f;
                    var12 = 1.0f;
                    var13 = true;
                }
            }
        } else if (var4 == EnumFacing.SOUTH) {
            IBlockState var144 = p_176304_1_.getBlockState(p_176304_2_.offsetNorth());
            Block var154 = var144.getBlock();
            if (BlockStairs.isBlockStairs(var154) && var5 == var144.getValue(HALF)) {
                var11 = 0.0f;
                var12 = 0.5f;
                EnumFacing var16 = (EnumFacing)((Object)var144.getValue(FACING));
                if (var16 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetWest(), var3)) {
                    var13 = true;
                } else if (var16 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetEast(), var3)) {
                    var9 = 0.5f;
                    var10 = 1.0f;
                    var13 = true;
                }
            }
        } else if (var4 == EnumFacing.NORTH && BlockStairs.isBlockStairs(var15 = (var14 = p_176304_1_.getBlockState(p_176304_2_.offsetSouth())).getBlock()) && var5 == var14.getValue(HALF)) {
            EnumFacing var16 = (EnumFacing)((Object)var14.getValue(FACING));
            if (var16 == EnumFacing.WEST && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetWest(), var3)) {
                var13 = true;
            } else if (var16 == EnumFacing.EAST && !BlockStairs.isSameStair(p_176304_1_, p_176304_2_.offsetEast(), var3)) {
                var9 = 0.5f;
                var10 = 1.0f;
                var13 = true;
            }
        }
        if (var13) {
            this.setBlockBounds(var9, var7, var11, var10, var8, var12);
        }
        return var13;
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        this.setBaseCollisionBounds(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        boolean var7 = this.func_176306_h(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        if (var7 && this.func_176304_i(worldIn, pos)) {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.modelBlock.randomDisplayTick(worldIn, pos, state, rand);
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
        return this.modelBlock.getMixedBrightnessForBlock(worldIn, pos);
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return this.modelBlock.getExplosionResistance(exploder);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.modelBlock.getBlockLayer();
    }

    @Override
    public int tickRate(World worldIn) {
        return this.modelBlock.tickRate(worldIn);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return this.modelBlock.getSelectedBoundingBox(worldIn, pos);
    }

    @Override
    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion) {
        return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
    }

    @Override
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    @Override
    public boolean canCollideCheck(IBlockState state, boolean p_176209_2_) {
        return this.modelBlock.canCollideCheck(state, p_176209_2_);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return this.modelBlock.canPlaceBlockAt(worldIn, pos);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.onNeighborBlockChange(worldIn, pos, this.modelState, Blocks.air);
        this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        this.modelBlock.breakBlock(worldIn, pos, this.modelState);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
        this.modelBlock.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.modelBlock.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return this.modelBlock.getMapColor(this.modelState);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState var9 = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        var9 = var9.withProperty(FACING, (Comparable)((Object)placer.func_174811_aO())).withProperty(SHAPE, (Comparable)((Object)EnumShape.STRAIGHT));
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5) ? var9.withProperty(HALF, (Comparable)((Object)EnumHalf.BOTTOM)) : var9.withProperty(HALF, (Comparable)((Object)EnumHalf.TOP));
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        MovingObjectPosition[] var5 = new MovingObjectPosition[8];
        IBlockState var6 = worldIn.getBlockState(pos);
        int var7 = ((EnumFacing)((Object)var6.getValue(FACING))).getHorizontalIndex();
        boolean var8 = var6.getValue(HALF) == EnumHalf.TOP;
        int[] var9 = field_150150_a[var7 + (var8 ? 4 : 0)];
        this.field_150152_N = true;
        for (int var10 = 0; var10 < 8; ++var10) {
            this.field_150153_O = var10;
            if (Arrays.binarySearch(var9, var10) >= 0) continue;
            var5[var10] = super.collisionRayTrace(worldIn, pos, start, end);
        }
        int[] var19 = var9;
        int var11 = var9.length;
        for (int var12 = 0; var12 < var11; ++var12) {
            int var13 = var19[var12];
            var5[var13] = null;
        }
        MovingObjectPosition var20 = null;
        double var21 = 0.0;
        MovingObjectPosition[] var22 = var5;
        int var14 = var5.length;
        for (int var15 = 0; var15 < var14; ++var15) {
            double var17;
            MovingObjectPosition var16 = var22[var15];
            if (var16 == null || !((var17 = var16.hitVec.squareDistanceTo(end)) > var21)) continue;
            var20 = var16;
            var21 = var17;
        }
        return var20;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState var2 = this.getDefaultState().withProperty(HALF, (Comparable)((Object)((meta & 4) > 0 ? EnumHalf.TOP : EnumHalf.BOTTOM)));
        var2 = var2.withProperty(FACING, (Comparable)((Object)EnumFacing.getFront(5 - (meta & 3))));
        return var2;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int var2 = 0;
        if (state.getValue(HALF) == EnumHalf.TOP) {
            var2 |= 4;
        }
        return var2 |= 5 - ((EnumFacing)((Object)state.getValue(FACING))).getIndex();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (this.func_176306_h(worldIn, pos)) {
            switch (this.func_176305_g(worldIn, pos)) {
                case 0: {
                    state = state.withProperty(SHAPE, (Comparable)((Object)EnumShape.STRAIGHT));
                    break;
                }
                case 1: {
                    state = state.withProperty(SHAPE, (Comparable)((Object)EnumShape.INNER_RIGHT));
                    break;
                }
                case 2: {
                    state = state.withProperty(SHAPE, (Comparable)((Object)EnumShape.INNER_LEFT));
                }
            }
        } else {
            switch (this.func_176307_f(worldIn, pos)) {
                case 0: {
                    state = state.withProperty(SHAPE, (Comparable)((Object)EnumShape.STRAIGHT));
                    break;
                }
                case 1: {
                    state = state.withProperty(SHAPE, (Comparable)((Object)EnumShape.OUTER_RIGHT));
                    break;
                }
                case 2: {
                    state = state.withProperty(SHAPE, (Comparable)((Object)EnumShape.OUTER_LEFT));
                }
            }
        }
        return state;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, HALF, SHAPE);
    }

    public static enum EnumHalf implements IStringSerializable
    {
        TOP("TOP", 0, "top"),
        BOTTOM("BOTTOM", 1, "bottom");

        private final String field_176709_c;
        private static final EnumHalf[] $VALUES;
        private static final String __OBFID = "CL_00002062";

        static {
            $VALUES = new EnumHalf[]{TOP, BOTTOM};
        }

        private EnumHalf(String p_i45683_1_, int p_i45683_2_, String p_i45683_3_) {
            this.field_176709_c = p_i45683_3_;
        }

        public String toString() {
            return this.field_176709_c;
        }

        @Override
        public String getName() {
            return this.field_176709_c;
        }
    }

    public static enum EnumShape implements IStringSerializable
    {
        STRAIGHT("STRAIGHT", 0, "straight"),
        INNER_LEFT("INNER_LEFT", 1, "inner_left"),
        INNER_RIGHT("INNER_RIGHT", 2, "inner_right"),
        OUTER_LEFT("OUTER_LEFT", 3, "outer_left"),
        OUTER_RIGHT("OUTER_RIGHT", 4, "outer_right");

        private final String field_176699_f;
        private static final EnumShape[] $VALUES;
        private static final String __OBFID = "CL_00002061";

        static {
            $VALUES = new EnumShape[]{STRAIGHT, INNER_LEFT, INNER_RIGHT, OUTER_LEFT, OUTER_RIGHT};
        }

        private EnumShape(String p_i45682_1_, int p_i45682_2_, String p_i45682_3_) {
            this.field_176699_f = p_i45682_3_;
        }

        public String toString() {
            return this.field_176699_f;
        }

        @Override
        public String getName() {
            return this.field_176699_f;
        }
    }
}

