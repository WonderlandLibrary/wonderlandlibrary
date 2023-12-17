// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.World;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import com.google.common.collect.Maps;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import java.util.Map;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFire extends Block
{
    public static final PropertyInteger AGE;
    public static final PropertyBool NORTH;
    public static final PropertyBool EAST;
    public static final PropertyBool SOUTH;
    public static final PropertyBool WEST;
    public static final PropertyBool UPPER;
    private final Map<Block, Integer> encouragements;
    private final Map<Block, Integer> flammabilities;
    
    @Override
    public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return (!worldIn.getBlockState(pos.down()).isTopSolid() && !Blocks.FIRE.canCatchFire(worldIn, pos.down())) ? state.withProperty((IProperty<Comparable>)BlockFire.NORTH, this.canCatchFire(worldIn, pos.north())).withProperty((IProperty<Comparable>)BlockFire.EAST, this.canCatchFire(worldIn, pos.east())).withProperty((IProperty<Comparable>)BlockFire.SOUTH, this.canCatchFire(worldIn, pos.south())).withProperty((IProperty<Comparable>)BlockFire.WEST, this.canCatchFire(worldIn, pos.west())).withProperty((IProperty<Comparable>)BlockFire.UPPER, this.canCatchFire(worldIn, pos.up())) : this.getDefaultState();
    }
    
    protected BlockFire() {
        super(Material.FIRE);
        this.encouragements = (Map<Block, Integer>)Maps.newIdentityHashMap();
        this.flammabilities = (Map<Block, Integer>)Maps.newIdentityHashMap();
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockFire.AGE, 0).withProperty((IProperty<Comparable>)BlockFire.NORTH, false).withProperty((IProperty<Comparable>)BlockFire.EAST, false).withProperty((IProperty<Comparable>)BlockFire.SOUTH, false).withProperty((IProperty<Comparable>)BlockFire.WEST, false).withProperty((IProperty<Comparable>)BlockFire.UPPER, false));
        this.setTickRandomly(true);
    }
    
    public static void init() {
        Blocks.FIRE.setFireInfo(Blocks.PLANKS, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.DOUBLE_WOODEN_SLAB, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.WOODEN_SLAB, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE_GATE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.OAK_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.SPRUCE_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.BIRCH_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.JUNGLE_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.ACACIA_FENCE, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.OAK_STAIRS, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.BIRCH_STAIRS, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.SPRUCE_STAIRS, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.JUNGLE_STAIRS, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.ACACIA_STAIRS, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.DARK_OAK_STAIRS, 5, 20);
        Blocks.FIRE.setFireInfo(Blocks.LOG, 5, 5);
        Blocks.FIRE.setFireInfo(Blocks.LOG2, 5, 5);
        Blocks.FIRE.setFireInfo(Blocks.LEAVES, 30, 60);
        Blocks.FIRE.setFireInfo(Blocks.LEAVES2, 30, 60);
        Blocks.FIRE.setFireInfo(Blocks.BOOKSHELF, 30, 20);
        Blocks.FIRE.setFireInfo(Blocks.TNT, 15, 100);
        Blocks.FIRE.setFireInfo(Blocks.TALLGRASS, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.DOUBLE_PLANT, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.YELLOW_FLOWER, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.RED_FLOWER, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.DEADBUSH, 60, 100);
        Blocks.FIRE.setFireInfo(Blocks.WOOL, 30, 60);
        Blocks.FIRE.setFireInfo(Blocks.VINE, 15, 100);
        Blocks.FIRE.setFireInfo(Blocks.COAL_BLOCK, 5, 5);
        Blocks.FIRE.setFireInfo(Blocks.HAY_BLOCK, 60, 20);
        Blocks.FIRE.setFireInfo(Blocks.CARPET, 60, 20);
    }
    
    public void setFireInfo(final Block blockIn, final int encouragement, final int flammability) {
        this.encouragements.put(blockIn, encouragement);
        this.flammabilities.put(blockIn, flammability);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
        return BlockFire.NULL_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 30;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, IBlockState state, final Random rand) {
        if (worldIn.getGameRules().getBoolean("doFireTick")) {
            if (!this.canPlaceBlockAt(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            final Block block = worldIn.getBlockState(pos.down()).getBlock();
            boolean flag = block == Blocks.NETHERRACK || block == Blocks.MAGMA;
            if (worldIn.provider instanceof WorldProviderEnd && block == Blocks.BEDROCK) {
                flag = true;
            }
            final int i = state.getValue((IProperty<Integer>)BlockFire.AGE);
            if (!flag && worldIn.isRaining() && this.canDie(worldIn, pos) && rand.nextFloat() < 0.2f + i * 0.03f) {
                worldIn.setBlockToAir(pos);
            }
            else {
                if (i < 15) {
                    state = state.withProperty((IProperty<Comparable>)BlockFire.AGE, i + rand.nextInt(3) / 2);
                    worldIn.setBlockState(pos, state, 4);
                }
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));
                if (!flag) {
                    if (!this.canNeighborCatchFire(worldIn, pos)) {
                        if (!worldIn.getBlockState(pos.down()).isTopSolid() || i > 3) {
                            worldIn.setBlockToAir(pos);
                        }
                        return;
                    }
                    if (!this.canCatchFire(worldIn, pos.down()) && i == 15 && rand.nextInt(4) == 0) {
                        worldIn.setBlockToAir(pos);
                        return;
                    }
                }
                final boolean flag2 = worldIn.isBlockinHighHumidity(pos);
                int j = 0;
                if (flag2) {
                    j = -50;
                }
                this.catchOnFire(worldIn, pos.east(), 300 + j, rand, i);
                this.catchOnFire(worldIn, pos.west(), 300 + j, rand, i);
                this.catchOnFire(worldIn, pos.down(), 250 + j, rand, i);
                this.catchOnFire(worldIn, pos.up(), 250 + j, rand, i);
                this.catchOnFire(worldIn, pos.north(), 300 + j, rand, i);
                this.catchOnFire(worldIn, pos.south(), 300 + j, rand, i);
                for (int k = -1; k <= 1; ++k) {
                    for (int l = -1; l <= 1; ++l) {
                        for (int i2 = -1; i2 <= 4; ++i2) {
                            if (k != 0 || i2 != 0 || l != 0) {
                                int j2 = 100;
                                if (i2 > 1) {
                                    j2 += (i2 - 1) * 100;
                                }
                                final BlockPos blockpos = pos.add(k, i2, l);
                                final int k2 = this.getNeighborEncouragement(worldIn, blockpos);
                                if (k2 > 0) {
                                    int l2 = (k2 + 40 + worldIn.getDifficulty().getId() * 7) / (i + 30);
                                    if (flag2) {
                                        l2 /= 2;
                                    }
                                    if (l2 > 0 && rand.nextInt(j2) <= l2 && (!worldIn.isRaining() || !this.canDie(worldIn, blockpos))) {
                                        int i3 = i + rand.nextInt(5) / 4;
                                        if (i3 > 15) {
                                            i3 = 15;
                                        }
                                        worldIn.setBlockState(blockpos, state.withProperty((IProperty<Comparable>)BlockFire.AGE, i3), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    protected boolean canDie(final World worldIn, final BlockPos pos) {
        return worldIn.isRainingAt(pos) || worldIn.isRainingAt(pos.west()) || worldIn.isRainingAt(pos.east()) || worldIn.isRainingAt(pos.north()) || worldIn.isRainingAt(pos.south());
    }
    
    @Override
    public boolean requiresUpdates() {
        return false;
    }
    
    private int getFlammability(final Block blockIn) {
        final Integer integer = this.flammabilities.get(blockIn);
        return (integer == null) ? 0 : integer;
    }
    
    private int getEncouragement(final Block blockIn) {
        final Integer integer = this.encouragements.get(blockIn);
        return (integer == null) ? 0 : integer;
    }
    
    private void catchOnFire(final World worldIn, final BlockPos pos, final int chance, final Random random, final int age) {
        final int i = this.getFlammability(worldIn.getBlockState(pos).getBlock());
        if (random.nextInt(chance) < i) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos)) {
                int j = age + random.nextInt(5) / 4;
                if (j > 15) {
                    j = 15;
                }
                worldIn.setBlockState(pos, this.getDefaultState().withProperty((IProperty<Comparable>)BlockFire.AGE, j), 3);
            }
            else {
                worldIn.setBlockToAir(pos);
            }
            if (iblockstate.getBlock() == Blocks.TNT) {
                Blocks.TNT.onPlayerDestroy(worldIn, pos, iblockstate.withProperty((IProperty<Comparable>)BlockTNT.EXPLODE, true));
            }
        }
    }
    
    private boolean canNeighborCatchFire(final World worldIn, final BlockPos pos) {
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            if (this.canCatchFire(worldIn, pos.offset(enumfacing))) {
                return true;
            }
        }
        return false;
    }
    
    private int getNeighborEncouragement(final World worldIn, final BlockPos pos) {
        if (!worldIn.isAirBlock(pos)) {
            return 0;
        }
        int i = 0;
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            i = Math.max(this.getEncouragement(worldIn.getBlockState(pos.offset(enumfacing)).getBlock()), i);
        }
        return i;
    }
    
    @Override
    public boolean isCollidable() {
        return false;
    }
    
    public boolean canCatchFire(final IBlockAccess worldIn, final BlockPos pos) {
        return this.getEncouragement(worldIn.getBlockState(pos).getBlock()) > 0;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isTopSolid() || this.canNeighborCatchFire(worldIn, pos);
    }
    
    @Override
    public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!worldIn.getBlockState(pos.down()).isTopSolid() && !this.canNeighborCatchFire(worldIn, pos)) {
            worldIn.setBlockToAir(pos);
        }
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (worldIn.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(worldIn, pos)) {
            if (!worldIn.getBlockState(pos.down()).isTopSolid() && !this.canNeighborCatchFire(worldIn, pos)) {
                worldIn.setBlockToAir(pos);
            }
            else {
                worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + worldIn.rand.nextInt(10));
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        if (rand.nextInt(24) == 0) {
            worldIn.playSound(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0f + rand.nextFloat(), rand.nextFloat() * 0.7f + 0.3f, false);
        }
        if (!worldIn.getBlockState(pos.down()).isTopSolid() && !Blocks.FIRE.canCatchFire(worldIn, pos.down())) {
            if (Blocks.FIRE.canCatchFire(worldIn, pos.west())) {
                for (int j = 0; j < 2; ++j) {
                    final double d3 = pos.getX() + rand.nextDouble() * 0.10000000149011612;
                    final double d4 = pos.getY() + rand.nextDouble();
                    final double d5 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.FIRE.canCatchFire(worldIn, pos.east())) {
                for (int k = 0; k < 2; ++k) {
                    final double d6 = pos.getX() + 1 - rand.nextDouble() * 0.10000000149011612;
                    final double d7 = pos.getY() + rand.nextDouble();
                    final double d8 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d7, d8, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.FIRE.canCatchFire(worldIn, pos.north())) {
                for (int l = 0; l < 2; ++l) {
                    final double d9 = pos.getX() + rand.nextDouble();
                    final double d10 = pos.getY() + rand.nextDouble();
                    final double d11 = pos.getZ() + rand.nextDouble() * 0.10000000149011612;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d9, d10, d11, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.FIRE.canCatchFire(worldIn, pos.south())) {
                for (int i1 = 0; i1 < 2; ++i1) {
                    final double d12 = pos.getX() + rand.nextDouble();
                    final double d13 = pos.getY() + rand.nextDouble();
                    final double d14 = pos.getZ() + 1 - rand.nextDouble() * 0.10000000149011612;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d12, d13, d14, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (Blocks.FIRE.canCatchFire(worldIn, pos.up())) {
                for (int j2 = 0; j2 < 2; ++j2) {
                    final double d15 = pos.getX() + rand.nextDouble();
                    final double d16 = pos.getY() + 1 - rand.nextDouble() * 0.10000000149011612;
                    final double d17 = pos.getZ() + rand.nextDouble();
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d15, d16, d17, 0.0, 0.0, 0.0, new int[0]);
                }
            }
        }
        else {
            for (int m = 0; m < 3; ++m) {
                final double d18 = pos.getX() + rand.nextDouble();
                final double d19 = pos.getY() + rand.nextDouble() * 0.5 + 0.5;
                final double d20 = pos.getZ() + rand.nextDouble();
                worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d18, d19, d20, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }
    
    @Override
    @Deprecated
    public MapColor getMapColor(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        return MapColor.TNT;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockFire.AGE, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockFire.AGE);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockFire.AGE, BlockFire.NORTH, BlockFire.EAST, BlockFire.SOUTH, BlockFire.WEST, BlockFire.UPPER });
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    static {
        AGE = PropertyInteger.create("age", 0, 15);
        NORTH = PropertyBool.create("north");
        EAST = PropertyBool.create("east");
        SOUTH = PropertyBool.create("south");
        WEST = PropertyBool.create("west");
        UPPER = PropertyBool.create("up");
    }
}
