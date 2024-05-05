package net.minecraft.block;

import com.google.common.cache.LoadingCache;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockPortal
extends BlockBreakable {
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create((String)"axis", EnumFacing.Axis.class, (Enum[])new EnumFacing.Axis[]{EnumFacing.Axis.X, EnumFacing.Axis.Z});

    public BlockPortal() {
        super(Material.portal, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, (Comparable)EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getDifficultyId()) {
            Entity entity;
            int i = pos.getY();
            BlockPos blockpos = pos;
            while (!World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)blockpos) && blockpos.getY() > 0) {
                blockpos = blockpos.down();
            }
            if (i > 0 && !worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube() && (entity = ItemMonsterPlacer.spawnCreature((World)worldIn, (int)57, (double)((double)blockpos.getX() + 0.5), (double)((double)blockpos.getY() + 1.1), (double)((double)blockpos.getZ() + 0.5))) != null) {
                entity.timeUntilPortal = entity.getPortalCooldown();
            }
        }
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)worldIn.getBlockState(pos).getValue(AXIS);
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
        return axis == EnumFacing.Axis.X ? 1 : (axis == EnumFacing.Axis.Z ? 2 : 0);
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean func_176548_d(World worldIn, BlockPos p_176548_2_) {
        Size blockportal$size = new Size(worldIn, p_176548_2_, EnumFacing.Axis.X);
        if (blockportal$size.func_150860_b() && Size.access$000((Size)blockportal$size) == 0) {
            blockportal$size.func_150859_c();
            return true;
        }
        Size blockportal$size1 = new Size(worldIn, p_176548_2_, EnumFacing.Axis.Z);
        if (blockportal$size1.func_150860_b() && Size.access$000((Size)blockportal$size1) == 0) {
            blockportal$size1.func_150859_c();
            return true;
        }
        return false;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        Size blockportal$size1;
        EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue(AXIS);
        if (enumfacing$axis == EnumFacing.Axis.X) {
            Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
            if (!blockportal$size.func_150860_b() || Size.access$000((Size)blockportal$size) < Size.access$100((Size)blockportal$size) * Size.access$200((Size)blockportal$size)) {
                worldIn.setBlockState(pos, Blocks.air.getDefaultState());
            }
        } else if (!(enumfacing$axis != EnumFacing.Axis.Z || (blockportal$size1 = new Size(worldIn, pos, EnumFacing.Axis.Z)).func_150860_b() && Size.access$000((Size)blockportal$size1) >= Size.access$100((Size)blockportal$size1) * Size.access$200((Size)blockportal$size1))) {
            worldIn.setBlockState(pos, Blocks.air.getDefaultState());
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        boolean flag5;
        EnumFacing.Axis enumfacing$axis = null;
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (worldIn.getBlockState(pos).getBlock() == this) {
            enumfacing$axis = (EnumFacing.Axis)iblockstate.getValue(AXIS);
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
        return flag4 && side == EnumFacing.WEST ? true : (flag4 && side == EnumFacing.EAST ? true : (flag5 && side == EnumFacing.NORTH ? true : flag5 && side == EnumFacing.SOUTH));
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null) {
            entityIn.setPortal(pos);
        }
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, "portal.portal", 0.5f, rand.nextFloat() * 0.4f + 0.8f, false);
        }
        for (int i = 0; i < 4; ++i) {
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
        }
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return null;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AXIS, (Comparable)((meta & 3) == 2 ? EnumFacing.Axis.Z : EnumFacing.Axis.X));
    }

    public int getMetaFromState(IBlockState state) {
        return BlockPortal.getMetaForAxis((EnumFacing.Axis)state.getValue(AXIS));
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{AXIS});
    }

    public BlockPattern.PatternHelper func_181089_f(World p_181089_1_, BlockPos p_181089_2_) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        Size blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.X);
        LoadingCache loadingcache = BlockPattern.func_181627_a((World)p_181089_1_, (boolean)true);
        if (!blockportal$size.func_150860_b()) {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new Size(p_181089_1_, p_181089_2_, EnumFacing.Axis.Z);
        }
        if (!blockportal$size.func_150860_b()) {
            return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        }
        int[] aint = new int[EnumFacing.AxisDirection.values().length];
        EnumFacing enumfacing = Size.access$300((Size)blockportal$size).rotateYCCW();
        BlockPos blockpos = Size.access$400((Size)blockportal$size).up(blockportal$size.func_181100_a() - 1);
        for (EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values()) {
            BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(Size.access$300((Size)blockportal$size), blockportal$size.func_181101_b() - 1), EnumFacing.getFacingFromAxis((EnumFacing.AxisDirection)enumfacing$axisdirection, (EnumFacing.Axis)enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
            for (int i = 0; i < blockportal$size.func_181101_b(); ++i) {
                for (int j = 0; j < blockportal$size.func_181100_a(); ++j) {
                    BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
                    if (blockworldstate.getBlockState() == null || blockworldstate.getBlockState().getBlock().getMaterial() == Material.air) continue;
                    int n = enumfacing$axisdirection.ordinal();
                    aint[n] = aint[n] + 1;
                }
            }
        }
        EnumFacing.AxisDirection enumfacing$axisdirection1 = EnumFacing.AxisDirection.POSITIVE;
        for (EnumFacing.AxisDirection enumfacing$axisdirection2 : EnumFacing.AxisDirection.values()) {
            if (aint[enumfacing$axisdirection2.ordinal()] >= aint[enumfacing$axisdirection1.ordinal()]) continue;
            enumfacing$axisdirection1 = enumfacing$axisdirection2;
        }
        return new BlockPattern.PatternHelper(enumfacing.getAxisDirection() == enumfacing$axisdirection1 ? blockpos : blockpos.offset(Size.access$300((Size)blockportal$size), blockportal$size.func_181101_b() - 1), EnumFacing.getFacingFromAxis((EnumFacing.AxisDirection)enumfacing$axisdirection1, (EnumFacing.Axis)enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.func_181101_b(), blockportal$size.func_181100_a(), 1);
    }
}
