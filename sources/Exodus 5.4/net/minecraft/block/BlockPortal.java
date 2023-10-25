/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.LoadingCache
 */
package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal
extends BlockBreakable {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create((String)"axis", EnumFacing.Axis.class, (Enum[])new EnumFacing.Axis[]{EnumFacing.Axis.X, EnumFacing.Axis.Z});

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AXIS);
    }

    public static int getMetaForAxis(EnumFacing.Axis axis) {
        return axis == EnumFacing.Axis.X ? 1 : (axis == EnumFacing.Axis.Z ? 2 : 0);
    }

    @Override
    public void randomDisplayTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSound((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, "portal.portal", 0.5f, random.nextFloat() * 0.4f + 0.8f, false);
        }
        int n = 0;
        while (n < 4) {
            double d = (float)blockPos.getX() + random.nextFloat();
            double d2 = (float)blockPos.getY() + random.nextFloat();
            double d3 = (float)blockPos.getZ() + random.nextFloat();
            double d4 = ((double)random.nextFloat() - 0.5) * 0.5;
            double d5 = ((double)random.nextFloat() - 0.5) * 0.5;
            double d6 = ((double)random.nextFloat() - 0.5) * 0.5;
            int n2 = random.nextInt(2) * 2 - 1;
            if (world.getBlockState(blockPos.west()).getBlock() != this && world.getBlockState(blockPos.east()).getBlock() != this) {
                d = (double)blockPos.getX() + 0.5 + 0.25 * (double)n2;
                d4 = random.nextFloat() * 2.0f * (float)n2;
            } else {
                d3 = (double)blockPos.getZ() + 0.5 + 0.25 * (double)n2;
                d6 = random.nextFloat() * 2.0f * (float)n2;
            }
            world.spawnParticle(EnumParticleTypes.PORTAL, d, d2, d3, d4, d5, d6, new int[0]);
            ++n;
        }
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        super.updateTick(world, blockPos, iBlockState, random);
        if (world.provider.isSurfaceWorld() && world.getGameRules().getBoolean("doMobSpawning") && random.nextInt(2000) < world.getDifficulty().getDifficultyId()) {
            Entity entity;
            int n = blockPos.getY();
            BlockPos blockPos2 = blockPos;
            while (!World.doesBlockHaveSolidTopSurface(world, blockPos2) && blockPos2.getY() > 0) {
                blockPos2 = blockPos2.down();
            }
            if (n > 0 && !world.getBlockState(blockPos2.up()).getBlock().isNormalCube() && (entity = ItemMonsterPlacer.spawnCreature(world, 57, (double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 1.1, (double)blockPos2.getZ() + 0.5)) != null) {
                entity.timeUntilPortal = entity.getPortalCooldown();
            }
        }
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos blockPos, IBlockState iBlockState, Block block) {
        Size size;
        EnumFacing.Axis axis = iBlockState.getValue(AXIS);
        if (axis == EnumFacing.Axis.X) {
            Size size2 = new Size(world, blockPos, EnumFacing.Axis.X);
            if (!size2.func_150860_b() || size2.field_150864_e < size2.field_150868_h * size2.field_150862_g) {
                world.setBlockState(blockPos, Blocks.air.getDefaultState());
            }
        } else if (!(axis != EnumFacing.Axis.Z || (size = new Size(world, blockPos, EnumFacing.Axis.Z)).func_150860_b() && size.field_150864_e >= size.field_150868_h * size.field_150862_g)) {
            world.setBlockState(blockPos, Blocks.air.getDefaultState());
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos blockPos, IBlockState iBlockState, Entity entity) {
        if (entity.ridingEntity == null && entity.riddenByEntity == null) {
            entity.func_181015_d(blockPos);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos blockPos, IBlockState iBlockState) {
        return null;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        EnumFacing.Axis axis = iBlockAccess.getBlockState(blockPos).getValue(AXIS);
        float f = 0.125f;
        float f2 = 0.125f;
        if (axis == EnumFacing.Axis.X) {
            f = 0.5f;
        }
        if (axis == EnumFacing.Axis.Z) {
            f2 = 0.5f;
        }
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f2, 0.5f + f, 1.0f, 0.5f + f2);
    }

    public boolean func_176548_d(World world, BlockPos blockPos) {
        Size size = new Size(world, blockPos, EnumFacing.Axis.X);
        if (size.func_150860_b() && size.field_150864_e == 0) {
            size.func_150859_c();
            return true;
        }
        Size size2 = new Size(world, blockPos, EnumFacing.Axis.Z);
        if (size2.func_150860_b() && size2.field_150864_e == 0) {
            size2.func_150859_c();
            return true;
        }
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(AXIS, (n & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        boolean bl;
        EnumFacing.Axis axis = null;
        IBlockState iBlockState = iBlockAccess.getBlockState(blockPos);
        if (iBlockAccess.getBlockState(blockPos).getBlock() == this) {
            axis = iBlockState.getValue(AXIS);
            if (axis == null) {
                return false;
            }
            if (axis == EnumFacing.Axis.Z && enumFacing != EnumFacing.EAST && enumFacing != EnumFacing.WEST) {
                return false;
            }
            if (axis == EnumFacing.Axis.X && enumFacing != EnumFacing.SOUTH && enumFacing != EnumFacing.NORTH) {
                return false;
            }
        }
        boolean bl2 = iBlockAccess.getBlockState(blockPos.west()).getBlock() == this && iBlockAccess.getBlockState(blockPos.west(2)).getBlock() != this;
        boolean bl3 = iBlockAccess.getBlockState(blockPos.east()).getBlock() == this && iBlockAccess.getBlockState(blockPos.east(2)).getBlock() != this;
        boolean bl4 = iBlockAccess.getBlockState(blockPos.north()).getBlock() == this && iBlockAccess.getBlockState(blockPos.north(2)).getBlock() != this;
        boolean bl5 = iBlockAccess.getBlockState(blockPos.south()).getBlock() == this && iBlockAccess.getBlockState(blockPos.south(2)).getBlock() != this;
        boolean bl6 = bl2 || bl3 || axis == EnumFacing.Axis.X;
        boolean bl7 = bl = bl4 || bl5 || axis == EnumFacing.Axis.Z;
        return bl6 && enumFacing == EnumFacing.WEST ? true : (bl6 && enumFacing == EnumFacing.EAST ? true : (bl && enumFacing == EnumFacing.NORTH ? true : bl && enumFacing == EnumFacing.SOUTH));
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public Item getItem(World world, BlockPos blockPos) {
        return null;
    }

    public BlockPortal() {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }

    public BlockPattern.PatternHelper func_181089_f(World world, BlockPos blockPos) {
        Object object;
        EnumFacing.AxisDirection axisDirection;
        EnumFacing.Axis axis = EnumFacing.Axis.Z;
        Size size = new Size(world, blockPos, EnumFacing.Axis.X);
        LoadingCache<BlockPos, BlockWorldState> loadingCache = BlockPattern.func_181627_a(world, true);
        if (!size.func_150860_b()) {
            axis = EnumFacing.Axis.X;
            size = new Size(world, blockPos, EnumFacing.Axis.Z);
        }
        if (!size.func_150860_b()) {
            return new BlockPattern.PatternHelper(blockPos, EnumFacing.NORTH, EnumFacing.UP, loadingCache, 1, 1, 1);
        }
        int[] nArray = new int[EnumFacing.AxisDirection.values().length];
        EnumFacing enumFacing = size.field_150866_c.rotateYCCW();
        BlockPos blockPos2 = size.field_150861_f.up(size.func_181100_a() - 1);
        EnumFacing.AxisDirection[] axisDirectionArray = EnumFacing.AxisDirection.values();
        int n = axisDirectionArray.length;
        int n2 = 0;
        while (n2 < n) {
            axisDirection = axisDirectionArray[n2];
            object = new BlockPattern.PatternHelper(enumFacing.getAxisDirection() == axisDirection ? blockPos2 : blockPos2.offset(size.field_150866_c, size.func_181101_b() - 1), EnumFacing.func_181076_a(axisDirection, axis), EnumFacing.UP, loadingCache, size.func_181101_b(), size.func_181100_a(), 1);
            int n3 = 0;
            while (n3 < size.func_181101_b()) {
                int n4 = 0;
                while (n4 < size.func_181100_a()) {
                    BlockWorldState blockWorldState = ((BlockPattern.PatternHelper)object).translateOffset(n3, n4, 1);
                    if (blockWorldState.getBlockState() != null && blockWorldState.getBlockState().getBlock().getMaterial() != Material.air) {
                        int n5 = axisDirection.ordinal();
                        nArray[n5] = nArray[n5] + 1;
                    }
                    ++n4;
                }
                ++n3;
            }
            ++n2;
        }
        axisDirection = EnumFacing.AxisDirection.POSITIVE;
        object = EnumFacing.AxisDirection.values();
        int n6 = ((EnumFacing.AxisDirection[])object).length;
        n = 0;
        while (n < n6) {
            EnumFacing.AxisDirection axisDirection2 = object[n];
            if (nArray[axisDirection2.ordinal()] < nArray[axisDirection.ordinal()]) {
                axisDirection = axisDirection2;
            }
            ++n;
        }
        return new BlockPattern.PatternHelper(enumFacing.getAxisDirection() == axisDirection ? blockPos2 : blockPos2.offset(size.field_150866_c, size.func_181101_b() - 1), EnumFacing.func_181076_a(axisDirection, axis), EnumFacing.UP, loadingCache, size.func_181101_b(), size.func_181100_a(), 1);
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        return BlockPortal.getMetaForAxis(iBlockState.getValue(AXIS));
    }

    public static class Size {
        private int field_150864_e = 0;
        private BlockPos field_150861_f;
        private int field_150862_g;
        private final EnumFacing field_150863_d;
        private int field_150868_h;
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing field_150866_c;

        public int func_181100_a() {
            return this.field_150862_g;
        }

        public int func_181101_b() {
            return this.field_150868_h;
        }

        public void func_150859_c() {
            int n = 0;
            while (n < this.field_150868_h) {
                BlockPos blockPos = this.field_150861_f.offset(this.field_150866_c, n);
                int n2 = 0;
                while (n2 < this.field_150862_g) {
                    this.world.setBlockState(blockPos.up(n2), Blocks.portal.getDefaultState().withProperty(AXIS, this.axis), 2);
                    ++n2;
                }
                ++n;
            }
        }

        public boolean func_150860_b() {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }

        protected boolean func_150857_a(Block block) {
            return block.blockMaterial == Material.air || block == Blocks.fire || block == Blocks.portal;
        }

        protected int func_180120_a(BlockPos blockPos, EnumFacing enumFacing) {
            Object object;
            int n = 0;
            while (n < 22) {
                object = blockPos.offset(enumFacing, n);
                if (!this.func_150857_a(this.world.getBlockState((BlockPos)object).getBlock()) || this.world.getBlockState(((BlockPos)object).down()).getBlock() != Blocks.obsidian) break;
                ++n;
            }
            return (object = this.world.getBlockState(blockPos.offset(enumFacing, n)).getBlock()) == Blocks.obsidian ? n : 0;
        }

        public Size(World world, BlockPos blockPos, EnumFacing.Axis axis) {
            this.world = world;
            this.axis = axis;
            if (axis == EnumFacing.Axis.X) {
                this.field_150863_d = EnumFacing.EAST;
                this.field_150866_c = EnumFacing.WEST;
            } else {
                this.field_150863_d = EnumFacing.NORTH;
                this.field_150866_c = EnumFacing.SOUTH;
            }
            BlockPos blockPos2 = blockPos;
            while (blockPos.getY() > blockPos2.getY() - 21 && blockPos.getY() > 0 && this.func_150857_a(world.getBlockState(blockPos.down()).getBlock())) {
                blockPos = blockPos.down();
            }
            int n = this.func_180120_a(blockPos, this.field_150863_d) - 1;
            if (n >= 0) {
                this.field_150861_f = blockPos.offset(this.field_150863_d, n);
                this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
                if (this.field_150868_h < 2 || this.field_150868_h > 21) {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }
            if (this.field_150861_f != null) {
                this.field_150862_g = this.func_150858_a();
            }
        }

        protected int func_150858_a() {
            int n;
            this.field_150862_g = 0;
            block0: while (this.field_150862_g < 21) {
                n = 0;
                while (n < this.field_150868_h) {
                    BlockPos blockPos = this.field_150861_f.offset(this.field_150866_c, n).up(this.field_150862_g);
                    Block block = this.world.getBlockState(blockPos).getBlock();
                    if (!this.func_150857_a(block)) break block0;
                    if (block == Blocks.portal) {
                        ++this.field_150864_e;
                    }
                    if (n == 0 ? (block = this.world.getBlockState(blockPos.offset(this.field_150863_d)).getBlock()) != Blocks.obsidian : n == this.field_150868_h - 1 && (block = this.world.getBlockState(blockPos.offset(this.field_150866_c)).getBlock()) != Blocks.obsidian) break block0;
                    ++n;
                }
                ++this.field_150862_g;
            }
            n = 0;
            while (n < this.field_150868_h) {
                if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, n).up(this.field_150862_g)).getBlock() != Blocks.obsidian) {
                    this.field_150862_g = 0;
                    break;
                }
                ++n;
            }
            if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
                return this.field_150862_g;
            }
            this.field_150861_f = null;
            this.field_150868_h = 0;
            this.field_150862_g = 0;
            return 0;
        }
    }
}

