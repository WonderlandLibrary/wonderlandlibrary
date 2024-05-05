/*
 * Decompiled with CFR 0.152.
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

    public BlockPortal() {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (!worldIn.provider.isSurfaceWorld()) return;
        if (!worldIn.getGameRules().getBoolean("doMobSpawning")) return;
        if (rand.nextInt(2000) >= worldIn.getDifficulty().getDifficultyId()) return;
        int i = pos.getY();
        BlockPos blockpos = pos;
        while (!World.doesBlockHaveSolidTopSurface(worldIn, blockpos) && blockpos.getY() > 0) {
            blockpos = blockpos.down();
        }
        if (i <= 0) return;
        if (worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube()) return;
        Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, 57, (double)blockpos.getX() + 0.5, (double)blockpos.getY() + 1.1, (double)blockpos.getZ() + 0.5);
        if (entity == null) return;
        entity.timeUntilPortal = entity.getPortalCooldown();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = worldIn.getBlockState(pos).getValue(AXIS);
        float f = 0.125f;
        float f1 = 0.125f;
        if (enumfacing$axis == EnumFacing.Axis.X) {
            f = 0.5f;
        }
        if (enumfacing$axis == EnumFacing.Axis.Z) {
            f1 = 0.5f;
        }
        this.setBlockBounds(0.5f - f, 0.0f, 0.5f - f1, 0.5f + f, 1.0f, 0.5f + f1);
    }

    public static int getMetaForAxis(EnumFacing.Axis axis) {
        if (axis == EnumFacing.Axis.X) {
            return 1;
        }
        if (axis != EnumFacing.Axis.Z) return 0;
        return 2;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    public boolean func_176548_d(World worldIn, BlockPos p_176548_2_) {
        Size blockportal$size = new Size(worldIn, p_176548_2_, EnumFacing.Axis.X);
        if (blockportal$size.func_150860_b() && blockportal$size.field_150864_e == 0) {
            blockportal$size.func_150859_c();
            return true;
        }
        Size blockportal$size1 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);
        if (!blockportal$size1.func_150860_b()) return false;
        if (blockportal$size1.field_150864_e != 0) return false;
        blockportal$size1.func_150859_c();
        return true;
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing.Axis enumfacing$axis = state.getValue(AXIS);
        if (enumfacing$axis == EnumFacing.Axis.X) {
            Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
            if (blockportal$size.func_150860_b()) {
                if (blockportal$size.field_150864_e >= blockportal$size.field_150868_h * blockportal$size.field_150862_g) return;
            }
            worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            return;
        }
        if (enumfacing$axis != EnumFacing.Axis.Z) return;
        Size blockportal$size1 = new Size(worldIn, pos, EnumFacing.Axis.Z);
        if (blockportal$size1.func_150860_b()) {
            if (blockportal$size1.field_150864_e >= blockportal$size1.field_150868_h * blockportal$size1.field_150862_g) return;
        }
        worldIn.setBlockState(pos, Blocks.air.getDefaultState());
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        boolean flag5;
        EnumFacing.Axis enumfacing$axis = null;
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (worldIn.getBlockState(pos).getBlock() == this) {
            enumfacing$axis = iblockstate.getValue(AXIS);
            if (enumfacing$axis == null) {
                return false;
            }
            if (enumfacing$axis == EnumFacing.Axis.Z && side != EnumFacing.EAST && side != EnumFacing.WEST) {
                return false;
            }
            if (enumfacing$axis == EnumFacing.Axis.X && side != EnumFacing.SOUTH && side != EnumFacing.NORTH) {
                return false;
            }
        }
        boolean flag = worldIn.getBlockState(pos.west()).getBlock() == this && worldIn.getBlockState(pos.west(2)).getBlock() != this;
        boolean flag1 = worldIn.getBlockState(pos.east()).getBlock() == this && worldIn.getBlockState(pos.east(2)).getBlock() != this;
        boolean flag2 = worldIn.getBlockState(pos.north()).getBlock() == this && worldIn.getBlockState(pos.north(2)).getBlock() != this;
        boolean flag3 = worldIn.getBlockState(pos.south()).getBlock() == this && worldIn.getBlockState(pos.south(2)).getBlock() != this;
        boolean flag4 = flag || flag1 || enumfacing$axis == EnumFacing.Axis.X;
        boolean bl = flag5 = flag2 || flag3 || enumfacing$axis == EnumFacing.Axis.Z;
        if (flag4 && side == EnumFacing.WEST) {
            return true;
        }
        if (flag4 && side == EnumFacing.EAST) {
            return true;
        }
        if (flag5 && side == EnumFacing.NORTH) {
            return true;
        }
        if (!flag5) return false;
        if (side != EnumFacing.SOUTH) return false;
        return true;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn.ridingEntity != null) return;
        if (entityIn.riddenByEntity != null) return;
        entityIn.func_181015_d(pos);
    }

    @Override
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "portal.portal", 0.5f, rand.nextFloat() * 0.4f + 0.8f, false);
        }
        int i = 0;
        while (i < 4) {
            double d0 = (float)pos.getX() + rand.nextFloat();
            double d1 = (float)pos.getY() + rand.nextFloat();
            double d2 = (float)pos.getZ() + rand.nextFloat();
            double d3 = ((double)rand.nextFloat() - 0.5) * 0.5;
            double d4 = ((double)rand.nextFloat() - 0.5) * 0.5;
            double d5 = ((double)rand.nextFloat() - 0.5) * 0.5;
            int j = rand.nextInt(2) * 2 - 1;
            if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
                d0 = (double)pos.getX() + 0.5 + 0.25 * (double)j;
                d3 = rand.nextFloat() * 2.0f * (float)j;
            } else {
                d2 = (double)pos.getZ() + 0.5 + 0.25 * (double)j;
                d5 = rand.nextFloat() * 2.0f * (float)j;
            }
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5, new int[0]);
            ++i;
        }
    }

    @Override
    public Item getItem(World worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing.Axis axis;
        IBlockState iBlockState = this.getDefaultState();
        if ((meta & 3) == 2) {
            axis = EnumFacing.Axis.Z;
            return iBlockState.withProperty(AXIS, axis);
        }
        axis = EnumFacing.Axis.X;
        return iBlockState.withProperty(AXIS, axis);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return BlockPortal.getMetaForAxis(state.getValue(AXIS));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AXIS);
    }

    public BlockPattern.PatternHelper func_181089_f(World p_181089_1_, BlockPos p_181089_2_) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        Size blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.X);
        LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.func_181627_a(p_181089_1_, true);
        if (!blockportal$size.func_150860_b()) {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.Z);
        }
        if (!blockportal$size.func_150860_b()) {
            return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        }
        int[] aint = new int[EnumFacing.AxisDirection.values().length];
        EnumFacing enumfacing = blockportal$size.field_150866_c.rotateYCCW();
        BlockPos blockpos = blockportal$size.field_150861_f.up(blockportal$size.func_181100_a() - 1);
        EnumFacing.AxisDirection[] axisDirectionArray = EnumFacing.AxisDirection.values();
        int n = axisDirectionArray.length;
        int n2 = 0;
        while (true) {
            BlockPattern.PatternHelper blockpattern$patternhelper;
            EnumFacing.AxisDirection enumfacing$axisdirection;
            if (n2 < n) {
                enumfacing$axisdirection = axisDirectionArray[n2];
                blockpattern$patternhelper = new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1), EnumFacing.func_181076_a(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
            } else {
                BlockPos blockPos;
                EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;
                for (EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values()) {
                    if (aint[enumfacing$axisdirection2.ordinal()] >= aint[enumfacing$axisdirection1.ordinal()]) continue;
                    enumfacing$axisdirection1 = enumfacing$axisdirection2;
                }
                if (enumfacing.getAxisDirection() == enumfacing$axisdirection1) {
                    blockPos = blockpos;
                    return new BlockPattern.PatternHelper(blockPos, EnumFacing.func_181076_a(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
                }
                blockPos = blockpos.offset(blockportal$size.field_150866_c, blockportal$size.func_181101_b() - 1);
                return new BlockPattern.PatternHelper(blockPos, EnumFacing.func_181076_a(enumfacing$axisdirection1, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
            }
            for (int i = 0; i < blockportal$size.func_181101_b(); ++i) {
                for (int j = 0; j < blockportal$size.func_181100_a(); ++j) {
                    BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
                    if (blockworldstate.getBlockState() == null || blockworldstate.getBlockState().getBlock().getMaterial() == Material.air) continue;
                    int n3 = enumfacing$axisdirection.ordinal();
                    aint[n3] = aint[n3] + 1;
                }
            }
            ++n2;
        }
    }

    public static class Size {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing field_150866_c;
        private final EnumFacing field_150863_d;
        private int field_150864_e = 0;
        private BlockPos field_150861_f;
        private int field_150862_g;
        private int field_150868_h;

        public Size(World worldIn, BlockPos p_i45694_2_, EnumFacing.Axis p_i45694_3_) {
            this.world = worldIn;
            this.axis = p_i45694_3_;
            if (p_i45694_3_ == EnumFacing.Axis.X) {
                this.field_150863_d = EnumFacing.EAST;
                this.field_150866_c = EnumFacing.WEST;
            } else {
                this.field_150863_d = EnumFacing.NORTH;
                this.field_150866_c = EnumFacing.SOUTH;
            }
            BlockPos blockpos = p_i45694_2_;
            while (p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && this.func_150857_a(worldIn.getBlockState(p_i45694_2_.down()).getBlock())) {
                p_i45694_2_ = p_i45694_2_.down();
            }
            int i = this.func_180120_a(p_i45694_2_, this.field_150863_d) - 1;
            if (i >= 0) {
                this.field_150861_f = p_i45694_2_.offset(this.field_150863_d, i);
                this.field_150868_h = this.func_180120_a(this.field_150861_f, this.field_150866_c);
                if (this.field_150868_h < 2 || this.field_150868_h > 21) {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }
            if (this.field_150861_f == null) return;
            this.field_150862_g = this.func_150858_a();
        }

        protected int func_180120_a(BlockPos p_180120_1_, EnumFacing p_180120_2_) {
            BlockPos blockpos;
            int i;
            for (i = 0; i < 22 && this.func_150857_a(this.world.getBlockState(blockpos = p_180120_1_.offset(p_180120_2_, i)).getBlock()) && this.world.getBlockState(blockpos.down()).getBlock() == Blocks.obsidian; ++i) {
            }
            Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
            if (block != Blocks.obsidian) return 0;
            int n = i;
            return n;
        }

        public int func_181100_a() {
            return this.field_150862_g;
        }

        public int func_181101_b() {
            return this.field_150868_h;
        }

        protected int func_150858_a() {
            this.field_150862_g = 0;
            block0: while (this.field_150862_g < 21) {
                for (int i = 0; i < this.field_150868_h; ++i) {
                    BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i).up(this.field_150862_g);
                    Block block = this.world.getBlockState(blockpos).getBlock();
                    if (!this.func_150857_a(block)) break block0;
                    if (block == Blocks.portal) {
                        ++this.field_150864_e;
                    }
                    if (i == 0 ? (block = this.world.getBlockState(blockpos.offset(this.field_150863_d)).getBlock()) != Blocks.obsidian : i == this.field_150868_h - 1 && (block = this.world.getBlockState(blockpos.offset(this.field_150866_c)).getBlock()) != Blocks.obsidian) break block0;
                }
                ++this.field_150862_g;
            }
            for (int j = 0; j < this.field_150868_h; ++j) {
                if (this.world.getBlockState(this.field_150861_f.offset(this.field_150866_c, j).up(this.field_150862_g)).getBlock() == Blocks.obsidian) continue;
                this.field_150862_g = 0;
                break;
            }
            if (this.field_150862_g <= 21 && this.field_150862_g >= 3) {
                return this.field_150862_g;
            }
            this.field_150861_f = null;
            this.field_150868_h = 0;
            this.field_150862_g = 0;
            return 0;
        }

        protected boolean func_150857_a(Block p_150857_1_) {
            if (p_150857_1_.blockMaterial == Material.air) return true;
            if (p_150857_1_ == Blocks.fire) return true;
            if (p_150857_1_ == Blocks.portal) return true;
            return false;
        }

        public boolean func_150860_b() {
            if (this.field_150861_f == null) return false;
            if (this.field_150868_h < 2) return false;
            if (this.field_150868_h > 21) return false;
            if (this.field_150862_g < 3) return false;
            if (this.field_150862_g > 21) return false;
            return true;
        }

        public void func_150859_c() {
            int i = 0;
            while (i < this.field_150868_h) {
                BlockPos blockpos = this.field_150861_f.offset(this.field_150866_c, i);
                for (int j = 0; j < this.field_150862_g; ++j) {
                    this.world.setBlockState(blockpos.up(j), Blocks.portal.getDefaultState().withProperty(AXIS, this.axis), 2);
                }
                ++i;
            }
        }
    }
}

