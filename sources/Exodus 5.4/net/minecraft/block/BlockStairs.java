/*
 * Decompiled with CFR 0.152.
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
    private final IBlockState modelState;
    private static final int[][] field_150150_a;
    public static final PropertyDirection FACING;
    private final Block modelBlock;
    public static final PropertyEnum<EnumHalf> HALF;
    private int rayTracePass;
    public static final PropertyEnum<EnumShape> SHAPE;
    private boolean hasRaytraced;

    @Override
    public IBlockState getActualState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        if (this.func_176306_h(iBlockAccess, blockPos)) {
            switch (this.func_176305_g(iBlockAccess, blockPos)) {
                case 0: {
                    iBlockState = iBlockState.withProperty(SHAPE, EnumShape.STRAIGHT);
                    break;
                }
                case 1: {
                    iBlockState = iBlockState.withProperty(SHAPE, EnumShape.INNER_RIGHT);
                    break;
                }
                case 2: {
                    iBlockState = iBlockState.withProperty(SHAPE, EnumShape.INNER_LEFT);
                }
            }
        } else {
            switch (this.func_176307_f(iBlockAccess, blockPos)) {
                case 0: {
                    iBlockState = iBlockState.withProperty(SHAPE, EnumShape.STRAIGHT);
                    break;
                }
                case 1: {
                    iBlockState = iBlockState.withProperty(SHAPE, EnumShape.OUTER_RIGHT);
                    break;
                }
                case 2: {
                    iBlockState = iBlockState.withProperty(SHAPE, EnumShape.OUTER_LEFT);
                }
            }
        }
        return iBlockState;
    }

    public int func_176305_g(IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState;
        Block block;
        boolean bl;
        IBlockState iBlockState2 = iBlockAccess.getBlockState(blockPos);
        EnumFacing enumFacing = iBlockState2.getValue(FACING);
        EnumHalf enumHalf = iBlockState2.getValue(HALF);
        boolean bl2 = bl = enumHalf == EnumHalf.TOP;
        if (enumFacing == EnumFacing.EAST) {
            IBlockState iBlockState3 = iBlockAccess.getBlockState(blockPos.west());
            Block block2 = iBlockState3.getBlock();
            if (BlockStairs.isBlockStairs(block2) && enumHalf == iBlockState3.getValue(HALF)) {
                EnumFacing enumFacing2 = iBlockState3.getValue(FACING);
                if (enumFacing2 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    return bl ? 1 : 2;
                }
                if (enumFacing2 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    return bl ? 2 : 1;
                }
            }
        } else if (enumFacing == EnumFacing.WEST) {
            IBlockState iBlockState4 = iBlockAccess.getBlockState(blockPos.east());
            Block block3 = iBlockState4.getBlock();
            if (BlockStairs.isBlockStairs(block3) && enumHalf == iBlockState4.getValue(HALF)) {
                EnumFacing enumFacing3 = iBlockState4.getValue(FACING);
                if (enumFacing3 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    return bl ? 2 : 1;
                }
                if (enumFacing3 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    return bl ? 1 : 2;
                }
            }
        } else if (enumFacing == EnumFacing.SOUTH) {
            IBlockState iBlockState5 = iBlockAccess.getBlockState(blockPos.north());
            Block block4 = iBlockState5.getBlock();
            if (BlockStairs.isBlockStairs(block4) && enumHalf == iBlockState5.getValue(HALF)) {
                EnumFacing enumFacing4 = iBlockState5.getValue(FACING);
                if (enumFacing4 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                    return bl ? 2 : 1;
                }
                if (enumFacing4 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                    return bl ? 1 : 2;
                }
            }
        } else if (enumFacing == EnumFacing.NORTH && BlockStairs.isBlockStairs(block = (iBlockState = iBlockAccess.getBlockState(blockPos.south())).getBlock()) && enumHalf == iBlockState.getValue(HALF)) {
            EnumFacing enumFacing5 = iBlockState.getValue(FACING);
            if (enumFacing5 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                return bl ? 1 : 2;
            }
            if (enumFacing5 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                return bl ? 2 : 1;
            }
        }
        return 0;
    }

    @Override
    public boolean canCollideCheck(IBlockState iBlockState, boolean bl) {
        return this.modelBlock.canCollideCheck(iBlockState, bl);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos blockPos) {
        return this.modelBlock.canPlaceBlockAt(world, blockPos);
    }

    @Override
    public int tickRate(World world) {
        return this.modelBlock.tickRate(world);
    }

    @Override
    public void onBlockAdded(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.onNeighborBlockChange(world, blockPos, this.modelState, Blocks.air);
        this.modelBlock.onBlockAdded(world, blockPos, this.modelState);
    }

    public int func_176307_f(IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState;
        Block block;
        boolean bl;
        IBlockState iBlockState2 = iBlockAccess.getBlockState(blockPos);
        EnumFacing enumFacing = iBlockState2.getValue(FACING);
        EnumHalf enumHalf = iBlockState2.getValue(HALF);
        boolean bl2 = bl = enumHalf == EnumHalf.TOP;
        if (enumFacing == EnumFacing.EAST) {
            IBlockState iBlockState3 = iBlockAccess.getBlockState(blockPos.east());
            Block block2 = iBlockState3.getBlock();
            if (BlockStairs.isBlockStairs(block2) && enumHalf == iBlockState3.getValue(HALF)) {
                EnumFacing enumFacing2 = iBlockState3.getValue(FACING);
                if (enumFacing2 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    return bl ? 1 : 2;
                }
                if (enumFacing2 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    return bl ? 2 : 1;
                }
            }
        } else if (enumFacing == EnumFacing.WEST) {
            IBlockState iBlockState4 = iBlockAccess.getBlockState(blockPos.west());
            Block block3 = iBlockState4.getBlock();
            if (BlockStairs.isBlockStairs(block3) && enumHalf == iBlockState4.getValue(HALF)) {
                EnumFacing enumFacing3 = iBlockState4.getValue(FACING);
                if (enumFacing3 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    return bl ? 2 : 1;
                }
                if (enumFacing3 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    return bl ? 1 : 2;
                }
            }
        } else if (enumFacing == EnumFacing.SOUTH) {
            IBlockState iBlockState5 = iBlockAccess.getBlockState(blockPos.south());
            Block block4 = iBlockState5.getBlock();
            if (BlockStairs.isBlockStairs(block4) && enumHalf == iBlockState5.getValue(HALF)) {
                EnumFacing enumFacing4 = iBlockState5.getValue(FACING);
                if (enumFacing4 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                    return bl ? 2 : 1;
                }
                if (enumFacing4 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                    return bl ? 1 : 2;
                }
            }
        } else if (enumFacing == EnumFacing.NORTH && BlockStairs.isBlockStairs(block = (iBlockState = iBlockAccess.getBlockState(blockPos.north())).getBlock()) && enumHalf == iBlockState.getValue(HALF)) {
            EnumFacing enumFacing5 = iBlockState.getValue(FACING);
            if (enumFacing5 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                return bl ? 1 : 2;
            }
            if (enumFacing5 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                return bl ? 2 : 1;
            }
        }
        return 0;
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        this.modelBlock.updateTick(world, blockPos, iBlockState, random);
    }

    public void setBaseCollisionBounds(IBlockAccess iBlockAccess, BlockPos blockPos) {
        if (iBlockAccess.getBlockState(blockPos).getValue(HALF) == EnumHalf.TOP) {
            this.setBlockBounds(0.0f, 0.5f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        IBlockState iBlockState = this.getDefaultState().withProperty(HALF, (n & 4) > 0 ? EnumHalf.TOP : EnumHalf.BOTTOM);
        iBlockState = iBlockState.withProperty(FACING, EnumFacing.getFront(5 - (n & 3)));
        return iBlockState;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        IBlockState iBlockState = super.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, n, entityLivingBase);
        iBlockState = iBlockState.withProperty(FACING, entityLivingBase.getHorizontalFacing()).withProperty(SHAPE, EnumShape.STRAIGHT);
        return enumFacing != EnumFacing.DOWN && (enumFacing == EnumFacing.UP || (double)f2 <= 0.5) ? iBlockState.withProperty(HALF, EnumHalf.BOTTOM) : iBlockState.withProperty(HALF, EnumHalf.TOP);
    }

    public boolean func_176304_i(IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState;
        Block block;
        IBlockState iBlockState2 = iBlockAccess.getBlockState(blockPos);
        EnumFacing enumFacing = iBlockState2.getValue(FACING);
        EnumHalf enumHalf = iBlockState2.getValue(HALF);
        boolean bl = enumHalf == EnumHalf.TOP;
        float f = 0.5f;
        float f2 = 1.0f;
        if (bl) {
            f = 0.0f;
            f2 = 0.5f;
        }
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = 0.5f;
        float f6 = 1.0f;
        boolean bl2 = false;
        if (enumFacing == EnumFacing.EAST) {
            IBlockState iBlockState3 = iBlockAccess.getBlockState(blockPos.west());
            Block block2 = iBlockState3.getBlock();
            if (BlockStairs.isBlockStairs(block2) && enumHalf == iBlockState3.getValue(HALF)) {
                EnumFacing enumFacing2 = iBlockState3.getValue(FACING);
                if (enumFacing2 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    f5 = 0.0f;
                    f6 = 0.5f;
                    bl2 = true;
                } else if (enumFacing2 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    f5 = 0.5f;
                    f6 = 1.0f;
                    bl2 = true;
                }
            }
        } else if (enumFacing == EnumFacing.WEST) {
            IBlockState iBlockState4 = iBlockAccess.getBlockState(blockPos.east());
            Block block3 = iBlockState4.getBlock();
            if (BlockStairs.isBlockStairs(block3) && enumHalf == iBlockState4.getValue(HALF)) {
                f3 = 0.5f;
                f4 = 1.0f;
                EnumFacing enumFacing3 = iBlockState4.getValue(FACING);
                if (enumFacing3 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    f5 = 0.0f;
                    f6 = 0.5f;
                    bl2 = true;
                } else if (enumFacing3 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    f5 = 0.5f;
                    f6 = 1.0f;
                    bl2 = true;
                }
            }
        } else if (enumFacing == EnumFacing.SOUTH) {
            IBlockState iBlockState5 = iBlockAccess.getBlockState(blockPos.north());
            Block block4 = iBlockState5.getBlock();
            if (BlockStairs.isBlockStairs(block4) && enumHalf == iBlockState5.getValue(HALF)) {
                f5 = 0.0f;
                f6 = 0.5f;
                EnumFacing enumFacing4 = iBlockState5.getValue(FACING);
                if (enumFacing4 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                    bl2 = true;
                } else if (enumFacing4 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                    f3 = 0.5f;
                    f4 = 1.0f;
                    bl2 = true;
                }
            }
        } else if (enumFacing == EnumFacing.NORTH && BlockStairs.isBlockStairs(block = (iBlockState = iBlockAccess.getBlockState(blockPos.south())).getBlock()) && enumHalf == iBlockState.getValue(HALF)) {
            EnumFacing enumFacing5 = iBlockState.getValue(FACING);
            if (enumFacing5 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                bl2 = true;
            } else if (enumFacing5 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                f3 = 0.5f;
                f4 = 1.0f;
                bl2 = true;
            }
        }
        if (bl2) {
            this.setBlockBounds(f3, f, f5, f4, f2, f6);
        }
        return bl2;
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos blockPos, Explosion explosion) {
        this.modelBlock.onBlockDestroyedByExplosion(world, blockPos, explosion);
    }

    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        this.modelBlock.onBlockClicked(world, blockPos, entityPlayer);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        if (iBlockState.getValue(HALF) == EnumHalf.TOP) {
            n |= 4;
        }
        return n |= 5 - iBlockState.getValue(FACING).getIndex();
    }

    @Override
    public boolean isCollidable() {
        return this.modelBlock.isCollidable();
    }

    public boolean func_176306_h(IBlockAccess iBlockAccess, BlockPos blockPos) {
        IBlockState iBlockState;
        Block block;
        IBlockState iBlockState2 = iBlockAccess.getBlockState(blockPos);
        EnumFacing enumFacing = iBlockState2.getValue(FACING);
        EnumHalf enumHalf = iBlockState2.getValue(HALF);
        boolean bl = enumHalf == EnumHalf.TOP;
        float f = 0.5f;
        float f2 = 1.0f;
        if (bl) {
            f = 0.0f;
            f2 = 0.5f;
        }
        float f3 = 0.0f;
        float f4 = 1.0f;
        float f5 = 0.0f;
        float f6 = 0.5f;
        boolean bl2 = true;
        if (enumFacing == EnumFacing.EAST) {
            f3 = 0.5f;
            f6 = 1.0f;
            IBlockState iBlockState3 = iBlockAccess.getBlockState(blockPos.east());
            Block block2 = iBlockState3.getBlock();
            if (BlockStairs.isBlockStairs(block2) && enumHalf == iBlockState3.getValue(HALF)) {
                EnumFacing enumFacing2 = iBlockState3.getValue(FACING);
                if (enumFacing2 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    f6 = 0.5f;
                    bl2 = false;
                } else if (enumFacing2 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    f5 = 0.5f;
                    bl2 = false;
                }
            }
        } else if (enumFacing == EnumFacing.WEST) {
            f4 = 0.5f;
            f6 = 1.0f;
            IBlockState iBlockState4 = iBlockAccess.getBlockState(blockPos.west());
            Block block3 = iBlockState4.getBlock();
            if (BlockStairs.isBlockStairs(block3) && enumHalf == iBlockState4.getValue(HALF)) {
                EnumFacing enumFacing3 = iBlockState4.getValue(FACING);
                if (enumFacing3 == EnumFacing.NORTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.south(), iBlockState2)) {
                    f6 = 0.5f;
                    bl2 = false;
                } else if (enumFacing3 == EnumFacing.SOUTH && !BlockStairs.isSameStair(iBlockAccess, blockPos.north(), iBlockState2)) {
                    f5 = 0.5f;
                    bl2 = false;
                }
            }
        } else if (enumFacing == EnumFacing.SOUTH) {
            f5 = 0.5f;
            f6 = 1.0f;
            IBlockState iBlockState5 = iBlockAccess.getBlockState(blockPos.south());
            Block block4 = iBlockState5.getBlock();
            if (BlockStairs.isBlockStairs(block4) && enumHalf == iBlockState5.getValue(HALF)) {
                EnumFacing enumFacing4 = iBlockState5.getValue(FACING);
                if (enumFacing4 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                    f4 = 0.5f;
                    bl2 = false;
                } else if (enumFacing4 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                    f3 = 0.5f;
                    bl2 = false;
                }
            }
        } else if (enumFacing == EnumFacing.NORTH && BlockStairs.isBlockStairs(block = (iBlockState = iBlockAccess.getBlockState(blockPos.north())).getBlock()) && enumHalf == iBlockState.getValue(HALF)) {
            EnumFacing enumFacing5 = iBlockState.getValue(FACING);
            if (enumFacing5 == EnumFacing.WEST && !BlockStairs.isSameStair(iBlockAccess, blockPos.east(), iBlockState2)) {
                f4 = 0.5f;
                bl2 = false;
            } else if (enumFacing5 == EnumFacing.EAST && !BlockStairs.isSameStair(iBlockAccess, blockPos.west(), iBlockState2)) {
                f3 = 0.5f;
                bl2 = false;
            }
        }
        this.setBlockBounds(f3, f, f5, f4, f2, f6);
        return bl2;
    }

    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        HALF = PropertyEnum.create("half", EnumHalf.class);
        SHAPE = PropertyEnum.create("shape", EnumShape.class);
        int[][] nArrayArray = new int[8][];
        nArrayArray[0] = new int[]{4, 5};
        nArrayArray[1] = new int[]{5, 7};
        nArrayArray[2] = new int[]{6, 7};
        nArrayArray[3] = new int[]{4, 6};
        int[] nArray = new int[2];
        nArray[1] = 1;
        nArrayArray[4] = nArray;
        nArrayArray[5] = new int[]{1, 3};
        nArrayArray[6] = new int[]{2, 3};
        int[] nArray2 = new int[2];
        nArray2[1] = 2;
        nArrayArray[7] = nArray2;
        field_150150_a = nArrayArray;
    }

    public static boolean isSameStair(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState iBlockState) {
        IBlockState iBlockState2 = iBlockAccess.getBlockState(blockPos);
        Block block = iBlockState2.getBlock();
        return BlockStairs.isBlockStairs(block) && iBlockState2.getValue(HALF) == iBlockState.getValue(HALF) && iBlockState2.getValue(FACING) == iBlockState.getValue(FACING);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World world, BlockPos blockPos) {
        return this.modelBlock.getSelectedBoundingBox(world, blockPos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        return this.modelBlock.onBlockActivated(world, blockPos, this.modelState, entityPlayer, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        if (this.hasRaytraced) {
            this.setBlockBounds(0.5f * (float)(this.rayTracePass % 2), 0.5f * (float)(this.rayTracePass / 4 % 2), 0.5f * (float)(this.rayTracePass / 2 % 2), 0.5f + 0.5f * (float)(this.rayTracePass % 2), 0.5f + 0.5f * (float)(this.rayTracePass / 4 % 2), 0.5f + 0.5f * (float)(this.rayTracePass / 2 % 2));
        } else {
            this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    protected BlockStairs(IBlockState iBlockState) {
        super(iBlockState.getBlock().blockMaterial);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, EnumHalf.BOTTOM).withProperty(SHAPE, EnumShape.STRAIGHT));
        this.modelBlock = iBlockState.getBlock();
        this.modelState = iBlockState;
        this.setHardness(this.modelBlock.blockHardness);
        this.setResistance(this.modelBlock.blockResistance / 3.0f);
        this.setStepSound(this.modelBlock.stepSound);
        this.setLightOpacity(255);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return this.modelBlock.getBlockLayer();
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return this.modelBlock.getMapColor(this.modelState);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, BlockPos blockPos, Vec3 vec3, Vec3 vec32) {
        MovingObjectPosition[] movingObjectPositionArray = new MovingObjectPosition[8];
        IBlockState iBlockState = world.getBlockState(blockPos);
        int n = iBlockState.getValue(FACING).getHorizontalIndex();
        boolean bl = iBlockState.getValue(HALF) == EnumHalf.TOP;
        int[] nArray = field_150150_a[n + (bl ? 4 : 0)];
        this.hasRaytraced = true;
        int n2 = 0;
        while (n2 < 8) {
            this.rayTracePass = n2;
            if (Arrays.binarySearch(nArray, n2) < 0) {
                movingObjectPositionArray[n2] = super.collisionRayTrace(world, blockPos, vec3, vec32);
            }
            ++n2;
        }
        Object object = nArray;
        int n3 = nArray.length;
        int n4 = 0;
        while (n4 < n3) {
            n2 = object[n4];
            movingObjectPositionArray[n2] = null;
            ++n4;
        }
        Object object2 = null;
        double d = 0.0;
        MovingObjectPosition[] movingObjectPositionArray2 = movingObjectPositionArray;
        int n5 = movingObjectPositionArray.length;
        int n6 = 0;
        while (n6 < n5) {
            double d2;
            object = movingObjectPositionArray2[n6];
            if (object != null && (d2 = object.hitVec.squareDistanceTo(vec32)) > d) {
                object2 = object;
                d = d2;
            }
            ++n6;
        }
        return object2;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.modelBlock.onBlockDestroyedByPlayer(world, blockPos, iBlockState);
    }

    @Override
    public int getMixedBrightnessForBlock(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return this.modelBlock.getMixedBrightnessForBlock(iBlockAccess, blockPos);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState iBlockState) {
        this.modelBlock.breakBlock(world, blockPos, this.modelState);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, Entity entity) {
        this.modelBlock.onEntityCollidedWithBlock(world, blockPos, entity);
    }

    @Override
    public float getExplosionResistance(Entity entity) {
        return this.modelBlock.getExplosionResistance(entity);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    public static boolean isBlockStairs(Block block) {
        return block instanceof BlockStairs;
    }

    @Override
    public Vec3 modifyAcceleration(World world, BlockPos blockPos, Entity entity, Vec3 vec3) {
        return this.modelBlock.modifyAcceleration(world, blockPos, entity, vec3);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, HALF, SHAPE);
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        this.modelBlock.randomDisplayTick(world, blockPos, iBlockState, random);
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos blockPos, IBlockState iBlockState, AxisAlignedBB axisAlignedBB, List<AxisAlignedBB> list, Entity entity) {
        this.setBaseCollisionBounds(world, blockPos);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        boolean bl = this.func_176306_h(world, blockPos);
        super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        if (bl && this.func_176304_i(world, blockPos)) {
            super.addCollisionBoxesToList(world, blockPos, iBlockState, axisAlignedBB, list, entity);
        }
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public static enum EnumHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private EnumHalf(String string2) {
            this.name = string2;
        }

        public String toString() {
            return this.name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public static enum EnumShape implements IStringSerializable
    {
        STRAIGHT("straight"),
        INNER_LEFT("inner_left"),
        INNER_RIGHT("inner_right"),
        OUTER_LEFT("outer_left"),
        OUTER_RIGHT("outer_right");

        private final String name;

        @Override
        public String getName() {
            return this.name;
        }

        private EnumShape(String string2) {
            this.name = string2;
        }

        public String toString() {
            return this.name;
        }
    }
}

