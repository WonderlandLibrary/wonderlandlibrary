// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockWorldState;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.Rotation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.init.Blocks;
import javax.annotation.Nullable;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityPigZombie;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyEnum;

public class BlockPortal extends BlockBreakable
{
    public static final PropertyEnum<EnumFacing.Axis> AXIS;
    protected static final AxisAlignedBB X_AABB;
    protected static final AxisAlignedBB Z_AABB;
    protected static final AxisAlignedBB Y_AABB;
    
    public BlockPortal() {
        super(Material.PORTAL, false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPortal.AXIS, EnumFacing.Axis.X));
        this.setTickRandomly(true);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        switch (state.getValue(BlockPortal.AXIS)) {
            case X: {
                return BlockPortal.X_AABB;
            }
            default: {
                return BlockPortal.Y_AABB;
            }
            case Z: {
                return BlockPortal.Z_AABB;
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.provider.isSurfaceWorld() && worldIn.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < worldIn.getDifficulty().getId()) {
            final int i = pos.getY();
            BlockPos blockpos;
            for (blockpos = pos; !worldIn.getBlockState(blockpos).isTopSolid() && blockpos.getY() > 0; blockpos = blockpos.down()) {}
            if (i > 0 && !worldIn.getBlockState(blockpos.up()).isNormalCube()) {
                final Entity entity = ItemMonsterPlacer.spawnCreature(worldIn, EntityList.getKey(EntityPigZombie.class), blockpos.getX() + 0.5, blockpos.getY() + 1.1, blockpos.getZ() + 0.5);
                if (entity != null) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockPortal.NULL_AABB;
    }
    
    public static int getMetaForAxis(final EnumFacing.Axis axis) {
        if (axis == EnumFacing.Axis.X) {
            return 1;
        }
        return (axis == EnumFacing.Axis.Z) ? 2 : 0;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    public boolean trySpawnPortal(final World worldIn, final BlockPos pos) {
        final Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
        if (blockportal$size.isValid() && blockportal$size.portalBlockCount == 0) {
            blockportal$size.placePortalBlocks();
            return true;
        }
        final Size blockportal$size2 = new Size(worldIn, pos, EnumFacing.Axis.Z);
        if (blockportal$size2.isValid() && blockportal$size2.portalBlockCount == 0) {
            blockportal$size2.placePortalBlocks();
            return true;
        }
        return false;
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        final EnumFacing.Axis enumfacing$axis = state.getValue(BlockPortal.AXIS);
        if (enumfacing$axis == EnumFacing.Axis.X) {
            final Size blockportal$size = new Size(worldIn, pos, EnumFacing.Axis.X);
            if (!blockportal$size.isValid() || blockportal$size.portalBlockCount < blockportal$size.width * blockportal$size.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z) {
            final Size blockportal$size2 = new Size(worldIn, pos, EnumFacing.Axis.Z);
            if (!blockportal$size2.isValid() || blockportal$size2.portalBlockCount < blockportal$size2.width * blockportal$size2.height) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, BlockPos pos, final EnumFacing side) {
        pos = pos.offset(side);
        EnumFacing.Axis enumfacing$axis = null;
        if (blockState.getBlock() == this) {
            enumfacing$axis = blockState.getValue(BlockPortal.AXIS);
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
        final boolean flag = blockAccess.getBlockState(pos.west()).getBlock() == this && blockAccess.getBlockState(pos.west(2)).getBlock() != this;
        final boolean flag2 = blockAccess.getBlockState(pos.east()).getBlock() == this && blockAccess.getBlockState(pos.east(2)).getBlock() != this;
        final boolean flag3 = blockAccess.getBlockState(pos.north()).getBlock() == this && blockAccess.getBlockState(pos.north(2)).getBlock() != this;
        final boolean flag4 = blockAccess.getBlockState(pos.south()).getBlock() == this && blockAccess.getBlockState(pos.south(2)).getBlock() != this;
        final boolean flag5 = flag || flag2 || enumfacing$axis == EnumFacing.Axis.X;
        final boolean flag6 = flag3 || flag4 || enumfacing$axis == EnumFacing.Axis.Z;
        return (flag5 && side == EnumFacing.WEST) || (flag5 && side == EnumFacing.EAST) || (flag6 && side == EnumFacing.NORTH) || (flag6 && side == EnumFacing.SOUTH);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        if (!entityIn.isRiding() && !entityIn.isBeingRidden() && entityIn.isNonBoss()) {
            entityIn.setPortal(pos);
        }
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        if (rand.nextInt(100) == 0) {
            worldIn.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5f, rand.nextFloat() * 0.4f + 0.8f, false);
        }
        for (int i = 0; i < 4; ++i) {
            double d0 = pos.getX() + rand.nextFloat();
            final double d2 = pos.getY() + rand.nextFloat();
            double d3 = pos.getZ() + rand.nextFloat();
            double d4 = (rand.nextFloat() - 0.5) * 0.5;
            final double d5 = (rand.nextFloat() - 0.5) * 0.5;
            double d6 = (rand.nextFloat() - 0.5) * 0.5;
            final int j = rand.nextInt(2) * 2 - 1;
            if (worldIn.getBlockState(pos.west()).getBlock() != this && worldIn.getBlockState(pos.east()).getBlock() != this) {
                d0 = pos.getX() + 0.5 + 0.25 * j;
                d4 = rand.nextFloat() * 2.0f * j;
            }
            else {
                d3 = pos.getZ() + 0.5 + 0.25 * j;
                d6 = rand.nextFloat() * 2.0f * j;
            }
            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d0, d2, d3, d4, d5, d6, new int[0]);
        }
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockPortal.AXIS, ((meta & 0x3) == 0x2) ? EnumFacing.Axis.Z : EnumFacing.Axis.X);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return getMetaForAxis(state.getValue(BlockPortal.AXIS));
    }
    
    @Override
    @Deprecated
    public IBlockState withRotation(final IBlockState state, final Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                switch (state.getValue(BlockPortal.AXIS)) {
                    case X: {
                        return state.withProperty(BlockPortal.AXIS, EnumFacing.Axis.Z);
                    }
                    case Z: {
                        return state.withProperty(BlockPortal.AXIS, EnumFacing.Axis.X);
                    }
                    default: {
                        return state;
                    }
                }
                break;
            }
            default: {
                return state;
            }
        }
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPortal.AXIS });
    }
    
    public BlockPattern.PatternHelper createPatternHelper(final World worldIn, final BlockPos p_181089_2_) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Z;
        Size blockportal$size = new Size(worldIn, p_181089_2_, EnumFacing.Axis.X);
        final LoadingCache<BlockPos, BlockWorldState> loadingcache = BlockPattern.createLoadingCache(worldIn, true);
        if (!blockportal$size.isValid()) {
            enumfacing$axis = EnumFacing.Axis.X;
            blockportal$size = new Size(worldIn, p_181089_2_, EnumFacing.Axis.Z);
        }
        if (!blockportal$size.isValid()) {
            return new BlockPattern.PatternHelper(p_181089_2_, EnumFacing.NORTH, EnumFacing.UP, loadingcache, 1, 1, 1);
        }
        final int[] aint = new int[EnumFacing.AxisDirection.values().length];
        final EnumFacing enumfacing = blockportal$size.rightDir.rotateYCCW();
        final BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);
        for (final EnumFacing.AxisDirection enumfacing$axisdirection : EnumFacing.AxisDirection.values()) {
            final BlockPattern.PatternHelper blockpattern$patternhelper = new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection) ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
            for (int i = 0; i < blockportal$size.getWidth(); ++i) {
                for (int j = 0; j < blockportal$size.getHeight(); ++j) {
                    final BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, 1);
                    if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getMaterial() != Material.AIR) {
                        final int[] array = aint;
                        final int ordinal = enumfacing$axisdirection.ordinal();
                        ++array[ordinal];
                    }
                }
            }
        }
        EnumFacing.AxisDirection enumfacing$axisdirection2 = EnumFacing.AxisDirection.POSITIVE;
        for (final EnumFacing.AxisDirection enumfacing$axisdirection3 : EnumFacing.AxisDirection.values()) {
            if (aint[enumfacing$axisdirection3.ordinal()] < aint[enumfacing$axisdirection2.ordinal()]) {
                enumfacing$axisdirection2 = enumfacing$axisdirection3;
            }
        }
        return new BlockPattern.PatternHelper((enumfacing.getAxisDirection() == enumfacing$axisdirection2) ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), EnumFacing.getFacingFromAxis(enumfacing$axisdirection2, enumfacing$axis), EnumFacing.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z);
        X_AABB = new AxisAlignedBB(0.0, 0.0, 0.375, 1.0, 1.0, 0.625);
        Z_AABB = new AxisAlignedBB(0.375, 0.0, 0.0, 0.625, 1.0, 1.0);
        Y_AABB = new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
    }
    
    public static class Size
    {
        private final World world;
        private final EnumFacing.Axis axis;
        private final EnumFacing rightDir;
        private final EnumFacing leftDir;
        private int portalBlockCount;
        private BlockPos bottomLeft;
        private int height;
        private int width;
        
        public Size(final World worldIn, BlockPos p_i45694_2_, final EnumFacing.Axis p_i45694_3_) {
            this.world = worldIn;
            this.axis = p_i45694_3_;
            if (p_i45694_3_ == EnumFacing.Axis.X) {
                this.leftDir = EnumFacing.EAST;
                this.rightDir = EnumFacing.WEST;
            }
            else {
                this.leftDir = EnumFacing.NORTH;
                this.rightDir = EnumFacing.SOUTH;
            }
            for (BlockPos blockpos = p_i45694_2_; p_i45694_2_.getY() > blockpos.getY() - 21 && p_i45694_2_.getY() > 0 && this.isEmptyBlock(worldIn.getBlockState(p_i45694_2_.down()).getBlock()); p_i45694_2_ = p_i45694_2_.down()) {}
            final int i = this.getDistanceUntilEdge(p_i45694_2_, this.leftDir) - 1;
            if (i >= 0) {
                this.bottomLeft = p_i45694_2_.offset(this.leftDir, i);
                this.width = this.getDistanceUntilEdge(this.bottomLeft, this.rightDir);
                if (this.width < 2 || this.width > 21) {
                    this.bottomLeft = null;
                    this.width = 0;
                }
            }
            if (this.bottomLeft != null) {
                this.height = this.calculatePortalHeight();
            }
        }
        
        protected int getDistanceUntilEdge(final BlockPos p_180120_1_, final EnumFacing p_180120_2_) {
            int i;
            for (i = 0; i < 22; ++i) {
                final BlockPos blockpos = p_180120_1_.offset(p_180120_2_, i);
                if (!this.isEmptyBlock(this.world.getBlockState(blockpos).getBlock())) {
                    break;
                }
                if (this.world.getBlockState(blockpos.down()).getBlock() != Blocks.OBSIDIAN) {
                    break;
                }
            }
            final Block block = this.world.getBlockState(p_180120_1_.offset(p_180120_2_, i)).getBlock();
            return (block == Blocks.OBSIDIAN) ? i : 0;
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public int getWidth() {
            return this.width;
        }
        
        protected int calculatePortalHeight() {
            this.height = 0;
        Label_0181:
            while (this.height < 21) {
                for (int i = 0; i < this.width; ++i) {
                    final BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i).up(this.height);
                    Block block = this.world.getBlockState(blockpos).getBlock();
                    if (!this.isEmptyBlock(block)) {
                        break Label_0181;
                    }
                    if (block == Blocks.PORTAL) {
                        ++this.portalBlockCount;
                    }
                    if (i == 0) {
                        block = this.world.getBlockState(blockpos.offset(this.leftDir)).getBlock();
                        if (block != Blocks.OBSIDIAN) {
                            break Label_0181;
                        }
                    }
                    else if (i == this.width - 1) {
                        block = this.world.getBlockState(blockpos.offset(this.rightDir)).getBlock();
                        if (block != Blocks.OBSIDIAN) {
                            break Label_0181;
                        }
                    }
                }
                ++this.height;
            }
            for (int j = 0; j < this.width; ++j) {
                if (this.world.getBlockState(this.bottomLeft.offset(this.rightDir, j).up(this.height)).getBlock() != Blocks.OBSIDIAN) {
                    this.height = 0;
                    break;
                }
            }
            if (this.height <= 21 && this.height >= 3) {
                return this.height;
            }
            this.bottomLeft = null;
            this.width = 0;
            return this.height = 0;
        }
        
        protected boolean isEmptyBlock(final Block blockIn) {
            return blockIn.material == Material.AIR || blockIn == Blocks.FIRE || blockIn == Blocks.PORTAL;
        }
        
        public boolean isValid() {
            return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
        }
        
        public void placePortalBlocks() {
            for (int i = 0; i < this.width; ++i) {
                final BlockPos blockpos = this.bottomLeft.offset(this.rightDir, i);
                for (int j = 0; j < this.height; ++j) {
                    this.world.setBlockState(blockpos.up(j), Blocks.PORTAL.getDefaultState().withProperty(BlockPortal.AXIS, this.axis), 2);
                }
            }
        }
    }
}
